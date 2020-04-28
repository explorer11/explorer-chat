package org.explorer.chat.users;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

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

    @Test
    public void shouldNotGetUserInEmptyFile() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/empty.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.get("foo")).isFalse();
    }

    @Test
    public void shouldNotGetNotExistingUserInSingleCollection() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/single-user.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.get("notExisting")).isFalse();
    }

    @Test
    public void shouldGetUserInSingleCollection() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/single-user.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.get("foo")).isTrue();
    }

    @Test
    public void shouldNotGetNotExistingUserInMultipleCollection() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/multiple-users.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.get("notExisting")).isFalse();
    }

    @Test
    public void shouldGetExistingUserInMultipleCollection() throws IOException {
        final File file = new File(UsersTest.class.getResource(
                "/users-lists/multiple-users.txt").getFile());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.get("bar")).isTrue();
    }

    @Test
    public void shouldCreateUserWhenFileIsEmpty() throws IOException {
        final Users users = new Users(temporaryFolder.newFile().getAbsolutePath());
        assertThat(users.createNewUser("foo")).isTrue();
    }

    @Test
    public void shouldNotCreateUserWhenAlreadyExistingInSingleList() throws IOException {
        final File file = temporaryFolder.newFile();
        final String foo = "foo";
        new FileOutputStream(file).write(foo.getBytes());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.createNewUser(foo)).isFalse();
    }

    @Test
    public void shouldCreateUserNotExistingInSingleList() throws IOException {
        final File file = temporaryFolder.newFile();
        final String foo = "foo";
        new FileOutputStream(file).write(foo.getBytes());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.createNewUser("bar")).isTrue();
    }

    @Test
    public void shouldNotCreateUserWhenAlreadyExistingInMultipleList() throws IOException {
        final File file = temporaryFolder.newFile();
        final String foo_bar = String.format("foo%nbar");
        new FileOutputStream(file).write(foo_bar.getBytes());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.createNewUser("foo")).isFalse();
        assertThat(users.createNewUser("bar")).isFalse();
    }

    @Test
    public void shouldCreateUserNotExistingInMultipleList() throws IOException {
        final File file = temporaryFolder.newFile();
        final String foo_bar = String.format("foo%nbar");
        new FileOutputStream(file).write(foo_bar.getBytes());
        final Users users = new Users(file.getAbsolutePath());
        assertThat(users.createNewUser("bob")).isTrue();
    }
}