package com.xyram.fkcci.serviceImpl;

import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xyram.fkcci.dao.ToNameDao;
import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.model.ToName;
import com.xyram.fkcci.service.ToNameService;
/**
 * 
 * @fileName : ToNameServiceImpl.java
 *
 * @description : 
 *
 * @version : 1.0
 *
 * @date: Jan 7, 2018
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Service
@Transactional
public class ToNameServiceImpl implements ToNameService {
	

	@Autowired
	private ToNameDao toNameDao;
	
	public ToName saveToName(ToName toName) throws Exception {
		return this.toNameDao.saveToName(toName);
	}

	public List<ToName> getAllToName() {
		return this.toNameDao.getAllToName();

	}

	public String deleteToNameById(Integer id) {
		return this.toNameDao.deleteToNameById(id);
	}

	public ToName getToNameById(Integer id) throws Exception {
		return this.toNameDao.getToNameById(id);
	}

	public List<Tappals> getMyPost(String toName, int offset, int limit) {
		return this.toNameDao.getMyPost(toName, offset, limit);
	}

	public List<Tappals> getMyTodayPost(String toName) {
		return this.toNameDao.getMyTodayPost(toName);
	}

	public List<Tappals> getMyYesterdayPost(String toName) {
		return this.toNameDao.getMyYesterdayPost(toName);
	}

	public List<Tappals> getMyLastWeekPost(String toName) {
		return this.toNameDao.getMyLastWeekPost(toName);
	}

	public List<Tappals> getMyLastMonthPost(String toName) {
		return this.toNameDao.getMyLastMonthPost(toName);
	}

	public List<Tappals> getAccountsPost() {
		return this.toNameDao.getAccountsPost();
	}

	public List<Tappals> getManagementPost() {
		return this.toNameDao.getManagementPost();
	}

	public List<Tappals> getOthersPost() {
		return this.toNameDao.getOthersPost();
	}

	public List<Map<String, Object>> getTotalHitcount(String roleName) {
		return this.toNameDao.getTotalHitcount(roleName);
	}

}
