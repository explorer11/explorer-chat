package org.explorer.chat.server;

import org.explorer.chat.data.MessageStore;
import org.explorer.chat.server.collect.MessageIndexing;
import org.explorer.chat.server.users.ConnectedUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

    /**
     * Expecting
     * - the path to the users file in the first argument
     * - the path to the message store directory in the second argument
     * Example
     * java -jar target/chat-server-2.4.0-SNAPSHOT.jar D:\\data\\chat-users.txt D:\\data\\chat-messages.txt
     */
	public static void main(final String[] args) {

        final Path path;
        try {
            path = messagesPath(args);
        } catch (final RuntimeException e) {
            e.printStackTrace();
            return;
        }

        final MessageStore messageStore = new MessageStore(path);
        final ConnectedUsers connectedUsers;
        try {
            connectedUsers = connectedUsers(args, messageStore);
        } catch (final IOException e) {
            e.printStackTrace();
            return;
        }


        final MessageIndexing messageIndexing = new MessageIndexing(messageStore);
		messageIndexing.start();

		logger.info("Server started. Waiting for connections");

		while(true){
			try {

				final ClientSocketManager clientSocketManager = new ClientSocketManager(
						ChatServerSocket.INSTANCE.getServerSocket().accept(),
                        messageIndexing, connectedUsers);
				
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.submit(clientSocketManager);
				
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	static ConnectedUsers connectedUsers(final String[] args, final MessageStore messageStore) throws IOException {
        return new ConnectedUsers(args[0], messageStore);
    }

    /**
     * @throws RuntimeException if args[1] isn't ane existing path
     */
    static Path messagesPath(final String[] args) {
        final String pathArgument = args[1];
        final Path path = Paths.get(pathArgument);
        if(!path.toFile().exists()) {
            throw new RuntimeException("path " + pathArgument + " does not exist");
        }
        return path;
    }

}
