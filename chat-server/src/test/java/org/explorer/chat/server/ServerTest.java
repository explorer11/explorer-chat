package org.explorer.chat.server;

import org.explorer.chat.server.users.ConnectedUsers;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ServerTest {

    @Test
    public void should_build_connectedUsers() throws IOException {
        final File file = new File(ServerTest.class.getResource(
                "/server-args/users-list.txt").getFile());
        final String[] correctArguments = new String[]{file.getAbsolutePath()};
        final ConnectedUsers connectedUsers = Server.connectedUsers(correctArguments);
        assertThat(connectedUsers).isNotNull();
    }

    @Test
    public void should_throw_exception_on_invalid_path() {
        final String[] incorrectArguments = new String[]{"test"};
        assertThatThrownBy(() -> Server.connectedUsers(incorrectArguments))
                .isInstanceOf(IOException.class);
    }

    @Test
    public void should_throw_exception_on_missing_argument() {
        assertThatThrownBy(() -> Server.connectedUsers(new String[0]))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void should_build_messages_path() {
        final File file = new File(ServerTest.class.getResource(
                "/server-args/messages.txt").getFile());
        final String[] correctArguments = new String[]{null, file.getAbsolutePath()};
        final Path path = Server.messagesPath(correctArguments);
        assertThat(path).isNotNull()
                .exists();
    }

    @Test
    public void should_throw_exception_on_invalid_messages_path() {
        final String notExistingPath = "test";
        assertThatThrownBy(() -> Server.messagesPath(new String[]{null, notExistingPath}))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("path " + notExistingPath + " does not exist");
    }

    @Test
    public void should_throw_exception_on_missing_path_argument() {
        assertThatThrownBy(() -> Server.messagesPath(new String[1]))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
}