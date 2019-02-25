package org.explorer.chat.client;

public class ClientArgs {

	private final String serverIP;
	private final Integer localPort;

	public ClientArgs(String serverIP, Integer localPort) {
		super();
		this.serverIP = serverIP;
		this.localPort = localPort;
	}

	public String getServerIP() {
		return serverIP;
	}

	public Integer getLocalPort() {
		return localPort;
	}
}
