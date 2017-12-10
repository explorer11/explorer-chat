package org.explorer.chat.client;

public class ClientArgs {

	private final Boolean localConnection;
	private final String serverIP;
	private final Integer localPort;

	public ClientArgs(Boolean localConnection, String serverIP, Integer localPort) {
		super();
		this.localConnection = localConnection;
		this.serverIP = serverIP;
		this.localPort = localPort;
	}

	public Boolean getLocalConnection() {
		return localConnection;
	}

	public String getServerIP() {
		return serverIP;
	}

	public Integer getLocalPort() {
		return localPort;
	}
}
