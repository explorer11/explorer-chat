package org.explorer.chat.users;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersTest {

    @Test
    public void shouldReturnEmptyCollectionIfNoUser() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/empty.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.list()).isEmpty();
    }

    @Test
    public void shouldReturnSingleUser() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/single-user.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.list()).containsExactly("foo");
    }

    @Test
    public void shouldReturnMultipleUsers() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/multiple-users.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.list()).containsExactlyInAnyOrder(
                "foo", "bar", "bob", "joe");
    }
}