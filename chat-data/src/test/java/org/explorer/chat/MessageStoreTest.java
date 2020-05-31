package org.explorer.chat;

import org.apache.commons.lang3.tuple.Pair;
import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageStoreTest {

    private final File singleMessageFile = new File(MessageStoreTest.class.getResource(
            "/single_message.txt").getFile());
    private final File messagesFile = new File(MessageStoreTest.class.getResource(
            "/messages.txt").getFile());

    private final MessageStore singleMessageStore = new MessageStore(Paths.get(singleMessageFile.getAbsolutePath()));
    private final MessageStore messagesStore = new MessageStore(Paths.get(messagesFile.getAbsolutePath()));

    @Test
    public void shouldSave() {
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .withMessage("bonjour")
                .build();
        singleMessageStore.save(chatMessage);
    }

    @Test
    public void shouldThrowExceptionOnWrongFormatMessage() {
        final File file = new File(MessageStoreTest.class.getResource(
                "/messages_wrongFormat.txt").getFile());
        final MessageStore messageStore = new MessageStore(Paths.get(file.getAbsolutePath()));
        assertThatThrownBy(() -> messageStore.readLast(1))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void shouldReadZeroMessage() throws IOException {
        assertThat(singleMessageStore.readLast(0)).isEmpty();
    }

    @Test
    public void shouldReadOneMessage() throws IOException {
        assertThat(singleMessageStore.readLast(1)).containsExactly(Pair.of("user", "bonjour"));
    }

    @Test
    public void shouldReadOneLastMessage() throws IOException {
        assertThat(messagesStore.readLast(1)).containsExactly(Pair.of("user", "how"));
    }

    @Test
    public void shouldReadSeveralMessages() throws IOException {
        assertThat(messagesStore.readLast(3)).containsExactly(
                        Pair.of("user", "bonjour"),
                        Pair.of("first", "hello"),
                        Pair.of("user", "how"));
    }

    @Test
    public void shouldReadMoreMessagesThanExisting() throws IOException {
        assertThat(messagesStore.readLast(10)).containsExactly(
                Pair.of("user", "bonjour"),
                Pair.of("first", "hello"),
                Pair.of("user", "how"));
    }
}