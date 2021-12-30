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

        final List<ChatMessage> actual = emptyMessageStore.findLast(1);
        assertThat(actual).hasSize(1);
        assertThat(actual).extracting(ChatMessage::getFromUserMessage).containsExactly("user");
        assertThat(actual).extracting(ChatMessage::getMessage).containsExactly("bonjour");
        assertThat(actual).extracting(ChatMessage::getInstant).containsExactly(instant);
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

        final List<ChatMessage> actual = emptyMessageStore.findLast(2);
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(ChatMessage::getFromUserMessage).containsExactly("user", "toto");
        assertThat(actual).extracting(ChatMessage::getMessage).containsExactly("bonjour", "hello");
        assertThat(actual).extracting(ChatMessage::getInstant).containsExactly(instant1, instant2);
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

        final List<ChatMessage> actual = emptyMessageStore.findLast(2);
        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(ChatMessage::getFromUserMessage).containsExactly(user, user);
        assertThat(actual).extracting(ChatMessage::getMessage).containsExactly("bonjour", "hello");
        assertThat(actual).extracting(ChatMessage::getInstant).containsExactly(instant1, instant2);
    }

    @Test
    public void shouldThrowExceptionOnWrongFormatMessage() {
        final File file = new File(MessageStoreTest.class.getResource(
                "/messages_wrongFormat.txt").getFile());
        final MessageStore messageStore = new MessageStore(Paths.get(file.getAbsolutePath()));
        assertThatThrownBy(() -> messageStore.findLast(1))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void shouldReadZeroMessage() throws IOException {
        assertThat(singleMessageStore.findLast(0)).isEmpty();
    }

    @Test
    public void shouldReadOneMessage() throws IOException {
        assertThat(singleMessageStore.findLast(1)).containsExactly(
                new ChatMessage.ChatMessageBuilder()
                        .withFromUserMessage("user")
                        .withMessage("bonjour")
                        .withMessageType(ChatMessageType.SENTENCE)
                        .build());
    }

    @Test
    public void shouldReadSeveralMessages() throws IOException {
        final List<ChatMessage> actual = messagesStore.findLast(3);

        assertThat(actual).hasSize(3);
        assertThat(actual).extracting(ChatMessage::getFromUserMessage).containsExactly("user", "first", "user");
        assertThat(actual).extracting(ChatMessage::getMessage).containsExactly("bonjour", "hello", "how");
        assertThat(actual.get(0).getInstant()).isNull();
        assertThat(actual.get(1).getInstant()).isNull();
        assertThat(actual.get(2).getInstant()).isNull();
    }

    @Test
    public void shouldReadMessagesWithInstant() throws IOException {
        final File messagesWithInstantFile = new File(MessageStoreTest.class.getResource(
                "/messages_with_instant.txt").getFile());
        final MessageStore messagesStoreWithInstant = new MessageStore(
                Paths.get(messagesWithInstantFile.getAbsolutePath()));
        final List<ChatMessage> actual = messagesStoreWithInstant.findLast(3);

        assertThat(actual).hasSize(3);
        assertThat(actual).extracting(ChatMessage::getFromUserMessage).containsExactly("user", "first", "user");
        assertThat(actual).extracting(ChatMessage::getMessage).containsExactly("bonjour", "hello", "how");
        assertThat(actual.get(0).getInstant()).isNull();
        assertThat(actual.get(1).getInstant()).isEqualTo("2021-02-24T22:10:50Z");
        assertThat(actual.get(2).getInstant()).isEqualTo("2021-02-24T22:11:05Z");
    }

    @Test
    public void shouldReadLessMessagesThanExisting() throws IOException {
        final List<ChatMessage> actual = messagesStore.findLast(2);

        assertThat(actual).hasSize(2);
        assertThat(actual).extracting(ChatMessage::getFromUserMessage).containsExactly("first", "user");
        assertThat(actual).extracting(ChatMessage::getMessage).containsExactly("hello", "how");
        assertThat(actual.get(0).getInstant()).isNull();
        assertThat(actual.get(1).getInstant()).isNull();
    }

    @Test
    public void shouldReadMoreMessagesThanExisting() throws IOException {
        final List<ChatMessage> actual = messagesStore.findLast(10);

        assertThat(actual).hasSize(3);
        assertThat(actual).extracting(ChatMessage::getFromUserMessage).containsExactly("user", "first", "user");
        assertThat(actual).extracting(ChatMessage::getMessage).containsExactly("bonjour", "hello", "how");
        assertThat(actual.get(0).getInstant()).isNull();
        assertThat(actual.get(1).getInstant()).isNull();
        assertThat(actual.get(2).getInstant()).isNull();
    }
}