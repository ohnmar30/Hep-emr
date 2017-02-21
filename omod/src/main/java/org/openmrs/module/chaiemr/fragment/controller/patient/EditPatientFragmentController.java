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

package org.openmrs.module.chaiemr.fragment.controller.patient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.VisitAttribute;
import org.openmrs.VisitAttributeType;
import org.openmrs.VisitType;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculation;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.ResultUtil;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.calculation.library.RecordedDeceasedCalculation;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.validator.TelephoneNumberValidator;
import org.openmrs.module.chaiemr.wrapper.PatientWrapper;
import org.openmrs.module.chaiemr.wrapper.PersonWrapper;
import org.openmrs.module.chaiui.form.AbstractWebForm;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for creating and editing patients in the registration app
 */
public class EditPatientFragmentController {

	// We don't record cause of death, but data model requires a concept
	private static final String CAUSE_OF_DEATH_PLACEHOLDER = Dictionary.UNKNOWN;

	/**
	 * Main controller method
	 * 
	 * @param patient
	 *            the patient (may be null)
	 * @param person
	 *            the person (may be null)
	 * @param model
	 *            the model
	 */
	public void controller(
			@FragmentParam(value = "patient", required = false) Patient patient,
			@FragmentParam(value = "person", required = false) Person person,
			FragmentModel model) {

		if (patient != null && person != null) {
			throw new RuntimeException(
					"A patient or person can be provided, but not both");
		}

		Person existing = patient != null ? patient : person;

		model.addAttribute("command", newEditPatientForm(existing));

		model.addAttribute("civilStatusConcept",
				Dictionary.getConcept(Dictionary.CIVIL_STATUS));
		model.addAttribute("occupationConcept",
				Dictionary.getConcept(Dictionary.OCCUPATION));
		model.addAttribute("educationConcept",
				Dictionary.getConcept(Dictionary.EDUCATION));
		model.addAttribute("ingoConcept",
				Dictionary.getConcept(Dictionary.INGO_NAME));
		model.addAttribute("enrollmentList",
				Dictionary.getConcept(Dictionary.ENROLLMENT_STATUS));
		model.addAttribute("entryPointList",
				Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT));
		Obs savedEnrollmentSatusConcept;
		savedEnrollmentSatusConcept = getLatestObs(patient,
				Dictionary.ENROLLMENT_STATUS);
		if (savedEnrollmentSatusConcept != null) {
			model.addAttribute("statusother",
					savedEnrollmentSatusConcept.getValueCoded());
		} else {
			model.addAttribute("statusother", 0);
		}

		model.addAttribute("townShipList",
				Dictionary.getConcept(Dictionary.TOWNSHIP));
		
		Obs savedEntryPointConcept;
		savedEntryPointConcept = getLatestObs(patient,
				Dictionary.METHOD_OF_ENROLLMENT);
		if (savedEntryPointConcept != null) {
			model.addAttribute("pointentry",
					savedEntryPointConcept.getValueCoded());
		} else {
			model.addAttribute("pointentry", 0);
		}

		// Create list of education answer concepts
		List<Concept> educationOptions = new ArrayList<Concept>();
		educationOptions.add(Dictionary.getConcept(Dictionary.NONE));
		educationOptions.add(Dictionary
				.getConcept(Dictionary.PRIMARY_EDUCATION));
		educationOptions.add(Dictionary
				.getConcept(Dictionary.SECONDARY_EDUCATION));
		educationOptions.add(Dictionary
				.getConcept(Dictionary.COLLEGE_UNIVERSITY_POLYTECHNIC));
		model.addAttribute("educationOptions", educationOptions);

		// Create a list of marital status answer concepts
		List<Concept> maritalStatusOptions = new ArrayList<Concept>();
		maritalStatusOptions.add(Dictionary
				.getConcept(Dictionary.MARRIED_POLYGAMOUS));
		maritalStatusOptions.add(Dictionary
				.getConcept(Dictionary.MARRIED_MONOGAMOUS));
		maritalStatusOptions.add(Dictionary.getConcept(Dictionary.DIVORCED));
		maritalStatusOptions.add(Dictionary.getConcept(Dictionary.WIDOWED));
		maritalStatusOptions.add(Dictionary
				.getConcept(Dictionary.LIVING_WITH_PARTNER));
		maritalStatusOptions.add(Dictionary
				.getConcept(Dictionary.NEVER_MARRIED));
		model.addAttribute("maritalStatusOptions", maritalStatusOptions);

		if (patient != null) {
			model.addAttribute("recordedAsDeceased",
					hasBeenRecordedAsDeceased(patient));
		} else {
			model.addAttribute("recordedAsDeceased", false);
		}

		// Create a list of cause of death answer concepts
		List<Concept> causeOfDeathOptions = new ArrayList<Concept>();
		causeOfDeathOptions.add(Dictionary.getConcept(Dictionary.UNKNOWN));
		model.addAttribute("causeOfDeathOptions", causeOfDeathOptions);
		
		

		// Algorithm to generate system generated patient Identifier
		Calendar now = Calendar.getInstance();
		String shortName = Context
				.getAdministrationService()
				.getGlobalProperty(
						OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_IDENTIFIER_PREFIX);

		String noCheck = shortName
				+ String.valueOf(now.get(Calendar.YEAR)).substring(2, 4)
				+ String.valueOf(now.get(Calendar.MONTH) + 1)
				+ String.valueOf(now.get(Calendar.DATE))

				+ String.valueOf(now.get(Calendar.HOUR))
				+ String.valueOf(now.get(Calendar.MINUTE))
				+ String.valueOf(now.get(Calendar.SECOND))
				+ String.valueOf(new Random().nextInt(9999 - 999 + 1));

