package org.explorer.chat.data;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class PersistedMessage {

    private static final String SEPARATOR = "|";

    private final String from;
    private final String message;
    private Instant instant;

    public PersistedMessage(final String from, final String message, final Instant instant) {
        this.from = from;
        this.message = message;
        this.instant = instant;
    }

    PersistedMessage(final String from, final String message) {
        this.from = from;
        this.message = message;
    }

    @Override
    public String toString() {
        return from + SEPARATOR + message + SEPARATOR + instant;
    }

    public static PersistedMessage parse(final String string) {
        final String[] split = string.split("\\|");

        return split.length == 3 ?
                new PersistedMessage(split[0], split[1], Instant.parse(split[2])) :
                new PersistedMessage(split[0], split[1]);
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public Optional<Instant> getInstant() {
        return Optional.ofNullable(instant);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistedMessage)) return false;

        final PersistedMessage that = (PersistedMessage) o;

        if (!from.equals(that.from)) return false;
        if (!message.equals(that.message)) return false;
        return Objects.equals(instant, that.instant);
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (instant != null ? instant.hashCode() : 0);
        return result;
    }
}
