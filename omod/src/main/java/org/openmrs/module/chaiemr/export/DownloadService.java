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

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DownloadService {
	public List<ReportModel> getDatasource(ExportAttributeDetailsApi adts,
			HttpServletRequest request) throws ParseException {
        List<ReportModel> reports=new LinkedList<ReportModel>();
        ReportModel reportModel=new ReportModel();
       
		return reports;

	}

	public void downloadXLS(ExportAttributeDetailsApi adts,
			HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, ParseException {

		// 1. Create new workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 2. Create new worksheet
		HSSFSheet worksheet = workbook.createSheet("Custom Report");

		// 3. Define starting indices for rows and columns
		int startRowIndex = 0;
		int startColIndex = 0;

		// 4. Build layout
		// Build title, date, and column headers
		ExportLayouter.buildReport(worksheet, startRowIndex, startColIndex);

		// 5. Fill report
		ExportFillManager.fillReport(worksheet, startRowIndex, startColIndex,
				getDatasource(adts, request));

		// 6. Set the response properties
		String months[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		
		/*
		String dateStr = adts.getDateStr();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date=new Date();
		date = sdf.parse(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String day=Integer.toString(date.getDate());
		String month=months[date.getMonth()];
		String year=Integer.toString(calendar.get(Calendar.YEAR));
		String dayMonthYear=day+"-"+month+"-"+year;
		String fileName = "CustomReport" + dayMonthYear + ".xls";
		*/
		String fileName = "CustomReport" + ".xls";
		response.setHeader("Content-Disposition", "inline; filename="
				+ fileName);
		// Make sure to set the correct content type
		response.setContentType("application/vnd.ms-excel");

		// 7. Write to the output stream
		ExportWriter.write(response, worksheet);
	}
}
