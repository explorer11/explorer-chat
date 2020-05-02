package org.explorer.chat.server;

import java.io.OutputStream;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.users.ConnectedUsers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ClientAuthenticationStrategyTest {
	
	private final ClientAuthenticationStrategy clientAuthenticationStrategy =
            new ClientAuthenticationStrategy(Mockito.mock(ConnectedUsers.class));
	
	@Before
	public void before() {
		ChatOutputWriter.INSTANCE.getUsersNames().forEach(ChatOutputWriter.INSTANCE::remove);
	}
	
	@Test
	public void right_client_name_is_correctly_handled() {
		final String userName= "mi";
		
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage(userName).withMessage("").build();
		boolean result = clientAuthenticationStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));
		
		Assert.assertTrue(result);
        Assert.assertEquals(1, ChatOutputWriter.INSTANCE.getUsersNames().size());
        Assert.assertEquals(userName, ChatOutputWriter.INSTANCE.getUsersNames().iterator().next());
	}
	
	@Test
	public void empty_client_name_is_incorrect() {
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage("").withMessage("").build();
		boolean result = clientAuthenticationStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));
		Assert.assertFalse(result);
	}
	
	@Test
	public void an_already_used_client_name_is_incorrect() {
		ChatOutputWriter.INSTANCE.add("client", Mockito.mock(OutputStream.class));
		ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder().withMessageType(ChatMessageType.WELCOME)
				.withFromUserMessage("client").withMessage("").build();
		boolean result = clientAuthenticationStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));
		Assert.assertFalse(result);
	}
}
