package org.explorer.chat.common;

import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

public record ChatMessage(ChatMessageType messageType,
						  String fromUserMessage,
						  String message,
						  Instant instant) implements Serializable {

	@Serial
	private static final long serialVersionUID = -6016135869121638280L;

	public ChatMessage(ChatMessageType chatMessageType, String fromUserMessage, String message) {
		this(chatMessageType, fromUserMessage, message, null);
	}

	public ChatMessageType getMessageType() {
		return messageType;
	}

	public String getMessage() {
		return message;
	}

	public String getFromUserMessage() {
		return fromUserMessage;
	}

    public Instant getInstant() {
        return instant;
    }

	@Override
	public String toString() {
		String delimiter = "::";
		String returnedMessage = messageType.toString() + delimiter;
		if(StringUtils.isNotEmpty(fromUserMessage)){
			returnedMessage += fromUserMessage + delimiter;
		}
		returnedMessage += message;
		if(instant != null) {
            returnedMessage += delimiter + instant;
        }
		return returnedMessage;
	}

}
