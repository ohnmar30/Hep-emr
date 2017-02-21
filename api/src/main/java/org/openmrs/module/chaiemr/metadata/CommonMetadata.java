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

import java.util.Date;

import org.openmrs.PatientIdentifierType.LocationBehavior;
import org.openmrs.PersonAttributeType;
import org.openmrs.module.idgen.validator.LuhnMod25IdentifierValidator;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.Metadata;
import org.openmrs.module.chaiemr.datatype.FormDatatype;
import org.openmrs.module.chaiemr.datatype.LocationDatatype;
import org.openmrs.module.chaiemr.metadata.HivMetadata._EncounterType;
import org.openmrs.module.chaiemr.metadata.HivMetadata._Form;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.*;

/**
 * Common metadata bundle
 */
@Component
public class CommonMetadata extends AbstractMetadataBundle {

	public static final class _EncounterType {
		public static final String CONSULTATION = "465a92f2-baf8-42e9-9612-53064be868e8";
		public static final String LAB_RESULTS = "17a381d1-7e29-406a-b782-aa903b963c28";
		public static final String REGISTRATION = "de1f9d67-b73e-4e1b-90d0-036166fc6995";
		public static final String TRIAGE = "d1059fb9-a079-4feb-a749-eedd709ae542";
		public static final String TB_SCREENING = "ed6dacc9-0827-4c82-86be-53c0d8c449be";
		public static final String LAB_ORDERS = "839cc70b-09cf-4d20-8bf5-b19dddde9e32";
		public static final String DISPENSE_DRUG= "0daa76ea-9c7d-49c9-8ea0-21d3d079f807";
		public static final String REGIMEN_ORDER= "00d1b629-4335-4031-b012-03f8af3231f8";
	}

	public static final class _Form {
		public static final String CONSULTATION_ENCOUNTER = "bd598114-4ef4-47b1-a746-a616180ccfc0";
		public static final String CLINICAL_ENCOUNTER = Metadata.Form.CLINICAL_ENCOUNTER;
		public static final String LAB_RESULTS = Metadata.Form.LAB_RESULTS;
		public static final String OBSTETRIC_HISTORY = Metadata.Form.OBSTETRIC_HISTORY;
		public static final String OTHER_MEDICATIONS = Metadata.Form.OTHER_MEDICATIONS;
		public static final String HOSPITAL_DISCHARGE_FORM = Metadata.Form.HOSPITAL_DISCHARGE_FORM;
		public static final String SURGICAL_AND_MEDICAL_HISTORY = Metadata.Form.SURGICAL_AND_MEDICAL_HISTORY;
		public static final String TRIAGE = Metadata.Form.TRIAGE;
		public static final String TB_SCREENING = Metadata.Form.TB_SCREENING;
		public static final String LAB_ORDERS = Metadata.Form.LAB_ORDERS;
		public static final String DRUG_DISPENSING = Metadata.Form.DRUG_DISPENSING;
	}

	public static final class _OrderType {
		public static final String DRUG = "131168f4-15f5-102d-96e4-000c29c2a5d7";
	}

	public static final class _PatientIdentifierType {
		public static final String NATIONAL_ID = Metadata.IdentifierType.NATIONAL_ID;
		//public static final String OLD_ID = Metadata.IdentifierType.OLD;
		public static final String OPENMRS_ID = Metadata.IdentifierType.MEDICAL_RECORD_NUMBER;
		public static final String PATIENT_CLINIC_NUMBER = Metadata.IdentifierType.PATIENT_CLINIC_NUMBER;
		public static final String MASTER_PATIENT_INDEX = Metadata.IdentifierType.MASTER_PATIENT_INDEX;
		public static final String REGISTRATION_NUMBER = Metadata.IdentifierType.REGISTRATION_NUMBER;
		public static final String HEP_C_REGISTRATION_NUMBER = Metadata.IdentifierType.HEP_C_REGISTRATION_NUMBER;
		public static final String SYSTEM_PATIENT_ID = Metadata.IdentifierType.SYSTEM_PATIENT_ID;
		public static final String OPD_NUMBER = Metadata.IdentifierType.OPD_NUMBER;
	}

