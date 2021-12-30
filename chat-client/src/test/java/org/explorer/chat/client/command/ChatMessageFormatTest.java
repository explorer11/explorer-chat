package org.explorer.chat.client.command;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatMessageFormatTest {

    private static final String FROM = "from";
    private static final String MESSAGE = "message";

    @Test
    public void shouldFormatSentence() {
        final Instant instant = Instant.parse("2012-04-03T12:15:30.304430800Z");

        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM, MESSAGE, instant);

        final ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(instant);
        final int hoursOffset = zoneOffset.getTotalSeconds() / 3600;
        final int expected = 12 + hoursOffset;

        assertThat(ChatMessageFormat.formatSentence(chatMessage)).isEqualTo("2012-04-03 " +
                expected + ":15:30 : " +
                FROM + " : " + MESSAGE);
    }

    @Test
    public void shouldFormatSentenceWithoutInstant() {

        final ChatMessage chatMessage = new ChatMessage(
                ChatMessageType.SENTENCE, FROM, MESSAGE, null);

        assertThat(ChatMessageFormat.formatSentence(chatMessage)).isEqualTo(FROM + " : " + MESSAGE);
    }
}