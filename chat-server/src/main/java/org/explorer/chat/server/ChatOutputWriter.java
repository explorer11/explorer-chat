package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;

public enum ChatOutputWriter {

	INSTANCE;
	
	private static final Logger logger = LoggerFactory.getLogger(ChatOutputWriter.class);
	
    public void writeToAll(final ChatMessage chatMessage, final Collection<OutputStream> outputs){
        for(OutputStream outputStream : outputs){
            try {
                new ObjectOutputStream(outputStream).writeObject(chatMessage);
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }
	
	public void write(final ChatMessage chatMessage, final OutputStream outputStream) throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeObject(chatMessage);
	}
	
	
}
