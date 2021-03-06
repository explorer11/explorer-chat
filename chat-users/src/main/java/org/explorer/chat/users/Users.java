package org.explorer.chat.users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashSet;

public class Users {

    private final Path path;
    private final Collection<String> names = new HashSet<>();

    public Users(final String stringPath) throws IOException {
        this.path = Paths.get(stringPath);
        Files.lines(path).forEach(names::add);
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
    public boolean createNewUser(final String user) throws IOException {
        if(!names.contains(user)) {
            final String newLine = names.size() > 0 ?
                    System.getProperty("line.separator") + user : user;
            Files.write(path, newLine.getBytes(),
                    StandardOpenOption.APPEND);
            names.add(user);
            return true;
        } else {
            return false;
        }
    }
}
