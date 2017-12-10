package org.explorer.chat.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageReaderStrategy;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.ChatOutputWriter;

public class ClientAuthenticationStrategy implements ChatMessageReaderStrategy {
	
	private String clientName;

	@Override
	public void handleInterruption() {
	}

	@Override
	public boolean apply(ChatMessage chatMessage, OutputStream outputStream) {
		String clientName = chatMessage.getFromUserMessage();
		
		if(StringUtils.isBlank(clientName) || ChatOutputWriter.INSTANCE.getUsersNames().contains(clientName)){
			handleWrongClientName(clientName, outputStream);
			return false;
		}
		
		try {
			ChatOutputWriter.INSTANCE.write(new ChatMessage.ChatMessageBuilder()
					.withMessageType(ChatMessageType.WELCOME)
					.withFromUserMessage("")
					.withMessage(clientName)
					.build(), outputStream);
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
			
		this.clientName = clientName;
		ChatOutputWriter.INSTANCE.add(clientName, outputStream);
		ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage.ChatMessageBuilder()
				.withMessageType(ChatMessageType.ARRIVAL).withFromUserMessage("").withMessage(clientName).build());
		ChatOutputWriter.INSTANCE.writeToAll(new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.LIST)
				.withFromUserMessage("").withMessage(ChatOutputWriter.INSTANCE.getUsersList()).build());
		
		return true;
	}
	
	private void handleWrongClientName(String clientName, OutputStream outputStream) {
		String message = (StringUtils.isNotBlank(clientName) ? "Vous avez saisi un nom déjà utilisé" : "Remplissez le champ");
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

	public Optional<String> getClientName() {
		return Optional.ofNullable(clientName);
	}

}
