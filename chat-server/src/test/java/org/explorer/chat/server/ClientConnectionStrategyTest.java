package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.collect.MessageIndexing;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.OutputStream;

public class ClientConnectionStrategyTest {

    @Test
    public void queue_is_filled(){
        final MessageIndexing messageIndexingSpy = Mockito.spy(new MessageIndexing());
        final String clientName = "client";
        final ClientConnectionStrategy clientConnectionStrategy = new ClientConnectionStrategy(
                clientName, messageIndexingSpy);
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage(clientName)
                .withMessage("")
                .build();

        clientConnectionStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));

        Mockito.verify(messageIndexingSpy).write(Mockito.eq(chatMessage));
    }
}