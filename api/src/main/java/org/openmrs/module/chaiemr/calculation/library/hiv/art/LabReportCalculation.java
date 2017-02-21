package org.openmrs.module.chaiemr.calculation.library.hiv.art;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;

public class LabReportCalculation  extends AbstractPatientCalculation {
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues,
	                                     PatientCalculationContext context) {
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);

		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inHivProgram = Filters.inProgram(hivProgram, alive, context);
		
		CalculationResultMap ret = new CalculationResultMap();
		
		return ret;
	}

}
