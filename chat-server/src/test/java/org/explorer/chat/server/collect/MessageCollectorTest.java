package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCollectorTest {

    @Test
    public void queue_is_filled(){
        final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(1);
        final MessageCollector messageCollector = new MessageCollector(queue);
        final ChatMessage chatMessage = new ChatMessage(ChatMessageType.SENTENCE, "user", "");

        messageCollector.write(chatMessage);

        assertThat(queue).hasSize(1);
    }
}