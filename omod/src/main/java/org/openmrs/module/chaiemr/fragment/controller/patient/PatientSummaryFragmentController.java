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

package org.openmrs.module.chaiemr.fragment.controller.patient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jxl.common.Logger;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.ResultUtil;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaicore.form.FormDescriptor;
import org.openmrs.module.chaicore.form.FormManager;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.calculation.library.RecordedDeceasedCalculation;
import org.openmrs.module.chaiemr.wrapper.EncounterWrapper;
import org.openmrs.module.chaiemr.wrapper.PatientWrapper;
import org.openmrs.module.chaiemr.wrapper.PersonWrapper;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

/**
 * Patient summary fragment
 */
public class PatientSummaryFragmentController {

	private static Logger logger = Logger.getLogger(PatientSummaryFragmentController.class);

	public void controller(@FragmentParam("patient") Patient patient, @FragmentParam("patient") Person person, @SpringBean FormManager formManager, @SpringBean ChaiUiUtils chaiUi, PageRequest pageRequest, UiUtils ui, FragmentModel model) {

		AppDescriptor currentApp = chaiUi.getCurrentApp(pageRequest);

		// Get all common per-patient forms as simple objects
		List<SimpleObject> forms = new ArrayList<SimpleObject>();
		for (FormDescriptor formDescriptor : formManager.getCommonFormsForPatient(currentApp, patient)) {
			forms.add(ui.simplifyObject(formDescriptor.getTarget()));
		}

		model.addAttribute("patient", patient);
		model.addAttribute("recordedAsDeceased", hasBeenRecordedAsDeceased(patient));
		model.addAttribute("forms", forms);

		// Patient address
		model.addAttribute("patientAdd", person.getPersonAddress());

		/*
		 * Get Entry point
		 */
		if (getLatestObs(patient, Dictionary.METHOD_OF_ENROLLMENT) != null) {
			Obs savedEntryPoint = getLatestObs(patient, Dictionary.METHOD_OF_ENROLLMENT);
			String entryPoint = savedEntryPoint.getValueCoded().getName().toString();
			String otherEntryPoint = savedEntryPoint.getValueText();

			model.addAttribute("savedEntryPoint", savedEntryPoint);

			model.addAttribute("entryPoint", entryPoint);
			model.addAttribute("otherEntryPoint", otherEntryPoint);
		} else {
			model.addAttribute("savedEntryPoint", "");
		}

		PatientWrapper wrapperPatient = new PatientWrapper(patient);
		PersonWrapper wrapperPerson = new PersonWrapper(person);

		model.addAttribute("patientWrap", wrapperPatient);
		model.addAttribute("personWrap", wrapperPerson);

		String comorbidity = "";

		Obs riskFactor = getAllLatestObs(patient, Dictionary.COMORBIDITY);
		if (riskFactor != null) {
			EncounterWrapper wrapped = new EncounterWrapper(riskFactor.getEncounter());
			List<Obs> obsList = wrapped.allObs(riskFactor.getConcept());

			for (Obs obs : obsList) {
				if (comorbidity.isEmpty()) {
					comorbidity = comorbidity.concat(obs.getValueCoded().getName().toString());
				} else {
					comorbidity = comorbidity.concat(", " + obs.getValueCoded().getName().toString());
				}
			}
		}

		model.addAttribute("comorbidity", comorbidity);

		/*
		 * Obstetric History
		 */
		String pregStatusVal = "";

		Obs pregStatus = getLatestObs(patient, Dictionary.PREGNANCY_STATUS);
		Obs birthStatus = getLatestObs(patient, Dictionary.PREGNANCY_OUTCOME);
		if (pregStatus != null) {
			pregStatusVal = pregStatus.getValueCoded().getName().toString();
		}
		model.addAttribute("pregStatusVal", birthStatus != null ? false : pregStatusVal);

		/*
		 * Drug History
		 */
		String drugAllergiesVal = "";
		String drugOtherVal = "";

		Obs drugAllergies = getLatestObs(patient, Dictionary.ALLERGY_DRUG);
		Obs drugOther = getLatestObs(patient, Dictionary.OTHER_DRUG);
		if (drugAllergies != null) {
			EncounterWrapper wrapped = new EncounterWrapper(drugAllergies.getEncounter());
			List<Obs> obsList = wrapped.allObs(drugAllergies.getConcept());
			for (Obs obs : obsList) {
				drugAllergiesVal = drugAllergiesVal.concat(obs.getValueCoded().getName().toString());

			}
		}
		if (drugOther != null) {
			EncounterWrapper wrap = new EncounterWrapper(drugOther.getEncounter());
			List<Obs> obsList = wrap.allObs(drugOther.getConcept());
			for (Obs obs : obsList) {
				drugOtherVal = drugOtherVal.concat(obs.getValueText());

			}
		}
		model.addAttribute("drugAllergiesVal", drugAllergiesVal);
		model.addAttribute("drugOtherVal", drugOtherVal);

		String methadone[] = getMethadone(patient);
		model.addAttribute("methadone", methadone == null ? "" : methadone);
	}

	/**
	 * Checks if a patient has been recorded as deceased by a program
	 * 
	 * @param patient
	 *            the patient
	 * @return true if patient was recorded as deceased
	 */
	protected boolean hasBeenRecordedAsDeceased(Patient patient) {
		PatientCalculation calc = CalculationUtils.instantiateCalculation(RecordedDeceasedCalculation.class, null);
		return ResultUtil.isTrue(Context.getService(PatientCalculationService.class).evaluate(patient.getId(), calc));
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

	private String[] getMethadone(Patient patient) {
		String[] startNendDate = new String[2];
		Obs obs = getLatestObs(patient, Dictionary.IF_TYPE_YES);
		if (obs == null) {
			return null;
		}
		if (obs.getValueCoded().getId() != 79661)
			return null;
		startNendDate[0] = getValueDateAsString(patient, Dictionary.METHADONE_START_DATE);
		startNendDate[1] = getValueDateAsString(patient, Dictionary.METHADONE_END_DATE);
		return startNendDate;
	}

	private String getValueDateAsString(Patient patient, String uuid) {
		Obs obs = getLatestObs(patient, uuid);
		if (obs == null)
			return "";
		return formatDate(obs.getValueDate());
	}

	private Obs getAllLatestObs(Patient patient, String conceptIdentifier) {
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		List<Obs> obs = Context.getObsService().getObservationsByPersonAndConcept(patient, concept);
		int count = obs.size() - 1;
		if (obs.size() > 0) {
			// these are in reverse chronological order
			return obs.get(count);
		}
		return null;
	}

	private String formatDate(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-YYYY");
		return simpleDateFormat.format(date);
	}

}