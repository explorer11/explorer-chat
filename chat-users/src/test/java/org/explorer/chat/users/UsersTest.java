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
    public void shouldCreateUserWhenFileIsEmptyAndSavesToFile() throws IOException {
        final String path = temporaryFolder.newFile().getAbsolutePath();
        final Users users = new Users(path);
        assertThat(users.createNewUser("foo")).isTrue();

        final Users usersAfterCreation = new Users(path);
        assertThat(usersAfterCreation.createNewUser("foo")).isFalse();
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
    public void shouldCreateUserNotExistingInSingleListAndWriteToFile() throws IOException {
        final File file = temporaryFolder.newFile();
        final String alreadyExisting = "foo";
        new FileOutputStream(file).write(alreadyExisting.getBytes());

        final String path = file.getAbsolutePath();
        final Users users = new Users(path);
        final String newUser = "bar";
        assertThat(users.createNewUser(newUser)).isTrue();

        final Users usersAfterCreation = new Users(path);
        assertThat(usersAfterCreation.createNewUser(newUser)).isFalse();
        assertThat(usersAfterCreation.createNewUser(alreadyExisting)).isFalse();
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

    @Test
    public void shouldCreateUserNotExistingInMultipleListAndWriteToFile() throws IOException {
        final File file = temporaryFolder.newFile();
        final String foo_bar = String.format("foo%nbar");
        new FileOutputStream(file).write(foo_bar.getBytes());

        final String path = file.getAbsolutePath();
        final Users users = new Users(path);
        final String newUser = "bob";
        assertThat(users.createNewUser(newUser)).isTrue();

        final Users usersAfterCreation = new Users(path);
        assertThat(usersAfterCreation.createNewUser(newUser)).isFalse();
        assertThat(usersAfterCreation.createNewUser("foo")).isFalse();
        assertThat(usersAfterCreation.createNewUser("bar")).isFalse();
    }
}