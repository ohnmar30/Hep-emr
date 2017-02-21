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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.wrapper.EncounterWrapper;
import org.openmrs.module.chaiemr.wrapper.VisitWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.util.PrivilegeConstants;

/**
 * Visit summary fragment
 */
public class VisitSummaryFragmentController {
	
	public void controller(@FragmentParam("visit") Visit visit, FragmentModel model) {

		model.addAttribute("visit", visit);
		model.addAttribute("sourceForm", new VisitWrapper(visit).getSourceForm());
		model.addAttribute("allowVoid", Context.hasPrivilege(PrivilegeConstants.DELETE_VISITS));
		
		Obs obsDate = getAllLatestObs( Dictionary.RETURN_VISIT_DATE, visit);
		if (obsDate != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					obsDate.getEncounter());
			List<Obs> obsList = wrapped.allObs(obsDate
					.getConcept());
			for (Obs obs : obsList) {
				obsDate = obs;
			}
		}
		
		if(obsDate!=null){
			model.addAttribute("appointmentDate", new SimpleDateFormat(
					"dd-MMMM-yyyy").format(obsDate.getValueDate()));
		}
		else{
			model.addAttribute("appointmentDate", null);
		}

	}

	private Obs getAllLatestObs( String conceptIdentifier, Visit visit) {
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		List<Obs> obs = Context.getObsService()
				.getObservationsByPersonAndConcept(visit.getPatient(), concept);
		List<Obs> currentVisitObs = new ArrayList<Obs>();
		for(Obs o : obs){
			if(o.getEncounter().getVisit().equals(visit)){
				currentVisitObs.add(o);
			}
		}
	
		int count = currentVisitObs.size() - 1;
		if (currentVisitObs.size() > 0) {
			// these are in reverse chronological order
			return currentVisitObs.get(count);
		}
		return null;
	}
}