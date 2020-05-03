package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.users.ConnectedUsers;
import org.explorer.chat.users.Users;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;

public class ClientAuthenticationStrategyTest {

    private final Users users = Mockito.mock(Users.class);
    private final ConnectedUsers connectedUsers = new ConnectedUsers(users);
	private final ClientAuthenticationStrategy clientAuthenticationStrategy =
            new ClientAuthenticationStrategy(connectedUsers);
	
	@Before
	public void before() {
		Mockito.reset(users);
	}
	
	@Test
	public void right_client_name_is_correctly_handled() throws IOException {
		final String userName= "mi";

		Mockito.when(users.createNewUser(Mockito.eq(userName))).thenReturn(true);
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage(userName).withMessage("").build();
		boolean result = clientAuthenticationStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));
		
		Assert.assertTrue(result);
        Assert.assertEquals(1, connectedUsers.getUsersNames().size());
        Assert.assertEquals(userName, connectedUsers.getUsersNames().iterator().next());
	}
	
	@Test
	public void empty_client_name_is_incorrect() {
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage("").withMessage("").build();
		boolean result = clientAuthenticationStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));
		Assert.assertFalse(result);
	}
	
	@Test
	public void an_already_used_client_name_is_incorrect() throws IOException {
        final String client = "client";
        Mockito.when(users.createNewUser(Mockito.eq(client))).thenReturn(true);
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage(client).withMessage("").build();

		clientAuthenticationStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));

		boolean secondAuthentication = clientAuthenticationStrategy.apply(
		        chatMessage, Mockito.mock(OutputStream.class));

		Assert.assertFalse(secondAuthentication);
	}
}
