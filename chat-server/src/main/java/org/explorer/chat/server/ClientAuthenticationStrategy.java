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
    private final OutputStream outputStream;
    private String clientName;

    public ClientAuthenticationStrategy(final ConnectedUsers connectedUsers, final OutputStream outputStream) {
        this.connectedUsers = connectedUsers;
        this.outputStream = outputStream;
    }

    @Override
	public void handleInterruption() {
	}

	@Override
	public boolean apply(final ChatMessage chatMessage) {
		final String clientName = chatMessage.getFromUserMessage();
		
		final AuthenticationResult authenticationResult = addClient(clientName, outputStream);

		if(!authenticationResult.isSuccess()) {
		    sendError(authenticationResult.getResult(), outputStream);
		    return false;
        }
			
		this.clientName = clientName;

		ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage(
                ChatMessageType.ARRIVAL, "", clientName),
                connectedUsers.getOutputs());

        final String usersList = connectedUsers.getUsersList();
        ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage(
                ChatMessageType.LIST, "", usersList),
                connectedUsers.getOutputs());

		return true;
	}

	private AuthenticationResult addClient(final String clientName, final OutputStream outputStream) {
        if(StringUtils.isBlank(clientName)){
            return new AuthenticationResult("Remplissez le champ");
        }

        final ChatMessage welcomeMessage = new ChatMessage(
                ChatMessageType.WELCOME, "", clientName);

        final boolean added;
        try {
            added = connectedUsers.add(clientName, outputStream, welcomeMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return new AuthenticationResult("Une erreur s'est produite");
        }

        if(!added) {
            return new AuthenticationResult("Vous avez saisi un nom déjà utilisé");
        }

        return new AuthenticationResult("");
    }
	
	private void sendError(final String message, final OutputStream outputStream) {
		try {
			ChatOutputWriter.INSTANCE.write(new ChatMessage(
                    ChatMessageType.CONNECTION_ERROR, "", message),
					outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Optional<String> getClientName() {
		return Optional.ofNullable(clientName);
	}

	private static class AuthenticationResult {

        private final String result;

        private AuthenticationResult(final String result) {
            this.result = result;
        }

        private boolean isSuccess() {
            return result.isEmpty();
        }

        private String getResult() {
            return result;
        }
    }

}
