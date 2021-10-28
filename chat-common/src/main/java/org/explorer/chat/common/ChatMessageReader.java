package org.explorer.chat.common;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

public class ChatMessageReader {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageReader.class);

	public void read(InputStream inputStream, OutputStream outputStream, ChatMessageReaderStrategy strategy) {
		
		boolean connectionOpened = true;
		while(connectionOpened) {
			ChatMessage chatMessage = null;
			try {
			    logger.debug("waiting for a message");
				chatMessage = (ChatMessage) new ObjectInputStream(inputStream).readObject();
                logger.debug("message received {}", chatMessage.toString());
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
