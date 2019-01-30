package com.xyram.fkcci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyram.fkcci.model.UserLogin;
import com.xyram.fkcci.service.UserService;
import com.xyram.fkcci.util.AESencryption;
import com.xyram.fkcci.util.CommnonUtil;
/**
 * 
 * @fileName : UserController.java
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
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	/**
	 * 
	 * @function addUsers 
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
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public UserLogin addUsers(@RequestBody UserLogin user) throws Exception {

		return userService.addUser(user);
	}
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
	/*@RequestMapping(value = "/login", method = RequestMethod.GET, params = {"email", "password" })
	@ResponseBody
	public UserLogin validateLogin(@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password) throws Exception {
		System.out.println("password:"+password);
		String decryptPassword = CommnonUtil.base64Decoder(password);
		System.out.println("decryptPassword :"+decryptPassword);
		UserLogin user = userService.validateLogin(email, AESencryption.encrypt(decryptPassword));
		return user;
	}*/
	@RequestMapping(value = "/login", method = RequestMethod.GET, params = {"email", "password" })
	 @ResponseBody
	 public UserLogin validateLogin(@RequestParam(value = "email") String email,
	   @RequestParam(value = "password") String password) throws Throwable {
	  
		String decryptPassword = CommnonUtil.base64Decoder(password);

		UserLogin user = userService.validateLogin(email, AESencryption.encrypt(decryptPassword));

		return user;
	}
	/**
	 * 
	 * @function getAllUsers 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : List<UserLogin>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<UserLogin> getAllUsers() throws Exception {

		List<UserLogin> usersList = userService.getAllUsers();
		return usersList;
	}
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
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	@ResponseBody
	public UserLogin updateUser(@RequestBody UserLogin user) {

		return userService.updateUser(user);
	}
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
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteUserById(@PathVariable Integer id) {

		String response = userService.deleteUserById(id);
		return response;
	}
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
	 * @parm(s) : @throws Exception
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public UserLogin getUserById(@PathVariable Integer id) throws Exception {

		UserLogin response = userService.getUserById(id);
		return response;
	}
	/**
	 * 
	 * @function isRegistered 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param email
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public UserLogin isRegistered(@RequestParam(value = "email") String email) throws Exception {

		UserLogin user = userService.validateLogin(email, null);
		 
		return user;
	}
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
	@RequestMapping(value = "/cpwd", method = RequestMethod.PUT)
	@ResponseBody
	public UserLogin changePassword(@RequestParam(value = "userId") Integer userId,@RequestParam(value="password") String password) throws Exception {
		String decryptPassword = CommnonUtil.base64Decoder(password);
		UserLogin user = userService.changePassword(userId,decryptPassword );
		 
		return user;
	}
	/**
	 * 
	 * @function forgetPassword 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param email
	 * @parm(s) : @return
	 * @parm(s) : @throws DataAccessException
	 * @parm(s) : @throws Exception
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/forgotpwd", method = RequestMethod.GET ,params={"email"})
	@ResponseBody
	public String forgetPassword(@RequestParam(value="email") String email) throws  DataAccessException,Exception {
		//String decryptPassword = CommnonUtil.base64Decoder(password);
		return userService.forgotPassword(email);
	}
	/**
	 * 
	 * @function getModelClass 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : UserLogin
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/model", method = RequestMethod.GET)
	 @ResponseBody
	 public UserLogin getModelClass() {

		UserLogin user = new UserLogin();

	  return user;
	 }
}
