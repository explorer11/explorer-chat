package org.explorer.chat.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Arguments {

    /**
     * @throws RuntimeException if args[0] isn't an existing path
     */
    public static Path messagesPath(final String[] args) {
        final String pathArgument = args[0];
        final Path path = Paths.get(pathArgument);
        if(!path.toFile().exists()) {
            throw new RuntimeException("path " + pathArgument + " does not exist");
        }
        return path;
    }
}
