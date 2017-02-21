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
 * HCV metadata bundle
 */
@Component
@Requires({ CommonMetadata.class })
public class HcvMetadata extends AbstractMetadataBundle {

	public static final class _EncounterType {
		public static final String HCV_ENROLLMENT = "ca819871-00b2-4458-9649-91e350544e4f";
		public static final String SCREENING_REGISTRATION = "f6b9c4b9-28c2-4f85-957b-909ca6bc6e60";
		public static final String SCREENING_RESULTS = "59fca8b5-18f9-447f-a4d6-cb472bedf4d7";
		
		public static final String VIRAL_LOAD_ENROLLMENT = "7b9232f1-d665-4812-8627-150f47f01318";
		
		public static final String HCV_TREATMENT = "e93797be-4412-4a31-8ae9-f1b8328f62a3";
		public static final String HCV_FOLLOWUP = "987bf3bf-5023-40e5-802c-8a50af89a842";
		public static final String HCV_DISCONTINUATION = "f7b00f63-5570-42d3-94bd-8714d56e834c";
	}

	public static final class _Form {
		public static final String FAMILY_HISTORY = Metadata.Form.HIV_FAMILY_HISTORY;
		
		public static final String HCV_ENROLLMENT = "766b98f9-602a-48b9-ac06-6905a83211df";
		public static final String SCREENING_REGISTRATION = "9501314d-1b82-4dde-9b2a-691cec8cbeb7";
		// only ok above
		
		
	}
		

	public static final String MOH_257_FACE_PAGE = "47814d87-2e53-45b1-8d05-ac2e944db64c";

	public static final class _PatientIdentifierType {
		public static final String UNIQUE_PATIENT_NUMBER = Metadata.IdentifierType.HEP_C_REGISTRATION_NUMBER;
	}

	public static final class _Program {
		public static final String HCV = Metadata.Program.HCV;
	}
	public static final class _Programs {
		public static final String HCV_TREATMENT = Metadata.Program.HCV;
	}
	
	/**
	 * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		
		install(encounterType("HCV Enrollment", "Enrollment onto HCV program", _EncounterType.HCV_ENROLLMENT));
		
		install(encounterType("HCV Screening Registration", "Enrollment for Screening Register", _EncounterType.SCREENING_REGISTRATION));
		install(encounterType("HCV Screening Results", "Enrollment for HCV Screening Results", _EncounterType.SCREENING_RESULTS));
		
		install(encounterType("Viral Load Enrollment", "Registration for Positive Viral Load Detected Patients", _EncounterType.VIRAL_LOAD_ENROLLMENT));
		install(form("HCV Enrollment", null, _EncounterType.HCV_ENROLLMENT, "1", _Form.HCV_ENROLLMENT));
		install(form("HCV Screening Registration", null, _EncounterType.SCREENING_REGISTRATION, "1", _Form.SCREENING_REGISTRATION));
		
		/*
		install(encounterType("HCV Treatment", "", _EncounterType.HCV_TREATMENT));
		install(encounterType("HCV Followup", "", _EncounterType.HCV_FOLLOWUP));
		install(encounterType("HCV Discontinuation", "Stop HCV Treatment", _EncounterType.HCV_DISCONTINUATION));

		
		install(form("HCV Enrollment", null, _EncounterType.HCV_ENROLLMENT, "1", _Form.HCV_ENROLLMENT));
		
		//temp remark
		//install(form("Family History", null, CommonMetadata._EncounterType.REGISTRATION, "1", _Form.FAMILY_HISTORY));
		
		
		//install(form("MOH 257 Face Page", null, _EncounterType.HIV_ENROLLMENT, "1", _Form.MOH_257_FACE_PAGE));
		//install(form("MOH 257 ARV Therapy", null, _EncounterType.HIV_ENROLLMENT, "1", _Form.MOH_257_ARV_THERAPY));
		//install(form("MOH 257 Visit Summary", null, _EncounterType.HIV_CONSULTATION, "1", _Form.MOH_257_VISIT_SUMMARY));
		install(form("End of follow up", null, _EncounterType.HCV_DISCONTINUATION, "1", _Form.HCV_DISCONTINUATION));
		
		//temp remark
		//install(form("Personal History", null, _EncounterType.HCV_ENROLLMENT, "1", _Form.HIV_PERSONAL_HISTORY));
		//install(form("Drug History", null, _EncounterType.HCV_ENROLLMENT, "1", _Form.HIV_DRUG_HISTORY));

		install(patientIdentifierType("Unique Patient Number", "Assigned to every HCV patient", "\\d+", "Facility code followed by sequential number",
				null, LocationBehavior.NOT_USED, false, _PatientIdentifierType.UNIQUE_PATIENT_NUMBER));

		//temp remark
		install(program("HCV", "Treatment for HCV Viral Load positive patients", Dictionary.HCV_PROGRAM, _Program.HCV));
		*/
	}
}