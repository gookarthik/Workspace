package com.xyram.fkcci.service;

import java.util.List;
import java.util.Map;

import com.xyram.fkcci.model.Visitors;
/**
 * 
 * @fileName : VisitorService.java
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
public interface VisitorService {

	public Visitors addVisitor(Visitors visitors);

	public String deleteVisitorById(Integer id);

	public Visitors getVisitorById(Integer id);

	public List<Visitors> getAllVisitoByDate(String searchKeyword);

	public List<Visitors> getAllVisitors(String searchKeyword);

	public List<Visitors> exportVisitorsByDate(String fromDate, String toDate);

	public List<Visitors> getLastWeekVisitorDetails();

	public List<Visitors> getLastMonthVisitorDetails();

	public List<Map<String, Object>> getTotalHitcount();
}
