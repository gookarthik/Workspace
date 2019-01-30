package com.xyram.fkcci.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {
	
	public static long getDayStartTimeInMs(Date date) {
		long dateInMs = 0;
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    dateInMs = calendar.getTime().getTime();
	    return dateInMs;
	}
	
	public static long getDayEndTimeInMs(Date date) {
		long dateInMs = 0;
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    dateInMs = calendar.getTime().getTime();
	    return dateInMs;
	}
	
	public static String getCurrentDate() {
	   DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	   Date date = new Date();
	   return dateFormat.format(date);
	}
	public static String getCurrentTime() {
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = new Date();
	   return dateFormat.format(date);
	}
	public static String getDateFormat(Date date) {
	   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	   return dateFormat.format(date);
	}
	
	public static String getTodayDate() {
	   DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	   Date date = new Date();
	   return dateFormat.format(date);
	 }
	
	public static Date convertStringToDate(String dateInString) {
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			date = formatter.parse(dateInString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date convertStringDate(String dateInString) {
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			date = formatter.parse(dateInString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getDateFromString(String dateInString){
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			System.out.println("Exception while converting date:" + e.toString()); 
		}
		return date;
	}
	
	public static String convertDateToString(Date date) {
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   return dateFormat.format(date);
	}
	/**
	 * 
	 * @function getNextMonthDate 
	 * 
	 * @created_date : Apr 17, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : Date
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static Date getNextMonthDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		Date date = cal.getTime();
		return date;
	}
	
	/**
	 * 
	 * @function getNextSevenDaysDate 
	 * 
	 * @created_date : Apr 22, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static String getNextSevenDaysDate() {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	
	public static String getsDateInDays(int days) {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	/**
	 * 
	 * @function getNextEightDaysDate 
	 * 
	 * @created_date : Apr 22, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static String getNextEightDaysDate() {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 8);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	/**
	 * 
	 * @function getLast24HoursTime 
	 * 
	 * @created_date : 29-Apr-2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static String getLast24HoursTime() {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -24);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	/**
	 * 
	 * @function getCourseDurationDate 
	 * 
	 * @created_date : May 5, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param duration
	 * @parm(s) : @return
	 * 
	 * @return : Date
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static Date getCourseDurationDate(int duration) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, duration);
		Date date = cal.getTime();
		return date;
	}
	/**
	 * 
	 * @function getNextFourDaysDate 
	 * 
	 * @created_date : May 8, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static String getNextFourDaysDate() {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 4);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	/**
	 * 
	 * @function getNextThreeDaysDate 
	 * 
	 * @created_date : May 8, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static String getNextThreeDaysDate() {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 3);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	/**
	 * 
	 * @function getNextTwoDaysDate 
	 * 
	 * @created_date : May 8, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static String getNextTwoDaysDate() {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 2);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	/**
	 * 
	 * @function getNextOneDayDate 
	 * 
	 * @created_date : May 8, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static String getNextOneDayDate() {
		String dateStr = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		Date date = cal.getTime();
		dateStr = DateUtility.convertDateToString(date);
		return dateStr;
	}
	
	public static long convertDateToMilliSeconds(String _date){
		long timeInMilliSeconds = 00L;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		Date date = null;
		try {
			date = dateFormat.parse(_date);
		} catch (ParseException e) {
			System.out.println("ParseException while convert date:" + e.toString());
		}
		timeInMilliSeconds = date.getTime();
		
		return timeInMilliSeconds;
	}
	
	public static long getDateInMilliSeconds(){
		Calendar cal = Calendar.getInstance();
		long timeInMilliSeconds = cal.getTime().getTime();
		return timeInMilliSeconds;
	} 
}
