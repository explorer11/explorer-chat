package org.explorer.chat.server;

import org.explorer.chat.server.collect.MessageIndexing;
import org.explorer.chat.server.users.ConnectedUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

    /**
     * Expecting the path to the users file in the first argument
     */
	public static void main(String[] args) {

        final ConnectedUsers connectedUsers;
        try {
            connectedUsers = connectedUsers(args);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

		final MessageIndexing messageIndexing = messageIndexing(args);
		messageIndexing.start();

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

	static ConnectedUsers connectedUsers(final String[] args) throws IOException {
        return new ConnectedUsers(args[0]);
    }

	static MessageIndexing messageIndexing(final String[] args) {
		return new MessageIndexing(Paths.get(args[1]));
	}

}
