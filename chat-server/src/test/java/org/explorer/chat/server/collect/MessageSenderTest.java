package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageSenderTest {

    @Test
    public void shouldRead() throws InterruptedException {
        final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(1);
        final MessageSender messageSender = new MessageSender(queue);

        queue.put(new ChatMessage.ChatMessageBuilder()
                .withMessage("message")
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .build());

        final ChatMessage chatMessage = messageSender.read();

        assertThat(chatMessage).isNotNull();

    }
}