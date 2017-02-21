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

package org.openmrs.module.chaiemr.fragment.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * Visit menu (check-in / check-out etc)
 */
public class VisitMenuFragmentController {
	
	public void controller(UiUtils ui,FragmentModel model, @FragmentParam("patient") Patient patient, @FragmentParam(value = "visit", required = false) Visit visit) {

		model.addAttribute("patient", patient);
		model.addAttribute("visit", visit);	
		
		
		Visit newVisit = new Visit();
		newVisit.setPatient(patient);
		newVisit.setStartDatetime(new Date());
		newVisit.setVisitType(MetadataUtils.existing(VisitType.class, CommonMetadata._VisitType.OUTPATIENT));
		
		String currenturl=ui.thisUrl();
		String typeOfUser="";
		if(currenturl.toLowerCase().contains("registration")){
			typeOfUser="registration";
		}
		else if(currenturl.toLowerCase().contains("clinician")){
			typeOfUser="clinician";
		}
		
		model.addAttribute("newCurrentVisit", newVisit);
		model.addAttribute("typeOfUser", typeOfUser);
		
		Date curDate = new Date();
		SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
				"dd-MMM-yy HH:mm:ss");
		Date date = new Date();
		if(visit!=null){
			String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(visit.getStartDatetime());
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
		
		model.addAttribute("activeVisitDate", date);
	}
}