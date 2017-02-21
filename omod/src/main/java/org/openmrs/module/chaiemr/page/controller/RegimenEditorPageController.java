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

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.chaiemr.regimen.RegimenManager;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.regimen.RegimenChange;
import org.openmrs.module.chaiemr.regimen.RegimenChangeHistory;
import org.openmrs.module.chaiemr.EmrWebConstants;
import org.openmrs.module.chaiui.annotation.SharedPage;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller for regimen editor page
 */
@SharedPage({ EmrConstants.APP_CLINICIAN, EmrConstants.APP_CHART })
public class RegimenEditorPageController {

	public void controller(@RequestParam("category") String category, @RequestParam("returnUrl") String returnUrl, PageModel model, UiUtils ui, @SpringBean RegimenManager regimenManager) {

		Patient patient = (Patient) model.getAttribute(EmrWebConstants.MODEL_ATTR_CURRENT_PATIENT);

		List<Visit> vList = Context.getVisitService().getActiveVisitsByPatient(patient);
		Visit activeVisit = null;
		for (Visit v : vList) {
			activeVisit = Context.getVisitService().getVisit(v.getVisitId());
		}

		Date curDate = new Date();
		SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
		Date date = new Date();
		if (activeVisit != null) {
			String modifiedDate = new SimpleDateFormat("dd-MMM-yyyy").format(activeVisit.getStartDatetime());
			try {
				date = mysqlDateTimeFormatter.parse(modifiedDate + " " + curDate.getHours() + ":" + curDate.getMinutes() + ":" + curDate.getSeconds());
			} catch (ParseException e) {
				date = curDate;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		model.addAttribute("category", category);
		model.addAttribute("returnUrl", returnUrl);

		Concept masterSet = regimenManager.getMasterSetConcept(category);
		RegimenChangeHistory history = RegimenChangeHistory.forPatient(patient, masterSet);
		model.addAttribute("history", history);

		RegimenChange lastChange = history.getLastChange();
		Date lastChangeDate = (lastChange != null) ? lastChange.getDate() : null;

		Date now = new Date();
		boolean futureChanges = OpenmrsUtil.compareWithNullAsEarliest(lastChangeDate, now) >= 0;

		model.addAttribute("initialDate", futureChanges ? lastChangeDate : date);
		String back = "ui.navigate('chaiemr', 'clinician/clinicianViewPatient', { patientId: " + patient.getId() + "});";
		model.addAttribute("back", back);
	}
}