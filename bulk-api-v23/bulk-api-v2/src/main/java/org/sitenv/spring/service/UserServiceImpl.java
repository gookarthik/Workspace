package org.sitenv.spring.service;

import java.sql.ResultSet;

import javax.transaction.Transactional;

import org.sitenv.spring.dao.UserDao;

import org.sitenv.spring.model.Login;
import org.sitenv.spring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Transactional

public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	public void register(User user) {
		this.userDao.register(user);
	}
	
	public User validateUser(Login login) {
		return this.userDao.validateUser(login);
	}
	
	

}
