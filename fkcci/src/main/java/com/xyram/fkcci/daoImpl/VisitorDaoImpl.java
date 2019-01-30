package com.xyram.fkcci.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xyram.fkcci.dao.VisitorDao;
import com.xyram.fkcci.model.Visitors;
import com.xyram.fkcci.util.CommnonUtil;
/**
 * 
 * @fileName : VisitorDaoImpl.java
 *
 * @description : 
 *
 * @version : 1.0
 *
 * @date: Dec 02, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Repository
@Transactional
public class VisitorDaoImpl implements VisitorDao {

	private static final Logger logger = Logger.getLogger(VisitorDaoImpl.class.getName());

	@Autowired
	private SessionFactory sessionFactory;

	public Visitors addVisitor(Visitors visitors) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(visitors);
		return visitors;
	}

	public String deleteVisitorById(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();
		Visitors visitors = (Visitors) session.get(Visitors.class, id);
		if (null != visitors) {
			session.delete(visitors);
		}
		logger.info("Visitor deleted successfully, visitor id=" + visitors.getVisitorId());
		return "Visitor deleted successfully, visitor id=" + visitors.getVisitorId();
	}

	public Visitors getVisitorById(Integer id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Visitors.class);
		criteria.add(Restrictions.eq("visitorId", id));
		return (Visitors) criteria.uniqueResult();
	}

	public List<Visitors> getAllVisitoByDate(String searchKeyword) {
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer();
		hql.append("FROM Visitors");
		if (searchKeyword != null) {
			hql.append(" where to_char(visit_date_time, 'yyyy-MM-dd')  like '" + searchKeyword + "'");
		}
		hql.append(" order by visitDateTime desc");
		Query query = session.createQuery(hql.toString());
		System.out.println
		("new query: " + query);
		List<Visitors> list = query.list();
		return list;
	}

	public List<Visitors> getAllVisitors(String searchKeyword) {
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer();
		System.out.println("search:" + searchKeyword);
		hql.append("FROM Visitors");
		if (searchKeyword != null) {
			System.out.println("search:" + searchKeyword);
			hql.append(" WHERE (LOWER(visitorName) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR contactNo LIKE '%" + searchKeyword
					+ "%' OR LOWER(address) LIKE '%" + searchKeyword.toLowerCase()
					+ "%' OR LOWER(visitPurpose) LIKE '%" + searchKeyword.toLowerCase() + "%' OR LOWER(toMeet) LIKE '%"
					+ searchKeyword.toLowerCase() + "%'" + ")");
		}

		/** regex to verify integers */
		if (searchKeyword != null && searchKeyword.matches("\\d+")) {
			hql.append("or visitorId = '" + searchKeyword + "'");
		}
		hql.append(" order by visitDateTime desc");
		Query query = session.createQuery(hql.toString());
		System.out.println("new query: " + query);
		List<Visitors> list = query.list();
		return list;
	}

	public List<Visitors> exportVisitorsByDate(String fromDate, String toDate) {
		Session session = this.sessionFactory.getCurrentSession();
		Date fDate = CommnonUtil.convertStringDate(fromDate+" 00:00:00");
		Date tDate = CommnonUtil.convertStringDate(toDate+" 23:59:59");
		System.out.println("from :"+fDate);
		System.out.println("tDate :"+tDate);
		Criteria criteria = session.createCriteria(Visitors.class);
		criteria.add(Restrictions.ge("visitDateTime", fDate));
		criteria.add(Restrictions.le("visitDateTime", tDate));
		criteria.addOrder(Order.desc("visitDateTime"));
		List<Visitors> list = criteria.list();
		return list;
	}

	public String getDateTime(Integer visitorId) {
		String time = "";
		Session session = this.sessionFactory.getCurrentSession();
		String sql = "select to_char(visitDateTime, 'yyyy-mm-dd hh12:mi:ss') from Visitors WHERE visitorId ="+visitorId;
		Query query = session.createQuery(sql);
		System.out.println("query :"+query);
		return time = query.getQueryString();
	}

	public List<Visitors> getLastWeekVisitorDetails() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Visitors.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastWeekDate() + " 00:00:00");
		System.out.println("CurrentDate: " + fromDate);
		System.out.println("LastWeekDate: " + toDate);
		criteria.add(Restrictions.le("visitDateTime", fromDate));
		criteria.add(Restrictions.ge("visitDateTime", toDate));
		criteria.addOrder(Order.desc("visitDateTime"));
		List<Visitors> list = criteria.list();
		return list;
	}

	public List<Visitors> getLastMonthVisitorDetails() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Visitors.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastMonthDate() + " 00:00:00");
		System.out.println("CurrentDate: " + fromDate);
		System.out.println("LastMonthDate: " + toDate);
		criteria.add(Restrictions.le("visitDateTime", fromDate));
		criteria.add(Restrictions.ge("visitDateTime", toDate));
		criteria.addOrder(Order.desc("visitDateTime"));
		List<Visitors> list = criteria.list();
		return list;
	}

	public List<Map<String, Object>> getTotalHitcount() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> lastWeekMap = new LinkedHashMap<String, Object>();
		lastWeekMap.put("name", "Visitor Last Week");
		lastWeekMap.put("totalHitCount", getLastWeekVisitorsCount());
		Map<String, Object> lastMonthMap = new LinkedHashMap<String, Object>();
		lastMonthMap.put("name", "Visitor Last Month");
		lastMonthMap.put("totalHitCount", getLastMonthVisitorsCount());
		list.add(lastWeekMap);
		list.add(lastMonthMap);
		return list;
	}
	
	public int getLastWeekVisitorsCount() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Visitors.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastWeekDate() + " 00:00:00");
		criteria.add(Restrictions.le("visitDateTime", fromDate));
		criteria.add(Restrictions.ge("visitDateTime", toDate));
		return criteria.list().size();
	}
	
	public int getLastMonthVisitorsCount() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Visitors.class);
		Date fromDate = CommnonUtil.convertStringDate(CommnonUtil.getTodayDate());
		Date toDate = CommnonUtil.convertStringDate(CommnonUtil.getLastMonthDate() + " 00:00:00");
		criteria.add(Restrictions.le("visitDateTime", fromDate));
		criteria.add(Restrictions.ge("visitDateTime", toDate));
		return criteria.list().size();
	}
}
