package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.data.MessageStore;
import org.explorer.chat.server.users.ConnectedUsers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientAuthenticationStrategyTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private ConnectedUsers connectedUsers;
	private ClientAuthenticationStrategy clientAuthenticationStrategy;
	
	@Before
	public void before() throws IOException {
        final File temporaryFile = temporaryFolder.newFile();
        final String temporaryPath = temporaryFile.getAbsolutePath();
        connectedUsers = new ConnectedUsers(temporaryPath, Mockito.mock(MessageStore.class));
        clientAuthenticationStrategy = new ClientAuthenticationStrategy(connectedUsers,
                Mockito.mock(OutputStream.class));
	}
	
	@Test
	public void right_client_name_is_correctly_handled() {
		final String userName= "mi";

		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage(userName).withMessage("").build();
		boolean result = clientAuthenticationStrategy.apply(chatMessage);
		
        assertThat(result).isTrue();
        assertThat(connectedUsers.getUsersNames()).containsExactly(userName);
	}
	
	@Test
	public void empty_client_name_is_incorrect() {
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage("").withMessage("").build();
		boolean result = clientAuthenticationStrategy.apply(chatMessage);
        assertThat(result).isFalse();
	}
	
	@Test
	public void an_already_used_client_name_is_incorrect() {
        final String client = "client";
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage(client).withMessage("").build();

		clientAuthenticationStrategy.apply(chatMessage);

		boolean secondAuthentication = clientAuthenticationStrategy.apply(chatMessage);

        assertThat(secondAuthentication).isFalse();
	}
}
