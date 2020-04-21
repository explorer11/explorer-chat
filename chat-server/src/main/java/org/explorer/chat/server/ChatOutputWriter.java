package org.explorer.chat.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.UsersList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ChatOutputWriter {

	INSTANCE;
	
	private static final Logger logger = LoggerFactory.getLogger(ChatOutputWriter.class);
	
	private final Map<String, OutputStream> objectWriters = new ConcurrentHashMap<>();
	
	public void add(String user, OutputStream p){
		objectWriters.put(user, p);
	}
	
	public void remove(String user){
		objectWriters.remove(user);
	}
	
	public void writeToAll(ChatMessage chatMessage){
		for(Entry<String, OutputStream> outputStream : objectWriters.entrySet()){
			try {
				new ObjectOutputStream(outputStream.getValue()).writeObject(chatMessage);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
	
	public void write(ChatMessage chatMessage, OutputStream outputStream) throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeObject(chatMessage);
	}
	
	public Set<String> getUsersNames(){
		return objectWriters.keySet();
	}
	
	public String getUsersList(){
		return new UsersList().getUsersList(objectWriters.keySet());
	}
	
	
}
