package org.explorer.chat.server;

import org.explorer.chat.server.collect.MessageIndexing;
import org.explorer.chat.server.users.ConnectedUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	
	public static void main(String[] args) {

        final ConnectedUsers connectedUsers;
        try {
            connectedUsers = connectedUsers(args);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final MessageIndexing messageIndexing = new MessageIndexing();
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

}
