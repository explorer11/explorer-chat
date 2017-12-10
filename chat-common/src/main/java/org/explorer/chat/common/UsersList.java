package org.explorer.chat.common;

import java.util.Collection;
import java.util.stream.Collectors;

public class UsersList {
	
	private final char separator = ',';
	
	public String getUsersList(Collection<String> usersNames) {
		return usersNames.stream().collect(Collectors.joining(String.valueOf(separator)));
	}
	
	public String getUsersListAsText(String userList) {
		return userList.replace(separator, '\n');
	}
}
