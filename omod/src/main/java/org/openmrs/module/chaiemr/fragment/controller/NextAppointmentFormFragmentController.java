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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.chaicore.form.FormDescriptor;
import org.openmrs.module.chaicore.form.FormManager;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.wrapper.EncounterWrapper;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

/**
 * Fragment to display available forms for a given visit
 */
public class NextAppointmentFormFragmentController {
	
	protected static final Log log = LogFactory.getLog(NextAppointmentFormFragmentController.class);

	public void controller(FragmentModel model,
						   @FragmentParam("patient") Patient patient,
						   UiUtils ui,
						   PageRequest request,
						   @SpringBean FormManager formManager,
						   @SpringBean ChaiUiUtils chaiUi) {

		AppDescriptor currentApp = chaiUi.getCurrentApp(request);
		model.addAttribute("patient", patient);
		
		
		Visit activeVisit = null;
		List<Visit> activeVisitList = Context.getVisitService()
				.getActiveVisitsByPatient(patient);
		for (Visit v : activeVisitList) {
			activeVisit = v;
		}
		
		Obs o=getLatestObs(patient);
		if(o!=null){
			if(o.getComment()!=null)
			{
				
				model.addAttribute("appoint", o.getComment());
			}
			else
			{
				model.addAttribute("appoint","");
			}
		}
		else
		{
			model.addAttribute("appoint","");
		}
		Obs obsDate = getAllLatestObs( Dictionary.RETURN_VISIT_DATE, activeVisit);
		if (obsDate != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					obsDate.getEncounter());
			List<Obs> obsList = wrapped.allObs(obsDate
					.getConcept());
			for (Obs obs : obsList) {
				obsDate = obs;
			}
		}
		Obs obsafterDate = getAllLatestObs( Dictionary.AFTERDATE, activeVisit);
		if (obsafterDate != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					obsafterDate.getEncounter());
			List<Obs> obsafterList = wrapped.allObs(obsafterDate
					.getConcept());
			for (Obs obss : obsafterList) {
				obsafterDate = obss;
			}
		}
		
		if(obsDate!=null){
			
			model.addAttribute("appointmentDate", obsDate.getValueDate());
		  
			
		}
		else{
			model.addAttribute("appointmentDate","");
			
		}
		
		if(obsafterDate!=null)
		{  
			model.addAttribute("appointmentafterDate", obsafterDate.getValueDate());
			
			model.addAttribute("appointmentafterDay", obsafterDate.getValueCoded().getConceptId());
		}
		else
		{  model.addAttribute("appointmentafterDate","");
		    model.addAttribute("appointmentafterDay","");
			
		}
		
		
		Concept afterDate=Context.getConceptService().getConceptByUuid("1879AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		model.addAttribute("afterDate",afterDate.getAnswers());
		
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
	private Obs getLatestObs(Patient patient) {
		
		List<Obs> obs = Context.getObsService()
				.getObservationsByPerson(patient);
		if (obs.size() > 0) {
			// these are in reverse chronological order
			return obs.get(0);
		}
		return null;
	}
}