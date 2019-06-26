package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageReaderStrategy;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.collect.MessageIndexing;

import java.io.OutputStream;

public class ClientConnectionStrategy implements ChatMessageReaderStrategy {
	
	private final String clientName;
	private final MessageIndexing messageIndexing;

	ClientConnectionStrategy(String clientName, final MessageIndexing messageIndexing) {
		super();
		this.clientName = clientName;
		this.messageIndexing = messageIndexing;
	}

	@Override
	public boolean apply(ChatMessage chatMessage, OutputStream outputStream) {

		final ChatMessage builtChatMessage = new ChatMessage.ChatMessageBuilder()
				.withMessageType(ChatMessageType.SENTENCE)
				.withFromUserMessage(clientName)
				.withMessage(chatMessage.getMessage())
				.build();
		ChatOutputWriter.INSTANCE.writeToAll(builtChatMessage);

		messageIndexing.write(builtChatMessage);

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
