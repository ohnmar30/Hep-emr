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

package org.openmrs.module.chaiemr.wrapper;

import java.util.Date;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.module.chaicore.wrapper.AbstractPatientWrapper;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.chaiemr.metadata.HivMetadata;

/**
 * Wrapper class for patients. Unfortunately this can't extend both AbstractPatientWrapper and PersonWrapper so we add a
 * PersonWrapper as a property.
 */
public class PatientWrapper extends AbstractPatientWrapper {

	private PersonWrapper person;

	/**
	 * Creates a new wrapper
	 * @param target the target
	 */
	public PatientWrapper(Patient target) {
		super(target);

		this.person = new PersonWrapper(target);
	}

	/**
	 * Gets the person wrapper
	 * @return the wrapper
	 */
	public PersonWrapper getPerson() {
		return person;
	}

	/**
	 * Gets the medical record number
	 * @return the identifier value
	 */
	public String getMedicalRecordNumber() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.OPENMRS_ID);
	}

	/**
	 * Gets the patient clinic number
	 * @return the identifier value
	 */
	public String getPatientClinicNumber() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.PATIENT_CLINIC_NUMBER);
	}
	public void setPatientClinicNumber(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.PATIENT_CLINIC_NUMBER, value, location);
	}	

	/**
	 * Sets the patient clinic number
	 * @param value the Master Patient Index
	 * @param location the identifier location
	 */
	public void setMasterPatientIndex(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.MASTER_PATIENT_INDEX, value, location);
	}	
	
	/**
	 * Gets the Master Patient Index
	 * @return the identifier value
	 */
	
	public String getMasterPatientIndex() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.MASTER_PATIENT_INDEX);
	}	
	
	
	/**
	 * Sets the patient clinic number
	 * @param value the Master Patient Index
	 * @param location the identifier location
	 */
	public void setRegistrationNumber(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.REGISTRATION_NUMBER, value, location);
	}
	/**
	 * Gets the Registration Number
	 * @return the identifier value
	 */
	public String getRegistrationNumber() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.REGISTRATION_NUMBER);
	}

	/**
	 * Sets the Hep C Registration Number
	 * @param value the identifier value
	 * @param location the identifier location
	 */	
	public void setHepCRegistrationNumber(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.HEP_C_REGISTRATION_NUMBER, value, location);
	}

	/**
	 * Gets the Hep C Registration Number
	 * @return the identifier value
	 */
	public String getHepCRegistrationNumber() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.HEP_C_REGISTRATION_NUMBER);
	}
	
	/**
	 * Sets the SystemPatientId
	 * @param value the identifier value
	 * @param location the identifier location
	 */	
	public void setSystemPatientId(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.SYSTEM_PATIENT_ID, value, location);
	}

	/**
	 * Gets the SystemPatientId
	 * @return the identifier value
	 */
	public String getSystemPatientId() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.SYSTEM_PATIENT_ID);
	}
	
		
	/**
	 * Sets the SystemPatientId
	 * @param value the identifier value
	 * @param location the identifier location
	 */	
	public void setOpdNumber(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.OPD_NUMBER, value, location);
	}

	/**
	 * Gets the SystemPatientId
	 * @return the identifier value
	 */
	public String getOpdNumber() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.OPD_NUMBER);
	}
	
		

	/**
	 * Sets the Nap Art Registration Number
	 * @param value the identifier value
	 * @param location the identifier location
	 */
	/*
	public void setNapArtRegistrationNumber(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.NAP_ART_REGISTRATION_NUMBER, value, location);
	}
	 */

	/**
	 * Gets the unique patient number
	 * @return the identifier value
	 */
	public String getUniquePatientNumber() {
		return getAsIdentifier(HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
	}

	/**
	 * Sets the unique patient number
	 * @param value the identifier value
	 * @param location the identifier location
	 */
	public void setUniquePatientNumber(String value, Location location) {
		setAsIdentifier(HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER, value, location);
	}

	/**
	 * Gets the national id number
	 * @return the identifier value
	 */
	public String getNationalIdNumber() {
		return getAsIdentifier(CommonMetadata._PatientIdentifierType.NATIONAL_ID);
	}

	/**
	 * Sets the national id number
	 * @param value the identifier value
	 * @param location the identifier location
	 */
	public void setNationalIdNumber(String value, Location location) {
		setAsIdentifier(CommonMetadata._PatientIdentifierType.NATIONAL_ID, value, location);
	}

	/**
	 * Gets the address of next of kin
	 * @return the address
	 */
	public String getNextOfKinAddress() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_ADDRESS);
	}

	/**
	 * Sets the address of next of kin
	 * @param value the address
	 */
	public void setNextOfKinAddress(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_ADDRESS, value);
	}

	/**
	 * Gets the telephone contact of next of kin
	 * @return the telephone number
	 */
	public String getNextOfKinContact() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_CONTACT);
	}

	/**
	 * Sets the telephone contact of next of kin
	 * @param value telephone number
	 */
	public void setNextOfKinContact(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_CONTACT, value);
	}

	/**
	 * Gets the name of next of kin
	 * @return the name
	 */
	public String getNextOfKinName() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_NAME);
	}

	/**
	 * Sets the name of next of kin
	 * @param value the name
	 */
	public void setNextOfKinName(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_NAME, value);
	}

	/**
	 * Gets the relationship of next of kin
	 * @return the relationship
	 */
	public String getNextOfKinRelationship() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP);
	}

	/**
	 * Sets the relationship of next of kin
	 * @param value the relationship
	 */
	public void setNextOfKinRelationship(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.NEXT_OF_KIN_RELATIONSHIP, value);
	}

	/**
	 * Gets the sub chief name
	 * @return the name
	 */
	public String getSubChiefName() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.SUBCHIEF_NAME);
	}
	/**
	 * Sets the sub chief name
	 * @param value the name
	 */
	public void setSubChiefName(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.SUBCHIEF_NAME, value);
	}
	
	/**
	 * Gets the previous clinic name
	 * @return the name
	 */
	public String getPreviousClinicName() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.PREVIOUS_CLINIC_NAME);
	}

	/**
	 * Sets the previous clinic name
	 * @param value the name
	 */
	public void setPreviousClinicName(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.PREVIOUS_CLINIC_NAME, value);
	}
	
	/**
	 * Gets the previous hiv test performed status
	 * @return the name
	 */
	public String getPreviousHivTestStatus() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.HIV_TEST_PERFORMED);
	}

	/**
	 * Sets the previous hiv test performed status
	 * @param value the name
	 */
	public void setPreviousHivTestStatus(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.HIV_TEST_PERFORMED, value);
	}
	
	/**
	 * Gets the previous hiv test performed place
	 * @return the name
	 */
	public String getPreviousHivTestPlace() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.HIV_TEST_PERFORMED_PLACE);
	}

	/**
	 * Sets the previous hiv test performed place
	 * @param value the name
	 */
	public void setPreviousHivTestPlace(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.HIV_TEST_PERFORMED_PLACE, value);
	}
	
	/**
	 * Gets the previous hiv test performed date
	 * @return the date
	 */
	public String getPreviousHivTestDate() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.HIV_TEST_PERFORMED_DATE);
	}

	/**
	 * Sets the previous hiv test performed date
	 * @param value the date
	 */
	public void setPreviousHivTestDate(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.HIV_TEST_PERFORMED_DATE, value);
	}
	
	public String getFatherName() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.FATHER_NAME);
	}

	public void setFatherName(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.FATHER_NAME, value);
	}
	
	public String getNationalId() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.NATIONAL_ID);
	}

	public void setNationalId(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.NATIONAL_ID, value);
	}
	
	public String getPlaceOfBirth() {
		return getAsAttribute(CommonMetadata._PersonAttributeType.PLACE_OF_BIRTH);
	}

	public void setPlaceOfBirth(String value) {
		setAsAttribute(CommonMetadata._PersonAttributeType.PLACE_OF_BIRTH, value);
	}
}