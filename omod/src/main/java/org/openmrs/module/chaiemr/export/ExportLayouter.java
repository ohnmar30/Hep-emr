/**
 *  Copyright 2012 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Laboratory module.
 *
 *  Laboratory module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Laboratory module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Laboratory module.  If not, see <http://www.gnu.org/licenses/>.
 *
 * author Ghanshyam
 * 24-sept-2012
 * New Requirement #361 [Laboratory] Export to Excel option in print worklist
 **/

package org.openmrs.module.chaiemr.export;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class ExportLayouter {

	/**
	 * Builds the report layout.
	 * 
	 * This doesn't have any data yet. This is template.
	 */
	public static void buildReport(HSSFSheet worksheet, int startRowIndex,
			int startColIndex) {

		// Set column widths
		worksheet.setColumnWidth(0, 5000);
		worksheet.setColumnWidth(1, 6000);
		worksheet.setColumnWidth(2, 5000);
		worksheet.setColumnWidth(3, 3000);
		worksheet.setColumnWidth(4, 3000);
		worksheet.setColumnWidth(5, 5000);
		worksheet.setColumnWidth(6, 5000);
		worksheet.setColumnWidth(7, 5000);
		worksheet.setColumnWidth(8, 5000);
		worksheet.setColumnWidth(9, 5000);

		// Build the title and date headers
		buildTitle(worksheet, startRowIndex, startColIndex);

		// Build the column headers
		buildHeaders(worksheet, startRowIndex, startColIndex);
	}

	/**
	 * Builds the report title and the date header
	 * 
	 * @param worksheet
	 * @param startRowIndex
	 *            starting row offset
	 * @param startColIndex
	 *            starting column offset
	 */
	public static void buildTitle(HSSFSheet worksheet, int startRowIndex,
			int startColIndex) {

		// Create font style for the report title
		Font fontTitle = worksheet.getWorkbook().createFont();
		fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTitle.setFontHeight((short) 280);

		// Create cell style for the report title
		HSSFCellStyle cellStyleTitle = worksheet.getWorkbook()
				.createCellStyle();
		cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTitle.setWrapText(true);
		cellStyleTitle.setFont(fontTitle);

		// Create report title
		HSSFRow rowTitle = worksheet.createRow((short) startRowIndex);
		rowTitle.setHeight((short) 500);
		HSSFCell cellTitle = rowTitle.createCell(startColIndex);
		cellTitle.setCellValue("Patient Lab Result Report");
		cellTitle.setCellStyle(cellStyleTitle);

		// Create merged region for the report title
		worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

		// Create date header
		HSSFRow dateTitle = worksheet.createRow((short) startRowIndex + 1);
		HSSFCell cellDate = dateTitle.createCell(startColIndex);
		//ghanshyam 27-sept-2012 Support #393 [Laboratory]Export to Excel option in print worklist (note: changed day,date and time format inside excel report sheet)
		String months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		String days[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		GregorianCalendar gcalendar = new GregorianCalendar();
		String dayName = days[gcalendar.get(Calendar.DAY_OF_WEEK) - 1];
		String date = (gcalendar.get(Calendar.DATE) + "-"
				+ months[gcalendar.get(Calendar.MONTH)] + "-" + gcalendar
				.get(Calendar.YEAR)).toString();
		Date d = new Date();
		cellDate.setCellValue("This report was generated on " + dayName + " " + date
				+ " " + "at" + " " + d.getHours() + ":" + d.getMinutes() + ":"
				+ d.getSeconds());
	}

	/**
	 * Builds the column headers
	 * 
	 * @param worksheet
	 * @param startRowIndex
	 *            starting row offset
	 * @param startColIndex
	 *            starting column offset
	 */
	public static void buildHeaders(HSSFSheet worksheet, int startRowIndex,
			int startColIndex) {

		// Create font style for the headers
		Font font = worksheet.getWorkbook().createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);

		// Create cell style for the headers
		HSSFCellStyle headerCellStyle = worksheet.getWorkbook()
				.createCellStyle();
		headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		headerCellStyle.setFillPattern(CellStyle.FINE_DOTS);
		headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerCellStyle.setWrapText(true);
		headerCellStyle.setFont(font);
		headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);

		// Create the column headers
		HSSFRow rowHeader = worksheet.createRow((short) startRowIndex + 2);
		rowHeader.setHeight((short) 500);

		HSSFCell cell1 = rowHeader.createCell(startColIndex + 0);
		cell1.setCellValue("Order Date");
		cell1.setCellStyle(headerCellStyle);

		HSSFCell cell2 = rowHeader.createCell(startColIndex + 1);
		cell2.setCellValue("Patient Identifier");
		cell2.setCellStyle(headerCellStyle);

		HSSFCell cell3 = rowHeader.createCell(startColIndex + 2);
		cell3.setCellValue("Name");
		cell3.setCellStyle(headerCellStyle);

		HSSFCell cell4 = rowHeader.createCell(startColIndex + 3);
		cell4.setCellValue("Age");
		cell4.setCellStyle(headerCellStyle);

		HSSFCell cell5 = rowHeader.createCell(startColIndex + 4);
		cell5.setCellValue("Gender");
		cell5.setCellStyle(headerCellStyle);

		HSSFCell cell6 = rowHeader.createCell(startColIndex + 5);
		cell6.setCellValue("Sample No.");
		cell6.setCellStyle(headerCellStyle);

		HSSFCell cell7 = rowHeader.createCell(startColIndex + 6);
		cell7.setCellValue("Lab");
		cell7.setCellStyle(headerCellStyle);

		HSSFCell cell8 = rowHeader.createCell(startColIndex + 7);
		cell8.setCellValue("Test");
		cell8.setCellStyle(headerCellStyle);

		HSSFCell cell9 = rowHeader.createCell(startColIndex + 8);
		cell9.setCellValue("Test name");
		cell9.setCellStyle(headerCellStyle);

		HSSFCell cell10 = rowHeader.createCell(startColIndex + 9);
		cell10.setCellValue("Result");
		cell10.setCellStyle(headerCellStyle);
	}
}
