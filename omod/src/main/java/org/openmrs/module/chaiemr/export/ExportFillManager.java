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

import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExportFillManager {
	/**
	 * Fills the report with content
	 * 
	 * @param worksheet
	 * @param startRowIndex
	 *            starting row offset
	 * @param startColIndex
	 *            starting column offset
	 * @param datasource
	 *            the data source
	 */
	public static void fillReport(HSSFSheet worksheet, int startRowIndex,
			int startColIndex, List<ReportModel> datasource) {
		// Row offset
		startRowIndex += 2;

		// Create cell style for the body
		HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
		bodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		bodyCellStyle.setWrapText(true);

		// Create body
		for (int i = startRowIndex; i + startRowIndex - 2 < datasource.size() + 2; i++) {

			// Create a new row
			HSSFRow row = worksheet.createRow((short) i + 1);

			// Retrieve the Accepted Date
			HSSFCell cell1 = row.createCell(startColIndex + 0);
			cell1.setCellValue("");
			cell1.setCellStyle(bodyCellStyle);

			// Retrieve the Patient Identifier
			HSSFCell cell2 = row.createCell(startColIndex + 1);
			cell2.setCellValue("");
			cell2.setCellStyle(bodyCellStyle);

			// Retrieve the Name of Patient
			HSSFCell cell3 = row.createCell(startColIndex + 2);
			cell3.setCellValue("");
			cell3.setCellStyle(bodyCellStyle);

			// Retrieve the Age of Patient
			HSSFCell cell4 = row.createCell(startColIndex + 3);
			cell4.setCellValue("");
			cell4.setCellStyle(bodyCellStyle);

			// Retrieve the Gender of Patient
			HSSFCell cell5 = row.createCell(startColIndex + 4);
			cell5.setCellValue("");
			cell5.setCellStyle(bodyCellStyle);

			// Retrieve the Sample Id
			HSSFCell cell6 = row.createCell(startColIndex + 5);
			cell6.setCellValue("");
			cell6.setCellStyle(bodyCellStyle);

			// Retrieve the Name of Investigation
			HSSFCell cell7 = row.createCell(startColIndex + 6);
			cell7.setCellValue("");
			cell7.setCellStyle(bodyCellStyle);

			// Retrieve the Name of Test
			HSSFCell cell8 = row.createCell(startColIndex + 7);
			cell8.setCellValue("");
			cell8.setCellStyle(bodyCellStyle);

			// Retrieve the Name of Test name
			HSSFCell cell9 = row.createCell(startColIndex + 8);
			cell9.setCellValue("");
			cell9.setCellStyle(bodyCellStyle);

			// Retrieve the Test Result
			HSSFCell cell10 = row.createCell(startColIndex + 9);
			cell10.setCellValue("");
			cell10.setCellStyle(bodyCellStyle);
		}
	}
}