package org.explorer.chat.server.users;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.common.UsersList;
import org.explorer.chat.data.MessageStore;
import org.explorer.chat.data.PersistedMessage;
import org.explorer.chat.server.ChatOutputWriter;
import org.explorer.chat.users.Users;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnectedUsers {

	private final Map<String, OutputStream> objectWriters = new ConcurrentHashMap<>();

	private final Users users;

    private final ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = reentrantReadWriteLock.readLock();
    private final Lock writeLock = reentrantReadWriteLock.writeLock();
    private final MessageStore messageStore;

    public ConnectedUsers(final String stringPath, final MessageStore messageStore) throws IOException {
        this.users = new Users(stringPath);
        this.messageStore = messageStore;
    }

    public ConnectedUsers(final Users users, final MessageStore messageStore) {
        this.users = users;
        this.messageStore = messageStore;
    }

    public boolean add(final String user,
                       final OutputStream outputStream,
                       final ChatMessage welcomeMessage) throws IOException {

        writeLock.lock();

        if(objectWriters.containsKey(user)) {
            writeLock.unlock();
            return false;
        }

        try {
            ChatOutputWriter.INSTANCE.write(welcomeMessage, outputStream);
            sendLastMessages(outputStream);
            users.createNewUser(user);
            objectWriters.put(user, outputStream);
        } finally {
            writeLock.unlock();
        }

        return true;
	}

	public void remove(final String user) {
        writeLock.lock();
		objectWriters.remove(user);
        writeLock.unlock();
	}

	public Collection<OutputStream> getOutputs() {
        readLock.lock();
        final Collection<OutputStream> values = new HashSet<>(objectWriters.values());
        readLock.unlock();
        return values;
    }

	public Set<String> getUsersNames() {
        readLock.lock();
        final Set<String> keys = new HashSet<>(objectWriters.keySet());
        readLock.unlock();
        return keys;
	}

	public String getUsersList(){
        readLock.lock();
        final Set<String> keys = new HashSet<>(objectWriters.keySet());
        readLock.unlock();
        return new UsersList().getUsersList(keys);
	}

    private void sendLastMessages(final OutputStream outputStream) {
        try {
            final List<PersistedMessage> lastMessages = messageStore.readLast(10);
            lastMessages.forEach(persistedMessage -> {
                try {
                    ChatOutputWriter.INSTANCE.write(new ChatMessage.ChatMessageBuilder()
                            .withMessageType(ChatMessageType.SENTENCE)
                            .withFromUserMessage(persistedMessage.getFrom())
                            .withMessage(persistedMessage.getMessage())
                            .withInstant(persistedMessage.getInstant().orElse(null))
                            .build(), outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
}
