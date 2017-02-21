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
import org.openmrs.module.chaiemr.HivConstants;
import static org.openmrs.module.chaiemr.calculation.EmrCalculationUtils.daysSince;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;


public class PatientsDueForViralLoadCalculation extends AbstractPatientCalculation implements PatientFlagCalculation {

    @Override
    public String getFlagMessage() {
        return "Patients Due for Viral Load";
    }

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> valueMap, PatientCalculationContext context) {

        Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
        Set<Integer> alive = Filters.alive(cohort, context);
        Set<Integer> inHIV = Filters.inProgram(hivProgram, alive, context);
        Set<Integer> ltfu = CalculationUtils.patientsThatPass(calculate(new LostToFollowUpCalculation(), cohort, context));
        CalculationResultMap finalList = new CalculationResultMap();
        
        ObsService obs = Context.getObsService();
        for (Integer pId : cohort) {
            if (inHIV.contains(pId)) {
                boolean trigger = false;

                Person p = new Patient(pId);
                List<Obs> obsListForPatient = obs.getObservationsByPerson(p);
                Date obsDate = null;
                int valueCount = 0;

                for (Obs values : obsListForPatient) {
                    if (values.getValueCoded() == Dictionary.getConcept(Dictionary.HIV_VIRAL_LOAD)) {
                        if (daysSince(values.getObsDatetime(), context) > HivConstants.DUE_DATE_FOR_VIRAL_LOAD) {
                            obsDate = values.getObsDatetime();
                            valueCount++;
                        }
                    }
                }
                if (valueCount > 0 && obsDate != null) {
                    trigger = true;
                }

                if (ltfu.contains(pId)) {
                    trigger = false;
                }

                finalList.put(pId, new BooleanResult(trigger, this, context));
            }
        }
        return finalList;
    }

}
