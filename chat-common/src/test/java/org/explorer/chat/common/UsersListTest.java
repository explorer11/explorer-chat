package org.explorer.chat.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsersListTest {
	
	private final UsersList usersList = new UsersList();
	
	@Test
	public void users_list_is_correctly_formatted_with_one_user() {
		Assert.assertEquals(usersList.getUsersList(
				Stream.of("a1").collect(Collectors.toList())), "a1");
	}
	
	@Test
	public void users_list_is_correctly_formatted_with_two_users() {
		Assert.assertEquals(usersList.getUsersList(
				Stream.of("a1","a2").collect(Collectors.toList())), "a1,a2");
	}
	
	@Test
	public void users_list_is_correctly_parsed_with_one_user() {
		Assert.assertEquals(usersList.getUsersListAsText("a1"), "a1");
	}

	@Test
	public void users_list_is_correctly_parsed_with_two_users() {
		Assert.assertEquals(usersList.getUsersListAsText("a1,a2"), "a1\na2");
	}
}
