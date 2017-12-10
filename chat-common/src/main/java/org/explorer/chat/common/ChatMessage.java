package org.explorer.chat.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -6016135869121638280L;

	private final ChatMessageType messageType;
	private final String fromUserMessage;
	private final String message;

	private ChatMessage(ChatMessageType messageType, String fromUserMessage, String message) {
		super();
		this.messageType = messageType;
		this.fromUserMessage = fromUserMessage;
		this.message = message;
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

	public static class ChatMessageBuilder {
		private ChatMessageType messageType;
		private String fromUserMessage;
		private String message;

		public ChatMessageBuilder withMessageType(ChatMessageType messageType) {
			this.messageType = messageType;
			return this;
		}

		public ChatMessageBuilder withFromUserMessage(String fromUserMessage) {
			this.fromUserMessage = fromUserMessage;
			return this;
		}

		public ChatMessageBuilder withMessage(String message) {
			this.message = message;
			return this;
		}

		public ChatMessage build() {
			return new ChatMessage(messageType, fromUserMessage, message);
		}
	}

	public static ChatMessageBuilder chatMessage() {
		return new ChatMessageBuilder();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromUserMessage == null) ? 0 : fromUserMessage.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatMessage other = (ChatMessage) obj;
		if (fromUserMessage == null) {
			if (other.fromUserMessage != null)
				return false;
		} else if (!fromUserMessage.equals(other.fromUserMessage))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (messageType != other.messageType)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String delimiter = "::";
		String returnedMessage = messageType.toString()+delimiter;
		if(StringUtils.isNotEmpty(fromUserMessage)){
			returnedMessage += fromUserMessage+delimiter;
		}
		returnedMessage += message;
		return returnedMessage;
	}

}
