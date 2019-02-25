package org.explorer.chat.server;

import java.io.OutputStream;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageReaderStrategy;
import org.explorer.chat.common.ChatMessageType;

public class ClientConnectionStrategy implements ChatMessageReaderStrategy {
	
	private final String clientName;

	ClientConnectionStrategy(String clientName) {
		super();
		this.clientName = clientName;
	}

	@Override
	public boolean apply(ChatMessage chatMessage, OutputStream outputStream) {
		ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage.ChatMessageBuilder()
				.withMessageType(ChatMessageType.SENTENCE)
				.withFromUserMessage(clientName)
				.withMessage(chatMessage.getMessage())
				.build());
		return false;
	}

	@Override
	public void handleInterruption() {
		ChatOutputWriter.INSTANCE.remove(clientName);
		ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage.ChatMessageBuilder()
    			.withMessageType(ChatMessageType.LEAVING)
    			.withFromUserMessage("")
    			.withMessage(clientName)
    			.build());
		ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage.ChatMessageBuilder()
    			.withMessageType(ChatMessageType.LIST)
    			.withFromUserMessage("")
    			.withMessage(ChatOutputWriter.INSTANCE.getUsersList())
    			.build());		
	}

}
