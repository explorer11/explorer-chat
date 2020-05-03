package org.explorer.chat.server;

import org.apache.commons.lang3.StringUtils;
import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageReaderStrategy;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.users.ConnectedUsers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class ClientAuthenticationStrategy implements ChatMessageReaderStrategy {
	
	private final ConnectedUsers connectedUsers;
    private String clientName;

    public ClientAuthenticationStrategy(final ConnectedUsers connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    @Override
	public void handleInterruption() {
	}

	@Override
	public boolean apply(final ChatMessage chatMessage, final OutputStream outputStream) {
		String clientName = chatMessage.getFromUserMessage();
		
		final String addResult = addClient(clientName, outputStream);
		if(!addResult.isEmpty()) {
		    sendError(addResult, outputStream);
		    return false;
        }
			
		this.clientName = clientName;

		ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage.ChatMessageBuilder()
                        .withMessageType(ChatMessageType.ARRIVAL)
                        .withFromUserMessage("")
                        .withMessage(clientName)
                        .build(),
                connectedUsers.getOutputs());

        final String usersList = connectedUsers.getUsersList();
        ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage.ChatMessageBuilder()
                        .withMessageType(ChatMessageType.LIST)
                        .withFromUserMessage("")
                        .withMessage(usersList)
                        .build(),
                connectedUsers.getOutputs());
		
		return true;
	}

	private String addClient(final String clientName, final OutputStream outputStream) {
        if(StringUtils.isBlank(clientName)){
            return "Remplissez le champ";
        }

        final ChatMessage welcomeMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.WELCOME)
                .withFromUserMessage("")
                .withMessage(clientName)
                .build();

        final boolean added;
        try {
            added = connectedUsers.add(clientName, outputStream, welcomeMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return "Une erreur s'est produite";
        }

        if(!added) {
            return "Vous avez saisi un nom déjà utilisé";
        }

        return "";
    }
	
	private void sendError(final String message, final OutputStream outputStream) {
		try {
			ChatOutputWriter.INSTANCE.write(new ChatMessage.ChatMessageBuilder()
					.withMessageType(ChatMessageType.CONNECTION_ERROR)
					.withFromUserMessage("")
					.withMessage(message)
					.build(), outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Optional<String> getClientName() {
		return Optional.ofNullable(clientName);
	}

}
