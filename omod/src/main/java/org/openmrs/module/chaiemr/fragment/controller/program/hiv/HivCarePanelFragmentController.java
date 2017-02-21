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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.chaicore.program.ProgramManager;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.chaiemr.calculation.library.hiv.LastCd4CountCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.LastCd4PercentageCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.LastCptCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.LastDiagnosisCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.LastOICalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.LastViralLoadCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.LastWhoStageCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.InitialArtRegimenCalculation;
import org.openmrs.module.chaiemr.regimen.RegimenChangeHistory;
import org.openmrs.module.chaiemr.regimen.RegimenManager;
import org.openmrs.module.chaiemr.wrapper.EncounterWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for HIV care summary
 */
public class HivCarePanelFragmentController {

	public void controller(@FragmentParam("patient") Patient patient, @FragmentParam("complete") Boolean complete, @RequestParam(required = false, value = "startDate") String startDate, @RequestParam(required = false, value = "endDate") String endDate, FragmentModel model, @SpringBean RegimenManager regimenManager, @SpringBean ProgramManager programManager) {

		Map<String, CalculationResult> calculationResults = new HashMap<String, CalculationResult>();

		/*
		 * Encounter details
		 */
		Date dateArt = new Date();
		List<Encounter> artEncounters = Context.getEncounterService()
				.getEncounters(patient);
		for (Encounter en : artEncounters) {
			if (en.getEncounterType().getUuid()
					.equals("0cb4417d-b98d-4265-92aa-c6ee3d3bb317")) {
				if (dateArt.after(en.getEncounterDatetime())) {
					dateArt = en.getEncounterDatetime();
				}
			}
		}
		model.addAttribute("initialHivStartDate", new SimpleDateFormat(
				"dd-MMMM-yyyy").format(dateArt));
		
		if (complete != null && complete.booleanValue()) {
			calculationResults.put("initialArtRegimen", EmrCalculationUtils.evaluateForPatient(InitialArtRegimenCalculation.class, null, patient));
//			calculationResults.put("initialArtStartDate", EmrCalculationUtils.evaluateForPatient(InitialArtStartDateCalculation.class, null, patient));
		}

		calculationResults.put("lastWHOStage", EmrCalculationUtils.evaluateForPatient(LastWhoStageCalculation.class, null, patient));

		model.addAttribute("patient", patient);

	    calculationResults.put("lastCD4Count", EmrCalculationUtils.evaluateForPatient(LastCd4CountCalculation.class, null, patient));
		calculationResults.put("lastCD4Percent", EmrCalculationUtils.evaluateForPatient(LastCd4PercentageCalculation.class, null, patient));
		calculationResults.put("viralDateResult", EmrCalculationUtils.evaluateForPatient(LastViralLoadCalculation.class, null, patient));
		Obs cdList = getLatestObs(patient, Dictionary.CD4_COUNT);
	
		String cd4Count = "";
		if (cdList != null) {
			if(cdList.getValueText()!= null)
			{
			cd4Count=cdList.getValueText().toString();
			}
		}
		model.addAttribute("cd4Count", cd4Count);
		
		Obs cdPerList = getLatestObs(patient, Dictionary.CD4_PERCENT);
		
		String cd4PerCount = "";
		if(cdPerList !=null)
		{
		if (cdPerList.getValueText() != null) {
			cd4PerCount=cdPerList.getValueText().toString();
		}
		}
		model.addAttribute("cd4PerCount", cd4PerCount);
		
		Obs viralLoad = getLatestObs(patient, Dictionary.HIV_VIRAL_LOAD);
	
		String viralLoadResult = ""; 
		if (viralLoad != null) {
			if (viralLoad.getValueText() != null) {
			viralLoadResult=viralLoad.getValueText().toString();
			
			}
		}
		model.addAttribute("viralLoadResult", viralLoadResult);
		
		
		
		String listAllDiag = "";
		
		Obs diagList = getLatestObs(patient, Dictionary.HIV_CARE_DIAGNOSIS);
		Obs consultationObs =   getLatestObs(patient, Dictionary.CONSULTATION_DETAIL);
		if(consultationObs!=null){
			EncounterWrapper wrappedG = new EncounterWrapper(
					consultationObs.getEncounter());
			List<Obs> obsGroupList = wrappedG.allObs(consultationObs.getConcept());
			for (Obs obsG : obsGroupList) {
				if (diagList != null) {
					List<Obs> obsList = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.HIV_CARE_DIAGNOSIS));
					for (Obs obs : obsList) {
						if(obs.getObsGroupId() == obsG.getObsId()){
							if (listAllDiag.isEmpty()) {
								listAllDiag = listAllDiag.concat(obs
										.getValueCoded().getName().toString());
							} else {
								listAllDiag = listAllDiag.concat(", "
										+ obs.getValueCoded().getName().toString());
							}
							
						}
					}
				}
			}
			
		}

		model.addAttribute("listAllDiag", listAllDiag);	
		
		String listAllOI = "";
		
		Obs tbOIList =   getLatestObs(patient, Dictionary.OI_GROUP_TB_FORM);
		if(tbOIList!=null){
			EncounterWrapper wrappedG = new EncounterWrapper(
					tbOIList.getEncounter());
			List<Obs> obsGroupList = wrappedG.allObs(tbOIList.getConcept());
			for (Obs obsG : obsGroupList) {
				if (diagList != null) {
					List<Obs> obsList = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.HIV_CARE_DIAGNOSIS));
					
					for (Obs obs : obsList) {
						if(obs.getObsGroupId() == obsG.getObsId()){
							if (listAllOI.isEmpty()) {
								listAllOI = listAllOI.concat(obs
										.getValueCoded().getName().toString());
							} else {
								listAllOI = listAllOI.concat(", "
										+ obs.getValueCoded().getName().toString());
							}
							
						}
					}
				}
			}
			
		}
		model.addAttribute("listAllOI", listAllOI);	
		
		String cptStatus="";
		List<Obs> obsListForOiTreatments = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.PROPHYLAXIS));
		for(Obs obsListForOiTreatment:obsListForOiTreatments){
		if(obsListForOiTreatment.getValueCoded().getUuid().equals("105281AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
			cptStatus="Yes";	
			
		  }
		
		}
		model.addAttribute("cpt", cptStatus);
		calculationResults.put("lastOI", EmrCalculationUtils.evaluateForPatient(LastOICalculation.class, null, patient));
	    calculationResults.put("lastDiagnosis", EmrCalculationUtils.evaluateForPatient(LastDiagnosisCalculation.class, null, patient));
		calculationResults.put("onCpt", EmrCalculationUtils.evaluateForPatient(LastCptCalculation.class, null, patient));
		
		model.addAttribute("calculations", calculationResults);
		List<Visit> visit = Context.getVisitService().getVisitsByPatient(patient);
		
				Double duration = 0.0;
				Integer duratin = 0;
				Obs prophly = getLatestObs(patient, Dictionary.PROPHYLAXIS);
				Obs medduration = getLatestObs(patient, Dictionary.MEDICATION_DURATION);
				if (prophly != null && medduration != null) {
					List<Obs> obsListForOiTreat = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.PROPHYLAXIS));
		
					for (Obs obsListForProphylaxi : obsListForOiTreat) {
						if (obsListForProphylaxi.getValueCoded().getUuid().equals("105281AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
							List<Obs> obsListForDuration = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.MEDICATION_DURATION));
							for (Obs obsListForDurationss : obsListForDuration) {
								if (obsListForDurationss.getObsGroupId().equals(obsListForProphylaxi.getObsGroupId())) {
									duration = obsListForDurationss.getValueNumeric();
		
									duratin = duration.intValue();
									Calendar calendar = Calendar.getInstance();
									Date endDatecpt = obsListForDurationss.getObsDatetime();
									calendar.setTime(endDatecpt);
									calendar.add(Calendar.DATE, duratin);
									endDatecpt = calendar.getTime();
									SimpleDateFormat smd = new SimpleDateFormat("dd/M/yyyy");
									Date startDatecpt = new Date();
									for (Visit visi : visit) {
		
										if (visi.getStopDatetime() == null && duratin >= 1) {
		
											model.addAttribute("duration", duratin);
		
										} else {
											if (smd.format(startDatecpt).equals(smd.format(endDatecpt)) || (startDatecpt.before(endDatecpt))) {
												model.addAttribute("duration", duratin);
											} else {
												model.addAttribute("duration", "");
											}
										}
									}
		
								}
							}
							break;
						} else {
							model.addAttribute("duration", "");
						}
		
					}
				} else {
					model.addAttribute("duration", "");
		}
		Concept medSet = regimenManager.getMasterSetConcept("ARV");		
		RegimenChangeHistory history = RegimenChangeHistory.forPatient(patient, medSet);
		model.addAttribute("regimenHistory", history);
		
		model.addAttribute("endDate", endDate);
		model.addAttribute("startDate", startDate);
		if(patient.getAge() > 15){
			model.addAttribute("graphingConcepts", Dictionary.getConcepts(Dictionary.WEIGHT_KG, Dictionary.CD4_COUNT,  Dictionary.HIV_VIRAL_LOAD,Dictionary.OI_COUNT));
		}
		else{
			model.addAttribute("graphingConcepts", Dictionary.getConcepts(Dictionary.WEIGHT_KG,  Dictionary.CD4_PERCENT, Dictionary.HIV_VIRAL_LOAD,Dictionary.OI_COUNT));
		}
		
		PatientProgram currentEnrollment = null;
		Program program=Context.getProgramWorkflowService().getProgramByUuid("96ec813f-aaf0-45b2-add6-e661d5bf79d6");
		
		// Gather all program enrollments for this patient and program
				List<PatientProgram> enrollments = programManager.getPatientEnrollments(patient, program);
				for (PatientProgram enrollment : enrollments) {
					if (enrollment.getActive()) {
						currentEnrollment = enrollment;
					}
				}

		model.addAttribute("currentEnrollment", currentEnrollment);
		
	}
	
/*	private Obs getAllLatestObs(Patient patient, String conceptIdentifier) {
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		List<Obs> obs = Context.getObsService()
				.getObservationsByPersonAndConcept(patient, concept);
		if (obs.size() > 0) {
			// these are in reverse chronological order
			return obs.get(0);
		}
		return null;
	}
*/
	
	
	private Obs getLatestObs(Patient patient, String conceptIdentifier) {
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		List<Obs> obs = Context.getObsService()
				.getObservationsByPersonAndConcept(patient, concept);
		if (obs.size() > 0) {
			// these are in reverse chronological order
			return obs.get(0);
		}
		return null;
	}

}