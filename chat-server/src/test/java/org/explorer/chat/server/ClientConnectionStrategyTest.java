package org.explorer.chat.server;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.server.collect.MessageCollector;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientConnectionStrategyTest {

    @Test
    public void queue_is_filled(){
        final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(1);
        final MessageCollector messageCollector = new MessageCollector(queue);
        final ClientConnectionStrategy clientConnectionStrategy = new ClientConnectionStrategy(
                "client", messageCollector);
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .withMessage("")
                .build();

        clientConnectionStrategy.apply(chatMessage, Mockito.mock(OutputStream.class));

        assertThat(queue).hasSize(1);
    }
}