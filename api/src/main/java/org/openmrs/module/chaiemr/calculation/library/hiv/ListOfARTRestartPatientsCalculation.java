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
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.calculation.parameter.SimpleParameterDefinition;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaiemr.Metadata;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;


public class ListOfARTRestartPatientsCalculation extends AbstractPatientCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {

        Set<Integer> ltfu = CalculationUtils.patientsThatPass(calculate(new LostToFollowUpCalculation(), cohort, context));
        Program artProgram = MetadataUtils.existing(Program.class, Metadata.Program.ART);
        CalculationResultMap activePatientPrograms = Calculations.allEnrollments(artProgram, cohort, context);
        CalculationResultMap ret = new CalculationResultMap();
        Set<Integer> listin = CalculationUtils.patientsThatPass(activePatientPrograms);
        Set<Integer> alive = Filters.alive(cohort, context);
        Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
        Set<Integer> inHIV = Filters.inProgram(hivProgram, alive, context);

        for (Integer ptId : cohort) {
            boolean restarted = false;
            int completeCount = 0;
            Date enrollDate = null;
            Date completeDate = null;
            if (inHIV.contains(ptId)) {
                if (listin.contains(ptId)) {
                    CalculationResult res = activePatientPrograms.get(ptId);
                    List<PatientProgram> enrollments = CalculationUtils.extractResultValues((ListResult) res);
                    for (PatientProgram enrollment : enrollments) {
                        if (enrollment.getDateCompleted() != null) {
                            completeCount++;
                        }
                        enrollDate = enrollment.getDateEnrolled();
                        completeDate = enrollment.getDateCompleted();
                    }
                }
                
                if (completeCount > 0 && enrollDate != null && completeDate == null ) {
                    restarted = true;
                }

                if (ltfu.contains(ptId)) {
                    restarted = false;
                }
                ret.put(ptId, new BooleanResult(restarted, this, context));
            }
        }
        return ret;

    }
}
