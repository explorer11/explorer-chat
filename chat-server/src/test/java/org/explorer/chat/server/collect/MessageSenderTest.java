package org.explorer.chat.server.collect;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.data.MessageStore;
import org.explorer.chat.save.MessageSave;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;
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

        queue.put(new ChatMessage(ChatMessageType.SENTENCE, "user", "message"));

        final ChatMessage chatMessage = messageSender.read();

        assertThat(chatMessage).isNotNull();

    }

    @Test
    public void shouldSend() throws InterruptedException, IOException {
        final BlockingQueue<ChatMessage> queue = new ArrayBlockingQueue<>(1);
        final Path messagesPath = temporaryFolder.newFile().toPath();
        final MessageSave messageSave = new MessageStore(messagesPath);
        final MessageSender messageSender = new MessageSender(
                messageSave, queue);
        final MessageSender messageSenderSpy = Mockito.spy(messageSender);

        final ChatMessage chatMessage = new ChatMessage(ChatMessageType.SENTENCE, "user", "message");
        queue.put(chatMessage);

        messageSenderSpy.readAndSend();

        Mockito.verify(messageSenderSpy, Mockito.times(1))
                .send(Mockito.eq(chatMessage));

    }
}