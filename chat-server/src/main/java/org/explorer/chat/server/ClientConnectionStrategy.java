package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageReaderStrategy;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.collect.MessageIndexing;
import org.explorer.chat.server.users.ConnectedUsers;

public class ClientConnectionStrategy implements ChatMessageReaderStrategy {
	
	private final String clientName;
	private final MessageIndexing messageIndexing;
	private final ConnectedUsers connectedUsers;

	ClientConnectionStrategy(final String clientName,
                             final MessageIndexing messageIndexing,
                             final ConnectedUsers connectedUsers) {
		super();
		this.clientName = clientName;
		this.messageIndexing = messageIndexing;
		this.connectedUsers = connectedUsers;
	}

	@Override
	public boolean apply(final ChatMessage chatMessage) {

		final ChatMessage builtChatMessage = new ChatMessage(
				ChatMessageType.SENTENCE,
				clientName,
				chatMessage.getMessage(),
				chatMessage.getInstant());
		ChatOutputWriter.INSTANCE.writeToAll(builtChatMessage, connectedUsers.getOutputs());

		messageIndexing.write(builtChatMessage);

		return false;
	}

	@Override
	public void handleInterruption() {
        connectedUsers.remove(clientName);

        final ChatMessage leavingMessage = new ChatMessage(ChatMessageType.LEAVING, "", clientName);
        ChatOutputWriter.INSTANCE.writeToAll(leavingMessage, connectedUsers.getOutputs());

        final String usersList = connectedUsers.getUsersList();
        final ChatMessage listMessage = new ChatMessage(ChatMessageType.LIST, "", usersList);
        ChatOutputWriter.INSTANCE.writeToAll(listMessage, connectedUsers.getOutputs());
	}

}
