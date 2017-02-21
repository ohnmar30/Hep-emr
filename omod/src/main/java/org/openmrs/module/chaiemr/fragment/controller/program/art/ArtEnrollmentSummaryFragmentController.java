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

package org.openmrs.module.chaiemr.fragment.controller.program.art;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import org.openmrs.Encounter;
import org.openmrs.PatientProgram;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * HIV program enrollment summary fragment
 */
public class ArtEnrollmentSummaryFragmentController {
	public String controller(@FragmentParam("patientProgram") PatientProgram patientProgram,
						   @FragmentParam(value = "encounter", required = false) Encounter encounter,
						   @FragmentParam("showClinicalData") boolean showClinicalData,
						   FragmentModel model) {
		
		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();
		
		List<Encounter> en= Context.getEncounterService().getEncountersByPatient(patientProgram.getPatient());
		Date initialDate = new Date();
		Date endDate = new Date();
		for(Encounter e : en){
			if(e.getEncounterType().getUuid().toString().equals("0cb4417d-b98d-4265-92aa-c6ee3d3bb317")){
				if(initialDate.after(e.getEncounterDatetime())){
					initialDate = e.getEncounterDatetime();
				}
			}
		}
		for(Encounter e : en){
			if(e.getEncounterType().getName().toString().equals("Stop ART")){
				if(endDate.after(e.getEncounterDatetime())){
					endDate = e.getEncounterDatetime();
				}
			}
		}
		if(patientProgram.getDateEnrolled().getTime() >= initialDate.getTime() &&  patientProgram.getDateEnrolled().getTime() <= endDate.getTime()){
			dataPoints.put("ART initiation date", patientProgram.getDateEnrolled());
		}
		else{
			dataPoints.put("ART restart date", patientProgram.getDateEnrolled());
		}

		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
	}
}