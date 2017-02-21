package org.openmrs.module.chaiemr.calculation.library.hiv;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaiemr.Dictionary;

import java.util.Collection;
import java.util.Map;


public class LastViralLoadCalculation extends AbstractPatientCalculation {


/**
 * Calculates the last Viral load  of patients
 */

	/**
	 * Evaluates the calculation
	 * @should calculate null for patients with no recorded CD4 count
	 * @should calculate last CD4 count for all patients with a recorded CD4 count
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
		return Calculations.lastObs(Dictionary.getConcept(Dictionary.HIV_VIRAL_LOAD), cohort, context);
	}

}