package org.explorer.chat.users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

class Users {

    private final Collection<String> names = new HashSet<>();

    Users(final String stringPath) throws IOException {
        final Path path = Paths.get(stringPath);
        Files.lines(path).forEach(names::add);
    }

    Collection<String> list() {
        return names;
    }
}
