package org.openmrs.module.chaiemr.calculation.library.mchms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.ui.SortButtonRenderer;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.PatientFlagCalculation;
import org.openmrs.module.chaiemr.Dictionary;

public class IsoniazidCalculation extends AbstractPatientCalculation implements PatientFlagCalculation {
	private static final int period = 168;

	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
		CalculationResultMap result = new CalculationResultMap();
		for (Integer patientId : cohort) {
			Patient patient = Context.getPatientService().getPatient(patientId);
			result.put(patientId, new BooleanResult(needToDisplay(patient), this));
		}
		return result;
	}

	@Override
	public String getFlagMessage() {
		return "IPT six months completed";
	}

	public static int calculateDuration(Patient patient) {
		int duration = 0;
		List<Obs> obsListForProphyl = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.PROPHYLAXIS));
		for (Obs obsListForProphylaxi : obsListForProphyl) {
			if (obsListForProphylaxi.getValueCoded().getUuid().equals(Dictionary.ISONIAZID)) {
				List<Obs> obsListForDuration = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.MEDICATION_DURATION));
				for (Obs obsListForDurationss : obsListForDuration) {
					if (obsListForDurationss != null && obsListForDurationss.getObsGroup().getId() == obsListForProphylaxi.getObsGroup().getId()) {
						duration += obsListForDurationss.getValueNumeric();
					}
				}
			}
		}
		System.out.println("Duration is: "+duration);
		return duration;
	}

	public static boolean isCompleted(Patient patient) {
		return calculateDuration(patient) >= period;
	}
	
	private boolean needToDisplay(Patient patient){
		int duration = 0;
		List<Obs> isonaizaidList = getAllObsForIsoniazid(patient);
		sortObsByDate(isonaizaidList);
		for(Obs obs : isonaizaidList){
			System.out.println(obs.getEncounter().getVisit().getStopDatetime());
			duration += obs.getValueNumeric();
			if(duration >= period){
				return obs.getEncounter().getVisit().getStopDatetime()==null;
			}
			
		}
		return false;
	}
	
	private static List<Obs> getAllObsForIsoniazid(Patient patient){
		List<Obs> isoniazidList = new ArrayList<Obs>();
		List<Obs> obsListForProphyl = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.PROPHYLAXIS));
		for(Obs obsListForProphylaxi : obsListForProphyl){
			if (obsListForProphylaxi.getValueCoded().getUuid().equals(Dictionary.ISONIAZID)) {
				List<Obs> obsListForDuration = Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.MEDICATION_DURATION));
				for (Obs obsListForDurationss : obsListForDuration) {
					if (obsListForDurationss != null && obsListForDurationss.getObsGroup().getId() == obsListForProphylaxi.getObsGroup().getId()) {
						isoniazidList.add(obsListForDurationss);
					}
				}
			}
		}
		return isoniazidList;
	}
	
	public static  Date getCompletedDate(Patient patient){
		int duration = 0;
		List<Obs> isonaizaidList = getAllObsForIsoniazid(patient);
		sortObsByDate(isonaizaidList);
		for(Obs obs : isonaizaidList){
			System.out.println(obs.getEncounter().getVisit().getStopDatetime());
			duration += obs.getValueNumeric();
			if(duration >= period){
				return new Date();
			}else{
				return obs.getEncounter().getVisit().getStopDatetime();
			}
		}
		return null;
	}
	
	private static void sortObsByDate(List<Obs> obsList){
		Collections.sort(obsList,new Comparator<Obs>() {
			@Override
			public int compare(Obs first, Obs second) {
				if(first.getEncounter().getVisit().getStopDatetime()==null && second.getEncounter().getVisit().getStopDatetime()==null)
					return 0;
				if(first.getEncounter().getVisit().getStopDatetime()==null)
					return -1;
				if(second.getEncounter().getVisit().getStopDatetime()==null)
					return -1;
				if(first.getEncounter().getVisit().getStopDatetime().before(second.getEncounter().getVisit().getStopDatetime()))
					return -1;
				return 1;
			}
		});
	}

}
