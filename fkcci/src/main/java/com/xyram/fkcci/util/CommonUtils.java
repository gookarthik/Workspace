package com.xyram.fkcci.util;

import java.util.Random;

public class CommonUtils {
	
	private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static String generateRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= length; i++) {
			int pos = rand.nextInt(CHARSET.length());
			sb.append(CHARSET.charAt(pos));
		}
		return sb.toString();
	}
	
	//private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{12,}$"
	
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#`~_%^&+=.,-?';:{}!()])(?=\\S+$).{8,}$";
	public static Boolean validatePassword(String password){
		return password.matches(PASSWORD_PATTERN);
	}
	
}
