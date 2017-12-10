package org.explorer.chat.common;

import java.io.OutputStream;

public interface ChatMessageReaderStrategy {
	
	public void handleInterruption();
	
	public boolean apply(ChatMessage chatMessage, OutputStream outputStream);
	
}
