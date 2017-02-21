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

package org.openmrs.module.chaiemr.fragment.controller.program.hiv;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PatientProgram;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.util.EmrUtils;
import org.openmrs.module.chaiemr.wrapper.EncounterWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HIV program discontinuation summary fragment
 */
public class HivCompletionSummaryFragmentController {
	
	public String controller(@FragmentParam("patientProgram") PatientProgram enrollment,
						   @FragmentParam(value = "encounter", required = false) Encounter encounter,
						   @FragmentParam("showClinicalData") boolean showClinicalData,
						   FragmentModel model) {

		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();

		dataPoints.put("End of Follow up Date", enrollment.getDateCompleted());

		if (showClinicalData && enrollment.getOutcome() != null) {
			dataPoints.put("Outcome", enrollment.getOutcome());
		}
		
		if (encounter != null) {
			EncounterWrapper wrapper = new EncounterWrapper(encounter);

			Obs reasonObs = wrapper.firstObs(Dictionary.getConcept(Dictionary.DATE_LAST_VISIT));
			if (reasonObs != null) {
				dataPoints.put("Last Date of Visit", reasonObs.getValueDatetime());
			}
		}

		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
	}
}