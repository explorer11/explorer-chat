package org.explorer.chat.common;

import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatMessageTest {

    private static final String MESSAGE = "message";
    private static final String FROM_USER_MESSAGE = "from";

    @Test
    public void shouldFormatToStringWithoutInstant() {

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        assertThat(chatMessage.toString()).isEqualTo(ChatMessageType.SENTENCE + "::" +
                FROM_USER_MESSAGE + "::" + MESSAGE);
    }

    @Test
    public void shouldFormatToString() {
        final String instant = "2007-12-03T10:15:30Z";

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .withInstant(Instant.parse(instant))
                .build();

        assertThat(chatMessage.toString()).isEqualTo(ChatMessageType.SENTENCE + "::" +
                FROM_USER_MESSAGE + "::" + MESSAGE + "::" + instant);
    }

    @Test
    public void shouldBeEqualsWithSameFields() {
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        final ChatMessage otherMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        assertThat(chatMessage).isEqualTo(otherMessage);
    }

    @Test
    public void shouldBeEqualsWithSameFieldsAndInstant() {
        final String instant = "2007-12-03T10:15:30Z";

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .withInstant(Instant.parse(instant))
                .build();

        final ChatMessage otherMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .withInstant(Instant.parse(instant))
                .build();

        assertThat(chatMessage).isEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentMessageField() {
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        final ChatMessage otherMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage("other")
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentUserField() {
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage("other")
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        final ChatMessage otherMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentMessageTypeField() {
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.ARRIVAL)
                .build();

        final ChatMessage otherMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .build();

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentInstantField() {
        final String instant1 = "2007-12-03T10:15:30Z";
        final String instant2 = "2007-12-03T10:15:50Z";

        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .withInstant(Instant.parse(instant1))
                .build();

        final ChatMessage otherMessage = new ChatMessage.ChatMessageBuilder()
                .withMessage(MESSAGE)
                .withFromUserMessage(FROM_USER_MESSAGE)
                .withMessageType(ChatMessageType.SENTENCE)
                .withInstant(Instant.parse(instant2))
                .build();

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

}