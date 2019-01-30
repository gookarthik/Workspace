package com.xyram.fkcci.dao;

import java.util.List;
import java.util.Map;
import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.model.ToName;
/**
 * 
 * @fileName : ToNameDao.java
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
public interface ToNameDao {
	
	public ToName saveToName(ToName toName) throws Exception;

	public List<ToName> getAllToName();

	public String deleteToNameById(Integer id);

	public ToName getToNameById(Integer id) throws Exception;

	public List<Tappals> getMyPost(String toName, int offset, int limit);

	public List<Tappals> getMyTodayPost(String toName);

	public List<Tappals> getMyYesterdayPost(String toName);

	public List<Tappals> getMyLastWeekPost(String toName);

	public List<Tappals> getMyLastMonthPost(String toName);

	public List<Tappals> getAccountsPost();

	public List<Tappals> getManagementPost();

	public List<Tappals> getOthersPost();

	public List<Map<String, Object>> getTotalHitcount(String roleName);

}
