package com.xyram.fkcci.serviceImpl;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xyram.fkcci.dao.UserDao;
import com.xyram.fkcci.model.UserLogin;
import com.xyram.fkcci.service.UserService;
/**
 * 
 * @fileName : UserServiceImpl.java
 *
 * @description : 
 *
 *
 * @version : 1.0
 *
 * @date: Nov 18, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public UserLogin addUser(UserLogin user) throws Exception {
		return this.userDao.addUser(user);
	}

	public UserLogin validateLogin(String email, String password) throws Exception {
		return this.userDao.validateLogin(email, password);
	}

	public UserLogin updateUser(UserLogin user) {
		return this.userDao.updateUser(user);
	}

	public List<UserLogin> getAllUsers() {
		return this.userDao.getAllUsers();
	}

	public String deleteUserById(Integer id) {
		return this.userDao.deleteUserById(id);
	}

	public UserLogin getUserById(Integer id) {
		return this.userDao.getUserById(id);
	}

	public UserLogin changePassword(Integer userId, String password) throws Exception {
		return this.userDao.changePassword(userId, password);
	}

	public String forgotPassword(String email) throws Exception {
		return this.userDao.forgotPassword(email);
	}

}
