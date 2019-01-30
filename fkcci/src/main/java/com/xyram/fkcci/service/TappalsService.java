package com.xyram.fkcci.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.xyram.fkcci.model.Tappals;
/**
 * 
 * @fileName : TappalsService.java
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
public interface TappalsService {

	public Tappals saveTappals(Tappals tappals);

	public List<Tappals> getAllTappals(String searchKeyword, int offset, int limit);

	public Tappals getTappalsById(Integer id, int offset, int limit);

	public Tappals updateTappals(Tappals tappals);

	public Map<String, Object> deleteTappalById(Integer id);

	public String saveFile(HashMap<String, String> params, MultipartFile[] files);

	public Integer downloadFile(Integer slNo, HttpServletResponse response);

	public List<Tappals> getAllTappalDate(String searchKeyword, int offset, int limit);

	public List<Tappals> exportTappalsByDate(String fromDate, String toDate);

	public List<Tappals> getMyTappals(String toRole, String searchKeyword);

	public List<Tappals> getMyTappalsByDate(String toRole, String searchKeyword);

	public List<Tappals> exportTappalsByRole(String toRole, String from, String to);

	public List<Tappals> getTodayTappals();

	public List<Tappals> getYesterdayTappals();
	
	public List<Tappals> getLastWeekTappals();

	public List<Tappals> getLastMonthTappals();

	public List<Map<String, Object>> getTotalHitcount();

	public List<Map<String, Object>> getTappalSummaryCount();

	public List<Tappals> getPreviousMonthTappals(int monthNo);

	public List<Map<String, Object>> getAccountsEachMonth();

	public List<Map<String, Object>> getManagementEachMonth();

	public List<Map<String, Object>> getOthersEachMonth();

	public List<Tappals> getAccountsEachMonthTappals(int month, int year);

	public List<Tappals> getManagementEachMonthTappals(int month, int year);

	public List<Tappals> getOthersEachMonthTappals(int month, int year);

}