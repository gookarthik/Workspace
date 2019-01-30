package com.xyram.fkcci.daoImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.xyram.fkcci.dao.AbstractDao;
import com.xyram.fkcci.dao.TappalsDao;
import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.util.CommnonUtil;

/**
 * 
 * @fileName : TappalsDaoImpl.java
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
@Repository
public class TappalsDaoImpl extends AbstractDao implements TappalsDao {

	public Tappals saveTappals(Tappals tappals) {
		getSession().saveOrUpdate(tappals);
		return tappals;
	}
	
	public List<Tappals> getAllTappals(String searchKeyword, int offset, int limit) {
		StringBuffer hql = new StringBuffer();
		System.out.println("search:" + searchKeyword);
		hql.append("FROM Tappals");
		if (searchKeyword != null) {
			System.out.println("search:" + searchKeyword);
			hql.append(" WHERE (LOWER(courierName) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(courierRigNo) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(toRole) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(fromName) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(fileName) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(particularsDetails) LIKE '%" + searchKeyword.toLowerCase()
					+ "%'"+ ")");
		}
		
		/** regex to verify integers */
		if(searchKeyword != null && searchKeyword.matches("\\d+")) {
			hql.append("or slNo = '"+searchKeyword+"'");
		}
	    hql.append(" order by receivedDate desc");
		Query query = getSession().createQuery(hql.toString());
		System.out.println("new query: " + query);
		//query.setFirstResult(offset);
		//query.setMaxResults(limit);
		List<Tappals> list = query.list();
		return list;
	}

	public List<Tappals> getAllTappalDate(String searchKeyword, int offset, int limit) {
		StringBuffer hql = new StringBuffer();
		System.out.println("search:" + searchKeyword);
		hql.append("FROM Tappals");
		if (searchKeyword != null) {
			System.out.println("search:" + searchKeyword);
			hql.append(" where (to_char(dispatchedDate, 'yyyy-MM-dd') like '" + searchKeyword
					+ "' OR to_char(date, 'yyyy-MM-dd') like '" + searchKeyword
					+ "' OR to_char(receivedDate, 'yyyy-MM-dd') like '" + searchKeyword + "')");
		}
	    hql.append(" order by receivedDate desc");
		Query query = getSession().createQuery(hql.toString());
		System.out.println("new query: " + query);
		//query.setFirstResult(offset);
		//query.setMaxResults(limit);
		List<Tappals> list = query.list();
		return list;
	}
	
	public Tappals getTappalsById(Integer id, int offset, int limit) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);
		return (Tappals) criteria.uniqueResult();
	}
	
	public Tappals updateTappals(Tappals tappals) {
		getSession().update(tappals);
		return tappals;
	}

	public Map<String, Object> deleteTappalById(Integer id) {
		Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
		try {
			Criteria criteria = getSession().createCriteria(Tappals.class);
			criteria.add(Restrictions.eq("id", id));
			List<Tappals> deleteList = criteria.list();
			if (deleteList.size() > 0) {
				Iterator it = deleteList.iterator();
				while (it.hasNext()) {
					Tappals tappals = (Tappals) it.next();
					getSession().delete(tappals);
					responseMap.put("status", "200");
					responseMap.put("message", "Tappals deleted successfully");
				}
			} else {
				responseMap.put("status", "404");
				responseMap.put("message", "No records found!");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			responseMap.put("status", "500");
			responseMap.put("message", "Exception while deleting tappals!");
		}
		return responseMap;
	}

	public String saveFile(HashMap<String, String> params, MultipartFile[] files) {
		String fileName = null, message = null;
		Integer slNo = Integer.parseInt(params.get("slNo"));
		Tappals tappals = getTappalsById(slNo, 0, 0);
		String courierName = tappals.getCourierName();
		String courierRigNo = tappals.getCourierRigNo();
		Date date = tappals.getDate();
		Date dispachDate = tappals.getDispatchedDate();
		Date receiveDate = tappals.getReceivedDate();
		String fromName = tappals.getFromName();
		String toRole = tappals.getToRole();
		String comments = tappals.getComments();
		String particularsDetails = tappals.getParticularsDetails();
		boolean userEditing = tappals.isUserEditing();
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			try {
				if (!file.isEmpty()) {
					String contextPath = System.getProperty("catalina.base");
					System.out.println("contextPath:" + contextPath + "\n" + System.getProperty("catalina.base"));
					String directoryPath = "/Courier Data/";
					//String directoryPath = "/Courier Data/" + courierName;
					File dir = new File(contextPath + directoryPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					fileName = file.getOriginalFilename();
					System.out.println("FileName :"+fileName);
					byte[] bytes = file.getBytes();
					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(new File(contextPath + directoryPath + fileName)));
					buffStream.write(bytes);
					buffStream.close();
					Tappals uploadDocument = new Tappals();
					uploadDocument.setSlNo(slNo);
					uploadDocument.setCourierName(courierName);
					uploadDocument.setFilePath(directoryPath);
					uploadDocument.setCourierRigNo(courierRigNo);
					uploadDocument.setDate(date);
					uploadDocument.setDispatchedDate(dispachDate);
					uploadDocument.setReceivedDate(receiveDate);
					uploadDocument.setUserEditing(userEditing);
					uploadDocument.setFromName(fromName);
					uploadDocument.setFileName(fileName);
					uploadDocument.setToRole(toRole);
					uploadDocument.setComments(comments);
					uploadDocument.setParticularsDetails(particularsDetails);
					getSession().merge(uploadDocument);
					message = "You have successfully uploaded";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "You failed to upload " + fileName + " => " + e.getMessage();
			}
		}
		return message;
	}

	public Integer downloadFile(Integer slNo, HttpServletResponse response) {
		Tappals info = getTappalsById(slNo, 0, 0);
		String filePath = info.getFilePath();
		String fileName = info.getFileName();
		String contextPath = System.getProperty("catalina.base");
		System.out.println("Path :" + filePath);
		File downloadFile = new File(contextPath + "/" + filePath + fileName);
		System.out.println("downloadFile :" + downloadFile);
		FileInputStream inputStream = null;
		OutputStream outStream = null;
		try {
			inputStream = new FileInputStream(downloadFile);
			response.setContentLength((int) downloadFile.length());

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
		return 404;
	}

	public List<Tappals> exportTappalsByDate(String fromDate, String toDate) {
		Date fDate = CommnonUtil.convertStringDate(fromDate+" 00:00:00");
		Date tDate = CommnonUtil.convertStringDate(toDate+" 23:59:59");
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.ge("date", fDate));
		criteria.add(Restrictions.le("date", tDate));
		criteria.addOrder(Order.desc("date"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getMyTappals(String toRole, String searchKeyword) {
		StringBuffer hql = new StringBuffer();
		System.out.println("search:" + searchKeyword);
		hql.append("FROM Tappals");
		if (searchKeyword != null) {
			System.out.println("search:" + searchKeyword);
			hql.append(" WHERE (LOWER(courierName) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(courierRigNo) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(fromName) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(fileName) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(particularsDetails) LIKE '%" + searchKeyword.toLowerCase() 
					+ "%'" + ") and toRole ='" + toRole + "'");
		}
		
		/** regex to verify integers */
		if(searchKeyword != null && searchKeyword.matches("\\d+")) {
			hql.append("or slNo = '"+searchKeyword+"'");
		}
	    hql.append(" order by receivedDate desc");
		Query query = getSession().createQuery(hql.toString());
		System.out.println("new query: " + query);
		List<Tappals> list = query.list();
		return list;
	}

	public List<Tappals> getMyTappalDate(String toRole, String searchKeyword) {
		StringBuffer hql = new StringBuffer();
		System.out.println("search:" + searchKeyword);
		hql.append("FROM Tappals");
		if (searchKeyword != null) {
			System.out.println("search:" + searchKeyword);
			hql.append(" where (to_char(dispatchedDate, 'yyyy-MM-dd') like '" + searchKeyword
					+ "' OR to_char(date, 'yyyy-MM-dd') like '" + searchKeyword
					+ "' OR to_char(receivedDate, 'yyyy-MM-dd') like '" + searchKeyword + "') and toRole ='" + toRole
					+ "'");
		}
	    hql.append(" order by receivedDate desc");
		Query query = getSession().createQuery(hql.toString());
		System.out.println("new query: " + query);
		List<Tappals> list = query.list();
		return list;
	}

	public List<Tappals> exportTappalsByRole(String toRole, String fromDate, String toDate) {
		Date fDate = CommnonUtil.convertStringDate(fromDate + " 00:00:00");
		Date tDate = CommnonUtil.convertStringDate(toDate + " 23:59:59");
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.eq("toRole", toRole));
		criteria.add(Restrictions.ge("date", fDate));
		criteria.add(Restrictions.le("date", tDate));
		criteria.addOrder(Order.desc("date"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getTodayTappals() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.getTodayDateStart();
		System.out.println("TodayStartDate: " + toDate);
		System.out.println("TodayCurrentDate: " + fromDate);
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getYesterdayTappals() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 23:59:59");
		System.out.println("YesterdayStartDate: " + fromDate);
		System.out.println("YesterdayEndDate: " + toDate);
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getLastWeekTappals() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastWeekDate() + " 00:00:00");
		System.out.println("CurrentDate: " + fromDate);
		System.out.println("LastWeekDate: " + toDate);
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getLastMonthTappals() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastMonthDate() + " 00:00:00");
		System.out.println("CurrentDate: " + fromDate);
		System.out.println("LastMonthDate: " + toDate);
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}
	
	public List<Map<String, Object>> getTotalHitcount() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("name", "Tappal Summary");
		map.put("totalHitCount", getTotalTappals());
		Map<String, Object> todayMap = new LinkedHashMap<String, Object>();
		todayMap.put("name", "Tappals for Today");
		todayMap.put("totalHitCount", getTodayTappalsCount());
		Map<String, Object> yesterdayMap = new LinkedHashMap<String, Object>();
		yesterdayMap.put("name", "Tappals for Yesterday");
		yesterdayMap.put("totalHitCount", getTotalYesterdayTappals());
		Map<String, Object> lastWeekMap = new LinkedHashMap<String, Object>();
		lastWeekMap.put("name", "Tappals for Last Week");
		lastWeekMap.put("totalHitCount", getLastWeekTappalsCount());
		Map<String, Object> lastMonthMap = new LinkedHashMap<String, Object>();
		lastMonthMap.put("name", "Tappals for Last Month");
		lastMonthMap.put("totalHitCount", getLastMonthTappalsCount());
		list.add(map);
		list.add(todayMap);
		list.add(yesterdayMap);
		list.add(lastWeekMap);
		list.add(lastMonthMap);
		return list;
	}
	
	public List<Map<String, Object>> getTappalSummaryCount() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> managementMap = new LinkedHashMap<String, Object>();
		managementMap.put("name", "Tappals To Management");
		managementMap.put("totalHitCount", getManagementTappalsCount());
		Map<String, Object> accountsMap = new LinkedHashMap<String, Object>();
		accountsMap.put("name", "Tappals To Account");
		accountsMap.put("totalHitCount", getAccountsTappalsCount());
		Map<String, Object> othersMap = new LinkedHashMap<String, Object>();
		othersMap.put("name", "Tappals To Others");
		othersMap.put("totalHitCount", getOthersTappalsCount());
		list.add(accountsMap);
		list.add(managementMap);
		list.add(othersMap);
		return list;
	}
	
	public int getTotalTappals() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		return criteria.list().size();
	}
	
	public int getTodayTappalsCount() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.getTodayDateStart();
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		return criteria.list().size();
	}
	
	public int getTotalYesterdayTappals() {
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 23:59:59");
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list().size();
	}
	
	public int getLastWeekTappalsCount() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastWeekDate() + " 00:00:00");
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		return criteria.list().size();
	}
	
	public int getLastMonthTappalsCount() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastMonthDate() + " 00:00:00");
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		return criteria.list().size();
	}
	
	public int getAccountsTappalsCount() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.eq("toRole", "accounts"));
		return criteria.list().size();
	}
	
	public int getManagementTappalsCount() {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM Tappals WHERE toRole = 'President' OR toRole = 'Vice President' OR toRole = 'Sr. Vice President'");
		Query query = getSession().createQuery(hql.toString());
		return query.list().size();
	}
	
	public int getOthersTappalsCount() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.ne("toRole", "accounts"));
		criteria.add(Restrictions.ne("toRole", "President"));
		criteria.add(Restrictions.ne("toRole", "Vice President"));
		criteria.add(Restrictions.ne("toRole", "Sr. Vice President"));
		return criteria.list().size();
	}

	public List<Tappals> getPreviousMonthTappals(int monthNo) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		List<Tappals> list = null;
		if (monthNo > 0) {
			Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(monthNo) + " 00:00:00");
			Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthEndDate(monthNo) + " 23:59:59");
			System.out.println("PreviousMonthStartDate: " + fromDate);
			System.out.println("PreviousMonthEndDate: " + toDate);
			criteria.add(Restrictions.ge("date", fromDate));
			criteria.add(Restrictions.le("date", toDate));
			criteria.addOrder(Order.desc("receivedDate"));
			list = criteria.list();
		} else {
			Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.geCurrentMonthStartDate());
			Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
			System.out.println("currentMonthStartDate: " + fromDate);
			System.out.println("currentDate: " + toDate);
			criteria.add(Restrictions.ge("date", fromDate));
			criteria.add(Restrictions.le("date", toDate));
			criteria.addOrder(Order.desc("receivedDate"));
			list = criteria.list();
		}
		return list;
	}
	
	public int getAccountsMonthTappals(int monthNo) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(monthNo) + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthEndDate(monthNo) + " 23:59:59");
		criteria.add(Restrictions.eq("toRole", "accounts"));
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list().size();
	}

	public List<Map<String, Object>> getAccountsEachMonth() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> month = new LinkedHashMap<String, Object>();
		Date date = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(0) + " 00:00:00");
		month.put("monthName", CommnonUtil.displayMonth(date));
		month.put("year", CommnonUtil.getYearFromDate(date));
		month.put("totalHitCount", getAccountsMonthTappals(0));
		list.add(month);
		
		Map<String, Object> month1 = new LinkedHashMap<String, Object>();
		Date date1 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(1) + " 00:00:00");
		month1.put("monthName", CommnonUtil.displayMonth(date1));
		month1.put("year", CommnonUtil.getYearFromDate(date1));
		month1.put("totalHitCount", getAccountsMonthTappals(1));
		list.add(month1);
		
		Map<String, Object> month2 = new LinkedHashMap<String, Object>();
		Date date2 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(2) + " 00:00:00");
		month2.put("monthName", CommnonUtil.displayMonth(date2));
		month2.put("year", CommnonUtil.getYearFromDate(date2));
		month2.put("totalHitCount", getAccountsMonthTappals(2));
		list.add(month2);
		
		Map<String, Object> month3 = new LinkedHashMap<String, Object>();
		Date date3 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(3) + " 00:00:00");
		month3.put("monthName", CommnonUtil.displayMonth(date3));
		month3.put("year", CommnonUtil.getYearFromDate(date3));
		month3.put("totalHitCount", getAccountsMonthTappals(3));
		list.add(month3);
		
		Map<String, Object> month4 = new LinkedHashMap<String, Object>();
		Date date4 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(4) + " 00:00:00");
		month4.put("monthName", CommnonUtil.displayMonth(date4));
		month4.put("year", CommnonUtil.getYearFromDate(date4));
		month4.put("totalHitCount", getAccountsMonthTappals(4));
		list.add(month4);
		
		Map<String, Object> month5 = new LinkedHashMap<String, Object>();
		Date date5 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(5) + " 00:00:00");
		month5.put("monthName", CommnonUtil.displayMonth(date5));
		month5.put("year", CommnonUtil.getYearFromDate(date5));
		month5.put("totalHitCount", getAccountsMonthTappals(5));
		list.add(month5);
		
		Map<String, Object> month6 = new LinkedHashMap<String, Object>();
		Date date6 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(6) + " 00:00:00");
		month6.put("monthName", CommnonUtil.displayMonth(date6));
		month6.put("year", CommnonUtil.getYearFromDate(date6));
		month6.put("totalHitCount", getAccountsMonthTappals(6));
		list.add(month6);
		
		Map<String, Object> month7 = new LinkedHashMap<String, Object>();
		Date date7 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(7) + " 00:00:00");
		month7.put("monthName", CommnonUtil.displayMonth(date7));
		month7.put("year", CommnonUtil.getYearFromDate(date7));
		month7.put("totalHitCount", getAccountsMonthTappals(7));
		list.add(month7);
		
		Map<String, Object> month8 = new LinkedHashMap<String, Object>();
		Date date8 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(8) + " 00:00:00");
		month8.put("monthName", CommnonUtil.displayMonth(date8));
		month8.put("year", CommnonUtil.getYearFromDate(date8));
		month8.put("totalHitCount", getAccountsMonthTappals(8));
		list.add(month8);
		
		Map<String, Object> month9 = new LinkedHashMap<String, Object>();
		Date date9 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(9) + " 00:00:00");
		month9.put("monthName", CommnonUtil.displayMonth(date9));
		month9.put("year", CommnonUtil.getYearFromDate(date9));
		month9.put("totalHitCount", getAccountsMonthTappals(9));
		list.add(month9);
		
		Map<String, Object> month10 = new LinkedHashMap<String, Object>();
		Date date10 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(10) + " 00:00:00");
		month10.put("monthName", CommnonUtil.displayMonth(date10));
		month10.put("year", CommnonUtil.getYearFromDate(date10));
		month10.put("totalHitCount", getAccountsMonthTappals(10));
		list.add(month10);
		
		Map<String, Object> month11 = new LinkedHashMap<String, Object>();
		Date date11 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(11) + " 00:00:00");
		month11.put("monthName", CommnonUtil.displayMonth(date11));
		month11.put("year", CommnonUtil.getYearFromDate(date11));
		month11.put("totalHitCount", getAccountsMonthTappals(11));
		list.add(month11);
		return list;
	}
	
	public int getManagementMonthTappals(int monthNo) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(monthNo) + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthEndDate(monthNo) + " 23:59:59");
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.eq("toRole","President"));
		or.add(Restrictions.eq("toRole","Vice President"));
		or.add(Restrictions.eq("toRole","Sr. Vice President"));
		criteria.add(or);
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list().size();
	}
	
	public List<Map<String, Object>> getManagementEachMonth() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> month = new LinkedHashMap<String, Object>();
		Date date = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(0) + " 00:00:00");
		month.put("monthName", CommnonUtil.displayMonth(date));
		month.put("year", CommnonUtil.getYearFromDate(date));
		month.put("totalHitCount", getManagementMonthTappals(0));
		list.add(month);
		
		Map<String, Object> month1 = new LinkedHashMap<String, Object>();
		Date date1 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(1) + " 00:00:00");
		month1.put("monthName", CommnonUtil.displayMonth(date1));
		month1.put("year", CommnonUtil.getYearFromDate(date1));
		month1.put("totalHitCount", getManagementMonthTappals(1));
		list.add(month1);
		
		Map<String, Object> month2 = new LinkedHashMap<String, Object>();
		Date date2 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(2) + " 00:00:00");
		month2.put("monthName", CommnonUtil.displayMonth(date2));
		month2.put("year", CommnonUtil.getYearFromDate(date2));
		month2.put("totalHitCount", getManagementMonthTappals(2));
		list.add(month2);
		
		Map<String, Object> month3 = new LinkedHashMap<String, Object>();
		Date date3 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(3) + " 00:00:00");
		month3.put("monthName", CommnonUtil.displayMonth(date3));
		month3.put("year", CommnonUtil.getYearFromDate(date3));
		month3.put("totalHitCount", getManagementMonthTappals(3));
		list.add(month3);
		
		Map<String, Object> month4 = new LinkedHashMap<String, Object>();
		Date date4 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(4) + " 00:00:00");
		month4.put("monthName", CommnonUtil.displayMonth(date4));
		month4.put("year", CommnonUtil.getYearFromDate(date4));
		month4.put("totalHitCount", getManagementMonthTappals(4));
		list.add(month4);
		
		Map<String, Object> month5 = new LinkedHashMap<String, Object>();
		Date date5 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(5) + " 00:00:00");
		month5.put("monthName", CommnonUtil.displayMonth(date5));
		month5.put("year", CommnonUtil.getYearFromDate(date5));
		month5.put("totalHitCount", getManagementMonthTappals(5));
		list.add(month5);
		
		Map<String, Object> month6 = new LinkedHashMap<String, Object>();
		Date date6 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(6) + " 00:00:00");
		month6.put("monthName", CommnonUtil.displayMonth(date6));
		month6.put("year", CommnonUtil.getYearFromDate(date6));
		month6.put("totalHitCount", getManagementMonthTappals(6));
		list.add(month6);
		
		Map<String, Object> month7 = new LinkedHashMap<String, Object>();
		Date date7 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(7) + " 00:00:00");
		month7.put("monthName", CommnonUtil.displayMonth(date7));
		month7.put("year", CommnonUtil.getYearFromDate(date7));
		month7.put("totalHitCount", getManagementMonthTappals(7));
		list.add(month7);
		
		Map<String, Object> month8 = new LinkedHashMap<String, Object>();
		Date date8 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(8) + " 00:00:00");
		month8.put("monthName", CommnonUtil.displayMonth(date8));
		month8.put("year", CommnonUtil.getYearFromDate(date8));
		month8.put("totalHitCount", getManagementMonthTappals(8));
		list.add(month8);
		
		Map<String, Object> month9 = new LinkedHashMap<String, Object>();
		Date date9 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(9) + " 00:00:00");
		month9.put("monthName", CommnonUtil.displayMonth(date9));
		month9.put("year", CommnonUtil.getYearFromDate(date9));
		month9.put("totalHitCount", getManagementMonthTappals(9));
		list.add(month9);
		
		Map<String, Object> month10 = new LinkedHashMap<String, Object>();
		Date date10 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(10) + " 00:00:00");
		month10.put("monthName", CommnonUtil.displayMonth(date10));
		month10.put("year", CommnonUtil.getYearFromDate(date10));
		month10.put("totalHitCount", getManagementMonthTappals(10));
		list.add(month10);
		
		Map<String, Object> month11 = new LinkedHashMap<String, Object>();
		Date date11 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(11) + " 00:00:00");
		month11.put("monthName", CommnonUtil.displayMonth(date11));
		month11.put("year", CommnonUtil.getYearFromDate(date11));
		month11.put("totalHitCount", getManagementMonthTappals(11));
		list.add(month11);
		return list;
	}

	public int getOthersMonthTappals(int monthNo) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(monthNo) + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthEndDate(monthNo) + " 23:59:59");
		criteria.add(Restrictions.ne("toRole", "accounts"));
		criteria.add(Restrictions.ne("toRole", "President"));
		criteria.add(Restrictions.ne("toRole", "Vice President"));
		criteria.add(Restrictions.ne("toRole", "Sr. Vice President"));
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list().size();
	}
	
	public List<Map<String, Object>> getOthersEachMonth() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> month = new LinkedHashMap<String, Object>();
		Date date = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(0) + " 00:00:00");
		month.put("monthName", CommnonUtil.displayMonth(date));
		month.put("year", CommnonUtil.getYearFromDate(date));
		month.put("totalHitCount", getOthersMonthTappals(0));
		list.add(month);
		
		Map<String, Object> month1 = new LinkedHashMap<String, Object>();
		Date date1 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(1) + " 00:00:00");
		month1.put("monthName", CommnonUtil.displayMonth(date1));
		month1.put("year", CommnonUtil.getYearFromDate(date1));
		month1.put("totalHitCount", getOthersMonthTappals(1));
		list.add(month1);
		
		Map<String, Object> month2 = new LinkedHashMap<String, Object>();
		Date date2 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(2) + " 00:00:00");
		month2.put("monthName", CommnonUtil.displayMonth(date2));
		month2.put("year", CommnonUtil.getYearFromDate(date2));
		month2.put("totalHitCount", getOthersMonthTappals(2));
		list.add(month2);
		
		Map<String, Object> month3 = new LinkedHashMap<String, Object>();
		Date date3 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(3) + " 00:00:00");
		month3.put("monthName", CommnonUtil.displayMonth(date3));
		month3.put("year", CommnonUtil.getYearFromDate(date3));
		month3.put("totalHitCount", getOthersMonthTappals(3));
		list.add(month3);
		
		Map<String, Object> month4 = new LinkedHashMap<String, Object>();
		Date date4 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(4) + " 00:00:00");
		month4.put("monthName", CommnonUtil.displayMonth(date4));
		month4.put("year", CommnonUtil.getYearFromDate(date4));
		month4.put("totalHitCount", getOthersMonthTappals(4));
		list.add(month4);
		
		Map<String, Object> month5 = new LinkedHashMap<String, Object>();
		Date date5 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(5) + " 00:00:00");
		month5.put("monthName", CommnonUtil.displayMonth(date5));
		month5.put("year", CommnonUtil.getYearFromDate(date5));
		month5.put("totalHitCount", getOthersMonthTappals(5));
		list.add(month5);
		
		Map<String, Object> month6 = new LinkedHashMap<String, Object>();
		Date date6 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(6) + " 00:00:00");
		month6.put("monthName", CommnonUtil.displayMonth(date6));
		month6.put("year", CommnonUtil.getYearFromDate(date6));
		month6.put("totalHitCount", getOthersMonthTappals(6));
		list.add(month6);
		
		Map<String, Object> month7 = new LinkedHashMap<String, Object>();
		Date date7 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(7) + " 00:00:00");
		month7.put("monthName", CommnonUtil.displayMonth(date7));
		month7.put("year", CommnonUtil.getYearFromDate(date7));
		month7.put("totalHitCount", getOthersMonthTappals(7));
		list.add(month7);
		
		Map<String, Object> month8 = new LinkedHashMap<String, Object>();
		Date date8 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(8) + " 00:00:00");
		month8.put("monthName", CommnonUtil.displayMonth(date8));
		month8.put("year", CommnonUtil.getYearFromDate(date8));
		month8.put("totalHitCount", getOthersMonthTappals(8));
		list.add(month8);
		
		Map<String, Object> month9 = new LinkedHashMap<String, Object>();
		Date date9 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(9) + " 00:00:00");
		month9.put("monthName", CommnonUtil.displayMonth(date9));
		month9.put("year", CommnonUtil.getYearFromDate(date9));
		month9.put("totalHitCount", getOthersMonthTappals(9));
		list.add(month9);
		
		Map<String, Object> month10 = new LinkedHashMap<String, Object>();
		Date date10 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(10) + " 00:00:00");
		month10.put("monthName", CommnonUtil.displayMonth(date10));
		month10.put("year", CommnonUtil.getYearFromDate(date10));
		month10.put("totalHitCount", getOthersMonthTappals(10));
		list.add(month10);
		
		Map<String, Object> month11 = new LinkedHashMap<String, Object>();
		Date date11 = CommnonUtil.convertStringDate(CommnonUtil.getPreviousMonthStartDate(11) + " 00:00:00");
		month11.put("monthName", CommnonUtil.displayMonth(date11));
		month11.put("year", CommnonUtil.getYearFromDate(date11));
		month11.put("totalHitCount", getOthersMonthTappals(11));
		list.add(month11);
		return list;
	}

	public List<Tappals> getAccountsEachMonthTappals(int month, int year) {
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getStartDateOnMonth(month, year) + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getEndDateOnMonth(month, year) + " 23:59:59");
		System.out.println("month start :"+CommnonUtil.getStartDateOnMonth(month, year));
		System.out.println("month start :"+CommnonUtil.getEndDateOnMonth(month, year));
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.eq("toRole", "accounts"));
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list();
	}

	public List<Tappals> getManagementEachMonthTappals(int month, int year) {
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getStartDateOnMonth(month, year) + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getEndDateOnMonth(month, year) + " 23:59:59");
		System.out.println("month start :"+CommnonUtil.getStartDateOnMonth(month, year));
		System.out.println("month start :"+CommnonUtil.getEndDateOnMonth(month, year));
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.eq("toRole","President"));
		or.add(Restrictions.eq("toRole","Vice President"));
		or.add(Restrictions.eq("toRole","Sr. Vice President"));
		criteria.add(or);
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list();
	}

	public List<Tappals> getOthersEachMonthTappals(int month, int year) {
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getStartDateOnMonth(month, year) + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getEndDateOnMonth(month, year) + " 23:59:59");
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.ne("toRole", "accounts"));
		criteria.add(Restrictions.ne("toRole", "President"));
		criteria.add(Restrictions.ne("toRole", "Vice President"));
		criteria.add(Restrictions.ne("toRole", "Sr. Vice President"));
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list();
	}
}