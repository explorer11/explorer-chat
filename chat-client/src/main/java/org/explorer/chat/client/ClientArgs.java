package org.explorer.chat.client;

public class ClientArgs {

	private final String serverIP;

	public ClientArgs(String serverIP) {
		super();
		this.serverIP = serverIP;
	}

	public String getServerIP() {
		return serverIP;
	}
}
