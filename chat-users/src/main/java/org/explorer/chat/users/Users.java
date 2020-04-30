package org.explorer.chat.users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;

class Users {

    private final Path path;
    private final Collection<String> names = new HashSet<>();

    Users(final String stringPath) throws IOException {
        this.path = Paths.get(stringPath);
        Files.lines(path).forEach(names::add);
    }

    Collection<String> list() {
        return names;
    }

    boolean get(final String user) {
        return names.contains(user);
    }

    /**
     * Creates the user if it does not exist. Writes the new user to file.
     * Returns false if the user already exists.
     *
     * @throws IOException if an exception occurs when writing to file. In this case,
     * the user isn't created in memory.
     */
    boolean createNewUser(final String user) throws IOException {
        if(!names.contains(user)) {
            Files.write(path, (System.getProperty("line.separator") + user).getBytes(),
                    StandardOpenOption.APPEND);
            names.add(user);
            return true;
        } else {
            return false;
        }
    }
}
