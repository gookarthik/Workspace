package org.sitenv.spring.dao;

import org.sitenv.spring.model.Login;
import org.sitenv.spring.model.User;

public interface UserDao {
	void register(User user);

	User validateUser(Login login);
}