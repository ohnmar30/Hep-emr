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

import org.openmrs.DrugOrder;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ObsResult;
import org.openmrs.module.chaicore.CoreUtils;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaicore.calculation.PatientFlagCalculation;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.HivConstants;
import org.openmrs.module.chaiemr.metadata.HivMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.openmrs.module.chaiemr.calculation.EmrCalculationUtils.daysSince;

/**
 * Calculates whether a patient has a declining CD4 count. Calculation returns true if patient
 * is alive, enrolled in the HIV program and last CD4 count is less than CD4 count from 6 months ago
 */
public class PatientsWhoHaveVirologicalFailureCalculation extends AbstractPatientCalculation {



	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection,
	 *      java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 * @should determine whether patients need a CD4
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {

		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);

		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inHivProgram = Filters.inProgram(hivProgram, alive, context);

		List<DrugOrder> drugOrderList = Context.getOrderService().getDrugOrders();
		List<Integer> virologicalList = new ArrayList<Integer>();
		
		//Add patients with Virological Failure to virologicalList
		for(DrugOrder o : drugOrderList){
			if(o.getDiscontinuedReason()!=null && o.getDiscontinuedReason().getUuid().equals(Dictionary.VIROLOGICAL_FAILURE)){
				virologicalList.add(o.getPatient().getPatientId());
			}
		}
		
		CalculationResultMap ret = new CalculationResultMap();
		
		for (Integer ptId : cohort) {
			
			boolean virologicalFailure =  false;

			// Is patient alive and in the HIV program
			if (inHivProgram.contains(ptId)) {
				
				if(virologicalList.contains(ptId)){
					virologicalFailure = true;
				}
				
			}
			ret.put(ptId, new BooleanResult(virologicalFailure, this, context));
		}

		return ret;
	}
}
