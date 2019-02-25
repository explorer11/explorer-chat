package org.explorer.chat.client;

import java.io.OutputStream;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageReaderStrategy;
import org.explorer.chat.client.command.ChatActionCommand;

public class ClientChatMessageStrategy implements ChatMessageReaderStrategy {

	private final ChatActionCommand chatActionCommand;
	
	ClientChatMessageStrategy(ChatActionCommand chatActionCommand) {
		super();
		this.chatActionCommand = chatActionCommand;
	}

	@Override
	public boolean apply(ChatMessage chatMessage, OutputStream outputStream) {
		chatActionCommand.performServerMessage(chatMessage);
		return false;
	}

	@Override
	public void handleInterruption() {
	}

}
