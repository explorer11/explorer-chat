package org.explorer.chat.client;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageReaderStrategy;
import org.explorer.chat.client.command.ChatActionCommand;

public class ClientChatMessageStrategy implements ChatMessageReaderStrategy {

	private final ChatActionCommand chatActionCommand;
	
	ClientChatMessageStrategy(final ChatActionCommand chatActionCommand) {
		super();
		this.chatActionCommand = chatActionCommand;
	}

	@Override
	public boolean apply(final ChatMessage chatMessage) {
		chatActionCommand.performServerMessage(chatMessage);
		return false;
	}

	@Override
	public void handleInterruption() {
	}

}
