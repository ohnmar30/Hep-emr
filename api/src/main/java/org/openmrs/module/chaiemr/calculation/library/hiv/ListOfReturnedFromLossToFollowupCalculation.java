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

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Program;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.Calculations;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaicore.calculation.PatientFlagCalculation;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.HivConstants;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.openmrs.module.chaiemr.calculation.EmrCalculationUtils.daysSince;

/**
 * Calculates whether a patient has been lost to follow up and returned after LOST_TO_FOLLOW_UP_THRESHOLD_DAYS. Calculation returns true if patient
 * is alive, enrolled in the HIV program, has a FOLLOW_UP, but have an encounter after LOST_TO_FOLLOW_UP_THRESHOLD_DAYS days
 */
public class ListOfReturnedFromLossToFollowupCalculation extends AbstractPatientCalculation {

	/**
	 * Evaluates the calculation
	 * @should calculate false for deceased patients
	 * @should calculate false for patients not in HIV program
	 * @should calculate false for patients with an encounter in last LOST_TO_FOLLOW_UP_THRESHOLD_DAYS days days since appointment date
	 * @should calculate true for patient with any encounters after LOST_TO_FOLLOW_UP_THRESHOLD_DAYS days since appointment date
	 */
	@Override
	public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> arg1, PatientCalculationContext context) {

		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
		Concept reasonForDiscontinuation = Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
		Concept transferout = Dictionary.getConcept(Dictionary.TRANSFERRED_OUT);

		Set<Integer> alive = Filters.alive(cohort, context);
		Set<Integer> inHivProgram = Filters.inProgram(hivProgram, alive, context);

		CalculationResultMap lastEncounters = Calculations.lastEncounter(null, inHivProgram, context);
		CalculationResultMap lastReturnDateObss = Calculations.lastObs(Dictionary.getConcept(Dictionary.RETURN_VISIT_DATE), inHivProgram, context);
		CalculationResultMap lastProgramDiscontinuation = Calculations.lastObs(reasonForDiscontinuation, cohort, context);

		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			boolean lostAndReturned = false;

			// Is patient alive and in the HIV program
			if (inHivProgram.contains(ptId)) {

				// Patient is lost to follow up and returned if encounters exist after X days from RETURN_VISIT_DATE;
				Date lastScheduledReturnDate = EmrCalculationUtils.datetimeObsResultForPatient(lastReturnDateObss, ptId);
				Date lastEncounterDate = EmrCalculationUtils.encounterResultForPatient(lastEncounters, ptId).getDateCreated();
				Obs discontuation = EmrCalculationUtils.obsResultForPatient(lastProgramDiscontinuation, ptId);
				
				if (lastScheduledReturnDate != null) {
					if(daysSince(lastScheduledReturnDate, context) - daysSince(lastEncounterDate, context) > HivConstants.LOST_TO_FOLLOW_UP_THRESHOLD_DAYS){
						lostAndReturned = true;
					}
					if(discontuation != null && discontuation.getValueCoded().equals(transferout)) {
						lostAndReturned = false;
					}
				}

			}
			ret.put(ptId, new SimpleResult(lostAndReturned, this, context));

		}
		return ret;
	}
}
