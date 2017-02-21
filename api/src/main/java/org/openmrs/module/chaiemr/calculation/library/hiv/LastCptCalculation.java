package org.openmrs.module.chaiemr.calculation.library.hiv;

import java.util.Collection;
import java.util.Map;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaiemr.Dictionary;

public class LastCptCalculation  extends AbstractPatientCalculation{
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
		return Calculations.lastObs(Dictionary.getConcept(Dictionary.PROPHYLAXIS), cohort, context);
	}
}
