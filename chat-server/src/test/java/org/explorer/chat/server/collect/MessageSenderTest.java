package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageSenderTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldRead() throws InterruptedException {
        final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(1);
        final MessageSender messageSender = new MessageSender( queue);

        queue.put(new ChatMessage.ChatMessageBuilder()
                .withMessage("message")
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .build());

        final ChatMessage chatMessage = messageSender.read();

        assertThat(chatMessage).isNotNull();

    }

    @Test
    public void shouldSend() throws InterruptedException, IOException {
        final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(1);
        final MessageSender messageSender = new MessageSender(
                temporaryFolder.newFile().toPath(), queue);
        final MessageSender messageSenderSpy = Mockito.spy(messageSender);

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage("message")
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .build();
        queue.put(chatMessage);

        messageSenderSpy.readAndSend();

        Mockito.verify(messageSenderSpy, Mockito.times(1))
                .send(Mockito.eq(chatMessage));

    }
}