package org.explorer.chat.server.users;

import org.explorer.chat.users.Users;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ConnectedUsersTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private ConnectedUsers connectedUsers;
    private final Users users = Mockito.mock(Users.class);

    @Before
    public void before() {
        connectedUsers = new ConnectedUsers(/*temporaryFolder.newFile().getAbsolutePath()*/users);
    }

    @Test
    public void shouldAdd() throws IOException {
        final String user1 = "user1";
        connectedUsers.add(user1, Mockito.mock(OutputStream.class), null);
        assertThat(connectedUsers.getUsersNames()).containsExactly(user1);
    }

    @Test
    public void shouldAddTwoElements() throws IOException {
        final String user1 = "user1";
        final String user2 = "user2";
        connectedUsers.add(user1, Mockito.mock(OutputStream.class), null);
        connectedUsers.add(user2, Mockito.mock(OutputStream.class), null);
        assertThat(connectedUsers.getUsersNames()).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    public void shouldAddAndCreateUser() throws IOException {
        final String user1 = "user1";
        connectedUsers.add(user1, Mockito.mock(OutputStream.class), null);

        assertThat(connectedUsers.getUsersNames()).containsExactly(user1);
        Mockito.verify(users, Mockito.times(1)).createNewUser(Mockito.eq(user1));
    }

    @Test
    public void shouldNotAddSameUserTwice() throws IOException {
        final String user1 = "user1";
        final boolean added = connectedUsers.add(user1, Mockito.mock(OutputStream.class), null);
        assertThat(added).isTrue();
        assertThat(connectedUsers.getUsersNames()).containsExactly(user1);

        final boolean addedTwice = connectedUsers.add(user1, Mockito.mock(OutputStream.class), null);
        assertThat(addedTwice).isFalse();
        assertThat(connectedUsers.getUsersNames()).containsExactly(user1);
    }

    @Test
    public void shouldAddAndRemove() throws IOException {
        final String user1 = "user1";
        connectedUsers.add(user1, Mockito.mock(OutputStream.class), null);
        connectedUsers.remove(user1);
        assertThat(connectedUsers.getUsersNames()).isEmpty();
    }

    @Test
    public void shouldAddTwoElementsAndRemove() throws IOException {
        final String user1 = "user1";
        final String user2 = "user2";
        connectedUsers.add(user1, Mockito.mock(OutputStream.class), null);
        connectedUsers.add(user2, Mockito.mock(OutputStream.class), null);
        connectedUsers.remove(user1);
        assertThat(connectedUsers.getUsersNames()).containsExactly(user2);
    }

    @Test
    public void shouldGiveOutputs() throws IOException {
        final String user1 = "user1";
        final String user2 = "user2";
        final OutputStream output1 = Mockito.mock(OutputStream.class);
        final OutputStream output2 = Mockito.mock(OutputStream.class);
        connectedUsers.add(user1, output1, null);
        connectedUsers.add(user2, output2, null);
        assertThat(connectedUsers.getOutputs()).containsExactlyInAnyOrder(output1, output2);
    }

    @Test
    public void shouldGiveUsersAsList() throws IOException {
        final OutputStream output1 = Mockito.mock(OutputStream.class);
        final OutputStream output2 = Mockito.mock(OutputStream.class);
        connectedUsers.add("user1", output1, null);
        connectedUsers.add("user2", output2, null);
        assertThat(connectedUsers.getUsersList()).isEqualTo("user1,user2");
    }
}