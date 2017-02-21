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
public class ArtMetadata extends AbstractMetadataBundle {

	public static final class _EncounterType {
		public static final String INITIATE_ART = "0cb4417d-b98d-4265-92aa-c6ee3d3bb317";
		public static final String STOP_ART = "3a4b0a86-56de-4b49-ace9-75b1dc6bc34f";
	}

	public static final class _Form {
		public static final String INITIATE_ART = "5f1526f6-64cd-4a90-b4ad-24bb9d2d8709";
		public static final String STOP_ART = "b55b8698-b1df-4c4a-83fb-efdcca4e6159";
	}
    /*
	public static final class _PatientIdentifierType {
		public static final String UNIQUE_PATIENT_NUMBER = Metadata.IdentifierType.UNIQUE_PATIENT_NUMBER;
	}*/

	public static final class _Program {
		public static final String ART = Metadata.Program.ART;
	}

	/**
	 * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		install(encounterType("ART", "Initiate ART FOR Patient", _EncounterType.INITIATE_ART));
		install(encounterType("Stop ART", "Stop ART for Patient", _EncounterType.STOP_ART));

		install(form("ART", null, _EncounterType.INITIATE_ART, "1", _Form.INITIATE_ART));
		install(form("Stop ART", null, _EncounterType.STOP_ART, "1", _Form.STOP_ART));

		//install(patientIdentifierType("Unique Patient Number", "Assigned to every HIV patient", "\\d+", "Facility code followed by sequential number",
		//		null, LocationBehavior.NOT_USED, false, _PatientIdentifierType.UNIQUE_PATIENT_NUMBER));

		install(program("ART", "ART for patients", Dictionary.ART_PROGRAM, _Program.ART));
	}
}