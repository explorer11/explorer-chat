package org.explorer.chat.data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.explorer.chat.save.MessageSave;
import org.explorer.chat.common.ChatMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageStore implements MessageSave {

    private static final String SEPARATOR = "|";

    private final Path path;

    public MessageStore(final Path path) {
        this.path = path;
    }

    public void save(final ChatMessage chatMessage) {

        final FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(path.toFile(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.write(chatMessage.getFromUserMessage());
        printWriter.write(SEPARATOR);
        printWriter.write(chatMessage.getMessage());
        printWriter.println();
        printWriter.close();
    }

    List<Pair<String,String>> readLast(final int number) throws IOException {
        final List<Pair<String,String>> lastLines = new ArrayList<>(number);
        Files.lines(path).limit(number).forEach(line -> {
            final String[] split = StringUtils.split(line, SEPARATOR);
            lastLines.add(Pair.of(split[0], split[1]));
        });
        Collections.reverse(lastLines);
        return lastLines;
    }
}
