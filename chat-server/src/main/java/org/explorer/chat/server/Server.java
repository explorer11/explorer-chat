package org.explorer.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private static Logger logger = LoggerFactory.getLogger(Server.class);
	
	public static void main(String[] args) {
		
		while(true){
			try {

				ClientSocketManager clientSocketManager = new ClientSocketManager(ChatServerSocket.INSTANCE.getServerSocket().accept());
				
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.submit(clientSocketManager);
				
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

}
