package com.privilance.service;

import com.privilance.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}
