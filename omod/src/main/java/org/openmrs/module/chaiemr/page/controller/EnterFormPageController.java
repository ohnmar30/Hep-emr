/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.chaiemr.page.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiui.annotation.SharedPage;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Page for entering a new form
 */
@SharedPage
public class EnterFormPageController {

	public void controller(@RequestParam(value = "formUuid", required = false) String formUuid,
						   @RequestParam(value = "patientId", required = false) Patient patient,
	                       @RequestParam("returnUrl") String returnUrl,
	                       PageModel model) {

		model.addAttribute("formUuid", formUuid);
		model.addAttribute("returnUrl", returnUrl);
		List<Visit> vList = Context.getVisitService().getActiveVisitsByPatient(patient);
		Visit activeVisit = null;
		for(Visit v : vList ){
			activeVisit = Context.getVisitService().getVisit(v.getVisitId());
		}
		
		
		Date curDate = new Date();
		SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
				"dd-MMM-yy HH:mm:ss");
		Date date = new Date();
		if(activeVisit!=null){
			String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(activeVisit.getStartDatetime());
			try {
				date = mysqlDateTimeFormatter.parse(modifiedDate
						+ " " + curDate.getHours() + ":" + curDate.getMinutes()
						+ ":" + curDate.getSeconds());
			} catch (ParseException e) {
				date = curDate;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		model.addAttribute("activeVisit", activeVisit);
		model.addAttribute("activeVisitDate", date);
		
	}
}