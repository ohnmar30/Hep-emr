package org.openmrs.module.chaiemr.fragment.controller.program;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.chaiemr.wrapper.EncounterWrapper;
import org.openmrs.module.chaiemr.wrapper.PatientWrapper;
import org.openmrs.module.chaiemr.wrapper.PersonWrapper;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ArtRegisterFragmentController {
	public void controller(
			@RequestParam(value = "patientId", required = false) Person person,
			@RequestParam(value = "patientId", required = false) Patient patient,
			@RequestParam("returnUrl") String returnUrl,
			FragmentModel model) {
		
		ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		/*
		 * Constant value across all visit
		 */
		model.addAttribute("returnUrl", returnUrl);
		model.addAttribute("patientName", person.getGivenName());
	//	model.addAttribute("patientAge", person.getAge());
		Date d = new Date();
		int daysInMon[] = {31, 28, 31, 30, 31, 30,31, 31, 30, 31, 30, 31};  //Days in month
		if(d.getYear()==person.getBirthdate().getYear()){
			if(d.getMonth()==person.getBirthdate().getMonth()){
				model.addAttribute("patientAge", d.getDay()-person.getBirthdate().getDay() + " days");
			}
			else{
				int mdiff = d.getMonth()-person.getBirthdate().getMonth();
				if(mdiff == 1 && d.getDate() - person.getBirthdate().getDate() < 1){
					model.addAttribute("patientAge", daysInMon[person.getBirthdate().getMonth()] - person.getBirthdate().getDate() + d.getDate()+ " days");
				}
				else {
					model.addAttribute("patientAge", mdiff+ " months");
				}
			}
		}
		else if(d.getYear()-person.getBirthdate().getYear() ==1 && d.getMonth() - person.getBirthdate().getMonth() < 1 ){
			model.addAttribute("patientAge", 12 - person.getBirthdate().getMonth() + d.getMonth()+ " months");
		}
		else {
			model.addAttribute("patientAge",d.getYear()-person.getBirthdate().getYear() +" years");
		}
		
		model.addAttribute("birthDate", new SimpleDateFormat("dd-MMMM-yyyy")
				.format(person.getBirthdate()));
		model.addAttribute("patientGender", person.getGender());
		model.addAttribute("address", person.getPersonAddress());

		PatientWrapper wrapperPatient = new PatientWrapper(patient);
		PersonWrapper wrapperPerson = new PersonWrapper(person);

		model.addAttribute("patientWrap", wrapperPatient);
		model.addAttribute("personWrap", wrapperPerson);

		/**
		 * Obstetric History
		 
		String pregStatusVal = "";
		String eddVal = "";
		String ancNumberVal = "";

		Obs pregStatus = getLatestObs(patient, Dictionary.PREGNANCY_STATUS);
		if (pregStatus != null) {
			pregStatusVal = pregStatus.getValueCoded().getName().toString();
		}
		model.addAttribute("pregStatusVal", pregStatusVal);

		Obs edd = getLatestObs(patient, Dictionary.EXPECTED_DATE_OF_DELIVERY);
		if (edd != null) {
			eddVal = new SimpleDateFormat("dd-MMMM-yyyy").format(edd
					.getValueDate());
		}
		model.addAttribute("eddVal", eddVal);

		Obs ancNumber = getLatestObs(patient, Dictionary.ANTENATAL_CASE_NUMBER);
		if (ancNumber != null) {
			ancNumberVal = ancNumber.getValueNumeric().toString();
		}
		model.addAttribute("ancNumberVal", ancNumberVal);

		Obs obstetricHistoryDetail = getAllLatestObs(patient,
				Dictionary.OBSTETRIC_HIS_DETAIL);
		Obs infantName = getAllLatestObs(patient, Dictionary.INFANT_NAME);

		Map<Integer, String> infantList = new HashMap<Integer, String>();
		Integer infantIndex = 0;
		if (obstetricHistoryDetail != null) {
			EncounterWrapper wrappedObsGroup = new EncounterWrapper(
					obstetricHistoryDetail.getEncounter());
			List<Obs> obsGroupList = wrappedObsGroup
					.allObs(obstetricHistoryDetail.getConcept());
			for (Obs obsG : obsGroupList) {
				String infantNameVal = "";

				if (infantName != null) {
					EncounterWrapper wrapped = new EncounterWrapper(
							infantName.getEncounter());
					List<Obs> obsList = wrapped.allObs(infantName.getConcept());
					for (Obs obs : obsList) {
						if (obs.getObsGroupId() == obsG.getObsId()) {
							infantNameVal = infantNameVal.concat(obs
									.getValueText().toString());
						}
					}
				}

				String val = infantNameVal;
				infantList.put(infantIndex, val);
				infantIndex++;

			}
		}
		model.addAttribute("infantList", infantList);

		*/
		
		
		/*
		 * Drug History
		 */
		String artReceivedVal = "";

		Obs artReceived = getAllLatestObs(patient,
				Dictionary.DRUG_HISTORY_ART_RECEIVED);
		if (artReceived != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					artReceived.getEncounter());
			List<Obs> obsList = wrapped.allObs(artReceived.getConcept());
			for (Obs obs : obsList) {
				artReceivedVal = artReceivedVal.concat(obs.getValueCoded()
						.getName().toString());
			}
		}
		model.addAttribute("artReceivedVal", artReceivedVal);

		String artReceivedTypeValue = "";

		Obs artReceivedType = getAllLatestObs(patient,
				Dictionary.DRUG_HISTORY_ART_RECEIVED_TYPE);
		if (artReceivedType != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					artReceivedType.getEncounter());
			List<Obs> obsList = wrapped.allObs(artReceivedType.getConcept());
			for (Obs obs : obsList) {
				artReceivedTypeValue = artReceivedTypeValue.concat(obs
						.getValueCoded().getName().toString());
			}
		}
		model.addAttribute("artReceivedTypeValue", artReceivedTypeValue);

		String artReceivedPlaceValue = "";

		Obs artReceivedPlace = getAllLatestObs(patient,
				Dictionary.DRUG_HISTORY_ART_RECEIVED_PLACE);
		if (artReceivedPlace != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					artReceivedPlace.getEncounter());
			List<Obs> obsList = wrapped.allObs(artReceivedPlace.getConcept());
			for (Obs obs : obsList) {
				artReceivedPlaceValue = artReceivedPlaceValue.concat(obs
						.getValueCoded().getName().toString());
			}
		}
		model.addAttribute("artReceivedPlaceValue", artReceivedPlaceValue);

		String drugStartDateVal = "";
		Obs drugStartDate = getAllLatestObs(patient, Dictionary.ART_START_DATE_DRUG_HISTORY);
		if (drugStartDate != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					drugStartDate.getEncounter());
			List<Obs> obsList = wrapped.allObs(drugStartDate
					.getConcept());
			for (Obs obs : obsList) {
				drugStartDateVal = new SimpleDateFormat("dd-MMMM-yyyy").format(obs.getValueDate());
			}
		}
		
		model.addAttribute("drugStartDateVal", drugStartDateVal);
		
		String drugDurationVal = "";
		Obs drugDuration = getAllLatestObs(patient, Dictionary.DRUG_DURATION);
		if (drugDuration != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					drugDuration.getEncounter());
			List<Obs> obsList = wrapped.allObs(drugDuration
					.getConcept());
			for (Obs obs : obsList) {
					drugDurationVal = drugDurationVal.concat(obs
							.getValueNumeric().toString());
			}
		}
		
		model.addAttribute("drugDurationVal", drugDurationVal);

		String drugNameVal = "";
		Obs drugName = getAllLatestObs(patient, Dictionary.DRUG_NAME);
		if (drugName != null) {
			EncounterWrapper wrapped = new EncounterWrapper(
					drugName.getEncounter());
			List<Obs> obsList = wrapped.allObs(drugName.getConcept());

			for (Obs obs : obsList) {
				if (drugNameVal.isEmpty()) {
					drugNameVal = drugNameVal.concat(obs
							.getValueCoded().getName().toString());
				} else {
					drugNameVal = drugNameVal.concat(", "
							+ obs.getValueCoded().getName().toString());
				}
			}
		}
		model.addAttribute("drugNameVal", drugNameVal);
	
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
		model.addAttribute("artInitiationDate", new SimpleDateFormat(
				"dd-MMMM-yyyy").format(dateArt));

		
		/*
		 * Get regimen history
		 * */
		Map<Integer, String> regimenList = new HashMap<Integer, String>();
		Integer regimenIndex = 0;
		
		List<DrugOrder> orderList =  Context.getOrderService().getDrugOrdersByPatient(patient);
		
		List<Encounter> encounterList =  Context.getEncounterService().getEncounters(patient);
		for(Encounter en : encounterList ){
			String regName = "";
			String changeStopReason = "";
			if(en.getEncounterType().getUuid().equals("00d1b629-4335-4031-b012-03f8af3231f8")){
				DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
				List<Order> orderListByEn =  Context.getOrderService().getOrdersByEncounter(en);
					for(Order o : orderListByEn){
						DrugOrder dr = Context.getOrderService().getDrugOrder(o.getOrderId());
						DrugOrderProcessed dop=chaiEmrService.getLastDrugOrderProcessed(dr);
						if(regName.equals("")){
							regName = regName.concat(dr.getConcept().getName() + "(" + dr.getDose()+dr.getUnits()+" "+dr.getFrequency()+")");	
						}
						else{
							regName = regName.concat(" + " +dr.getConcept().getName() + "(" + dr.getDose()+dr.getUnits()+" "+dr.getFrequency()+")");
						}
						if(dr.getDiscontinuedReason()!=null){
							changeStopReason = dr.getDiscontinuedReason().getName().toString();	
						}
						if(dop.getRegimenChangeType().equals("Restart")){
							drugOrderProcessed=dop;	
						}
					}
					
					if(regName!=""){
						if(drugOrderProcessed.getDrugOrder()!=null){
						regimenList.put(regimenIndex,new SimpleDateFormat("dd-MMMM-yyyy").format(en.getEncounterDatetime()) + ", " + changeStopReason+ ","+ new SimpleDateFormat("dd-MMMM-yyyy").format(drugOrderProcessed.getDrugOrder().getStartDate()) + ","+regName  );
						}
						else{
							regimenList.put(regimenIndex,new SimpleDateFormat("dd-MMMM-yyyy").format(en.getEncounterDatetime()) + ", " + changeStopReason+ ","+ " " + ","+regName  );	
						}
						regimenIndex++;
					}
				}
		}
		model.addAttribute("regimenList", regimenList);
		
		/*
		 * HIV discontinuation part
		 * */
		String dicontinuationReasonVal ="";
		String dicontinuationDateVal = "";
		Obs dicontinuationReason = getLatestObs(patient, Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
		if (dicontinuationReason != null) {
			dicontinuationReasonVal =dicontinuationReason.getValueCoded().getName().toString();
			if(dicontinuationReason.getValueCoded().toString().equals("159492")){
				dicontinuationDateVal = new SimpleDateFormat("dd-MMMM-yyyy").format(getLatestObs(patient, Dictionary.DATE_TRANSFERRED_OUT).getValueDatetime()); 
			}
			else if(dicontinuationReason.getValueCoded().toString().equals("160034")){
				dicontinuationDateVal = new SimpleDateFormat("dd-MMMM-yyyy").format(getLatestObs(patient, Dictionary.DEATH_DATE).getValueDatetime());
			}
			else
				dicontinuationDateVal = new SimpleDateFormat("dd-MMMM-yyyy").format(getLatestObs(patient, Dictionary.DATE_LAST_VISIT).getValueDatetime());
		}
		model.addAttribute("dicontinuationReasonVal", dicontinuationReasonVal);
		model.addAttribute("dicontinuationDateVal", dicontinuationDateVal);
		
		
		if(patient.getAge() > 15){
			model.addAttribute("graphingConcepts", Dictionary.getConcepts(
					Dictionary.TUBERCULOSIS_TREATMENT_NUMBER,
					Dictionary.TUBERCULOSIS_DRUG_TREATMENT_START_DATE,Dictionary.PERFORMANCE,
					Dictionary.CURRENT_WHO_STAGE,Dictionary.WEIGHT_KG,Dictionary.CD4_COUNT));
		}
		else{
			model.addAttribute("graphingConcepts", Dictionary.getConcepts(
					Dictionary.TUBERCULOSIS_TREATMENT_NUMBER,
					Dictionary.TUBERCULOSIS_DRUG_TREATMENT_START_DATE,Dictionary.PERFORMANCE,
					Dictionary.CURRENT_WHO_STAGE,Dictionary.WEIGHT_KG, Dictionary.CD4_PERCENT));
		}
	}

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

	private Obs getAllLatestObs(Patient patient, String conceptIdentifier) {
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		List<Obs> obs = Context.getObsService()
				.getObservationsByPersonAndConcept(patient, concept);
		int count = obs.size() - 1;
		if (obs.size() > 0) {
			// these are in reverse chronological order
			return obs.get(count);
		}
		return null;
	}
}