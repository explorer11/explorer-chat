package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;
import org.mockito.Mockito;

public class MessageIndexingTest {

    @Test
    public void shouldSend() throws InterruptedException {
        final MessageSender messageSenderSpy = Mockito.spy(new MessageSender());
        Mockito.doNothing().when(messageSenderSpy).send(Mockito.any(ChatMessage.class));

        final MessageIndexing messageIndexing = new MessageIndexing(messageSenderSpy);

        messageIndexing.start();

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .withMessage("")
                .build();
        messageIndexing.write(chatMessage);

        Thread.sleep(100);

        Mockito.verify(messageSenderSpy, Mockito.times(1))
                .send(Mockito.eq(chatMessage));
    }
}