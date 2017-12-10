package org.explorer.chat.client;

import org.explorer.chat.client.command.ClientLaunchCommand;

public class Client {
	
	public static void main(String[] args) throws InterruptedException {
		
		ClientLaunchCommand clientLaunchCommand = new ClientLaunchCommand();
		clientLaunchCommand.start();
		
		System.out.println("Client::main:finished");
	}

}
