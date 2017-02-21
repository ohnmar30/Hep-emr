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

package org.openmrs.module.chaiemr.fragment.controller.header;

import java.util.List;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.ui.framework.WebConstants;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

/**
 * Banner showing which patient this page is in the context of
 */
public class PatientHeaderFragmentController {
	private Obs savedIngoTypeConcept;
	
	public void controller(@FragmentParam("patient") Patient patient,
						   FragmentModel model,
						   PageRequest pageRequest,
						   @SpringBean ChaiUiUtils chaiUi) {
		
	
		model.addAttribute("patient", patient);
		
		model.addAttribute("patientName", patient.getGivenName());
		AppDescriptor currentApp = chaiUi.getCurrentApp(pageRequest);
		
		if(pageRequest.getPageName().equals("dispensary/drugOrder")){
				model.addAttribute("appHomepageUrl", "/" + WebConstants.CONTEXT_PATH + "/" + "chaiemr/dispensary/dispensing.page?");
			}
		else{
			if (currentApp != null) {
				model.addAttribute("appHomepageUrl", "/" + WebConstants.CONTEXT_PATH + "/" + currentApp.getUrl());
			} else {
				model.addAttribute("appHomepageUrl", null);
			}
		}
		savedIngoTypeConcept = getLatestObs(patient, Dictionary.INGO_NAME);
		if (savedIngoTypeConcept != null) {
			model.addAttribute("ingoName",savedIngoTypeConcept.getValueCoded().getName());
		}	
		else{
			model.addAttribute("ingoName","");
		}
		
	}

	
	private Obs getLatestObs(Patient patient, String conceptIdentifier) {
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		List<Obs> obs = Context.getObsService().getObservationsByPersonAndConcept(patient, concept);
		if (obs.size() > 0) {
			// these are in reverse chronological order
			return obs.get(0);
		}
		return null;
	}
}