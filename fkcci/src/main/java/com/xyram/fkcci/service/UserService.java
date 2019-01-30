package com.xyram.fkcci.service;

import java.util.List;
import com.xyram.fkcci.model.UserLogin;
/**
 * 
 * @fileName : UserService.java
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
public interface UserService {
	
	public UserLogin addUser(UserLogin user) throws Exception;

	public UserLogin validateLogin(String email, String password) throws Exception;

	public UserLogin updateUser(UserLogin user);

	public List<UserLogin> getAllUsers();

	public String deleteUserById(Integer id);

	public UserLogin getUserById(Integer id);
	
	public UserLogin changePassword(Integer userId, String password) throws Exception;

	//public UserLogin forgotPassword(String email);
	
	public String forgotPassword(String email) throws Exception ;
}
