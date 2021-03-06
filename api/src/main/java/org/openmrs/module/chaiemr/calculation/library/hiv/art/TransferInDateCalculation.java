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

package org.openmrs.module.chaiemr.calculation.library.hiv.art;

import org.openmrs.Concept;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Calculates whether a patient is a transfer in date
 */
public class TransferInDateCalculation extends AbstractPatientCalculation {
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection,
	 *      java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
										 PatientCalculationContext context) {

		Concept transferInDate = Dictionary.getConcept(Dictionary.TRANSFER_IN_DATE);

		CalculationResultMap transferInDateResults = Calculations.lastObs(transferInDate, cohort, context);

		CalculationResultMap result = new CalculationResultMap();
		for (Integer ptId : cohort) {

			Date tDate = EmrCalculationUtils.datetimeObsResultForPatient(transferInDateResults, ptId);

			result.put(ptId, tDate == null ? null : new SimpleResult(tDate, null));

		}
		return result;
	}
}
