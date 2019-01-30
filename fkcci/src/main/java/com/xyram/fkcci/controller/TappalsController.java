
package com.xyram.fkcci.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.service.TappalsService;
import com.xyram.fkcci.util.CommnonUtil;

/**
 * 
 * @fileName : TappalsController.java
 *
 * @description :
 *
 * @version : 1.0
 *
 * @date: Nov 18, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Controller
@RequestMapping("/tappals")
public class TappalsController {
	@Autowired
	private TappalsService tappalsService;

	/**
	 * 
	 * @function saveTappals
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * 				@parm(s) : @param tappals @parm(s) : @return
	 * 
	 * @return : Tappals
	 *
	 * @throws :
	 *             <Mentioned if any exceptions>
	 *
	 */

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Tappals saveTappals(@RequestBody Tappals tappals) {
		return tappalsService.saveTappals(tappals);
	}

	/**
	 * 
	 * @function getAllTappals
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * 				@parm(s) : @param searchKeyword @parm(s) : @param
	 *              offsetStr @parm(s) : @param limitStr @parm(s) : @return @parm(s)
	 *              : @throws Exception
	 * 
	 * @return : List<Tappals>
	 *
	 * @throws :
	 *             <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getAllTappals(@RequestParam(value = "searchKeyword", required = false) String searchKeyword,
			@RequestParam(value = "offset", required = false) String offsetStr,
			@RequestParam(value = "limit", required = false) String limitStr) throws Exception {
		int offset = 0, limit = 10;
		if (offsetStr != null) {
			offset = Integer.parseInt(offsetStr);
		}
		if (limitStr != null) {
			limit = Integer.parseInt(limitStr);
		}

		/**
		 * Verifying, is given searchkeyword is date format? if yes converting it to
		 * another format
		 */
		if (searchKeyword != null && searchKeyword.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
			searchKeyword = CommnonUtil.convertStringDateFormat(searchKeyword, "dd/MM/yyyy");
			return this.tappalsService.getAllTappalDate(searchKeyword, offset, limit);
		} else if (searchKeyword != null && searchKeyword.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
			searchKeyword = CommnonUtil.convertStringDateFormat(searchKeyword, "dd-MM-YYYY");
			return this.tappalsService.getAllTappalDate(searchKeyword, offset, limit);
		} else {
			return this.tappalsService.getAllTappals(searchKeyword, offset, limit);
		}
	}

	/**
	 * 
	 * @function getTappalsById
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * 				@parm(s) : @param id @parm(s) : @param offsetStr @parm(s)
	 *              : @param limitStr @parm(s) : @return @parm(s) : @throws
	 *              Exception
	 * 
	 * @return : Tappals
	 *
	 * @throws :
	 *             <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Tappals getTappalsById(@PathVariable Integer id,
			@RequestParam(value = "offset", required = false) String offsetStr,
			@RequestParam(value = "limit", required = false) String limitStr) throws Exception {
		int offset = 0, limit = 10;
		if (offsetStr != null) {
			offset = Integer.parseInt(offsetStr);
		}
		if (limitStr != null) {
			limit = Integer.parseInt(limitStr);
		}
		return this.tappalsService.getTappalsById(id, offset, limit);
	}

	/**
	 * 
	 * @function updateTappals
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * 				@parm(s) : @param tappals @parm(s) : @return
	 * 
	 * @return : Tappals
	 *
	 * @throws :
	 *             <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	@ResponseBody
	public Tappals updateTappals(@RequestBody Tappals tappals) {

		return this.tappalsService.updateTappals(tappals);
	}

	/**
	 * 
	 * @function deleteTappalById
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param id @parm(s) : @return
	 * 
	 * @return : Map<String,Object>
	 *
	 * @throws :
	 * <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Map<String, Object> deleteTappalById(@PathVariable Integer id) {
		return tappalsService.deleteTappalById(id);
	}

	/**
	 * 
	 * @function getModelClass 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : Tappals
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/model", method = RequestMethod.GET)
	@ResponseBody
	public Tappals getModelClass() {

		Tappals tappls = new Tappals();

		return tappls;
	}

	/**
	 * 
	 * @function uploadFile 
	 * 
	 * @created_date : Nov 22, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param files
	 * @parm(s) : @param params
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : String
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile[] files, @RequestParam HashMap<String, String> params)
			throws Exception {

		return tappalsService.saveFile(params, files);
	}

	/**
	 * 
	 * @function downloadFile 
	 * 
	 * @created_date : Nov 22, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param slNo
	 * @parm(s) : @param response
	 * @parm(s) : @return
	 * @parm(s) : @throws IOException
	 * 
	 * @return : Integer
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	public Integer downloadFile(@RequestParam("slNo") Integer slNo, HttpServletResponse response) throws IOException {

		return tappalsService.downloadFile(slNo, response);
	}

	/**
	 * 
	 * @function exportTappals 
	 * 
	 * @created_date : Nov 28, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param params
	 * @parm(s) : @param response
	 * @parm(s) : @return
	 * @parm(s) : @throws IOException
	 * @parm(s) : @throws ParseException
	 * 
	 * @return : int
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody
	public int exportTappals(@RequestParam HashMap<String, String> params, HttpServletResponse response)
			throws IOException, ParseException {
		String from = params.get("fromDate");
		String to = params.get("toDate");
		List<Tappals> tappals = tappalsService.exportTappalsByDate(from, to);
		String contextPath = System.getProperty("catalina.base") + "/Courier Data/CSV/";
		File dir = new File(contextPath);
		if (!dir.exists())
			dir.mkdirs();
		String csvFileName = contextPath + "Tappals.csv";
		File exportCsvFile = exportResponseToCSV(tappals, csvFileName);
		return CommnonUtil.downloadFIleByName(exportCsvFile, response);
	}

	/**
	 * 
	 * @function exportResponseToCSV 
	 * 
	 * @created_date : Nov 28, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param tappals
	 * @parm(s) : @param csvFileName
	 * @parm(s) : @return
	 * @parm(s) : @throws IOException
	 * 
	 * @return : File
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	public static File exportResponseToCSV(List<Tappals> tappals, String csvFileName) throws IOException {
		System.out.println("Exporting tappals...");
		String receivedDate = null, date = null;
		File csvFile = new File(csvFileName);
		StringBuilder responseData = new StringBuilder();
		String title = "Sl No ,Date ,Courier Name ,Courier/RIG Post Number ,From ,To ,Received Date ,Particular details, Sign";
		responseData.append(title);
		for (Tappals tappal : tappals) {
			StringBuilder exportData = new StringBuilder();
			if (tappal.getDate() != null) {
				date = CommnonUtil.convertDateToString(tappal.getDate());
			}
			if (tappal.getReceivedDate() != null) {
				receivedDate = CommnonUtil.convertDateToString(tappal.getReceivedDate());
			}
			exportData.append('\n');
			exportData.append(tappal.getSlNo() + "," + date + "," + tappal.getCourierName() + ","
					+ tappal.getCourierRigNo() + "," + "\"" + tappal.getFromName() + "\"" + "," + tappal.getToRole()
					+ "," + "\"" + receivedDate + "\"" + "," + tappal.getParticularsDetails());
			responseData.append(exportData);
		}

		PrintWriter pw = new PrintWriter(csvFile);
		pw.write(responseData.toString());
		pw.close();

		System.out.println("Response Data exported to the CSV File...!");
		return csvFile;
	}

	/**
	 * 
	 * @function viewTappals 
	 * 
	 * @created_date : Nov 29, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param params
	 * @parm(s) : @param response
	 * @parm(s) : @return
	 * @parm(s) : @throws IOException
	 * @parm(s) : @throws ParseException
	 * 
	 * @return : List<Tappals>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> viewTappals(@RequestParam HashMap<String, String> params, HttpServletResponse response)
			throws IOException, ParseException {
		String from = params.get("fromDate");
		String to = params.get("toDate");
		return tappalsService.exportTappalsByDate(from, to);
	}

	@RequestMapping(value = "/role/search", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getMyTappals(@RequestParam(value = "toRole", required = false) String toRole,
			@RequestParam(value = "searchKeyword", required = false) String searchKeyword) throws Exception {
		/**
		 * Verifying, is given searchkeyword is date format? if yes converting it to
		 * another format
		 */
		if (searchKeyword != null && searchKeyword.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
			searchKeyword = CommnonUtil.convertStringDateFormat(searchKeyword, "dd/MM/yyyy");
			return this.tappalsService.getMyTappalsByDate(toRole, searchKeyword);
		} else if (searchKeyword != null && searchKeyword.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
			searchKeyword = CommnonUtil.convertStringDateFormat(searchKeyword, "dd-MM-YYYY");
			return this.tappalsService.getMyTappalsByDate(toRole, searchKeyword);
		} else {
			return this.tappalsService.getMyTappals(toRole, searchKeyword);
		}
	}

	@RequestMapping(value = "/role/view", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> viewTappalsByRole(@RequestParam HashMap<String, String> params, HttpServletResponse response)
			throws IOException, ParseException {
		String toRole = params.get("toRole");
		String from = params.get("fromDate");
		String to = params.get("toDate");
		return tappalsService.exportTappalsByRole(toRole, from, to);

	}

	@RequestMapping(value = "/role/export", method = RequestMethod.GET)
	@ResponseBody
	public int exportTappalsByRole(@RequestParam HashMap<String, String> params, HttpServletResponse response)
			throws IOException, ParseException {
		String toRole = params.get("toRole");
		String from = params.get("fromDate");
		String to = params.get("toDate");
		List<Tappals> tappals = tappalsService.exportTappalsByRole(toRole, from, to);
		String contextPath = System.getProperty("catalina.base") + "/Courier Data/CSV/";
		File dir = new File(contextPath);
		if (!dir.exists())
			dir.mkdirs();
		String csvFileName = contextPath + "Tappals.csv";
		File exportCsvFile = exportResponseToCSV(tappals, csvFileName);
		return CommnonUtil.downloadFIleByName(exportCsvFile, response);
	}
	
	@RequestMapping(value = "/today", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getTodayTappals() throws Exception {
		return this.tappalsService.getTodayTappals();
	}

	@RequestMapping(value = "/yesterday", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getYesterdayTappals() throws Exception {
		return this.tappalsService.getYesterdayTappals();
	}

	@RequestMapping(value = "/lastWeek", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getLastWeekTappals() throws Exception {
		return this.tappalsService.getLastWeekTappals();
	}

	@RequestMapping(value = "/lastMonth", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getLastMonthTappals() throws Exception {
		return this.tappalsService.getLastMonthTappals();
	}

	@RequestMapping(value = "/hitCount", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getTotalHitcount() throws Exception {
		return (List<Map<String, Object>>) this.tappalsService.getTotalHitcount();
	}

	@RequestMapping(value = "/tappalSummary", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getTappalSummaryCount() throws Exception {
		return (List<Map<String, Object>>) this.tappalsService.getTappalSummaryCount();
	}

	@RequestMapping(value = "/previousMonth", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getPreviousMonthTappals(@RequestParam(value = "monthNo", required = false) int monthNo)
			throws Exception {
		return (List<Tappals>) this.tappalsService.getPreviousMonthTappals(monthNo);
	}

	/**
	 * 
	 */
	@RequestMapping(value = "/exportNew", method = RequestMethod.GET)
	@ResponseBody
	public int exportTappalNew(@RequestParam HashMap<String, String> params, HttpServletResponse response)
			throws IOException, ParseException {
		String from = params.get("fromDate");
		String to = params.get("toDate");
		List<Tappals> tappals = tappalsService.exportTappalsByDate(from, to);
		File exportCsvFile = exportResponseToExcel(tappals);
		return CommnonUtil.downloadFIleByName(exportCsvFile, response);
	}

	public static File exportResponseToExcel(List<Tappals> tappals) throws IOException {
		System.out.println("Exporting tappals...");
		String receivedDate = null, date = null;
		FileOutputStream fileOut = null;
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("MYSheet");
		InputStream inputStream = new FileInputStream("F:/bhojana/bhojana/src/main/resources/images/fkcci.png");
		byte[] imageBytes = IOUtils.toByteArray(inputStream);
		int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
		inputStream.close();

		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
		anchor.setCol1(4); // first anchor determines upper left position
		anchor.setRow1(0);
		anchor.setRow2(8); // second anchor determines bottom right position
		anchor.setCol2(5);
		anchor.setDx2(Units.toEMU(12)); // dx = left + wanted width
		anchor.setDy2(Units.toEMU(12)); // dy= top + wanted height

		drawing.createPicture(anchor, pictureureIdx);
		Row header = sheet.createRow(9);

		Cell cell = header.createCell(0);
		cell.setCellValue("Sl No");
		sheet.setColumnWidth(0, 1500);

		Cell cell1 = header.createCell(1);
		cell1.setCellValue("Date");
		sheet.setColumnWidth(1, 3000);

		Cell cell2 = header.createCell(2);
		cell2.setCellValue("Courier Name");
		sheet.setColumnWidth(2, 5500);

		Cell cell3 = header.createCell(3);
		cell3.setCellValue("Courier/RIG Post Number");
		sheet.setColumnWidth(3, 7500);

		Cell cell4 = header.createCell(4);
		cell4.setCellValue("From");
		sheet.setColumnWidth(4, 8500);

		Cell cell5 = header.createCell(5);
		cell5.setCellValue("To");
		sheet.setColumnWidth(5, 4500);

		Cell cell6 = header.createCell(6);
		cell6.setCellValue("Received Date");
		sheet.setColumnWidth(6, 4500);

		Cell cell7 = header.createCell(7);
		cell7.setCellValue("Particulars Details");
		sheet.setColumnWidth(7, 9500);

		Cell cell8 = header.createCell(8);
		cell8.setCellValue("Sign");

		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		XSSFFont hSSFFont = (XSSFFont) workbook.createFont();
		hSSFFont.setFontHeightInPoints((short) 11);
		hSSFFont.setFontName("Arial");
		hSSFFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(hSSFFont);
		cellStyle.setAlignment(CellStyle.ALIGN_FILL);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		hSSFFont.setColor(XSSFFont.BOLDWEIGHT_BOLD);
		cell.setCellStyle(cellStyle);
		cell1.setCellStyle(cellStyle);
		cell2.setCellStyle(cellStyle);
		cell3.setCellStyle(cellStyle);
		cell4.setCellStyle(cellStyle);
		cell5.setCellStyle(cellStyle);
		cell6.setCellStyle(cellStyle);
		cell7.setCellStyle(cellStyle);
		cell8.setCellStyle(cellStyle);

		int rowNo = 10;
		for (Tappals tappal : tappals) {
			XSSFRow row = (XSSFRow) sheet.createRow(rowNo);
			// row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
			if (tappal.getDate() != null) {
				date = CommnonUtil.convertDateToString(tappal.getDate());
			}
			if (tappal.getReceivedDate() != null) {
				receivedDate = CommnonUtil.convertDateToString(tappal.getReceivedDate());
			}
			row.createCell((short) 0).setCellValue(tappal.getSlNo().toString());
			row.createCell((short) 1).setCellValue(date);
			row.createCell((short) 2).setCellValue(tappal.getCourierName().toString());
			row.createCell((short) 3).setCellValue(tappal.getCourierRigNo().toString());
			row.createCell((short) 4).setCellValue(tappal.getFromName().toString());
			row.createCell((short) 5).setCellValue(tappal.getToRole().toString());
			row.createCell((short) 6).setCellValue(receivedDate);
			String particularsDetails = tappal.getParticularsDetails();
			if (particularsDetails != null) {
				row.createCell((short) 7).setCellValue(tappal.getParticularsDetails());
			}
			rowNo++;
		}
		String contextPath = System.getProperty("catalina.base") + "/Courier Data/XLS/";
		File dir = new File(contextPath);
		System.out.println("Path: " + contextPath);
		if (!dir.exists())
			dir.mkdirs();
		String csvFileName = contextPath + "Tappals.xlsx";
		File file = new File(csvFileName);
		// fileOut = new FileOutputStream("E:\\output.xlsx");
		fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();

		System.out.println("Response Data exported to the Excel sheet...!");
		return file;
	}

	@RequestMapping(value = "/account/eachMonth/hitCount", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getAccountsEachMonth() throws Exception {
		return (List<Map<String, Object>>) this.tappalsService.getAccountsEachMonth();
	}

	@RequestMapping(value = "/management/eachMonth/hitCount", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getManagementEachMonth() throws Exception {
		return (List<Map<String, Object>>) this.tappalsService.getManagementEachMonth();
	}

	@RequestMapping(value = "/others/eachMonth/hitCount", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getOthersEachMonth() throws Exception {
		return (List<Map<String, Object>>) this.tappalsService.getOthersEachMonth();
	}
	
	@RequestMapping(value = "/account/eachMonth/tappals", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getAccountsEachMonthTappals(@RequestParam(value = "month", required = false) int month,
			@RequestParam(value = "year", required = false) int year) throws Exception {
		return (List<Tappals>) this.tappalsService.getAccountsEachMonthTappals(month, year);
	}
	
	@RequestMapping(value = "/management/eachMonth/tappals", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getManagementEachMonthTappals(@RequestParam(value = "month", required = false) int month,
			@RequestParam(value = "year", required = false) int year) throws Exception {
		return (List<Tappals>) this.tappalsService.getManagementEachMonthTappals(month, year);
	}
	
	@RequestMapping(value = "/others/eachMonth/tappals", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getOthersEachMonthTappals(@RequestParam(value = "month", required = false) int month,
			@RequestParam(value = "year", required = false) int year) throws Exception {
		return (List<Tappals>) this.tappalsService.getOthersEachMonthTappals(month, year);
	}
}
