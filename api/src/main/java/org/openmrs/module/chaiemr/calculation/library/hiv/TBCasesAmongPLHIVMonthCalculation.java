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

package org.openmrs.module.chaiemr.calculation.library.hiv;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.Program;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.parameter.ParameterDefinitionSet;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ObsResult;
import org.openmrs.module.chaicore.CoreUtils;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaicore.calculation.PatientFlagCalculation;
import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.builder.ReportBuilder;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.HivConstants;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.metadata.TbMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.openmrs.module.chaiemr.calculation.EmrCalculationUtils.daysSince;


public class TBCasesAmongPLHIVMonthCalculation extends AbstractPatientCalculation {

	
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection,
	 *      java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 * @should determine whether patients need a CD4
	 */

	
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
		
		Calendar calendar = Calendar.getInstance();
		
		//endDate value have to be set from UI
        Date endDate = new Date(); 
        
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, -30); //1 month
        Date startDate = calendar.getTime();
        
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);

		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inHivProgram = Filters.inProgram(hivProgram, alive, context);

		CalculationResultMap obsTBData = Calculations.lastObs(Dictionary.getConcept(Dictionary.TB_PATIENT), cohort, context);
		CalculationResultMap obsDateData = Calculations.lastObs(Dictionary.getConcept(Dictionary.TUBERCULOSIS_DRUG_TREATMENT_START_DATE), cohort, context);
		
		CalculationResultMap ret = new CalculationResultMap();
				
		for (Integer ptId : cohort) {
			boolean TBStatus = false;

			// Is patient alive and in the HIV program
			if (inHivProgram.contains(ptId)) {
									
				Concept c = EmrCalculationUtils.codedObsResultForPatient(obsTBData, ptId);
				Obs o = EmrCalculationUtils.obsResultForPatient(obsDateData, ptId);
				
				if(o!= null && o.getValueDatetime().after(startDate) && o.getValueDatetime().before(endDate)){
					System.out.println("TUBERCULOSIS_DRUG_TREATMENT_START_DATE ===> " + o.getValueDatetime().toString());
					if (c!= null && c.getUuid().equals(Dictionary.YES)){
						TBStatus = true;
					}
				}

			}
			ret.put(ptId, new BooleanResult(TBStatus, this, context));
		}
		return ret;
	}
	
}
