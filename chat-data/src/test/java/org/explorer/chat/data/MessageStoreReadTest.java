package org.explorer.chat.data;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.explorer.chat.data.TestFiles.SINGLE_MESSAGE;

public class MessageStoreReadTest {

    @Test
    public void shouldReadOneMessage() throws URISyntaxException, IOException {
        final Path singleMessageFile = Path.of(
                Objects.requireNonNull(MessageStoreTest.class.getResource(
                        SINGLE_MESSAGE)).toURI());

        final MessageStore messageStore = new MessageStore(singleMessageFile);

        assertThat(messageStore.findAll())
                .hasSize(1)
                .containsOnly(new ChatMessage.ChatMessageBuilder()
                        .withMessageType(ChatMessageType.SENTENCE)
                        .withMessage("bonjour")
                        .withFromUserMessage("user")
                        .build());
    }
}
