package org.explorer.chat.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class ChatMessageReader {

	public void read(InputStream inputStream, OutputStream outputStream, ChatMessageReaderStrategy strategy) {
		
		boolean connectionOpened = true;
		while(connectionOpened) {
			ChatMessage chatMessage = null;
			try {
				chatMessage = (ChatMessage) new ObjectInputStream(inputStream).readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				IOUtils.closeQuietly(inputStream);
				connectionOpened = false;
				strategy.handleInterruption();
			}
			
			if(chatMessage != null) {
				boolean closeConnection = strategy.apply(chatMessage, outputStream);
				if(closeConnection) {
                    connectionOpened = false;
                }
			}
		}
	}
}
