package org.explorer.chat.common;

import java.io.OutputStream;

public interface ChatMessageReaderStrategy {
	
	void handleInterruption();
	
	boolean apply(ChatMessage chatMessage, OutputStream outputStream);
	
}
