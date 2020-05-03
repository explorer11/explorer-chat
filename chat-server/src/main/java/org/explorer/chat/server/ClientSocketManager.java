package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessageReader;
import org.explorer.chat.server.collect.MessageIndexing;
import org.explorer.chat.server.users.ConnectedUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

public class ClientSocketManager implements Callable<String> {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientSocketManager.class);
	
	private final Socket socket;
	private final MessageIndexing messageIndexing;
	private final ConnectedUsers connectedUsers;

	ClientSocketManager(final Socket socket,
                        final MessageIndexing messageIndexing,
                        final ConnectedUsers connectedUsers) {
		super();
		this.socket = socket;
		this.messageIndexing = messageIndexing;
		this.connectedUsers = connectedUsers;
	}

	@Override
	public String call() {
		
		logger.info("call");
		
		try (OutputStream outputStream = socket.getOutputStream();
				InputStream inputStream = socket.getInputStream()) {
				
			logger.info("call::accept client");
				
			final ClientAuthenticationStrategy clientAuthenticationStrategy =
                    new ClientAuthenticationStrategy(connectedUsers);
			new ChatMessageReader().read(inputStream, outputStream, 
					clientAuthenticationStrategy);
			logger.info("call::clientName " + clientAuthenticationStrategy.getClientName().orElse(""));
				
			clientAuthenticationStrategy.getClientName().ifPresentOrElse(clientName->
			new ChatMessageReader().read(inputStream, outputStream, 
					new ClientConnectionStrategy(clientName, messageIndexing, connectedUsers)),
			()->IOUtils.closeQuietly(socket));
					
		} catch (IOException e) {
			logger.error("", e);
			IOUtils.closeQuietly(socket);
		}
		
		return "";
		
	}

}
