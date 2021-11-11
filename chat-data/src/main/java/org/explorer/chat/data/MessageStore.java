package org.explorer.chat.data;

import org.explorer.chat.common.ChatMessage;
import org.explorer.chat.common.ChatMessageType;
import org.explorer.chat.save.MessageRead;
import org.explorer.chat.save.MessageSave;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageStore implements MessageSave, MessageRead {

    private final Path path;

    public MessageStore(final Path path) {
        this.path = path;
    }

    @Override
    public void save(final ChatMessage chatMessage) {

        final FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(path.toFile(), StandardCharsets.UTF_8, true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.write(new PersistedMessage(
                chatMessage.getFromUserMessage(),
                chatMessage.getMessage(),
                chatMessage.getInstant()).toString());
        printWriter.println();
        printWriter.close();
    }

    public List<PersistedMessage> readLast(final int number) throws IOException {
        final List<PersistedMessage> lastLines = new ArrayList<>(number);
        final List<String> linesList;

        try(final Stream<String> lines = Files.lines(path)){
            linesList = lines.collect(Collectors.toList());
        }

        final int size = linesList.size();
        final int skip = size >= number ? size - number : 0;

        linesList.stream().skip(skip).
                forEach(line -> lastLines.add(PersistedMessage.parse(line)));

        return lastLines;
    }

    @Override
    public List<ChatMessage> findAll() throws IOException {
        return Files.lines(path)
                .map(PersistedMessage::parse)
                .map(message -> new ChatMessage.ChatMessageBuilder()
                        .withMessageType(ChatMessageType.SENTENCE)
                        .withMessage(message.getMessage())
                        .withFromUserMessage(message.getFrom())
                        .withInstant(message.getInstant().orElse(null))
                        .build())
                .collect(Collectors.toList());
    }

}
