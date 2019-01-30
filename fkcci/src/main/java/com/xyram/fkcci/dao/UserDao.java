package com.xyram.fkcci.dao;
import java.util.List;

import com.xyram.fkcci.model.UserLogin;
/**
 * 
 * @fileName : UserDao.java
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
public interface UserDao {
	/**
	 * 
	 * @function addUser 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param user
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public UserLogin addUser(UserLogin user) throws Exception;
	/**
	 * 
	 * @function validateLogin 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param email
	 * @parm(s) : @param password
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public UserLogin validateLogin(String email, String password) throws Exception;
	/**
	 * 
	 * @function updateUser 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param user
	 * @parm(s) : @return
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public UserLogin updateUser(UserLogin user);
	/**
	 * 
	 * @function getAllUsers 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : List<UserLogin>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public List<UserLogin> getAllUsers();
	/**
	 * 
	 * @function deleteUserById 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param id
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public String deleteUserById(Integer id);
	/**
	 * 
	 * @function getUserById 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param id
	 * @parm(s) : @return
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public UserLogin getUserById(Integer id);
	/**
	 * 
	 * @function changePassword 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param userId
	 * @parm(s) : @param password
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public UserLogin changePassword(Integer userId, String password) throws Exception;

	//public UserLogin forgotPassword(String email);
	public String forgotPassword(String email) throws Exception ;
}
