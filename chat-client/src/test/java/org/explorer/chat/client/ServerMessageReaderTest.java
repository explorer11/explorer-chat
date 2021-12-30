package org.explorer.chat.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Instant;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.client.command.ChatActionCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServerMessageReaderTest {
	
	private final ChatActionCommand chatActionCommand = Mockito.mock(ChatActionCommand.class);

	private ServerMessageReader serverMessageReader;
	
	private final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
            .withMessageType(ChatMessageType.LIST)
			.withFromUserMessage("").withMessage("al,mo").withInstant(Instant.now())
            .build();
	
	@Before
	public void before() {

        final String fileName = "unitTestFile.txt";
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream output = new ObjectOutputStream(fileOutputStream)){
			
			output.writeObject(chatMessage);

			FileInputStream fileInputStream = new FileInputStream(new File(fileName));
			
			serverMessageReader = new ServerMessageReader(fileInputStream, chatActionCommand);

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void server_message_is_correctly_read() {
		serverMessageReader.run();
		Mockito.verify(chatActionCommand, Mockito.times(1)).performServerMessage(
		        Mockito.eq(chatMessage));
	}

}
