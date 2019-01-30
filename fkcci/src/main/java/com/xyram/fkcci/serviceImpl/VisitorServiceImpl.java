package com.xyram.fkcci.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xyram.fkcci.dao.VisitorDao;
import com.xyram.fkcci.model.Visitors;
import com.xyram.fkcci.service.VisitorService;
/**
 * 
 * @fileName : VisitorServiceImpl.java
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
@Service
@Transactional
public class VisitorServiceImpl implements VisitorService {

	@Autowired
	private VisitorDao visitorDao;

	public Visitors addVisitor(Visitors visitors) {
		return this.visitorDao.addVisitor(visitors);
	}

	public String deleteVisitorById(Integer id) {
		return this.visitorDao.deleteVisitorById(id);
	}

	public Visitors getVisitorById(Integer id) {
		return this.visitorDao.getVisitorById(id);
	}

	public List<Visitors> getAllVisitoByDate(String searchKeyword) {
		return this.visitorDao.getAllVisitoByDate(searchKeyword);
	}

	public List<Visitors> getAllVisitors(String searchKeyword) {
		return this.visitorDao.getAllVisitors(searchKeyword);
	}

	public List<Visitors> exportVisitorsByDate(String fromDate, String toDate) {
		return this.visitorDao.exportVisitorsByDate(fromDate, toDate);
	}

	public List<Visitors> getLastWeekVisitorDetails() {
		return this.visitorDao.getLastWeekVisitorDetails();
	}

	public List<Visitors> getLastMonthVisitorDetails() {
		return this.visitorDao.getLastMonthVisitorDetails();
	}

	public List<Map<String, Object>> getTotalHitcount() {
		return this.visitorDao.getTotalHitcount();
	}
}
