package org.explorer.chat.common;

public interface ChatMessageReaderStrategy {
	
	void handleInterruption();
	
	boolean apply(ChatMessage chatMessage);
	
}
