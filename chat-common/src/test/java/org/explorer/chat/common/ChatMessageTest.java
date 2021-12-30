package org.explorer.chat.common;

import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatMessageTest {

    private static final String MESSAGE = "message";
    private static final String FROM_USER_MESSAGE = "from";

    @Test
    public void shouldFormatToStringWithoutInstant() {

        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE);

        assertThat(chatMessage.toString()).isEqualTo(ChatMessageType.SENTENCE + "::" +
                FROM_USER_MESSAGE + "::" + MESSAGE);
    }

    @Test
    public void shouldFormatToString() {
        final String instant = "2007-12-03T10:15:30Z";

        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE, Instant.parse(instant));

        assertThat(chatMessage.toString()).isEqualTo(ChatMessageType.SENTENCE + "::" +
                FROM_USER_MESSAGE + "::" + MESSAGE + "::" + instant);
    }

    @Test
    public void shouldBeEqualsWithSameFields() {
        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE);

        final ChatMessage otherMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE);

        assertThat(chatMessage).isEqualTo(otherMessage);
    }

    @Test
    public void shouldBeEqualsWithSameFieldsAndInstant() {
        final String instant = "2007-12-03T10:15:30Z";

        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE, Instant.parse(instant));

        final ChatMessage otherMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE, Instant.parse(instant));

        assertThat(chatMessage).isEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentMessageField() {
        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE);

        final ChatMessage otherMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, "other");

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentUserField() {
        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, "other", MESSAGE);

        final ChatMessage otherMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE);

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentMessageTypeField() {
        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.ARRIVAL, FROM_USER_MESSAGE, MESSAGE);

        final ChatMessage otherMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE);

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

    @Test
    public void shouldNotBeEqualsWithDifferentInstantField() {
        final String instant1 = "2007-12-03T10:15:30Z";
        final String instant2 = "2007-12-03T10:15:50Z";

        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE, Instant.parse(instant1));

        final ChatMessage otherMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM_USER_MESSAGE, MESSAGE, Instant.parse(instant2));

        assertThat(chatMessage).isNotEqualTo(otherMessage);
    }

}