package org.explorer.chat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.explorer.chat.common.ChatMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MessageStore {

    private final String SEPARATOR = "|";

    private final Path path;

    MessageStore(final Path path) {
        this.path = path;
    }

    void save(final ChatMessage chatMessage) {

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
