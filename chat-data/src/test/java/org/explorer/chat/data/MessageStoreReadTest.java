package org.explorer.chat.data;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.explorer.chat.data.TestFiles.SINGLE_MESSAGE;
import static org.explorer.chat.data.TestFiles.MESSAGES_WITH_INSTANT;

public class MessageStoreReadTest {

    @Test
    public void shouldReadOneMessage() throws URISyntaxException, IOException {
        final Path singleMessagePath = Path.of(
                Objects.requireNonNull(MessageStoreTest.class.getResource(
                        SINGLE_MESSAGE)).toURI());

        final MessageStore messageStore = new MessageStore(singleMessagePath);

        assertThat(messageStore.findAll())
                .hasSize(1)
                .containsOnly(new ChatMessage(
                        ChatMessageType.SENTENCE, "user", "bonjour"));
    }

    @Test
    public void shouldReadSeveralMessages() throws URISyntaxException, IOException {
        final Path messagesPath = Path.of(
                Objects.requireNonNull(MessageStoreTest.class.getResource(
                        MESSAGES_WITH_INSTANT)).toURI());

        final MessageStore messageStore = new MessageStore(messagesPath);

        assertThat(messageStore.findAll())
                .hasSize(3)
                .containsOnly(
                        new ChatMessage(ChatMessageType.SENTENCE, "user", "bonjour"),
                        new ChatMessage(ChatMessageType.SENTENCE, "first", "hello",
                                Instant.parse("2021-02-24T22:10:50Z")),
                        new ChatMessage(ChatMessageType.SENTENCE, "user", "how",
                                Instant.parse("2021-02-24T22:11:05Z")));
    }
}
