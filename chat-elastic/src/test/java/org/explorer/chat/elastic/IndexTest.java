package org.explorer.chat.elastic;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IndexTest {

    private final Index index = new Index();

    @Test
    public void shouldFindMessages() throws IOException {
        final File file = new File(Objects.requireNonNull(IndexTest.class.getResource(
                "/messages.txt")).getFile());
        final String[] arguments = new String[]{file.getAbsolutePath()};

        assertThat(index.getMessages(arguments)).containsOnly(
                new ChatMessage(ChatMessageType.SENTENCE, "user", "bonjour"),
                new ChatMessage(ChatMessageType.SENTENCE, "first", "hello",
                        Instant.parse("2021-02-24T22:10:50Z")),
                new ChatMessage(ChatMessageType.SENTENCE, "user", "how",
                        Instant.parse("2021-02-24T22:11:05Z")));
    }

    @Test
    public void should_throw_exception_on_invalid_messages_path() {
        final String notExistingPath = "test";
        assertThatThrownBy(() -> index.getMessages(new String[]{notExistingPath}))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("path " + notExistingPath + " does not exist");
    }

    @Test
    public void should_throw_exception_on_missing_path_argument() {
        assertThatThrownBy(() -> index.getMessages(new String[0]))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
}