	public static final class _PersonAttributeType {
		public static final String FATHER_NAME = "20d98e55-796c-4d41-ae07-fca1bcc1a10f";
		public static final String NATIONAL_ID = "757c803e-98ab-43f7-9932-292703a1a5aa";
		public static final String PLACE_OF_BIRTH = "2e5e901c-fe07-45c6-b355-d394fc1917ed";
		public static final String NEXT_OF_KIN_ADDRESS = "7cf22bec-d90a-46ad-9f48-035952261294";
		public static final String NEXT_OF_KIN_CONTACT = "342a1d39-c541-4b29-8818-930916f4c2dc";
		public static final String NEXT_OF_KIN_NAME = "830bef6d-b01f-449d-9f8d-ac0fede8dbd3";
		public static final String NEXT_OF_KIN_RELATIONSHIP = "d0aa9fd1-2ac5-45d8-9c5e-4317c622c8f5";
		public static final String SUBCHIEF_NAME = "40fa0c9c-7415-43ff-a4eb-c7c73d7b1a7a";
		public static final String TELEPHONE_CONTACT = "b2c38640-2603-4629-aebd-3b54f33f1e3a";
		public static final String EMAIL_ADDRESS = "b8d0b331-1d2d-4a9a-b741-1816f498bdb6";
		public static final String PREVIOUS_CLINIC_NAME = "200eac3d-30ef-4a84-b463-643a3215a634";
		public static final String  HIV_TEST_PERFORMED = "496fae44-0f1c-40e0-be46-eeb7bb915a17";
		public static final String  HIV_TEST_PERFORMED_PLACE = "9791c7e0-aedc-442c-a122-da1aae9ea53a";
		public static final String  HIV_TEST_PERFORMED_DATE = "dc107e00-2f24-4f55-a23c-c7b733b5387f";
		
	}

	public static final class _Provider {
		public static final String UNKNOWN = "ae01b8ff-a4cc-4012-bcf7-72359e852e14";
	}

	public static final class _RelationshipType {
		public static final String PARTNER = "d6895098-5d8d-11e3-94ee-b35a4132a5e3";
		public static final String GUARDIAN_DEPENDANT = "5f115f62-68b7-11e3-94ee-6bef9086de92";
	}

	public static final class _VisitAttributeType {
		public static final String SOURCE_FORM = "8bfab185-6947-4958-b7ab-dfafae1a3e3d";
		public static final String NEW_PATIENT = "57c86d59-5cad-47b8-9015-4551a4e20cec";
	}

	public static final class _VisitType {
		public static final String OUTPATIENT = "3371a4d4-f66f-4454-a86d-92c7b3da990c";
		public static final String NEW_PATIENT = "e5eae353-3b3a-4312-8a3b-d75d553547ec";
	}

