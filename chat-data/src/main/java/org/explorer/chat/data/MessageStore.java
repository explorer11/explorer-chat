package org.explorer.chat.data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.explorer.chat.common.ChatMessage;
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

public class MessageStore implements MessageSave {

    private static final String SEPARATOR = "|";

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
        printWriter.write(chatMessage.getFromUserMessage());
        printWriter.write(SEPARATOR);
        printWriter.write(chatMessage.getMessage());
        printWriter.println();
        printWriter.close();
    }

    public List<Pair<String,String>> readLast(final int number) throws IOException {
        final List<Pair<String,String>> lastLines = new ArrayList<>(number);
        final List<String> linesList;

        try(final Stream<String> lines = Files.lines(path)){
            linesList = lines.collect(Collectors.toList());
        }

        final int size = linesList.size();
        final int skip = size >= number ? size - number : 0;

        linesList.stream().skip(skip).forEach(line -> {
            final String[] split = StringUtils.split(line, SEPARATOR);
            lastLines.add(Pair.of(split[0], split[1]));
        });

        return lastLines;
    }
}
