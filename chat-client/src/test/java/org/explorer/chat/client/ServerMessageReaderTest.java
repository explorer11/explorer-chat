package org.explorer.chat.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.client.command.ChatActionCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServerMessageReaderTest {
	
	private ChatActionCommand chatActionHandler = Mockito.mock(ChatActionCommand.class);

	private ServerMessageReader serverMessageReader;
	
	private ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.LIST)
			.withFromUserMessage("").withMessage("al,mo").build();
	
	@Before
	public void before() {
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(
				"unitTestFile.txt");
			ObjectOutputStream output = new ObjectOutputStream(fileOutputStream);){
			
			output.writeObject(chatMessage);

			FileInputStream fileInputStream = new FileInputStream(new File(
					"unitTestFile.txt"));
			
			serverMessageReader = new ServerMessageReader(fileInputStream, chatActionHandler);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void server_message_is_correctly_read() {
		serverMessageReader.run();
		Mockito.verify(chatActionHandler, Mockito.times(1)).performServerMessage(Mockito.eq(chatMessage));
	}

}