		if (patient != null) {
			PatientWrapper wrapper = new PatientWrapper(patient);
			model.addAttribute("patientIdentifier",
					wrapper.getSystemPatientId());
			model.addAttribute("patientId", wrapper.getTarget().getPatientId());
		} else {
			model.addAttribute("patientIdentifier", noCheck + "-"
					+ generateCheckdigit(noCheck));
			model.addAttribute("patientId", null);
		}
		
		
	}

	private Obs getLatestObs(Patient patient, String conceptIdentifier) {
		Concept concept = Dictionary.getConcept(conceptIdentifier);
		List<Obs> obs = Context.getObsService()
				.getObservationsByPersonAndConcept(patient, concept);
		if (obs.size() > 0) {
			// these are in reverse chronological order
			return obs.get(0);
		}
		return null;
	}

	/**
	 * Checks if a patient has been recorded as deceased by a program
	 * 
	 * @param patient
	 *            the patient
	 * @return true if patient was recorded as deceased
	 */
	protected boolean hasBeenRecordedAsDeceased(Patient patient) {
		PatientCalculation calc = CalculationUtils.instantiateCalculation(
				RecordedDeceasedCalculation.class, null);
		return ResultUtil.isTrue(Context.getService(
				PatientCalculationService.class)
				.evaluate(patient.getId(), calc));
	}

	/**
	 * Using the Luhn Algorithm to generate check digits
	 * 
	 * @param idWithoutCheckdigit
	 * 
	 * @return idWithCheckdigit
	 */
	private static int generateCheckdigit(String input) {
		int factor = 2;
		int sum = 0;
		int n = 10;
		int length = input.length();

		if (!input.matches("[\\w]+"))
			throw new RuntimeException("Invalid character in patient id: "
					+ input);
		// Work from right to left
		for (int i = length - 1; i >= 0; i--) {
			int codePoint = input.charAt(i) - 48;
			// slight openmrs peculiarity to Luhn's algorithm
			int accum = factor * codePoint - (factor - 1)
					* (int) (codePoint / 5) * 9;

			// Alternate the "factor"
			factor = (factor == 2) ? 1 : 2;

			sum += accum;
		}

		int remainder = sum % n;
		return (n - remainder) % n;
	}

	/**
	 * Saves the patient being edited by this form
	 * 
	 * @param form
	 *            the edit patient form
	 * @param ui
	 *            the UI utils
	 * @return a simple object { patientId }
	 */
	public SimpleObject savePatient(
			@MethodParam("newEditPatientForm") @BindParams EditPatientForm form,
			UiUtils ui) {
		ui.validate(form, form, null);

		Patient patient = form.save();

		// if this patient is the current user i need to refresh the current
		// user
		if (patient.getPersonId().equals(
				Context.getAuthenticatedUser().getPerson().getPersonId())) {
			Context.refreshAuthenticatedUser();
		}

		return SimpleObject.create("id", patient.getId());
	}

	/**
	 * Creates an edit patient form
	 * 
	 * @param person
	 *            the person
	 * @return the form
	 */
	public EditPatientForm newEditPatientForm(
			@RequestParam(value = "personId", required = false) Person person) {
		if (person != null && person.isPatient()) {
			return new EditPatientForm((Patient) person); // For editing
															// existing patient
		} else if (person != null) {
			return new EditPatientForm(person); // For creating patient from
												// existing person
		} else {
			return new EditPatientForm(); // For creating patient and person
											// from scratch
		}

	}

	/**
	 * The form command object for editing patients
	 */
	public class EditPatientForm extends AbstractWebForm {

		private Person original;
		private Location location;
		private PersonName personName;
		private Date birthdate;
		private Boolean birthdateEstimated;
		private String gender;
		private PersonAddress personAddress;
		private Concept maritalStatus;
		private Concept occupation;
		private Concept ingoTypeConcept;
		private Concept enrollmentName;
		private Concept entryPoint;
		private Concept education;
		private Obs savedMaritalStatus;
		private Obs savedOccupation;
		private Obs savedIngoTypeConcept;
		private Obs savedEnrollmentNameConcept;
		private Obs savedEntryPoint;
		private Obs savedEducation;
		private Boolean dead = false;
		private Date deathDate;

		// private String nationalIdNumber;
		// private String patientClinicNumber;
		
		//chai update
		//private String artRegistrationNumber;
		//private String preArtRegistrationNumber;
		//private String napArtRegistrationNumber;
		
		private String masterPatientIndex;
		private String registrationNumber;
		private String hepCRegistrationNumber;
		
		private String systemPatientId;
		private String uniquePatientNumber;

		private String telephoneContact;
		private String nameOfNextOfKin;
		private String nextOfKinRelationship;
		private String nextOfKinContact;
		private String nextOfKinAddress;
		private String subChiefName;

		private Integer identifierCount;
		private String fatherName;
		private String otherEntryPoint;
		private String otherStatus;
		private String previousClinicName;
		private Date transferredInDate;
		private String hivTestPerformed;
		private Date hivTestPerformedDate;
		private String hivTestPerformedPlace;
		private String checkInType;

		private String nationalId;
		private Concept placeOfBirth;
		private Obs savedPlaceOfBirth;
		private String dateOfRegistration;

		/**
		 * Creates an edit form for a new patient
		 */
		public EditPatientForm() {
			location = Context.getService(ChaiEmrService.class)
					.getDefaultLocation();

			personName = new PersonName();
			personAddress = new PersonAddress();
		}

		/**
		 * Creates an edit form for an existing patient
		 */
		public EditPatientForm(Person person) {
			this();

			original = person;

			if (person.getPersonName() != null) {
				personName = person.getPersonName();
			} else {
				personName.setPerson(person);
			}

			if (person.getPersonAddress() != null) {
				personAddress = person.getPersonAddress();
			} else {
				personAddress.setPerson(person);
			}

			gender = person.getGender();
			birthdate = person.getBirthdate();
//			birthdateEstimated = person.getBirthdateEstimated();
			dead = person.isDead();
			deathDate = person.getDeathDate();

			PersonWrapper wrapper = new PersonWrapper(person);
			telephoneContact = wrapper.getTelephoneContact();
		}

		/**
		 * Creates an edit form for an existing patient
		 */
		public EditPatientForm(Patient patient) {
			this((Person) patient);

			PatientWrapper wrapper = new PatientWrapper(patient);

			
			
			//chai update
			//preArtRegistrationNumber = wrapper.getPreArtRegistrationNumber();
			//napArtRegistrationNumber = wrapper.getNapArtRegistrationNumber();
			//artRegistrationNumber = wrapper.getArtRegistrationNumber();
			
			masterPatientIndex = wrapper.getMasterPatientIndex();
			registrationNumber = wrapper.getRegistrationNumber();
			hepCRegistrationNumber = wrapper.getHepCRegistrationNumber();
			
			systemPatientId = wrapper.getSystemPatientId();

			//uniquePatientNumber = wrapper.getUniquePatientNumber();

			nameOfNextOfKin = wrapper.getNextOfKinName();
			nextOfKinRelationship = wrapper.getNextOfKinRelationship();
			nextOfKinContact = wrapper.getNextOfKinContact();
			nextOfKinAddress = wrapper.getNextOfKinAddress();
			subChiefName = wrapper.getSubChiefName();
			previousClinicName = wrapper.getPreviousClinicName();
			hivTestPerformed = wrapper.getPreviousHivTestStatus();
			hivTestPerformedPlace = wrapper.getPreviousHivTestPlace();
			fatherName = wrapper.getFatherName();
			nationalId = wrapper.getNationalId();
//			placeOfBirth = wrapper.getPlaceOfBirth();
			
			
		  savedPlaceOfBirth = getLatestObs(patient,
							Dictionary.TOWNSHIP);
				
			
			
			if (savedPlaceOfBirth != null) {
				List<Obs>BirthPlace= Context.getObsService().getObservationsByPersonAndConcept(patient, Dictionary.getConcept(Dictionary.TOWNSHIP));
				for(Obs placeofbirth:BirthPlace)
				{
					if(placeofbirth.getEncounter()==null)
					{
				
				placeOfBirth =placeofbirth.getValueCoded();
					}
					
				}
				
			}
			
			try {
				String datestr = wrapper.getPreviousHivTestDate();
				DateFormat formatter;
				formatter = new SimpleDateFormat("dd-MMMM-yyyy");
				hivTestPerformedDate = (Date) formatter.parse(datestr);
			} catch (Exception e) {
			}

			savedMaritalStatus = getLatestObs(patient, Dictionary.CIVIL_STATUS);
			if (savedMaritalStatus != null) {
				maritalStatus = savedMaritalStatus.getValueCoded();
			}

			savedOccupation = getLatestObs(patient, Dictionary.OCCUPATION);
			if (savedOccupation != null) {
				occupation = savedOccupation.getValueCoded();
			}

			savedIngoTypeConcept = getLatestObs(patient, Dictionary.INGO_NAME);
			if (savedIngoTypeConcept != null) {
				ingoTypeConcept = savedIngoTypeConcept.getValueCoded();
			}

			savedEnrollmentNameConcept = getLatestObs(patient,
					Dictionary.ENROLLMENT_STATUS);
			if (savedEnrollmentNameConcept != null) {
				enrollmentName = savedEnrollmentNameConcept.getValueCoded();

				otherStatus = savedEnrollmentNameConcept.getValueText();

			}

			savedEntryPoint = getLatestObs(patient,
					Dictionary.METHOD_OF_ENROLLMENT);
			if (savedEntryPoint != null) {
				entryPoint = savedEntryPoint.getValueCoded();
				otherEntryPoint = savedEntryPoint.getValueText();

				transferredInDate = savedEntryPoint.getValueDate();
			}

			savedEducation = getLatestObs(patient, Dictionary.EDUCATION);
			if (savedEducation != null) {
				education = savedEducation.getValueCoded();
			}
		}

		/**
		 * @see org.springframework.validation.Validator#validate(java.lang.Object,
		 *      org.springframework.validation.Errors)
		 */
		@Override
		public void validate(Object target, Errors errors) {

			require(errors, "personName.givenName");
			// require(errors, "personName.familyName");

			if (personName.getGivenName().length() > 50) {
				errors.rejectValue("personName.givenName",
						"Expected length of Name is exceeding");
			}
			;

//			require(errors, "fatherName");
			if (fatherName !=null && fatherName.length() > 50) {
				errors.rejectValue("fatherName",
						"Expected length of Name is exceeding");
			}
			;

			if (nationalId.length() > 50) {
				errors.rejectValue("nationalId",
						"Expected length of National Id is exceeding");
			}
			;

/*			if (placeOfBirth.length() > 50) {
				errors.rejectValue("placeOfBirth",
						"Expected length of Birth Place is exceeding");
			}
			;
*/
			if (nameOfNextOfKin.length() > 50) {
				errors.rejectValue("nameOfNextOfKin",
						"Expected length of Name is exceeding");
			}
			;

			if (nextOfKinAddress.length() > 50) {
				errors.rejectValue("nextOfKinAddress",
						"Length of Address is exceeding it's limit");
			}
			;

			if (personAddress.getAddress1().length() > 200) {
				errors.rejectValue("personAddress.address1",
						"Length of Address is exceeding it's limit");
			}
			;

			if (personAddress.getAddress2().length() > 200) {
				errors.rejectValue("personAddress.address2",
						"Length of Address is exceeding it's limit");
			}
			;
			require(errors, "gender");
			require(errors, "birthdate");
			// require(errors, "entryPoint");
			// require(errors, "enrollmentName");
			if (entryPoint != null) {
				if (entryPoint
						.getName()
						.toString()
						.equals("Patient transferred in pre ART from another clinic")
						|| entryPoint
								.getName()
								.toString()
								.equals("Patient transferred in on ART from another HIV care or ART clinic")) {
					require(errors, "previousClinicName");
					require(errors, "transferredInDate");
				}
			}
			require(errors, "hivTestPerformed");

			if (hivTestPerformed != null && hivTestPerformed.equals("Yes")) {
				require(errors, "hivTestPerformedPlace");
				require(errors, "hivTestPerformedDate");

			}

			if (hivTestPerformedDate != null) {
				if (hivTestPerformedDate.after(new Date())) {
					errors.rejectValue("hivTestPerformedDate",
							"Cannot be in the future");
				}
			}

			if (transferredInDate != null) {
				if (transferredInDate.after(new Date())) {
					errors.rejectValue("transferredInDate",
							"Cannot be in the future");
				}
			}

			// Check for Other entry point
			if (entryPoint != null) {
				if (entryPoint.getName().toString().equals("Other")) {
					require(errors, "otherEntryPoint");
				}
			}

			// Check for Other enrollment status
			if (enrollmentName != null) {
				if (enrollmentName.getName().toString().equals("Other")) {
					require(errors, "otherStatus");
				}

				if (enrollmentName.getName().toString().equals("Pregnancy")
						|| enrollmentName.getName().toString()
								.equals("Postpartum")) {
					if (gender.toString().equals("M")) {
						errors.rejectValue("enrollmentName",
								"Cannot be selected for Male patient");
					}

				}
			}

			// Require death details if patient is deceased
			if (dead) {
				require(errors, "deathDate");

				if (deathDate != null) {
					if (birthdate != null && deathDate.before(birthdate)) {
						errors.rejectValue("deathDate",
								"Cannot be before birth date");
					}
					if (deathDate.after(new Date())) {
						errors.rejectValue("deathDate",
								"Cannot be in the future");
					}
				}
			} else if (deathDate != null) {
				errors.rejectValue("deathDate",
						"Must be empty if patient not deceased");
			}

			if (StringUtils.isNotBlank(telephoneContact)) {
				validateField(errors, "telephoneContact",
						new TelephoneNumberValidator());
			}
			if (StringUtils.isNotBlank(nextOfKinContact)) {
				validateField(errors, "nextOfKinContact",
						new TelephoneNumberValidator());
			}

			validateField(errors, "personAddress");

			// validateIdentifierField(errors, "nationalIdNumber",
			// CommonMetadata._PatientIdentifierType.NATIONAL_ID);
			// validateIdentifierField(errors, "patientClinicNumber",
			// CommonMetadata._PatientIdentifierType.PATIENT_CLINIC_NUMBER);
			identifierCount = 0;
			validateIdentifierField(
					errors,
					"hepCRegistrationNumber",
					CommonMetadata._PatientIdentifierType.HEP_C_REGISTRATION_NUMBER);
			validateIdentifierField(
					errors,
					"masterPatientIndex",
					CommonMetadata._PatientIdentifierType.MASTER_PATIENT_INDEX);
			validateIdentifierField(
					errors,
					"registrationNumber",
					CommonMetadata._PatientIdentifierType.REGISTRATION_NUMBER);

			// Check, not more than two identifier number get entered (not
			// required now)
			/*
			 * if(identifierCount > 2){ errors.rejectValue("systemPatientId",
			 * "At max only two registration numbers can be entered."); }
			 */

			// validateIdentifierField(errors, "systemPatientId",
			// CommonMetadata._PatientIdentifierType.SYSTEM_PATIENT_ID);
			// validateIdentifierField(errors, "uniquePatientNumber",
			// HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);

			// Check INGO name is entered, if INGO number is entered
			String value = (String) errors
					.getFieldValue("hepCRegistrationNumber");
			if (!value.isEmpty()) {
				require(errors, "ingoTypeConcept");
			}

			if (ingoTypeConcept != null) {
				require(errors, "hepCRegistrationNumber");
			}

			// check birth date against future dates and really old dates
			if (birthdate != null) {
				if (birthdate.after(new Date()))
					errors.rejectValue("birthdate", "error.date.future");
				else {
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.add(Calendar.YEAR, -120); // person cannot be older than
												// 120 years old
					if (birthdate.before(c.getTime())) {
						errors.rejectValue("birthdate",
								"error.date.nonsensical");
					}
				}
			}
		}

		/**
		 * Validates an identifier field
		 * 
		 * @param errors
		 * @param field
		 * @param idTypeUuid
		 */
		protected void validateIdentifierField(Errors errors, String field,
				String idTypeUuid) {
			String value = (String) errors.getFieldValue(field);

			if (StringUtils.isNotBlank(value)) {
				PatientIdentifierType idType = MetadataUtils.existing(
						PatientIdentifierType.class, idTypeUuid);
				if (!value.matches(idType.getFormat())) {
					errors.rejectValue(field, idType.getFormatDescription());
				}

				PatientIdentifier stub = new PatientIdentifier(value, idType,
						null);

				if (original != null && original.isPatient()) { // Editing an
																// existing
																// patient
					stub.setPatient((Patient) original);
				}

				if (Context.getPatientService()
						.isIdentifierInUseByAnotherPatient(stub)) {
					errors.rejectValue(field, "In use by another patient");
				}
				identifierCount++;
			}
		}

		/**
		 * @see org.openmrs.module.chaiui.form.AbstractWebForm#save()
		 */
		@Override
		public Patient save() {
			Patient toSave;
			boolean isNewPatient = false;

			if (original != null && original.isPatient()) { // Editing an
															// existing patient
				toSave = (Patient) original;
			} else if (original != null) {
				toSave = new Patient(original); // Creating a patient from an
												// existing person
			} else {
				toSave = new Patient(); // Creating a new patient and person
				isNewPatient = true;
			}

			toSave.setGender(gender);
			toSave.setBirthdate(birthdate);
			toSave.setBirthdateEstimated(true);
			toSave.setDead(dead);
			toSave.setDeathDate(deathDate);
			toSave.setCauseOfDeath(dead ? Dictionary
					.getConcept(CAUSE_OF_DEATH_PLACEHOLDER) : null);

			if (anyChanges(toSave.getPersonName(), personName, "givenName")) {
				if (toSave.getPersonName() != null) {
					voidData(toSave.getPersonName());
				}
				personName.setGivenName(personName.getGivenName());
				personName.setFamilyName("(NULL)");
				toSave.addName(personName);
			}

			// toSave.

			if (anyChanges(toSave.getPersonAddress(), personAddress,
					"address1", "address2", "address5", "address6",
					"countyDistrict", "address3", "cityVillage",
					"stateProvince", "country", "postalCode", "address4")) {
				if (toSave.getPersonAddress() != null) {
					voidData(toSave.getPersonAddress());
				}
				toSave.addAddress(personAddress);
			}

			PatientWrapper wrapper = new PatientWrapper(toSave);

			wrapper.getPerson().setTelephoneContact(telephoneContact);
			// wrapper.setNationalIdNumber(nationalIdNumber, location);
			// wrapper.setPatientClinicNumber(patientClinicNumber, location);
			
			// chai update
			/*
			if(!preArtRegistrationNumber.equals("")){
				wrapper.setPreArtRegistrationNumber(preArtRegistrationNumber,
					location);
			}
			if(!artRegistrationNumber.equals("")){
			wrapper.setArtRegistrationNumber(artRegistrationNumber, location);
			}
			if(!napArtRegistrationNumber.equals("")){
			wrapper.setNapArtRegistrationNumber(napArtRegistrationNumber,
					location);
			}
			*/
			
			if(!masterPatientIndex.equals("")){
				wrapper.setMasterPatientIndex(masterPatientIndex,
					location);
			}
			if(!hepCRegistrationNumber.equals("")){
			wrapper.setHepCRegistrationNumber(hepCRegistrationNumber, location);
			}
			if(!registrationNumber.equals("")){
			wrapper.setRegistrationNumber(registrationNumber,
					location);
			}
			
			wrapper.setSystemPatientId(systemPatientId, location);
			//wrapper.setUniquePatientNumber(uniquePatientNumber, location);
			wrapper.setNextOfKinName(nameOfNextOfKin);
			wrapper.setNextOfKinRelationship(nextOfKinRelationship);
			wrapper.setNextOfKinContact(nextOfKinContact);
			wrapper.setNextOfKinAddress(nextOfKinAddress);
			wrapper.setSubChiefName(subChiefName);
			wrapper.setPreviousClinicName(previousClinicName);

			wrapper.setPreviousHivTestStatus(hivTestPerformed);

			if (hivTestPerformed.equals("Yes")) {
				wrapper.setPreviousHivTestPlace(hivTestPerformedPlace);
				DateFormat testDate = new SimpleDateFormat("dd-MMMM-yyyy");
				Date capturedTestDate = hivTestPerformedDate;
				wrapper.setPreviousHivTestDate(testDate
						.format(capturedTestDate));
			}

			wrapper.getPerson().setFatherName(fatherName);
			wrapper.getPerson().setNationalId(nationalId);
//			wrapper.getPerson().setPlaceOfBirth(placeOfBirth);

			// Make sure everyone gets an OpenMRS ID
			PatientIdentifierType openmrsIdType = MetadataUtils.existing(
					PatientIdentifierType.class,
					CommonMetadata._PatientIdentifierType.OPENMRS_ID);
			PatientIdentifier openmrsId = toSave
					.getPatientIdentifier(openmrsIdType);

			if (openmrsId == null) {
				String generated = Context.getService(
						IdentifierSourceService.class).generateIdentifier(
						openmrsIdType, "Registration");
				openmrsId = new PatientIdentifier(generated, openmrsIdType,
						location);
				toSave.addIdentifier(openmrsId);

				if (!toSave.getPatientIdentifier().isPreferred()) {
					openmrsId.setPreferred(true);
				}
			}

			Patient ret = Context.getPatientService().savePatient(toSave);

			// Explicitly save all identifier objects including voided
			for (PatientIdentifier identifier : toSave.getIdentifiers()) {
				Context.getPatientService().savePatientIdentifier(identifier);
			}

			// Save remaining fields as obs
			List<Obs> obsToSave = new ArrayList<Obs>();
			List<Obs> obsToVoid = new ArrayList<Obs>();

			handleOncePerPatientObs(ret, obsToSave, obsToVoid,
					Dictionary.getConcept(Dictionary.TOWNSHIP),
					savedPlaceOfBirth, placeOfBirth);
			handleOncePerPatientObs(ret, obsToSave, obsToVoid,
					Dictionary.getConcept(Dictionary.CIVIL_STATUS),
					savedMaritalStatus, maritalStatus);
			handleOncePerPatientObs(ret, obsToSave, obsToVoid,
					Dictionary.getConcept(Dictionary.OCCUPATION),
					savedOccupation, occupation);
			handleOncePerPatientObs(ret, obsToSave, obsToVoid,
					Dictionary.getConcept(Dictionary.INGO_NAME),
					savedIngoTypeConcept, ingoTypeConcept);
			handleOncePerPatientObs(ret, obsToSave, obsToVoid,
					Dictionary.getConcept(Dictionary.EDUCATION),
					savedEducation, education);

			if (enrollmentName != null) {
				if (enrollmentName.getName().toString().equals("Other")) {
					handleOncePerPatientObs(
							ret,
							obsToSave,
							obsToVoid,
							Dictionary.getConcept(Dictionary.ENROLLMENT_STATUS),
							savedEnrollmentNameConcept, enrollmentName,
							otherStatus, new Date(), 1);
				}

				else {
					handleOncePerPatientObs(
							ret,
							obsToSave,
							obsToVoid,
							Dictionary.getConcept(Dictionary.ENROLLMENT_STATUS),
							savedEnrollmentNameConcept, enrollmentName, null,
							new Date(), 1);
				}
			}

			// With value text and Date
			if (entryPoint != null) {

				if (transferredInDate != null) {
					if (entryPoint.getName().toString().equals("Other")) {
						handleOncePerPatientObs(
								ret,
								obsToSave,
								obsToVoid,
								Dictionary
										.getConcept(Dictionary.METHOD_OF_ENROLLMENT),
								savedEntryPoint, entryPoint, otherEntryPoint,
								transferredInDate, 0);
					} else {
						handleOncePerPatientObs(
								ret,
								obsToSave,
								obsToVoid,
								Dictionary
										.getConcept(Dictionary.METHOD_OF_ENROLLMENT),
								savedEntryPoint, entryPoint, "",
								transferredInDate, 0);
					}
				} else {
					if (entryPoint.getName().toString().equals("Other")) {
						handleOncePerPatientObs(
								ret,
								obsToSave,
								obsToVoid,
								Dictionary
										.getConcept(Dictionary.METHOD_OF_ENROLLMENT),
								savedEntryPoint, entryPoint, otherEntryPoint,
								transferredInDate, 1);
					} else {
						handleOncePerPatientObs(
								ret,
								obsToSave,
								obsToVoid,
								Dictionary
										.getConcept(Dictionary.METHOD_OF_ENROLLMENT),
								savedEntryPoint, entryPoint, "",
								transferredInDate, 1);
					}
				}
			}
			for (Obs o : obsToVoid) {
				Context.getObsService().voidObs(o, "ChaiEMR edit patient");
			}

			for (Obs o : obsToSave) {
				Context.getObsService().saveObs(o, "ChaiEMR edit patient");
			}

			/*
			 * To check in directly
			 */
			List<Encounter> hivEnrollEncTypePrev = Context
					.getEncounterService().getEncountersByPatient(ret);
			Date curDate = new Date();
			if (checkInType.equals("1")) {
/*				Visit visit = new Visit();
				visit.setPatient(ret);
				visit.setStartDatetime(curDate);
				visit.setVisitType(MetadataUtils.existing(VisitType.class,
						CommonMetadata._VisitType.OUTPATIENT));
				visit.setLocation(Context.getService(ChaiEmrService.class)
						.getDefaultLocation());

				if (isNewPatient) {
					VisitAttributeType attrType = Context.getService(
							VisitService.class).getVisitAttributeTypeByUuid(
							CommonMetadata._VisitAttributeType.NEW_PATIENT);
					if (attrType != null) {
						VisitAttribute attr = new VisitAttribute();
						attr.setAttributeType(attrType);
						attr.setVisit(visit);
						attr.setDateCreated(curDate);
						attr.setValue(true);
						visit.addAttribute(attr);
					}
				}
				
				Visit visitSave = Context.getVisitService().saveVisit(visit);

				if (hivEnrollEncTypePrev.isEmpty()) {

					EncounterType hivEnrollEncType = MetadataUtils.existing(
							EncounterType.class,
							HivMetadata._EncounterType.HIV_ENROLLMENT);
					Encounter hivEnrollmentEncounter = new Encounter();

					hivEnrollmentEncounter.setEncounterType(hivEnrollEncType);
					hivEnrollmentEncounter.setPatient(ret);
					hivEnrollmentEncounter.setLocation(Context.getService(
							ChaiEmrService.class).getDefaultLocation());
					hivEnrollmentEncounter.setDateCreated(visitSave
							.getStartDatetime());
					hivEnrollmentEncounter.setEncounterDatetime(visitSave
							.getStartDatetime());
					hivEnrollmentEncounter.setForm(MetadataUtils.existing(
							Form.class, HivMetadata._Form.HIV_ENROLLMENT));
					hivEnrollmentEncounter.setVisit(visitSave);
					hivEnrollmentEncounter.setVoided(false);
					Context.getEncounterService().saveEncounter(
							hivEnrollmentEncounter);

					PatientProgram patientProgram = new PatientProgram();
					patientProgram.setPatient(ret);
					patientProgram.setProgram(MetadataUtils.existing(
							Program.class, HivMetadata._Program.HIV));
					patientProgram
							.setDateEnrolled(visitSave.getStartDatetime());
					patientProgram.setDateCreated(visitSave.getStartDatetime());
					Context.getProgramWorkflowService().savePatientProgram(
							patientProgram);
				} */
			}

			else {

				SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
						"dd-MMM-yy HH:mm:ss");
				Date date = null;
				try {
					date = mysqlDateTimeFormatter.parse(dateOfRegistration
							+ " " + curDate.getHours() + ":" + curDate.getMinutes()
							+ ":" + curDate.getSeconds());
				} catch (ParseException e) {
					date = curDate;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Visit visit = new Visit();
				visit.setPatient(ret);
				visit.setStartDatetime(date);
				visit.setVisitType(MetadataUtils.existing(VisitType.class,
						CommonMetadata._VisitType.OUTPATIENT));
				visit.setLocation(Context.getService(ChaiEmrService.class)
						.getDefaultLocation());

				if (isNewPatient) {
					VisitAttributeType attrType = Context.getService(
							VisitService.class).getVisitAttributeTypeByUuid(
							CommonMetadata._VisitAttributeType.NEW_PATIENT);
					if (attrType != null) {
						VisitAttribute attr = new VisitAttribute();
						attr.setAttributeType(attrType);
						attr.setVisit(visit);
						attr.setDateCreated(date);
						attr.setValue(true);
						visit.addAttribute(attr);
					}
				}

				Visit visitSave = Context.getVisitService().saveVisit(visit);

				
				
				if (hivEnrollEncTypePrev.isEmpty()) {
					EncounterType hivEnrollEncType = MetadataUtils.existing(
							EncounterType.class,
							HivMetadata._EncounterType.HIV_ENROLLMENT);
					Encounter hivEnrollmentEncounter = new Encounter();

					hivEnrollmentEncounter.setEncounterType(hivEnrollEncType);
					hivEnrollmentEncounter.setPatient(ret);
					hivEnrollmentEncounter.setLocation(Context.getService(
							ChaiEmrService.class).getDefaultLocation());

					hivEnrollmentEncounter.setDateCreated(curDate);
					hivEnrollmentEncounter.setEncounterDatetime(visitSave.getStartDatetime());

					hivEnrollmentEncounter.setForm(MetadataUtils.existing(
							Form.class, HivMetadata._Form.HIV_ENROLLMENT));

					hivEnrollmentEncounter.setVoided(false);
					Encounter enHivNew = Context.getEncounterService().saveEncounter(
							hivEnrollmentEncounter);

					PatientProgram patientProgram = new PatientProgram();
					patientProgram.setPatient(ret);
					patientProgram.setProgram(MetadataUtils.existing(
							Program.class, HivMetadata._Program.HIV));
					patientProgram.setDateEnrolled(enHivNew.getEncounterDatetime());
					patientProgram.setDateCreated(curDate);
					Context.getProgramWorkflowService().savePatientProgram(
							patientProgram);
				}
			}

			return ret;
		}

		/**
		 * Handles saving a field which is stored as an obs
		 * 
		 * @param patient
		 *            the patient being saved
		 * @param obsToSave
		 * @param obsToVoid
		 * @param question
		 * @param savedObs
		 * @param newValue
		 */
		protected void handleOncePerPatientObs(Patient patient,
				List<Obs> obsToSave, List<Obs> obsToVoid, Concept question,
				Obs savedObs, Concept newValue) {
			if (!OpenmrsUtil.nullSafeEquals(
					savedObs != null ? savedObs.getValueCoded() : null,
					newValue)) {
				// there was a change
				if (savedObs != null && newValue == null) {
					// treat going from a value to null as voiding all past
					// civil status obs
					obsToVoid.addAll(Context.getObsService()
							.getObservationsByPersonAndConcept(patient,
									question));
				}
				if (newValue != null) {
					Obs o = new Obs();
					o.setPerson(patient);
					o.setConcept(question);
					//o.setObsDatetime(new Date());
                    Date curDate=new Date();
					SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
							"dd-MMM-yy HH:mm:ss");
					Date date = null;
					if(dateOfRegistration!=null)
					{	try {
						
						date = mysqlDateTimeFormatter.parse(dateOfRegistration
								+ " " + curDate.getHours() + ":" + curDate.getMinutes()
								+ ":" + curDate.getSeconds());
					
						o.setObsDatetime(date);
						
					} catch (ParseException e) {
				
						date=curDate;
						
						e.printStackTrace();
					}
					}
					else
					{
						List<Visit> vis=Context.getVisitService().getActiveVisitsByPatient(patient);
						
						if(!vis.isEmpty())
						{
							
							
							
							for(Visit v:vis)
							{
								o.setObsDatetime(v.getStartDatetime());
							}
						}
						else
						{
							List<Visit> visit=Context.getVisitService().getVisitsByPatient(patient);
							
							if(visit!=null)
							{ if (visit.size() > 0) 
								{Visit v=visit.get(0);
								 
									
									 
								
									o.setObsDatetime(v.getStartDatetime());
								
								}
							}	
						}
						
					}
					o.setLocation(Context.getService(ChaiEmrService.class)
							.getDefaultLocation());
					o.setValueCoded(newValue);
					obsToSave.add(o);
				}
			}
		}

		protected void handleOncePerPatientObs(Patient patient,
				List<Obs> obsToSave, List<Obs> obsToVoid, Concept question,
				Obs savedObs, Concept newValue, String textValue,
				Date textDate, int check) {
			if (!OpenmrsUtil.nullSafeEquals(
					savedObs != null ? savedObs.getValueCoded() : null,
					newValue)) {
				// there was a change
				if (savedObs != null && newValue == null) {
					// treat going from a value to null as voiding all past
					// civil status obs
					obsToVoid.addAll(Context.getObsService()
							.getObservationsByPersonAndConcept(patient,
									question));
				}
				if (newValue != null) {
					Obs o = new Obs();
					o.setPerson(patient);
					o.setConcept(question);
				//	o.setObsDatetime(new Date());
				Date curDate=new Date();
					SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
							"dd-MMM-yy HH:mm:ss");
					Date date = null;
					if(dateOfRegistration!=null)
					{	try {
						
						date = mysqlDateTimeFormatter.parse(dateOfRegistration
								+ " " + curDate.getHours() + ":" + curDate.getMinutes()
								+ ":" + curDate.getSeconds());
						
						o.setObsDatetime(date);
						
					} catch (ParseException e) {
				
						date=curDate;
						
						e.printStackTrace();
					}
					}
					else
					{
						List<Visit> vis=Context.getVisitService().getActiveVisitsByPatient(patient);
						
						if(!vis.isEmpty())
						{
							
							
							
							for(Visit v:vis)
							{
								o.setObsDatetime(v.getStartDatetime());
							}
						}
						else
						{
							List<Visit> visit=Context.getVisitService().getVisitsByPatient(patient);
							
							if(visit!=null)
							{ if (visit.size() > 0) 
								{Visit v=visit.get(0);
								 
									
									 
								
									o.setObsDatetime(v.getStartDatetime());
								
								}
							}	
						}
						
					}
					o.setLocation(Context.getService(ChaiEmrService.class)
							.getDefaultLocation());
					o.setValueCoded(newValue);

					o.setValueText(textValue);
					if (check == 0) {
						o.setValueDate(textDate);
					}
					obsToSave.add(o);
				}
			}
		}

		public boolean isInHivProgram() {
			if (original == null || !original.isPatient()) {
				return false;
			}
			ProgramWorkflowService pws = Context.getProgramWorkflowService();
			Program hivProgram = MetadataUtils.existing(Program.class,
					HivMetadata._Program.HIV);
			for (PatientProgram pp : pws.getPatientPrograms((Patient) original,
					hivProgram, null, null, null, null, false)) {
				if (pp.getActive()) {
					return true;
				}
			}
			return false;
		}

		/**
		 * @return the original
		 */
		public Person getOriginal() {
			return original;
		}

		/**
		 * @param original
		 *            the original to set
		 */
		public void setOriginal(Patient original) {
			this.original = original;
		}

		/**
		 * @return the personName
		 */
		public PersonName getPersonName() {
			return personName;
		}

		/**
		 * @param personName
		 *            the personName to set
		 */
		public void setPersonName(PersonName personName) {
			this.personName = personName;
		}

		public Concept getIngoTypeConcept() {
			return ingoTypeConcept;
		}

		public void setIngoTypeConcept(Concept ingoTypeConcept) {
			this.ingoTypeConcept = ingoTypeConcept;
		}

		public Concept getEnrollmentName() {
			return enrollmentName;
		}

		public void setEnrollmentName(Concept enrollmentName) {
			this.enrollmentName = enrollmentName;
		}

		public Concept getEntryPoint() {
			return entryPoint;
		}

		public void setEntryPoint(Concept entryPoint) {
			this.entryPoint = entryPoint;
		}

		public String getHivTestPerformedPlace() {
			return hivTestPerformedPlace;
		}

		public void setHivTestPerformedPlace(String hivTestPerformedPlace) {
			this.hivTestPerformedPlace = hivTestPerformedPlace;
		}

		public Obs getSavedEntryPoint() {
			return savedEntryPoint;
		}

		public String getOtherEntryPoint() {
			return otherEntryPoint;
		}

		public void setOtherEntryPoint(String otherEntryPoint) {
			this.otherEntryPoint = otherEntryPoint;
		}

		public String getPreviousClinicName() {
			return previousClinicName;
		}

		public void setPreviousClinicName(String previousClinicName) {
			this.previousClinicName = previousClinicName;
		}

		public Date getTransferredInDate() {
			return transferredInDate;
		}

		public void setTransferredInDate(Date transferredInDate) {
			this.transferredInDate = transferredInDate;
		}

		public String getHivTestPerformed() {
			return hivTestPerformed;
		}

		public void setHivTestPerformed(String hivTestPerformed) {
			this.hivTestPerformed = hivTestPerformed;
		}

		public Date getHivTestPerformedDate() {
			return hivTestPerformedDate;
		}

		public void setHivTestPerformedDate(Date hivTestPerformedDate) {
			this.hivTestPerformedDate = hivTestPerformedDate;
		}

		public void setSavedEntryPoint(Obs savedEntryPoint) {
			this.savedEntryPoint = savedEntryPoint;
		}

		// chai update
		/*
		public String getArtRegistrationNumber() {
			return artRegistrationNumber;
		}

		public void setArtRegistrationNumber(String artRegistrationNumber) {
			this.artRegistrationNumber = artRegistrationNumber;
		}

		
		public String getPreArtRegistrationNumber() {
			return preArtRegistrationNumber;
		}

		public void setPreArtRegistrationNumber(String preArtRegistrationNumber) {
			this.preArtRegistrationNumber = preArtRegistrationNumber;
		}

		public String getNapArtRegistrationNumber() {
			return napArtRegistrationNumber;
		}

		public void setNapArtRegistrationNumber(String napArtRegistrationNumber) {
			this.napArtRegistrationNumber = napArtRegistrationNumber;
		}

		*/
		
		public String getMasterPatientIndex() {
			return masterPatientIndex;
		}

		public void setMasterPatientIndex(String masterPatientIndex) {
			this.masterPatientIndex = masterPatientIndex;
		}

		
		public String getHepCRegistrationNumber() {
			return hepCRegistrationNumber;
		}

		public void setHepCRegistrationNumber(String hepCRegistrationNumber) {
			this.hepCRegistrationNumber = hepCRegistrationNumber;
		}

		public String getRegistrationNumber() {
			return registrationNumber;
		}

		public void setRegistrationNumber(String registrationNumber) {
			this.registrationNumber = registrationNumber;
		}
		
		public String getSystemPatientId() {
			return systemPatientId;
		}

		public void setSystemPatientId(String systemPatientId) {
			this.systemPatientId = systemPatientId;
		}

		public String getCheckInType() {
			return checkInType;
		}

		public void setCheckInType(String checkInType) {
			this.checkInType = checkInType;
		}

		public String getOtherStatus() {
			return otherStatus;
		}

		public void setOtherStatus(String otherStatus) {
			this.otherStatus = otherStatus;
		}

		/**
		 * @return the patientClinicNumber
		 * 
		 *         public String getPatientClinicNumber() { return
		 *         patientClinicNumber; }
		 */
		/**
		 * @param patientClinicNumber
		 *            the patientClinicNumber to set
		 * 
		 *            public void setPatientClinicNumber(String
		 *            patientClinicNumber) { this.patientClinicNumber =
		 *            patientClinicNumber; }
		 */
		/**
		 * @return the hivIdNumber
		 */
		public String getUniquePatientNumber() {
			return uniquePatientNumber;
		}

		/**
		 * @param uniquePatientNumber
		 *            the uniquePatientNumber to set
		 */
		public void setUniquePatientNumber(String uniquePatientNumber) {
			this.uniquePatientNumber = uniquePatientNumber;
		}

		/**
		 * @return the nationalIdNumber
		 * 
		 *         public String getNationalIdNumber() { return
		 *         nationalIdNumber; }
		 */
		/**
		 * @param nationalIdNumber
		 *            the nationalIdNumber to set
		 * 
		 *            public void setNationalIdNumber(String nationalIdNumber) {
		 * 
		 *            this.nationalIdNumber = nationalIdNumber; }
		 */
		/**
		 * @return the birthdate
		 */
		public Date getBirthdate() {
			return birthdate;
		}

		/**
		 * @param birthdate
		 *            the birthdate to set
		 */
		public void setBirthdate(Date birthdate) {
			this.birthdate = birthdate;
		}

		/**
		 * @return the birthdateEstimated
		 */
		public Boolean getBirthdateEstimated() {
			return birthdateEstimated;
		}

		/**
		 * @param birthdateEstimated
		 *            the birthdateEstimated to set
		 */
		public void setBirthdateEstimated(Boolean birthdateEstimated) {
			this.birthdateEstimated = birthdateEstimated;
		}

		/**
		 * @return the gender
		 */
		public String getGender() {
			return gender;
		}

		/**
		 * @param gender
		 *            the gender to set
		 */
		public void setGender(String gender) {
			this.gender = gender;
		}

		/**
		 * @return the personAddress
		 */
		public PersonAddress getPersonAddress() {
			return personAddress;
		}

		/**
		 * @param personAddress
		 *            the personAddress to set
		 */
		public void setPersonAddress(PersonAddress personAddress) {
			this.personAddress = personAddress;
		}

		/**
		 * @return the maritalStatus
		 */
		public Concept getMaritalStatus() {
			return maritalStatus;
		}

		/**
		 * @param maritalStatus
		 *            the maritalStatus to set
		 */
		public void setMaritalStatus(Concept maritalStatus) {
			this.maritalStatus = maritalStatus;
		}

		/**
		 * @return the education
		 */
		public Concept getEducation() {
			return education;
		}

		/**
		 * @param education
		 *            the education to set
		 */
		public void setEducation(Concept education) {
			this.education = education;
		}

		/**
		 * @return the occupation
		 */
		public Concept getOccupation() {
			return occupation;
		}

		/**
		 * @param occupation
		 *            the occupation to set
		 */
		public void setOccupation(Concept occupation) {
			this.occupation = occupation;
		}

		/**
		 * @return the telephoneContact
		 */
		public String getTelephoneContact() {
			return telephoneContact;
		}

		/**
		 * @param telephoneContact
		 *            the telephoneContact to set
		 */
		public void setTelephoneContact(String telephoneContact) {
			this.telephoneContact = telephoneContact;
		}

		public Boolean getDead() {
			return dead;
		}

		public void setDead(Boolean dead) {
			this.dead = dead;
		}

		public Date getDeathDate() {
			return deathDate;
		}

		public void setDeathDate(Date deathDate) {
			this.deathDate = deathDate;
		}

		/**
		 * @return the nameOfNextOfKin
		 */
		public String getNameOfNextOfKin() {
			return nameOfNextOfKin;
		}

		/**
		 * @param nameOfNextOfKin
		 *            the nameOfNextOfKin to set
		 */
		public void setNameOfNextOfKin(String nameOfNextOfKin) {
			this.nameOfNextOfKin = nameOfNextOfKin;
		}

		/**
		 * @return the nextOfKinRelationship
		 */
		public String getNextOfKinRelationship() {
			return nextOfKinRelationship;
		}

		/**
		 * @param nextOfKinRelationship
		 *            the nextOfKinRelationship to set
		 */
		public void setNextOfKinRelationship(String nextOfKinRelationship) {
			this.nextOfKinRelationship = nextOfKinRelationship;
		}

		/**
		 * @return the nextOfKinContact
		 */
		public String getNextOfKinContact() {
			return nextOfKinContact;
		}

		/**
		 * @param nextOfKinContact
		 *            the nextOfKinContact to set
		 */
		public void setNextOfKinContact(String nextOfKinContact) {
			this.nextOfKinContact = nextOfKinContact;
		}

		/**
		 * @return the nextOfKinAddress
		 */
		public String getNextOfKinAddress() {
			return nextOfKinAddress;
		}

		/**
		 * @param nextOfKinAddress
		 *            the nextOfKinAddress to set
		 */
		public void setNextOfKinAddress(String nextOfKinAddress) {
			this.nextOfKinAddress = nextOfKinAddress;
		}

		/**
		 * @return the subChiefName
		 */
		public String getSubChiefName() {
			return subChiefName;
		}

		/**
		 * @param subChiefName
		 *            the subChiefName to set
		 */
		public void setSubChiefName(String subChiefName) {
			this.subChiefName = subChiefName;
		}

		public String getFatherName() {
			return fatherName;
		}

		public void setFatherName(String fatherName) {
			this.fatherName = fatherName;
		}

		public String getNationalId() {
			return nationalId;
		}

		public void setNationalId(String nationalId) {
			this.nationalId = nationalId;
		}

		public Concept getPlaceOfBirth() {
			return placeOfBirth;
		}

		public void setPlaceOfBirth(Concept placeOfBirth) {
			this.placeOfBirth = placeOfBirth;
		}

		public Obs getSavedPlaceOfBirth() {
			return savedPlaceOfBirth;
		}

		public void setSavedPlaceOfBirth(Obs savedPlaceOfBirth) {
			this.savedPlaceOfBirth = savedPlaceOfBirth;
		}

		public String getDateOfRegistration() {
			return dateOfRegistration;
		}

		public void setDateOfRegistration(String dateOfRegistration) {
			this.dateOfRegistration = dateOfRegistration;
		}

	}

}