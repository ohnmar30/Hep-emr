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
import org.apache.commons.lang.math.NumberUtils;
import org.jfree.util.Log;
//import org.apache.derby.tools.sysinfo;
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
import org.openmrs.module.chaiemr.metadata.HcvMetadata;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
//import org.openmrs.module.chaiemr.validator.TelephoneNumberValidator;
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
public class ScreeningResultPatientFragmentController {

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
				
		//System.out.println("Screening Register Patient Controller");
		Person existing = patient != null ? patient : person;

		model.addAttribute("command", newEditPatientForm(existing));

		model.addAttribute("civilStatusConcept",
				Dictionary.getConcept(Dictionary.CIVIL_STATUS));
				
		model.addAttribute("entryPointList",
				Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT));
		//model.addAttribute(", value);
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
		
		model.addAttribute("enrollmentList",
				Dictionary.getConcept(Dictionary.ENROLLMENT_STATUS));
		Obs savedEntryPointConcept;
		
		savedEntryPointConcept = getLatestObs(patient,
				Dictionary.METHOD_OF_ENROLLMENT);
		if (savedEntryPointConcept != null) {
			model.addAttribute("pointentry",
					savedEntryPointConcept.getValueCoded());
		} else {
			model.addAttribute("pointentry", 0);
		}
		
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
					+ generateCheckdigit(noCheck)); // generate new patient id
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
		private String opdNumber;
		private Boolean hBsAg;
		private Boolean antiHCV;
		private String hBsAgResult;
		private String antiHCVResult;
		
		public String gethBsAgResult() {
			return hBsAgResult;
		}

		public void sethBsAgResult(String hBsAgResult) {
			this.hBsAgResult = hBsAgResult;
		}

		public String getAntiHCVResult() {
			return antiHCVResult;
		}

		public void setAntiHCVResult(String antiHCVResult) {
			this.antiHCVResult = antiHCVResult;
		}

		public Boolean gethBsAg() {
			return hBsAg;
		}

		public void sethBsAg(Boolean hBsAg) {
			this.hBsAg = hBsAg;
		}

		public Boolean getAntiHCV() {
			return antiHCV;
		}

		public void setAntiHCV(Boolean antiHCV) {
			this.antiHCV = antiHCV;
		}

		public String getOpdNumber() {
			return opdNumber;
		}

		public void setOpdNumber(String opdNumber) {
			this.opdNumber = opdNumber;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public Obs getSavedIngoTypeConcept() {
			return savedIngoTypeConcept;
		}

		public void setSavedIngoTypeConcept(Obs savedIngoTypeConcept) {
			this.savedIngoTypeConcept = savedIngoTypeConcept;
		}

		public Obs getSavedEnrollmentNameConcept() {
			return savedEnrollmentNameConcept;
		}

		public void setSavedEnrollmentNameConcept(Obs savedEnrollmentNameConcept) {
			this.savedEnrollmentNameConcept = savedEnrollmentNameConcept;
		}

		public Obs getSavedEducation() {
			return savedEducation;
		}

		public void setSavedEducation(Obs savedEducation) {
			this.savedEducation = savedEducation;
		}

		public Integer getIdentifierCount() {
			return identifierCount;
		}

		public void setIdentifierCount(Integer identifierCount) {
			this.identifierCount = identifierCount;
		}

		public void setOriginal(Person original) {
			this.original = original;
		}

		private String fatherName;
		
		private Obs savedIngoTypeConcept;
		private Obs savedEnrollmentNameConcept;
		private Obs savedEntryPoint;
		private Obs savedEducation;
		private Boolean dead = false;
		private Date deathDate;		
		
		private String systemPatientId;
		private String uniquePatientNumber;
	
		
		private String checkInType;
		
		private String dateOfRegistration;
		private String hepBId;
		private String antiId;
		
		
		public String getHepBId() {
			return hepBId;
		}

		public void setHepBId(String hepBId) {
			this.hepBId = hepBId;
		}

		public String getAntiId() {
			return antiId;
		}

		public void setAntiId(String antiId) {
			this.antiId = antiId;
		}
		
		private Integer identifierCount;
		
		/**
		 * Creates an edit form for a new patient
		 */
		public EditPatientForm() {
			location = Context.getService(ChaiEmrService.class)
					.getDefaultLocation();

			personName = new PersonName();
			hBsAg = true;
			antiHCV = true;
			
		}

		/**
		 * Creates an edit form for an existing patient
		 */
		public EditPatientForm(Person person) {
			this();

			hBsAg = true;
			antiHCV = true;
			original = person;

			if (person.getPersonName() != null) {
				personName = person.getPersonName();
			} else {
				personName.setPerson(person);
			}		
			
			PersonWrapper wrapper = new PersonWrapper(person);
			fatherName = wrapper.getFatherName();
			
		}

		/**
		 * Creates an edit form for an existing patient
		 */
		public EditPatientForm(Patient patient) {
			this((Person) patient);

			PatientWrapper wrapper = new PatientWrapper(patient);
			fatherName = wrapper.getFatherName();
			
			systemPatientId = wrapper.getSystemPatientId();
			fatherName = wrapper.getFatherName();
			gender = patient.getGender();
			opdNumber=wrapper.getOpdNumber();
			birthdate = patient.getBirthdate();

			hBsAg = true;
			antiHCV = true;
			
			List<Encounter> encounterList = Context.getEncounterService().getEncounters(patient);
			int encounterCount = encounterList.size();
			
			if (encounterCount == 0)
				return;
			
			Encounter lastEncounter = encounterList.get(encounterCount - 1);
			if (!lastEncounter.getEncounterType().getUuid().equals(HcvMetadata._EncounterType.SCREENING_REGISTRATION))
				return;
			
			
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
			};

			require(errors, "gender");
			require(errors, "birthdate");
			
			if (fatherName !=null && fatherName.length() > 50) {
				errors.rejectValue("fatherName",
						"Expected length of Name is exceeding");
			}
			;
			
			// require(errors, "entryPoint");
			// require(errors, "enrollmentName");
			
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
			
			identifierCount = 0;

			// chai update
			/*
			// Check INGO name is entered, if INGO number is entered
			String value = (String) errors
					.getFieldValue("hepCRegistrationNumber");
			
			if (!value.isEmpty()) {
				require(errors, "ingoTypeConcept");
			}
			*/
			
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
			
			if (anyChanges(toSave.getPersonName(), personName, "givenName")) {
				if (toSave.getPersonName() != null) {
					voidData(toSave.getPersonName());
				}
				personName.setGivenName(personName.getGivenName());
				personName.setFamilyName("(NULL)");
				toSave.addName(personName);
			}
			
			PatientWrapper wrapper = new PatientWrapper(toSave);			
			wrapper.getPerson().setFatherName(fatherName);
			wrapper.setSystemPatientId(systemPatientId, location);
			wrapper.setOpdNumber(opdNumber,location);
			
			
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


			/*
			 * To check in directly
			 */
			List<Encounter> hepCEnrollEncTypePrev = Context
					.getEncounterService().getEncountersByPatient(ret);
			
			Date curDate = new Date();
			
		//maintain

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
			
			if (hepCEnrollEncTypePrev.size() == 1) {
				EncounterType hcvEnrollEncType = MetadataUtils.existing(
						EncounterType.class,
						HcvMetadata._EncounterType.SCREENING_RESULTS);
				Encounter hcvEnrollmentEncounter = new Encounter();

				hcvEnrollmentEncounter.setEncounterType(hcvEnrollEncType);
				hcvEnrollmentEncounter.setPatient(ret);
				hcvEnrollmentEncounter.setLocation(Context.getService(
						ChaiEmrService.class).getDefaultLocation());

				hcvEnrollmentEncounter.setDateCreated(curDate);
				hcvEnrollmentEncounter.setEncounterDatetime(visitSave.getStartDatetime());

				hcvEnrollmentEncounter.setForm(MetadataUtils.existing(
						Form.class, HcvMetadata._Form.HCV_ENROLLMENT));

				hcvEnrollmentEncounter.setVoided(false);
				
				Encounter enHcvNew = Context.getEncounterService().saveEncounter(
						hcvEnrollmentEncounter);

				/*
				PatientProgram patientProgram = new PatientProgram();
				patientProgram.setPatient(ret);
				patientProgram.setProgram(MetadataUtils.existing(
						Program.class, HcvMetadata._Program.HCV));
				patientProgram.setDateEnrolled(enHcvNew.getEncounterDatetime());
				patientProgram.setDateCreated(curDate);
				Context.getProgramWorkflowService().savePatientProgram(
						patientProgram);
				*/
				
				//Concept conceptlaborder = Context.getConceptService().getConceptByUuid(Dictionary.lABORATORY_RESULTS);
				if (hBsAg)
				{					
					Concept conceptHbsAg = Context.getConceptService().getConceptByUuid(Dictionary.HBsAg);
					Concept cptHBs = Context.getConceptService().getConceptByUuid(hBsAgResult);
					
					Obs oLaborderHBsAg = new Obs();
					oLaborderHBsAg.setPerson(ret);
					oLaborderHBsAg.setLocation(location);
					oLaborderHBsAg.setConcept(conceptHbsAg);
					oLaborderHBsAg.setEncounter(enHcvNew);
					oLaborderHBsAg.setObsDatetime(date);
					oLaborderHBsAg.setValueCoded(cptHBs);
					Context.getObsService().saveObs(oLaborderHBsAg, "HBsAg Lab result for Screening Registration");
				}
				
				if (antiHCV)
				{
					Concept conceptAntiHCV = Context.getConceptService().getConceptByUuid(Dictionary.ANTI_HCV);
					Concept cptHCV = Context.getConceptService().getConceptByUuid(antiHCVResult);
					
					Obs oLaborderAntiHCV = new Obs();
					oLaborderAntiHCV.setPerson(ret);
					oLaborderAntiHCV.setLocation(location);
					oLaborderAntiHCV.setConcept(conceptAntiHCV);
					oLaborderAntiHCV.setEncounter(enHcvNew);
					oLaborderAntiHCV.setObsDatetime(date);
					oLaborderAntiHCV.setValueCoded(cptHCV);
					Context.getObsService().saveObs(oLaborderAntiHCV, "AntiHCV Lab result for Screening Registration");
					
					if (cptHCV.equals(Dictionary.NEGATIVE))
					{
						
					}
					
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
		
		public Obs getSavedEntryPoint() {
			return savedEntryPoint;
		}
		
		public void setSavedEntryPoint(Obs savedEntryPoint) {
			this.savedEntryPoint = savedEntryPoint;
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
		
		public String getDateOfRegistration() {
			return dateOfRegistration;
		}

		public void setDateOfRegistration(String dateOfRegistration) {
			this.dateOfRegistration = dateOfRegistration;
		}
		
		public String getFatherName() {
			return fatherName;
		}

		public void setFatherName(String fatherName) {
			this.fatherName = fatherName;
		}
		
	}

}