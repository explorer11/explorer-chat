package org.explorer.chat.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ChatMessageReaderTest {

	private ChatMessageReader chatMessageReader = new ChatMessageReader();
	
	private ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.LIST)
			.withFromUserMessage("").withMessage("al,mo").build();
	
	private ChatMessageReaderStrategy chatMessageReaderStrategy = Mockito.spy(ChatMessageReaderStrategy.class);
	
	@Test
	public void server_message_is_correctly_read() {
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(
				"unitTestFile.txt");
			ObjectOutputStream output = new ObjectOutputStream(fileOutputStream);
			OutputStream outputStream = new ByteArrayOutputStream();){
			
			output.writeObject(chatMessage);

			FileInputStream fileInputStream = new FileInputStream(new File(
					"unitTestFile.txt"));
			
			chatMessageReader.read(fileInputStream, outputStream, chatMessageReaderStrategy);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		Mockito.verify(chatMessageReaderStrategy, Mockito.times(1)).apply(Mockito.eq(chatMessage), Mockito.any());
	}

}
