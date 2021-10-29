package org.explorer.chat.data;

import org.junit.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistedMessageTest {

    @Test
    public void shouldGenerateToString() {
        final String user = "user";
        final String message = "hello";
        final String instant = "2003-08-25T10:15:30Z";
        final PersistedMessage persistedMessage = new PersistedMessage(
                user, message, Instant.parse(instant));

        assertThat(persistedMessage.toString()).isEqualTo(user + "|" + message + "|" + instant);
    }

    @Test
    public void shouldParseMessageWithInstant() {
        final String user = "user";
        final String message = "hello";
        final String instant = "2014-02-28T15:20:50Z";

        final PersistedMessage persistedMessage = PersistedMessage.parse(
                user + "|" + message + "|" + instant);

        assertThat(persistedMessage.getFrom()).isEqualTo(user);
        assertThat(persistedMessage.getMessage()).isEqualTo(message);

        final Optional<Instant> parsedInstant = persistedMessage.getInstant();
        assertThat(parsedInstant).isNotEmpty();
        assertThat(parsedInstant.get()).isEqualTo(Instant.parse(instant));
    }

    @Test
    public void shouldParseMessageWithoutInstant() {
        final String user = "user";
        final String message = "hello";

        final PersistedMessage persistedMessage = PersistedMessage.parse(
                user + "|" + message);

        assertThat(persistedMessage.getFrom()).isEqualTo(user);
        assertThat(persistedMessage.getMessage()).isEqualTo(message);
        assertThat(persistedMessage.getInstant()).isEmpty();
    }

    @Test
    public void shouldDetectEquals() {
        final String instant = "2021-02-24T22:10:50Z";
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message", Instant.parse(instant));
        final PersistedMessage message2 = new PersistedMessage(
                "user", "message", Instant.parse(instant));

        assertThat(message1).isEqualTo(message2);
    }

    @Test
    public void shouldDetectNotEqualsOnFromField() {
        final String instant = "2021-02-24T22:10:50Z";
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message", Instant.parse(instant));
        final PersistedMessage message2 = new PersistedMessage(
                "notEqual", "message", Instant.parse(instant));

        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    public void shouldDetectNotEqualsOnMessageField() {
        final String instant = "2021-02-24T22:10:50Z";
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message", Instant.parse(instant));
        final PersistedMessage message2 = new PersistedMessage(
                "user", "notEqual", Instant.parse(instant));

        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    public void shouldDetectNotEqualsOnInstantField() {
        final String instant = "2021-02-24T22:10:50Z";
        final String notEqual = "2019-12-24T22:10:50Z";
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message", Instant.parse(instant));
        final PersistedMessage message2 = new PersistedMessage(
                "user", "message", Instant.parse(notEqual));

        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    public void shouldDetectEqualsWithoutInstant() {
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message");
        final PersistedMessage message2 = new PersistedMessage(
                "user", "message");

        assertThat(message1).isEqualTo(message2);
    }

    @Test
    public void shouldDetectNotEqualsWithoutInstantOnFromField() {
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message");
        final PersistedMessage message2 = new PersistedMessage(
                "notEqual", "message");

        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    public void shouldDetectNotEqualsWithoutInstantOnMessageField() {
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message");
        final PersistedMessage message2 = new PersistedMessage(
                "user", "notEqual");

        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    public void shouldDetectNotEqualsWithInstantNotEmpty() {
        final String instant = "2021-02-24T22:10:50Z";
        final PersistedMessage message1 = new PersistedMessage(
                "user", "message", Instant.parse(instant));
        final PersistedMessage message2 = new PersistedMessage(
                "user", "message");

        assertThat(message1).isNotEqualTo(message2);
    }
}