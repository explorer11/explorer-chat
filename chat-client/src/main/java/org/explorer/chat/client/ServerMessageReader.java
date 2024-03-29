package org.explorer.chat.client;

import java.io.InputStream;

import org.explorer.chat.common.ChatMessageReader;
import org.explorer.chat.client.command.ChatActionCommand;

public class ServerMessageReader implements Runnable {

	private final InputStream inputStream;
	private final ChatActionCommand chatActionCommand;
	
	public ServerMessageReader(InputStream inputStream, ChatActionCommand chatActionCommand) {
		super();
		this.inputStream = inputStream;
		this.chatActionCommand = chatActionCommand;
	}

	@Override
	public void run() {
		System.out.println("ServerMessageReader::run");
        final ClientChatMessageStrategy strategy = new ClientChatMessageStrategy(chatActionCommand);
        new ChatMessageReader().read(inputStream, strategy);
		System.out.println("ServerMessageReader::run:finished");

	}

}
