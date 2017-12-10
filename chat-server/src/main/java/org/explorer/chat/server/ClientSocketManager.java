package org.explorer.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.explorer.chat.common.ChatMessageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSocketManager implements Callable<String> {
	
	private static Logger logger = LoggerFactory.getLogger(ClientSocketManager.class);
	
	private final Socket socket;
	private String clientName = "";

	public ClientSocketManager(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public String call() {
		
		logger.info("call");
		
		try (OutputStream outputStream = socket.getOutputStream();
				InputStream inputStream = socket.getInputStream();) {
				
			logger.info("call::accept client");
				
			ClientAuthenticationStrategy clientAuthenticationStrategy = new ClientAuthenticationStrategy();
			new ChatMessageReader().read(inputStream, outputStream, 
					clientAuthenticationStrategy);
			logger.info("call::clientName " + clientAuthenticationStrategy.getClientName().orElse(""));
				
			clientAuthenticationStrategy.getClientName().ifPresentOrElse(clientName->
			new ChatMessageReader().read(inputStream, outputStream, 
					new ClientConnectionStrategy(clientName)), 
			()->IOUtils.closeQuietly(socket));
					
		} catch (IOException e) {
			logger.error("", e);
			IOUtils.closeQuietly(socket);
		}
		
		return clientName;
		
	}

}
