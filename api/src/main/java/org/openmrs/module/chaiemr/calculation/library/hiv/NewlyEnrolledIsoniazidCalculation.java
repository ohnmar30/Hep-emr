/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.chaiemr.calculation.library.hiv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Program;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaicore.calculation.PatientFlagCalculation;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;


public class NewlyEnrolledIsoniazidCalculation extends AbstractPatientCalculation {

      @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
        
    	Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
        Set<Integer> alive = Filters.alive(cohort, context);
        Set<Integer> inHIV = Filters.inProgram(hivProgram, alive, context);
        Set<Integer> ltfu = CalculationUtils.patientsThatPass(calculate(new LostToFollowUpCalculation(), cohort, context));
        

		Calendar calendar = Calendar.getInstance();
		
		//endDate value have to be set from UI
        Date endDate = new Date(); 
        
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, -180); //6 months
        Date startDate = calendar.getTime();
        
        
        CalculationResultMap finalList = new CalculationResultMap();
        ObsService obs = Context.getObsService();
        
        for (Integer pID : cohort) {
            if (inHIV.contains(pID)) {
                if (alive.contains(pID)) {

                    boolean trigger = false;
                    Person p = new Patient(pID);
                    List<Obs> obsListForPatient = obs.getObservationsByPerson(p);
                    Date obsDate = null;

                    for (Obs patient : obsListForPatient) {
                        if (patient.getValueCoded() == Dictionary.getConcept(Dictionary.ISONIAZID)) {
                            obsDate = patient.getObsDatetime();
                            break;
                        }
                    }
                    if (obsDate != null && obsDate.after(startDate)) {
                        trigger = true;
                    }

                    if (ltfu.contains(pID)) {
                        trigger = false;
                    }

                    finalList.put(pID, new BooleanResult(trigger, this, context));
                }
            }
        }
        return finalList;
    }

}
