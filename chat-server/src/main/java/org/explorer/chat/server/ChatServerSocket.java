package org.explorer.chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.explorer.chat.common.ServerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ChatServerSocket {
	
	INSTANCE;
	
	private final Logger logger = LoggerFactory.getLogger(ChatServerSocket.class);
	
	ChatServerSocket() {
		try {
			serverSocket = new ServerSocket(ServerPort.PORT, max_pending_connections_allowed, InetAddress.getLocalHost());
		} catch (IOException e) {
			logger.error("", e);
			throw new RuntimeException("unable to start server socket");
		}
	}
	
	/**
	 * Exception occurs when a client wants to connect and when there are 2 clients already waiting
	 */
	public static final int max_pending_connections_allowed = 2;
	
	private final ServerSocket serverSocket;

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	
}
