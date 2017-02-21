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

package org.openmrs.module.chaiemr.page.controller.clinician;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.EmrWebConstants;
import org.openmrs.module.chaiui.annotation.AppPage;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * View patient page for clinician app
 */
@AppPage(EmrConstants.APP_CLINICIAN)
public class ClinicianViewPatientPageController {
	
	protected final Log log = LogFactory.getLog(ClinicianViewPatientPageController.class);
	
	public void controller(@RequestParam("patientId") Patient patient,
			@RequestParam(value="newVisit",required=false) String newVisit) {
		
		
		if (patient != null && StringUtils.isNotBlank(newVisit) && "true".equalsIgnoreCase(newVisit)) {
			List<Visit> visits = Context.getVisitService().getActiveVisitsByPatient(patient);
			for(Visit v : visits) {
				if(v.getVisitType().getName().equalsIgnoreCase(EmrWebConstants.VISIT_TYPE_NEW_PATIENT)){
					v.setStopDatetime(Calendar.getInstance().getTime());
					Context.getVisitService().saveVisit(v);
					break;
				}
			}
		}
	}
}