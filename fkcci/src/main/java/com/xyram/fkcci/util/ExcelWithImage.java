package com.xyram.fkcci.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xyram.fkcci.daoImpl.TappalsDaoImpl;

public class ExcelWithImage {
	/*public static void main(String[] args) {
		excel();

	}*/

	public static void excel() {
		try {
			TappalsDaoImpl tappals = new TappalsDaoImpl();
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("MYSheet");
			String s = workbook.getSheetName(0);
			System.out.println("S: " + s);
			InputStream inputStream = new FileInputStream("F:/bhojana/bhojana/src/main/resources/images/fkcci.png");
			byte[] imageBytes = IOUtils.toByteArray(inputStream);
			int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
			inputStream.close();

			CreationHelper helper = workbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
			anchor.setCol1(3); // first anchor determines upper left position
			anchor.setRow1(0);
			anchor.setRow2(6); // second anchor determines bottom right position
			anchor.setCol2(4);
			anchor.setDx2(Units.toEMU(12)); // dx = left + wanted width
			anchor.setDy2(Units.toEMU(12)); // dy= top + wanted height

			drawing.createPicture(anchor, pictureureIdx);
			Row header = sheet.createRow(7);

			Cell cell = header.createCell(0);
			cell.setCellValue("Sl No");

			Cell cell1 = header.createCell(1);
			cell1.setCellValue("Date");

			Cell cell2 = header.createCell(2);
			cell2.setCellValue("Courier Name");
			int columnIndex2 = cell2.getColumnIndex();
			sheet.autoSizeColumn(columnIndex2);

			Cell cell3 = header.createCell(3);
			cell3.setCellValue("Courier/RIG Post Number");
			int columnIndex = cell3.getColumnIndex();
			sheet.autoSizeColumn(columnIndex);

			Cell cell4 = header.createCell(4);
			cell4.setCellValue("From");

			Cell cell5 = header.createCell(5);
			cell5.setCellValue("To");

			Cell cell6 = header.createCell(6);
			cell6.setCellValue("Received Date");
			int columnIndex6 = cell6.getColumnIndex();
			sheet.autoSizeColumn(columnIndex6);

			Cell cell7 = header.createCell(7);
			cell7.setCellValue("Prticulars Details");
			int columnIndex7 = cell7.getColumnIndex();
			sheet.autoSizeColumn(columnIndex7);

			Cell cell8 = header.createCell(8);
			cell8.setCellValue("Sign");

			XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
			XSSFFont hSSFFont = (XSSFFont) workbook.createFont();
			hSSFFont.setFontHeightInPoints((short) 10);
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

			FileOutputStream fileOut = null;
			fileOut = new FileOutputStream("output.xlsx");
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
