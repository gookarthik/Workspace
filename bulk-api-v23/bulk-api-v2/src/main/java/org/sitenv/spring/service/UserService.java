package org.sitenv.spring.service;

import org.sitenv.spring.model.Login;
import org.sitenv.spring.model.User;

public interface UserService {

	void register(User user);

	User validateUser(Login login);
}
