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

package org.openmrs.module.chaiemr.metadata;

import org.openmrs.PatientIdentifierType.LocationBehavior;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.Metadata;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.Requires;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;

/**
 * HIV metadata bundle
 */
@Component
@Requires({ CommonMetadata.class })
public class HivMetadata extends AbstractMetadataBundle {

	public static final class _EncounterType {
		public static final String HIV_CONSULTATION = "a0034eee-1940-4e35-847f-97537a35d05e";
		public static final String HIV_DISCONTINUATION = "2bdada65-4c72-4a48-8730-859890e25cee";
		public static final String HIV_ENROLLMENT = "de78a6be-bfc5-4634-adc3-5f1a280455cc";
	}

	public static final class _Form {
		public static final String FAMILY_HISTORY = Metadata.Form.HIV_FAMILY_HISTORY;
		public static final String HIV_DISCONTINUATION = "e3237ede-fa70-451f-9e6c-0908bc39f8b9";
		public static final String HIV_ENROLLMENT = "e4b506c1-7379-42b6-a374-284469cba8da";
		public static final String MOH_257_FACE_PAGE = "47814d87-2e53-45b1-8d05-ac2e944db64c";
		public static final String MOH_257_ARV_THERAPY = "8f5b3ba5-1677-450f-8445-33b9a38107ae";
		public static final String MOH_257_VISIT_SUMMARY = "23b4ebbd-29ad-455e-be0e-04aa6bc30798";
		public static final String HIV_PERSONAL_HISTORY = "d1db31d0-b415-4788-a233-e4000bf4d108";
		public static final String HIV_DRUG_HISTORY = "5286ae88-85bb-46e8-a2f7-6361f463ffd4";
	}
	
	

	public static final class _PatientIdentifierType {
		public static final String UNIQUE_PATIENT_NUMBER = Metadata.IdentifierType.UNIQUE_PATIENT_NUMBER;
	}

	public static final class _Program {
		public static final String HIV = Metadata.Program.HIV;
	}
	public static final class _Programs {
		public static final String ART = Metadata.Program.ART;
	}
	
	/**
	 * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		install(encounterType("HIV Enrollment", "Enrollment onto HIV program", _EncounterType.HIV_ENROLLMENT));
		install(encounterType("HIV Consultation", "Collection of HIV-specific data during the main consultation", _EncounterType.HIV_CONSULTATION));
		install(encounterType("HIV Discontinuation", "Discontinuation from HIV program", _EncounterType.HIV_DISCONTINUATION));

		install(form("HIV Enrollment", null, _EncounterType.HIV_ENROLLMENT, "1", _Form.HIV_ENROLLMENT));
		install(form("Family History", null, CommonMetadata._EncounterType.REGISTRATION, "1", _Form.FAMILY_HISTORY));
		install(form("MOH 257 Face Page", null, _EncounterType.HIV_ENROLLMENT, "1", _Form.MOH_257_FACE_PAGE));
		install(form("MOH 257 ARV Therapy", null, _EncounterType.HIV_ENROLLMENT, "1", _Form.MOH_257_ARV_THERAPY));
		install(form("MOH 257 Visit Summary", null, _EncounterType.HIV_CONSULTATION, "1", _Form.MOH_257_VISIT_SUMMARY));
		install(form("End of follow up", null, _EncounterType.HIV_DISCONTINUATION, "1", _Form.HIV_DISCONTINUATION));
		install(form("Personal History", null, _EncounterType.HIV_ENROLLMENT, "1", _Form.HIV_PERSONAL_HISTORY));
		install(form("Drug History", null, _EncounterType.HIV_ENROLLMENT, "1", _Form.HIV_DRUG_HISTORY));

		install(patientIdentifierType("Unique Patient Number", "Assigned to every HIV patient", "\\d+", "Facility code followed by sequential number",
				null, LocationBehavior.NOT_USED, false, _PatientIdentifierType.UNIQUE_PATIENT_NUMBER));

		install(program("HIV", "Treatment for HIV-positive patients", Dictionary.HIV_PROGRAM, _Program.HIV));
		
	}
}