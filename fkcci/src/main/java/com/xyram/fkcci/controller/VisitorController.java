package com.xyram.fkcci.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyram.fkcci.model.Visitors;
import com.xyram.fkcci.service.VisitorService;
import com.xyram.fkcci.util.CommnonUtil;
/**
 * 
 * @fileName : VisitorController.java
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
@Controller
@RequestMapping("/visitor")
public class VisitorController {

	@Autowired
	private VisitorService visitorService;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Visitors addVisitor(@RequestBody Visitors visitors) throws Exception {
		return visitorService.addVisitor(visitors);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteVisitorById(@PathVariable Integer id) {
		String response = visitorService.deleteVisitorById(id);
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Visitors getVisitorById(@PathVariable Integer id) throws Exception {
		Visitors response = visitorService.getVisitorById(id);
		return response;
	}

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<Visitors> getAllVisitors(@RequestParam(value = "searchKeyword", required = false) String searchKeyword)
			throws Exception {
		
		/**	Verifying, is given searchkeyword is date format? if yes converting it to another format	*/
		if (searchKeyword != null && searchKeyword.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
			searchKeyword = CommnonUtil.convertStringDateFormat(searchKeyword, "dd/MM/yyyy");
			return this.visitorService.getAllVisitoByDate(searchKeyword);
		} else if (searchKeyword != null && searchKeyword.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
			searchKeyword = CommnonUtil.convertStringDateFormat(searchKeyword, "dd-MM-YYYY");
			return this.visitorService.getAllVisitoByDate(searchKeyword);
		} else {
			return this.visitorService.getAllVisitors(searchKeyword);
		}
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public List<Visitors> viewVisitors(@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate) throws Exception {
		return visitorService.exportVisitorsByDate(fromDate, toDate);

	}
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody
	public int exportTappals(@RequestParam HashMap<String, String> params, HttpServletResponse response)
			throws IOException, ParseException {
		String from = params.get("fromDate");
		String to = params.get("toDate");
		List<Visitors> visitors = visitorService.exportVisitorsByDate(from, to);
		String contextPath = System.getProperty("catalina.base") + "/Visitor Data/CSV/";
		File dir = new File(contextPath);
		if (!dir.exists())dir.mkdirs();
		String csvFileName = contextPath + "Tappals.csv";
		File exportCsvFile = exportResponseToCSV(visitors, csvFileName);
		return CommnonUtil.downloadFIleByName(exportCsvFile, response);
	}
	
	public static File exportResponseToCSV(List<Visitors> visitors, String csvFileName) throws IOException {
		System.out.println("Exporting tappals...");
		String timeIn = "";
		File csvFile = new File(csvFileName);
		StringBuilder responseData = new StringBuilder();
		String title = "Sl No ,Name ,Contact No ,Addres ,Purpose of Visit ,To Meet ,Time In";
		responseData.append(title);
		for (Visitors visitor : visitors) {
			StringBuilder exportData = new StringBuilder();
			
			if (visitor.getVisitDateTime() != null && visitor.getVisitorId() != null) {
				System.out.println("dtetime :"+visitor.getVisitDateTime());
				timeIn = CommnonUtil.convertDateTimeToString(visitor.getVisitDateTime());
				System.out.println("timeIn :"+timeIn);
				/*VisitorDaoImpl visitorDaoImpl = new VisitorDaoImpl();
				timeIn = visitorDaoImpl.getDateTime(visitor.getVisitorId());*/
			}
			exportData.append('\n');
			exportData.append(visitor.getVisitorId() + "," + visitor.getVisitorName() + ","
					+ visitor.getContactNo() + "," + "\"" + visitor.getAddress() + "\"" + "," + visitor.getVisitPurpose()
					+ "," + "\"" + visitor.getToMeet() + "\"" + "," + timeIn);
			responseData.append(exportData);
		}

		PrintWriter pw = new PrintWriter(csvFile);
		pw.write(responseData.toString());
		pw.close();

		System.out.println("Response Data exported to the CSV File...!");
		return csvFile;
	}
	
	@RequestMapping(value = "/lastWeek", method = RequestMethod.GET)
	@ResponseBody
	public List<Visitors> getLastWeekVisitorDetails() throws Exception {
		return this.visitorService.getLastWeekVisitorDetails();
	}

	@RequestMapping(value = "/lastMonth", method = RequestMethod.GET)
	@ResponseBody
	public List<Visitors> getLastMonthVisitorDetails() throws Exception {
		return this.visitorService.getLastMonthVisitorDetails();
	}
	
	@RequestMapping(value = "/hitCount", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getTotalHitcount() throws Exception {
		return (List<Map<String, Object>>) this.visitorService.getTotalHitcount();
	}
	
	@RequestMapping(value = "/model", method = RequestMethod.GET)
	@ResponseBody
	public Visitors getModelClass() {
		Visitors visitors = new Visitors();
		return visitors;
	}
}
