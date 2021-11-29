package org.explorer.chat.elastic;


import java.io.IOException;
import java.util.function.Supplier;

interface ElasticAction {

    default void execute(final Supplier<String> arguments) throws IOException {
        // do nothing
    }
}
