package com.xyram.fkcci.serviceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.xyram.fkcci.dao.TappalsDao;
import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.service.TappalsService;
/**
 * 
 * @fileName : TappalsServiceImpl.java
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
@Service
@Transactional
public class TappalsServiceImpl implements TappalsService {
	
	@Autowired
	private TappalsDao tappalsDao;

	public Tappals saveTappals(Tappals tappals){
		return tappalsDao.saveTappals(tappals);
	}

	public List<Tappals> getAllTappals(String searchKeyword, int offset, int limit) {
		return tappalsDao.getAllTappals(searchKeyword, offset, limit);
	}

	public Tappals getTappalsById(Integer id, int offset, int limit) {
		return tappalsDao.getTappalsById(id, offset, limit);
	}

	public Tappals updateTappals(Tappals tappals) {
	return tappalsDao.updateTappals(tappals);
	}

	public Map<String, Object> deleteTappalById(Integer id) {
		return this.tappalsDao.deleteTappalById(id);
	}
	
	public String saveFile(HashMap<String, String> params, MultipartFile[] files) {
		return this.tappalsDao.saveFile(params, files);
	}

	public Integer downloadFile(Integer slNo, HttpServletResponse response) {
		return this.tappalsDao.downloadFile(slNo, response);
	}

	public List<Tappals> getAllTappalDate(String searchKeyword, int offset, int limit) {
		return tappalsDao.getAllTappalDate(searchKeyword, offset, limit);
	}

	public List<Tappals> exportTappalsByDate(String fromDate, String toDate) {
		return tappalsDao.exportTappalsByDate(fromDate, toDate);
	}

	public List<Tappals> getMyTappals(String toRole, String searchKeyword) {
		return tappalsDao.getMyTappals(toRole, searchKeyword);
	}

	public List<Tappals> getMyTappalsByDate(String toRole, String searchKeyword) {
		return tappalsDao.getMyTappalDate(toRole, searchKeyword);
	}

	public List<Tappals> exportTappalsByRole(String toRole, String fromDate, String toDate) {
		return tappalsDao.exportTappalsByRole(toRole, fromDate, toDate);
	}

	public List<Tappals> getTodayTappals() {
		return tappalsDao.getTodayTappals();
	}

	public List<Tappals> getYesterdayTappals() {
		return tappalsDao.getYesterdayTappals();
	}
	
	public List<Tappals> getLastWeekTappals() {
		return tappalsDao.getLastWeekTappals();
	}

	public List<Tappals> getLastMonthTappals() {
		return tappalsDao.getLastMonthTappals();
	}

	public List<Map<String, Object>> getTotalHitcount() {
		return tappalsDao.getTotalHitcount();
	}

	public List<Map<String, Object>> getTappalSummaryCount() {
		return tappalsDao.getTappalSummaryCount();
	}

	public List<Tappals> getPreviousMonthTappals(int monthNo) {
		return tappalsDao.getPreviousMonthTappals(monthNo);
	}

	public List<Map<String, Object>> getAccountsEachMonth() {
		return tappalsDao.getAccountsEachMonth();
	}

	public List<Map<String, Object>> getManagementEachMonth() {
		return tappalsDao.getManagementEachMonth();
	}

	public List<Map<String, Object>> getOthersEachMonth() {
		return tappalsDao.getOthersEachMonth();
	}

	public List<Tappals> getAccountsEachMonthTappals(int month, int year) {
		return tappalsDao.getAccountsEachMonthTappals(month, year);
	}

	public List<Tappals> getManagementEachMonthTappals(int month, int year) {
		return tappalsDao.getManagementEachMonthTappals(month, year);
	}

	public List<Tappals> getOthersEachMonthTappals(int month, int year) {
		return tappalsDao.getOthersEachMonthTappals(month, year);
	}

}
