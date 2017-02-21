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

package org.openmrs.module.chaiemr.calculation.library.tb;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.chaiemr.metadata.TbMetadata;

/**
 * Calculates whether patients are (alive and) in the TB program
 */
public class InTbProgramCalculation extends AbstractPatientCalculation {

	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection, java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
		Program tbProgram = MetadataUtils.existing(Program.class, TbMetadata._Program.TB);

		// Get all patients who are alive and in TB program
		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inTbProgram = Filters.inProgram(tbProgram, alive, context);

		// Get concepts
		Concept methodOfenrollment = Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);
		Concept tbprogram = Dictionary.getConcept(Dictionary.TUBERCULOSIS_TREATMENT_PROGRAM);

		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			boolean eligible = false;

			// check if a patient is alive
			if (alive.contains(ptId)) {

				List<Obs> obs = Context.getObsService()
						.getObservationsByPersonAndConcept(Context.getPatientService().getPatient(ptId), methodOfenrollment);
				List<Obs> requiredObs = new LinkedList<Obs>();
				for(Obs o : obs ) {
					if(o.getValueCoded()==tbprogram){
						requiredObs.add(o);
						eligible=true;
					}
				}
			ret.put(ptId, new BooleanResult(eligible, this));
		}

		
	}
		return ret;
}
}