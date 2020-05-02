package org.explorer.chat.server;

import org.explorer.chat.server.users.ConnectedUsers;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ServerTest {

    @Test
    public void should_build_connectedUsers() throws IOException {
        final File file = new File(ServerTest.class.getResource(
                "/users-lists/empty.txt").getFile());
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
}