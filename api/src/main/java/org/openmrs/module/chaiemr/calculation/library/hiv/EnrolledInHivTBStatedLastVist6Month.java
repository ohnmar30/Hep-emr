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

import org.joda.time.DateTime;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.HivConstants;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.metadata.TbMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import static org.openmrs.module.chaiemr.calculation.EmrCalculationUtils.daysSince;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates HIV patients enrolled in HIV care who had TB status assessed and recorded during their last visit
 */
public class EnrolledInHivTBStatedLastVist6Month extends AbstractPatientCalculation {

	/**
	 * Evaluates the calculation
	 * @param cohort the patient cohort
	 * @param params the calculation parameters
	 * @param context the calculation context
	 * @return the result map
	 * @should calculate null for patients who are not enrolled in the HIV program or not alive
	 * @should calculate true for patients who's TB status assessed and recorded during their last visit in 6 months
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> params, PatientCalculationContext context) {
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);


		Concept tbScreening = Dictionary.getConcept(Dictionary.TB_SCREENING);

		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inHivProgram = Filters.inProgram(hivProgram, alive, context);

		CalculationResultMap screeningObs = Calculations.allObs(null, cohort, context);
		CalculationResultMap ret = new CalculationResultMap();
		
		//Date
				Calendar calendar = Calendar.getInstance();
				Date endDate = new Date(); //endDate value have to be set from UI
				calendar.setTime(endDate);
				calendar.add(Calendar.DATE, -180); //6 months
				Date startDate = calendar.getTime();
				
				for (Integer ptId : cohort) {
					boolean hivAndTBScreened = false;

					if (inHivProgram.contains(ptId)) {
							hivAndTBScreened = false;

						List<Obs> diseasesStatuses = CalculationUtils.extractResultValues((ListResult) screeningObs.get(ptId));

						for (Obs diseaseStatus : diseasesStatuses) {
							
							Date obsDate = diseaseStatus.getObsDatetime();
							
							if (tbScreening.equals(diseaseStatus.getConcept())  && obsDate.after(startDate) && obsDate.before(endDate)) {
								
								hivAndTBScreened = true;
								break;
							}
						}
					}

					ret.put(ptId, new BooleanResult(hivAndTBScreened, this, context));
				}
				return ret;
	}
}