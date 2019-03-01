package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.server.collect.MessageSender;
import org.explorer.chat.server.collect.MessageCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	private static Logger logger = LoggerFactory.getLogger(Server.class);
	
	public static void main(String[] args) {

		final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(100);
		final MessageCollector messageCollector = new MessageCollector(queue);
		final ExecutorService collectorExecutorService = Executors.newSingleThreadExecutor();
		collectorExecutorService.submit(new MessageSender(queue));

		while(true){
			try {

				ClientSocketManager clientSocketManager = new ClientSocketManager(
						ChatServerSocket.INSTANCE.getServerSocket().accept(), messageCollector);
				
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.submit(clientSocketManager);
				
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

}
