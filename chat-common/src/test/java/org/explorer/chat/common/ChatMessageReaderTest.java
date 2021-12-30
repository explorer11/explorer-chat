package org.explorer.chat.common;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ChatMessageReaderTest {

	private final ChatMessageReader chatMessageReader = new ChatMessageReader();
	
	private final ChatMessage chatMessage = new ChatMessage(ChatMessageType.LIST, "", "al,mo");
	
	private final ChatMessageReaderStrategy chatMessageReaderStrategy = Mockito.spy(ChatMessageReaderStrategy.class);
	
	@Test
	public void server_message_is_correctly_read() {
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(
				"unitTestFile.txt");
			ObjectOutputStream output = new ObjectOutputStream(fileOutputStream)){
			
			output.writeObject(chatMessage);

			FileInputStream fileInputStream = new FileInputStream("unitTestFile.txt");
			
			chatMessageReader.read(fileInputStream, chatMessageReaderStrategy);

		} catch (final IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		Mockito.verify(chatMessageReaderStrategy, Mockito.times(1))
                .apply(Mockito.eq(chatMessage));
	}

}
