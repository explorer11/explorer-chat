package org.explorer.chat.server.users;

import org.explorer.chat.common.UsersList;
import org.explorer.chat.users.Users;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
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

    public ConnectedUsers(final String stringPath) throws IOException {
        this.users = new Users(stringPath);
    }

    public ConnectedUsers(final Users users) {
        this.users = users;
    }

    public boolean add(final String user, final OutputStream p) throws IOException {

        writeLock.lock();

        if(objectWriters.containsKey(user)) {
            return false;
        }

        try {
            users.createNewUser(user);
            objectWriters.put(user, p);
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
	
	
}
