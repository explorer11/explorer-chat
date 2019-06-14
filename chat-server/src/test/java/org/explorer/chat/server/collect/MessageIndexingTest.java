package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;
import org.mockito.Mockito;

public class MessageIndexingTest {

    @Test
    public void shouldSend(){
        final MessageIndexing messageIndexingSpy = Mockito.spy(new MessageIndexing());

        messageIndexingSpy.start();

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .withMessage("")
                .build();
        messageIndexingSpy.write(chatMessage);

        Mockito.verify(messageIndexingSpy, Mockito.times(1))
                .send(Mockito.eq(chatMessage));
    }
}