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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaicore.calculation.PatientFlagCalculation;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
public class NewHIVPatientEnrolledCalculation extends AbstractPatientCalculation {
	private int minPercentage,maxPercentage;
	
	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection,
	 *      java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 */
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
