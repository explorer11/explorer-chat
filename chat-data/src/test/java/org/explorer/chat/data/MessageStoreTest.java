package org.explorer.chat.data;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.explorer.chat.data.TestFiles.MESSAGES;
import static org.explorer.chat.data.TestFiles.SINGLE_MESSAGE;

public class MessageStoreTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final File singleMessageFile = new File(MessageStoreTest.class.getResource(
            SINGLE_MESSAGE).getFile());
    private final File messagesFile = new File(MessageStoreTest.class.getResource(
            MESSAGES).getFile());

    private final MessageStore singleMessageStore = new MessageStore(Paths.get(singleMessageFile.getAbsolutePath()));
    private final MessageStore messagesStore = new MessageStore(Paths.get(messagesFile.getAbsolutePath()));

    @Test
    public void shouldSaveToEmptyFile() throws IOException {
        final MessageStore emptyMessageStore = new MessageStore(
                Paths.get(temporaryFolder.newFile().getAbsolutePath()));
        final Instant instant = Instant.parse("2010-10-03T10:15:30.00Z");
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .withMessage("bonjour")
                .withInstant(instant)
                .build();
        emptyMessageStore.save(chatMessage);

        final List<PersistedMessage> actual = emptyMessageStore.readLast(1);
        assertThat(actual).hasSize(1);
        assertThat(actual).extracting(PersistedMessage::getFrom).containsExactly("user");
        assertThat(actual).extracting(PersistedMessage::getMessage).containsExactly("bonjour");
        final Optional<Instant> actualInstant = actual.get(0).getInstant();
        assertThat(actualInstant).isEqualTo(Optional.of(instant));
    }

    @Test
    public void shouldSaveToNotEmptyFile() throws IOException {
        final MessageStore emptyMessageStore = new MessageStore(Paths.get(temporaryFolder.newFile().getAbsolutePath()));
        final Instant instant1 = Instant.parse("2010-10-03T10:15:30.00Z");
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("user")
                .withMessage("bonjour")
                .withInstant(instant1)
                .build();
        emptyMessageStore.save(chatMessage);

        final Instant instant2 = Instant.parse("2012-04-03T10:15:30.00Z");
        final ChatMessage chatMessage2 = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage("toto")
                .withMessage("hello")
                .withInstant(instant2)
                .build();
        emptyMessageStore.save(chatMessage2);

        final List<PersistedMessage> actual = emptyMessageStore.readLast(2);
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(PersistedMessage::getFrom).containsExactly("user", "toto");
        assertThat(actual).extracting(PersistedMessage::getMessage).containsExactly("bonjour", "hello");
        final Optional<Instant> actualInstant1 = actual.get(0).getInstant();
        assertThat(actualInstant1).isEqualTo(Optional.of(instant1));
        final Optional<Instant> actualInstant2 = actual.get(1).getInstant();
        assertThat(actualInstant2).isEqualTo(Optional.of(instant2));
    }

    @Test
    public void shouldSaveToNotEmptyFileWithSameUser() throws IOException {
        final MessageStore emptyMessageStore = new MessageStore(Paths.get(temporaryFolder.newFile().getAbsolutePath()));
        final String user = "user";
        final Instant instant1 = Instant.parse("2010-10-03T10:15:30.00Z");
        final ChatMessage chatMessage = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage(user)
                .withMessage("bonjour")
                .withInstant(instant1)
                .build();
        emptyMessageStore.save(chatMessage);

        final Instant instant2 = Instant.parse("2012-04-03T10:15:30.00Z");
        final ChatMessage chatMessage2 = new ChatMessage.ChatMessageBuilder()
                .withMessageType(ChatMessageType.SENTENCE)
                .withFromUserMessage(user)
                .withMessage("hello")
                .withInstant(instant2)
                .build();
        emptyMessageStore.save(chatMessage2);

        final List<PersistedMessage> actual = emptyMessageStore.readLast(2);
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(PersistedMessage::getFrom).containsExactly(user, user);
        assertThat(actual).extracting(PersistedMessage::getMessage).containsExactly("bonjour", "hello");
        final Optional<Instant> actualInstant1 = actual.get(0).getInstant();
        assertThat(actualInstant1).isEqualTo(Optional.of(instant1));
        final Optional<Instant> actualInstant2 = actual.get(1).getInstant();
        assertThat(actualInstant2).isEqualTo(Optional.of(instant2));
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
        assertThat(singleMessageStore.readLast(1)).containsExactly(
                new PersistedMessage("user", "bonjour"));
    }

    @Test
    public void shouldReadSeveralMessages() throws IOException {
        final List<PersistedMessage> actual = messagesStore.readLast(3);

        assertThat(actual).hasSize(3);
        assertThat(actual).extracting(PersistedMessage::getFrom).containsExactly("user", "first", "user");
        assertThat(actual).extracting(PersistedMessage::getMessage).containsExactly("bonjour", "hello", "how");
        assertThat(actual.get(0).getInstant()).isEmpty();
        assertThat(actual.get(1).getInstant()).isEmpty();
        assertThat(actual.get(2).getInstant()).isEmpty();
    }

    @Test
    public void shouldReadMessagesWithInstant() throws IOException {
        final File messagesWithInstantFile = new File(MessageStoreTest.class.getResource(
                "/messages_with_instant.txt").getFile());
        final MessageStore messagesStoreWithInstant = new MessageStore(
                Paths.get(messagesWithInstantFile.getAbsolutePath()));
        final List<PersistedMessage> actual = messagesStoreWithInstant.readLast(3);

        assertThat(actual).hasSize(3);
        assertThat(actual).extracting(PersistedMessage::getFrom).containsExactly("user", "first", "user");
        assertThat(actual).extracting(PersistedMessage::getMessage).containsExactly("bonjour", "hello", "how");
        assertThat(actual.get(0).getInstant()).isEmpty();
        final Optional<Instant> instant1 = actual.get(1).getInstant();
        assertThat(instant1).isEqualTo(Optional.of(Instant.parse("2021-02-24T22:10:50Z")));
        final Optional<Instant> instant2 = actual.get(2).getInstant();
        assertThat(instant2).isEqualTo(Optional.of(Instant.parse("2021-02-24T22:11:05Z")));
    }

    @Test
    public void shouldReadLessMessagesThanExisting() throws IOException {
        final List<PersistedMessage> actual = messagesStore.readLast(2);

        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(PersistedMessage::getFrom).containsExactly("first", "user");
        assertThat(actual).extracting(PersistedMessage::getMessage).containsExactly("hello", "how");
        assertThat(actual.get(0).getInstant()).isEmpty();
        assertThat(actual.get(1).getInstant()).isEmpty();
    }

    @Test
    public void shouldReadMoreMessagesThanExisting() throws IOException {
        final List<PersistedMessage> actual = messagesStore.readLast(10);

        assertThat(actual).hasSize(3);
        assertThat(actual).extracting(PersistedMessage::getFrom).containsExactly("user", "first", "user");
        assertThat(actual).extracting(PersistedMessage::getMessage).containsExactly("bonjour", "hello", "how");
        assertThat(actual.get(0).getInstant()).isEmpty();
        assertThat(actual.get(1).getInstant()).isEmpty();
        assertThat(actual.get(2).getInstant()).isEmpty();
    }
}