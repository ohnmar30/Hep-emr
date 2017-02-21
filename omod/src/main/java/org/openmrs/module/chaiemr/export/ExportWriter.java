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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ExportWriter {
	/**
	 * Writes the report to the output stream
	 */
	public static void write(HttpServletResponse response, HSSFSheet worksheet) {

		try {
			// Retrieve the output stream
			ServletOutputStream outputStream = response.getOutputStream();
			// Write to the output stream
			worksheet.getWorkbook().write(outputStream);
			// Flush the stream
			outputStream.flush();

		} catch (Exception e) {
		}
	}
}
