package org.explorer.chat.client;

import org.explorer.chat.client.command.ClientLaunchCommand;

public class Client {
	
	public static void main(String[] args) {
		
		final ClientLaunchCommand clientLaunchCommand = new ClientLaunchCommand();
		clientLaunchCommand.run();
		
		System.out.println("Client::main:finished");
	}

}
