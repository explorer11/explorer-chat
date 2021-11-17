package org.explorer.chat.config;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ArgumentsTest {

    @Test
    public void should_build_messages_path() {
        final File file = new File(Objects.requireNonNull(ArgumentsTest.class.getResource(
                "/test-arguments/messages.txt")).getFile());
        final String[] correctArguments = new String[]{file.getAbsolutePath()};
        final Path path = Arguments.messagesPath(correctArguments);
        assertThat(path).isNotNull().exists();
    }

    @Test
    public void should_throw_exception_on_invalid_messages_path() {
        final String notExistingPath = "test";
        assertThatThrownBy(() -> Arguments.messagesPath(new String[]{notExistingPath}))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("path " + notExistingPath + " does not exist");
    }

    @Test
    public void should_throw_exception_on_missing_path_argument() {
        assertThatThrownBy(() -> Arguments.messagesPath(new String[0]))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
}