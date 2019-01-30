package com.xyram.fkcci.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class CommnonUtil {

	public static int downloadFIleByName(String fileName, HttpServletResponse response) throws IOException {

		// File file = new File(contextPath+"/"+filePath+fileName);

		File downloadFile = new File(fileName);
		if (downloadFile.exists()) {
			FileInputStream inputStream = null;
			OutputStream outStream = null;

			try {
				inputStream = new FileInputStream(downloadFile);

				response.setContentLength((int) downloadFile.length());
				// response.setContentType(context.getMimeType("C:/JavaHonk/CustomJar.jar"));

				// response header
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// Write response
				outStream = response.getOutputStream();
				return IOUtils.copy(inputStream, outStream);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != inputStream)
						inputStream.close();
					if (null != inputStream)
						outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return 1;
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
			return 404;
		}
	}

	public static String base64Encoder(String string) {

		// encoding byte array into base 64
		byte[] encoded = Base64.getEncoder().encode(string.getBytes());

		return new String(encoded);
	}

	public static String base64Decoder(String encodedString) {

		// decoding byte array into base64
		byte[] decoded = Base64.getDecoder().decode(encodedString);

		return new String(decoded);

	}

	public static String convertStringDateFormat(String givenDate, String givenFormat) throws ParseException {

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat(givenFormat);
		Date date = format2.parse(givenDate);
		String convertedDate = format1.format(date);
		System.out.println("converted date: " + convertedDate);
		return convertedDate;
	}

	public static int downloadFIleByName(File downloadFile, HttpServletResponse response) throws IOException {
		if (downloadFile.exists()) {
			/*
			 * FileInputStream file = new FileInputStream(downloadFile); // Create Workbook
			 * instance holding reference to .xlsx file Workbook workbook = new
			 * XSSFWorkbook(file);
			 */
			FileInputStream inputStream = null;
			OutputStream outStream = null;
			try {
				inputStream = new FileInputStream(downloadFile);
				response.setContentLength((int) downloadFile.length());
				// response.setContentType(context.getMimeType("C:/JavaHonk/CustomJar.jar"));
				// response header
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// Write response
				outStream = response.getOutputStream();
				return IOUtils.copy(inputStream, outStream);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != inputStream)
						inputStream.close();
					if (null != inputStream)
						outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return 1;
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
			return 404;
		}
	}

	public static String convertDateToString(Date date) {
		DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
		return format1.format(date);
	}

	public static Date convertStringDate(String dateInString) {
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = formatter.parse(dateInString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String convertDateTimeToString(Date date) {
		DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		return format1.format(date);
		/*
		 * String formattedDate=""; DateFormat dateFormat = new
		 * SimpleDateFormat("dd-MM-yyyy hh.mm.ss"); System.out.println(dateFormat);
		 * return formattedDate = dateFormat.format(date.toString());
		 */

	}

	public static String getLastWeekDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		long dateInMillis = cal.getTimeInMillis();
		String dateString = formatter.format(new Date(dateInMillis));
		return dateString;
	}

	public static String getLastMonthDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		long dateInMillis = cal.getTimeInMillis();
		String dateString = formatter.format(new Date(dateInMillis));
		return dateString;
	}

	public static String getTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateStr = dateFormat.format(date);
		return dateStr;
	}

	public static Date getTodayDateStart() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, 0); // anything 0 - 23
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date date = c.getTime();
		return date;
	}

	public static String getYesterdayDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		long dateInMillis = cal.getTimeInMillis();
		String dateString = formatter.format(new Date(dateInMillis));
		return dateString;
	}

	/**
	 */
	public static String getCurrentMonth() {
		// String month = "";
		Calendar now = Calendar.getInstance();
		// System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-" +
		// now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
		String[] strMonths = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
				"Dec" };
		// System.out.println("Current month is : " +
		// strMonths[now.get(Calendar.MONTH)]);
		return strMonths[now.get(Calendar.MONTH)];
	}

	public static String getPreToPreMonthDate(Date date) {
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DATE, -1);

		Date preToPreMonthDate = cal.getTime();
		return format.format(preToPreMonthDate);
	}

	public static String getPreviousMonthDate(Date date) {
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 2);
		cal.add(Calendar.DATE, -1);

		Date preMonthDate = cal.getTime();
		return format.format(preMonthDate);
	}

	public static String getLastDayOfMonth() {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date lastDayOfMonth = calendar.getTime();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("Today            : " + sdf.format(today));
		System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
		return "";
	}

	public static void getPreviousMonthStartDateAndEndDate() {
		Calendar aCalendar = Calendar.getInstance();
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH, -1);
		// set DATE to 1, so first date of previous month
		aCalendar.set(Calendar.DATE, 1);
		Date firstDateOfPreviousMonth = aCalendar.getTime();
		// set actual maximum date of previous month
		aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		// read it
		Date lastDateOfPreviousMonth = aCalendar.getTime();
	}

	public static String getPreviousMonthStartDate(int monthNo) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar aCalendar = Calendar.getInstance();
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH, -monthNo);
		// set DATE to 1, so first date of previous month
		aCalendar.set(Calendar.DATE, 1);
		long dateInMillis = aCalendar.getTimeInMillis();
		return formatter.format(new Date(dateInMillis));
	}

	public static String getPreviousMonthEndDate(int monthNo) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar aCalendar = Calendar.getInstance();
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH, -monthNo);
		// set DATE to 1, so first date of previous month
		aCalendar.set(Calendar.DATE, 1);
		// set actual maximum date of previous month
		aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		long dateInMillis = aCalendar.getTimeInMillis();
		return formatter.format(new Date(dateInMillis));
	}

	public static String geCurrentMonthStartDate() {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.set(Calendar.DATE, 1);
		aCalendar.set(Calendar.HOUR_OF_DAY, 0);
		aCalendar.set(Calendar.MINUTE, 0);
		aCalendar.set(Calendar.SECOND, 0);
		Date firstDateOfCurrentMonth = aCalendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dayFirst = sdf.format(firstDateOfCurrentMonth);
		System.out.println(dayFirst);
		return dayFirst;
	}
	public static String theMonth(int month){
	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    return monthNames[month];
	}
	
	public static String displayMonth(Date date) {
		DateFormat monthFormat = null;
		String month = null;
		monthFormat = new SimpleDateFormat("MMM");
		// Convert Date to String.
		month = monthFormat.format(date);
		return month;
	}
	
	public static int getYearFromDate(Date date) {
	     int result = -1;
	     if (date != null) {
	         Calendar cal = Calendar.getInstance();
	         cal.setTime(date);
	         result = cal.get(Calendar.YEAR);
	     }
	     return result;
	 }
	
	public static String getStartDateOnMonth(int month, int year) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		long dateInMillis = calendar.getTimeInMillis();
		return formatter.format(new Date(dateInMillis));
	}
	
	public static String getEndDateOnMonth(int month, int year) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		long dateInMillis = calendar.getTimeInMillis();
		return formatter.format(new Date(dateInMillis));
	}
	
	public static void main(String[] args) {
		System.out.println("Current month: " + getCurrentMonth());
		System.out.println(getPreToPreMonthDate(new Date()));
		System.out.println(getPreviousMonthDate(new Date()));
		System.out.println(getLastDayOfMonth());
		System.out.println("currentMonthStartDate: " + geCurrentMonthStartDate());
		System.out.println("currentDate: " + getTodayDate());
		// getPreviousMonthStartDateAndEndDate();
		// Date date = getPreviousMonthStartDate();
		System.out.println("getPreviousMonthStartDate: " + getPreviousMonthStartDate(1));
		System.out.println("getPreviousMonthEndDate: " + getPreviousMonthEndDate(1));
		System.out.println("Month :"+theMonth(0));
		System.out.println("Month----:"+displayMonth(new Date()));
		System.out.println("month date: "+getStartDateOnMonth(1, 2017));
		System.out.println("month end date: "+getEndDateOnMonth(1, 2017));
	}
}