	/**
	 * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
	 */
	@Override
	public void install() {
		install(encounterType("Consultation", "Collection of clinical data during the main consultation", _EncounterType.CONSULTATION));
		install(encounterType("TB Screening", "Screening of patient for TB", _EncounterType.TB_SCREENING));
		install(encounterType("Lab Results", "Collection of laboratory results", _EncounterType.LAB_RESULTS));
		install(encounterType("Registration", "Initial data collection for a patient, not specific to any program", _EncounterType.REGISTRATION));
		install(encounterType("Record Vitals", "Collection of limited vital data for better examination", _EncounterType.TRIAGE));
		install(encounterType("Lab Order", "Lab Tests Order", _EncounterType.LAB_ORDERS));
		install(encounterType("Drug Dispensing", "Drug dispensed to patient", _EncounterType.DISPENSE_DRUG));
		install(encounterType("Regimen Order", "Regimen Order for a patient", _EncounterType.REGIMEN_ORDER));
		
		
		install(form("Consultation", null, _EncounterType.CONSULTATION, "1", _Form.CONSULTATION_ENCOUNTER));
		install(form("Clinical Encounter", null, _EncounterType.CONSULTATION, "1", _Form.CLINICAL_ENCOUNTER));
		install(form("TB, OI and Staging Form", null, _EncounterType.CONSULTATION, "1", _Form.TB_SCREENING));
		install(form("Lab Results", null, _EncounterType.LAB_RESULTS, "1", _Form.LAB_RESULTS));
		install(form("Obstetric History", null, _EncounterType.REGISTRATION, "1", _Form.OBSTETRIC_HISTORY));
		install(form("Other Medications", "Recording of non-regimen medications", _EncounterType.CONSULTATION, "1", _Form.OTHER_MEDICATIONS));
		install(form("Hospital Discharge Form", "For additional information for admitted and discharged patients.", _EncounterType.CONSULTATION, "1", _Form.HOSPITAL_DISCHARGE_FORM));
		install(form("Surgical and Medical History", null, _EncounterType.REGISTRATION, "1", _Form.SURGICAL_AND_MEDICAL_HISTORY));
		install(form("Record Vitals", null, _EncounterType.CONSULTATION, "1", _Form.TRIAGE));
		install(form("Lab Order", null, _EncounterType.LAB_ORDERS, "1", _Form.LAB_ORDERS));
		install(form("Dispense Drug", "Dispense Drug in dispensing module", _EncounterType.DISPENSE_DRUG, "1", _Form.DRUG_DISPENSING));

		install(globalProperty(EmrConstants.GP_DEFAULT_LOCATION, "The facility for which this installation is configured",
				LocationDatatype.class, null, null));

		
		
		install(patientIdentifierType("OpenMRS ID", "Medical Record Number generated by OpenMRS for every patient",
				null, null, LuhnMod25IdentifierValidator.class,
				LocationBehavior.REQUIRED, true, _PatientIdentifierType.OPENMRS_ID));
		install(patientIdentifierType("Patient Clinic Number", "Assigned to the patient at a clinic service (not globally unique)",
				".{1,15}", "At most 15 characters long", null,
				LocationBehavior.REQUIRED, false, _PatientIdentifierType.PATIENT_CLINIC_NUMBER));
		install(patientIdentifierType("National ID", "Myanmar national identity card number",
				"\\d{5,10}", "Between 5 and 10 consecutive digits", null,
				LocationBehavior.NOT_USED, false, _PatientIdentifierType.NATIONAL_ID));
		
		//temp remark by ohnmar
		
		install(patientIdentifierType("OPD Registration Number", "Opd number",
				null, null, null,
				LocationBehavior.NOT_USED, false, _PatientIdentifierType.OPD_NUMBER));
		 
		
		install(personAttributeType("Telephone contact", "Telephone contact number",
				String.class, null, false, 1.0, _PersonAttributeType.TELEPHONE_CONTACT));
		install(personAttributeType("Email address", "Email address of person",
				String.class, null, false, 2.0, _PersonAttributeType.EMAIL_ADDRESS));

		// Patient only person attributes..
		install(personAttributeType("Subchief name", "Name of subchief or chief of patient's area",
				String.class, null, false, 3.0, _PersonAttributeType.SUBCHIEF_NAME));
		install(personAttributeType("Treatment Supporter's Name", "Name of patient's next of kin",
				String.class, null, false, 4.0, _PersonAttributeType.NEXT_OF_KIN_NAME));
	
		install(personAttributeType("Previous Clinic Name", "Name of previous clinic trnsferred from for patient",
				String.class, null, false, 4.0, _PersonAttributeType.PREVIOUS_CLINIC_NAME));
		install(personAttributeType("Previous HIV test performed", "Patient performed any HIV test previously",
				String.class, null, false, 4.0, _PersonAttributeType.HIV_TEST_PERFORMED));
		install(personAttributeType("Previous HIV test performed place", "Place of patient's HIV test previously performed",
				String.class, null, false, 4.0, _PersonAttributeType.HIV_TEST_PERFORMED_PLACE));
		install(personAttributeType("Previous HIV test performed date", "Date of patient's HIV test previously performed",
				Date.class, null, false, 4.0, _PersonAttributeType.HIV_TEST_PERFORMED_DATE));
		
		
		
		install(personAttributeType("Treatment Supporter's Relationship", "Next of kin relationship to the patient",
				String.class, null, false, 4.1, _PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP));
		install(personAttributeType("Treatment Supporter's Contact", "Telephone contact of patient's next of kin",
				String.class, null, false, 4.2, _PersonAttributeType.NEXT_OF_KIN_CONTACT));
		install(personAttributeType("Treatment Supporter's Address", "Address of patient's next of kin",
				String.class, null, false, 4.3, _PersonAttributeType.NEXT_OF_KIN_ADDRESS));
		install(personAttributeType("Father's Name", "First or last name of this person's father",
				String.class, null, false, 4.4, _PersonAttributeType.FATHER_NAME));
		install(personAttributeType("National ID", "National Id for the patient",
				String.class, null, false, 4.5, _PersonAttributeType.NATIONAL_ID));
		install(personAttributeType("Place of Birth", "Place of Birth for the patient",
				String.class, null, false, 4.6, _PersonAttributeType.PLACE_OF_BIRTH));

		install(relationshipType("Guardian", "Dependant", "One that guards, watches over, or protects", _RelationshipType.GUARDIAN_DEPENDANT));
		install(relationshipType("Partner", "Partner", "A spouse is a partner in a marriage, civil union, domestic partnership or common-law marriage a male spouse is a husband and a female spouse is a wife", _RelationshipType.PARTNER));

		install(visitAttributeType("Source form", "The form whose submission created the visit",
				FormDatatype.class, null, 0, 1, _VisitAttributeType.SOURCE_FORM));

		install(visitType("Follow up visit", "Visit where the patient is not admitted to the hospital", _VisitType.OUTPATIENT));
		install(visitType("NEW PATIENT", "Visit for first time new Patient", _VisitType.NEW_PATIENT));

		uninstall(possible(PersonAttributeType.class, "73d34479-2f9e-4de3-a5e6-1f79a17459bb"), "Became patient identifier"); // National ID attribute type
	}
}
