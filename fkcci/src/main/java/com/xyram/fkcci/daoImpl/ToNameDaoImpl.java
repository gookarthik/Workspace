package com.xyram.fkcci.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xyram.fkcci.dao.AbstractDao;
import com.xyram.fkcci.dao.ToNameDao;
import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.model.ToName;
import com.xyram.fkcci.util.CommnonUtil;
/**
 * 
 * @fileName : ToNameDaoImpl.java
 *
 * @description : 
 *
 * @version : 1.0
 *
 * @date: Nov 23, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Repository
public class ToNameDaoImpl extends AbstractDao implements ToNameDao {

	private static final Logger logger = Logger.getLogger(ToNameDaoImpl.class.getName());

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public ToName saveToName(ToName toName) throws Exception {
		getSession().saveOrUpdate(toName);
		return toName;
	}

	public List<ToName> getAllToName() {
		Criteria criteria = getSession().createCriteria(ToName.class);
		@SuppressWarnings("unchecked")
		List<ToName> list = criteria.list();
		return list;
	}

	public String deleteToNameById(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();
		ToName p = (ToName) session.get(ToName.class, id);
		if (null != p) {
			session.delete(p);
		}
		logger.info("ToName deleted successfully, ToName details=" + p.getId());
		return "ToName deleted successfully, ToName details=" + p.getId();
	}

	public ToName getToNameById(Integer id) throws Exception {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(ToName.class);
		c.add(Restrictions.eq("Id", id));
		return (ToName) c.uniqueResult();
	}

	public List<Tappals> getMyPost(String toName, int offset, int limit) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(Tappals.class);
		c.add(Restrictions.eq("toRole", toName));
		List<Tappals> list = c.list();
		return list;
	}

	public List<Tappals> getMyTodayPost(String toName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.getTodayDateStart();
		System.out.println("TodayStartDate: " + toDate);
		System.out.println("TodayCurrentDate: " + fromDate);
		System.out.println("role: " + toName);
		criteria.add(Restrictions.eq("toRole", toName));
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getMyYesterdayPost(String toName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 23:59:59");
		System.out.println("YesterdayStartDate: " + fromDate);
		System.out.println("YesterdayEndDate: " + toDate);
		System.out.println("role: " + toName);
		criteria.add(Restrictions.eq("toRole", toName));
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getMyLastWeekPost(String toName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastWeekDate() + " 00:00:00");
		System.out.println("CurrentDate: " + fromDate);
		System.out.println("LastWeekDate: " + toDate);
		System.out.println("role: " + toName);
		criteria.add(Restrictions.eq("toRole", toName));
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getMyLastMonthPost(String toName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastMonthDate() + " 00:00:00");
		System.out.println("CurrentDate: " + fromDate);
		System.out.println("LastMonthDate: " + toDate);
		System.out.println("role: " + toName);
		criteria.add(Restrictions.eq("toRole", toName));
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getAccountsPost() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.eq("toRole", "accounts"));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Tappals> getManagementPost() {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM Tappals WHERE toRole = 'President' OR toRole = 'Vice President' OR toRole = 'Sr. Vice President'");
		hql.append(" order by receivedDate desc");
		Query query = getSession().createQuery(hql.toString());
		System.out.println("query: " + hql.toString());
		List<Tappals> list = query.list();
		return list;
	}

	public List<Tappals> getOthersPost() {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.ne("toRole", "accounts"));
		criteria.add(Restrictions.ne("toRole", "President"));
		criteria.add(Restrictions.ne("toRole", "Vice President"));
		criteria.add(Restrictions.ne("toRole", "Sr. Vice President"));
		criteria.addOrder(Order.desc("receivedDate"));
		List<Tappals> list = criteria.list();
		return list;
	}

	public List<Map<String, Object>> getTotalHitcount(String roleName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> totalMytappals = new LinkedHashMap<String, Object>();
		totalMytappals.put("name", "Total My Post");
		totalMytappals.put("totalHitCount", getMyTotalTappalsCount(roleName));
		Map<String, Object> myTodayTappals = new LinkedHashMap<String, Object>();
		myTodayTappals.put("name", "My Post for Today");
		myTodayTappals.put("totalHitCount", getMyTodayTappalsCount(roleName));
		Map<String, Object> myYesterdayTappals = new LinkedHashMap<String, Object>();
		myYesterdayTappals.put("name", "My Post for Yesterday");
		myYesterdayTappals.put("totalHitCount", getMyYesterdayTappalsCount(roleName));
		Map<String, Object> myLastWeekTappals = new LinkedHashMap<String, Object>();
		myLastWeekTappals.put("name", "My Post for Last Week");
		myLastWeekTappals.put("totalHitCount", getMyLastWeekTappalsCount(roleName));
		Map<String, Object> myLastMonthTappals = new LinkedHashMap<String, Object>();
		myLastMonthTappals.put("name", "My Post for Last Month");
		//myLastMonthTappals.put("totalHitCount", getMyLastMonthTappalsCount(roleName));
		list.add(totalMytappals);
		list.add(myTodayTappals);
		list.add(myYesterdayTappals);
		list.add(myLastWeekTappals);
		//list.add(myLastMonthTappals);
		return list;
	}
	
	public int getMyTotalTappalsCount(String roleName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.eq("toRole", roleName));
		return criteria.list().size();
	}
	
	public int getMyTodayTappalsCount(String roleName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.getTodayDateStart();
		criteria.add(Restrictions.eq("toRole", roleName));
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		return criteria.list().size();
	}
	
	public int getMyYesterdayTappalsCount(String roleName) {
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 00:00:00");
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getYesterdayDate() + " 23:59:59");
		Criteria criteria = getSession().createCriteria(Tappals.class);
		criteria.add(Restrictions.eq("toRole", roleName));
		criteria.add(Restrictions.ge("date", fromDate));
		criteria.add(Restrictions.le("date", toDate));
		return criteria.list().size();
	}
	
	public int getMyLastWeekTappalsCount(String roleName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastWeekDate() + " 00:00:00");
		criteria.add(Restrictions.eq("toRole", roleName));
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		return criteria.list().size();
	}
	
	public int getMyLastMonthTappalsCount(String roleName) {
		Criteria criteria = getSession().createCriteria(Tappals.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastMonthDate() + " 00:00:00");
		criteria.add(Restrictions.eq("toRole", roleName));
		criteria.add(Restrictions.le("date", fromDate));
		criteria.add(Restrictions.ge("date", toDate));
		return criteria.list().size();
	}
}
