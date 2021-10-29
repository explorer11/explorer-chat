package org.explorer.chat.common;

import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatMessageTest {

    @Test
    public void shouldFormatToStringWithoutInstant() {
        final String message = "message";
        final String fromUserMessage = "from";

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(message)
                .withFromUserMessage(fromUserMessage)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        assertThat(chatMessage.toString()).isEqualTo(ChatMessageType.SENTENCE + "::" +
                fromUserMessage + "::" + message);
    }

    @Test
    public void shouldFormatToString() {
        final String message = "message";
        final String fromUserMessage = "from";
        final String instant = "2007-12-03T10:15:30Z";

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(message)
                .withFromUserMessage(fromUserMessage)
                .withMessageType(ChatMessageType.SENTENCE)
                .withInstant(Instant.parse(instant))
                .build();

        assertThat(chatMessage.toString()).isEqualTo(ChatMessageType.SENTENCE + "::" +
                fromUserMessage + "::" + message + "::" + instant);
    }
}