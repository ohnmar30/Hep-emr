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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.chaicore.form.FormDescriptor;
import org.openmrs.module.chaicore.form.FormManager;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.model.DrugInfo;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Fragment to display available forms for a given visit
 */
public class NextAppointmentCountFragmentController {

	protected static final Log log = LogFactory
			.getLog(NextAppointmentCountFragmentController.class);

	public JSONObject getTotalPatient(@RequestParam("patient") Patient patient,
			@RequestParam("date") String date,UiUtils ui) {

		SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
				"dd-MMM-yy");
		Date dateEntered = null;
		try {
			dateEntered = mysqlDateTimeFormatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Obs> obsList = Context.getObsService()
				.getObservationsByPersonAndConcept(
						null,
						Context.getConceptService().getConceptByUuid(
								Dictionary.RETURN_VISIT_DATE));
		int totalPatient = 0;
		for (Obs o : obsList) {
			if (o.getValueDate().compareTo(dateEntered) == 0) {
				totalPatient++;
			}
		}

		JSONObject drugsInfoDetailsJson = new JSONObject();
		drugsInfoDetailsJson.put("count", totalPatient);
		return drugsInfoDetailsJson;
	}

	public JSONObject saveAppointment(@RequestParam("patient") Patient patient,
			@RequestParam("date") String date, UiUtils ui) {

		Visit activeVisit = null;
		Encounter enounterAvailable = null;

		SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
				"dd-MMM-yy");
		Date dateEntered = null;
		try {
			dateEntered = mysqlDateTimeFormatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Visit> activeVisitList = Context.getVisitService()
				.getActiveVisitsByPatient(patient);
		for (Visit v : activeVisitList) {
			activeVisit = v;
		}
		
		List<Obs> obsAllList = Context.getObsService().getObservationsByPersonAndConcept(patient, Context.getConceptService().getConceptByUuid(
									Dictionary.RETURN_VISIT_DATE ));
		
		List<Obs> currentVisitObs = new ArrayList<Obs>();
		for(Obs o : obsAllList){
			if(o.getEncounter().getVisit().equals(activeVisit)){
				currentVisitObs.add(o);
			}
		}
		
		for(Obs o : currentVisitObs){
			enounterAvailable=o.getEncounter();
		}
		
		if (enounterAvailable != null) {
			Date curDate = new Date();
			SimpleDateFormat mysqlDateTimeFormatter1 = new SimpleDateFormat(
					"dd-MMM-yy HH:mm:ss");
			Date obsDateTime = null;

				String modifiedDate = new SimpleDateFormat("dd-MMM-yyyy")
						.format(enounterAvailable.getEncounterDatetime());
				try {
					obsDateTime = mysqlDateTimeFormatter1
							.parse(modifiedDate + " " + curDate.getHours()
									+ ":" + curDate.getMinutes() + ":"
									+ curDate.getSeconds());
				} catch (ParseException e) {
					obsDateTime = curDate;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			List<Obs> obsList = Context.getObsService()
					.getObservationsByPersonAndConcept(
							patient,
							Context.getConceptService().getConceptByUuid(
									Dictionary.RETURN_VISIT_DATE));
			
			
			for(Obs o : obsList){
				if(o.getEncounter().equals(enounterAvailable)){
					o.setVoided(true);
					o.setVoidReason("updated");
					o.setDateVoided(new Date());
					o.setVoidedBy(Context.getAuthenticatedUser());
				}
			}

			Obs obs = new Obs();
			obs.setEncounter(enounterAvailable);
			obs.setObsDatetime(obsDateTime);
			obs.setPerson(patient);
			obs.setLocation(Context.getService(ChaiEmrService.class)
					.getDefaultLocation());
			obs.setDateCreated(new Date());
			obs.setCreator(Context.getAuthenticatedUser());
			obs.setValueDatetime(dateEntered);
			obs.setConcept(Context.getConceptService().getConceptByUuid(
					Dictionary.RETURN_VISIT_DATE));
			obs.setComment("next apointment");
			Context.getObsService().saveObs(obs, null);
			
		} else {
			Date curDate = new Date();
			SimpleDateFormat mysqlDateTimeFormatter1 = new SimpleDateFormat(
					"dd-MMM-yy HH:mm:ss");
			Date encounterDateTime = null;
			if (activeVisit != null) {
				String modifiedDate = new SimpleDateFormat("dd-MMM-yyyy")
						.format(activeVisit.getStartDatetime());
				try {
					encounterDateTime = mysqlDateTimeFormatter1
							.parse(modifiedDate + " " + curDate.getHours()
									+ ":" + curDate.getMinutes() + ":"
									+ curDate.getSeconds());
				} catch (ParseException e) {
					encounterDateTime = curDate;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Encounter newEncounter = new Encounter();
			newEncounter.setEncounterType(Context.getEncounterService()
					.getEncounterTypeByUuid(
							"465a92f2-baf8-42e9-9612-53064be868e8"));
			newEncounter.setPatient(patient);
			newEncounter.setLocation(Context.getService(ChaiEmrService.class)
					.getDefaultLocation());
			newEncounter.setDateCreated(new Date());
			newEncounter.setEncounterDatetime(encounterDateTime);
			newEncounter.setVisit(activeVisit);
			newEncounter.setCreator(Context.getAuthenticatedUser());
			newEncounter.setVoided(false);
			enounterAvailable = Context.getEncounterService().saveEncounter(
					newEncounter);
			
			List<Obs> obsList = Context.getObsService()
					.getObservationsByPersonAndConcept(
							patient,
							Context.getConceptService().getConceptByUuid(
									Dictionary.RETURN_VISIT_DATE));
			for(Obs o : obsList){
				if(o.getEncounter().equals(enounterAvailable)){
					o.setVoided(true);
					o.setVoidReason("updated");
					o.setDateVoided(new Date());
					o.setVoidedBy(Context.getAuthenticatedUser());
				}
			}

			Obs obs = new Obs();
			obs.setEncounter(enounterAvailable);
			obs.setObsDatetime(encounterDateTime);
			obs.setPerson(patient);
			obs.setDateCreated(new Date());
			obs.setLocation(Context.getService(ChaiEmrService.class)
					.getDefaultLocation());
			obs.setCreator(Context.getAuthenticatedUser());
			obs.setValueDatetime(dateEntered);
			obs.setConcept(Context.getConceptService().getConceptByUuid(
					Dictionary.RETURN_VISIT_DATE));
			obs.setComment("next apointment");
			Context.getObsService().saveObs(obs, null);
		}

		int totalPatient = 0;
		JSONObject drugsInfoDetailsJson = new JSONObject();
		drugsInfoDetailsJson.put("count", totalPatient);
		return drugsInfoDetailsJson;
	}

}