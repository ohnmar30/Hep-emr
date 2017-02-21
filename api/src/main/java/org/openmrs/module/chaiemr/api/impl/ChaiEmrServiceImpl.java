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

package org.openmrs.module.chaiemr.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.GlobalProperty;
import org.openmrs.Location;
import org.openmrs.LocationAttributeType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.FormService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.idgen.AutoGenerationOption;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.idgen.SequentialIdentifierGenerator;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.idgen.validator.LuhnModNIdentifierValidator;
import org.openmrs.module.chaicore.identifier.IdentifierManager;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.api.db.ChaiEmrDAO;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.chaiemr.metadata.FacilityMetadata;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.model.DrugInfo;
import org.openmrs.module.chaiemr.model.DrugObsProcessed;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.chaiemr.wrapper.Facility;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementations of business logic methods for ChaiEMR
 */
public class ChaiEmrServiceImpl extends BaseOpenmrsService implements ChaiEmrService {

	protected static final Log log = LogFactory.getLog(ChaiEmrServiceImpl.class);

	protected static final String OPENMRS_MEDICAL_RECORD_NUMBER_NAME = "Chai EMR - OpenMRS Medical Record Number";
	protected static final String HIV_UNIQUE_PATIENT_NUMBER_NAME = "Chai EMR - OpenMRS HIV Unique Patient Number";

	@Autowired
	private IdentifierManager identifierManager;

	@Autowired
	private LocationService locationService;

	private boolean setupRequired = true;

	private ChaiEmrDAO dao;

	/**
	 * Method used to inject the data access object.
	 * @param dao the data access object.
	 */
	public void setChaiEmrDAO(ChaiEmrDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.module.chaiemr.api.ChaiEmrService#isSetupRequired()
	 */
	@Override
	public boolean isSetupRequired() {
		// Assuming that it's not possible to _un_configure after having configured, i.e. after the first
		// time we return true we can save time by not re-checking things
		if (!setupRequired) {
			return false;
		}

		boolean defaultLocationConfigured = getDefaultLocation() != null;
		boolean mrnConfigured = identifierManager.getIdentifierSource(MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.OPENMRS_ID)) != null;
		boolean upnConfigured = identifierManager.getIdentifierSource(MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER)) != null;

		setupRequired = !(defaultLocationConfigured && mrnConfigured && upnConfigured);
		return setupRequired;
	}

	/**
	 * @see org.openmrs.module.chaiemr.api.ChaiEmrService#setDefaultLocation(org.openmrs.Location)
	 */
	@Override
	public void setDefaultLocation(Location location) {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(EmrConstants.GP_DEFAULT_LOCATION);
		gp.setValue(location);
		Context.getAdministrationService().saveGlobalProperty(gp);
	}
	
	/**
	 * @see org.openmrs.module.chaiemr.api.ChaiEmrService#getDefaultLocation()
	 */
	@Override
	public Location getDefaultLocation() {
		try {
			Context.addProxyPrivilege(PrivilegeConstants.VIEW_LOCATIONS);
			Context.addProxyPrivilege(PrivilegeConstants.VIEW_GLOBAL_PROPERTIES);

			GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(EmrConstants.GP_DEFAULT_LOCATION);
			return gp != null ? ((Location) gp.getValue()) : null;
		}
		finally {
			Context.removeProxyPrivilege(PrivilegeConstants.VIEW_LOCATIONS);
			Context.removeProxyPrivilege(PrivilegeConstants.VIEW_GLOBAL_PROPERTIES);
		}
	}
	
	/**
	 * @see org.openmrs.module.chaiemr.api.ChaiEmrService#getDefaultLocationMflCode()
	 */
	@Override
	public String getDefaultLocationMflCode() {
		try {
			Context.addProxyPrivilege(PrivilegeConstants.VIEW_LOCATION_ATTRIBUTE_TYPES);

			Location location = getDefaultLocation();
			return (location != null) ? new Facility(location).getMflCode() : null;
		}
		finally {
			Context.removeProxyPrivilege(PrivilegeConstants.VIEW_LOCATION_ATTRIBUTE_TYPES);
		}
	}

	/**
	 * @see org.openmrs.module.chaiemr.api.ChaiEmrService#getLocationByMflCode(String)
	 */
	@Override
	public Location getLocationByMflCode(String mflCode) {
		LocationAttributeType mflCodeAttrType = MetadataUtils.existing(LocationAttributeType.class, FacilityMetadata._LocationAttributeType.MASTER_FACILITY_CODE);
		Map<LocationAttributeType, Object> attrVals = new HashMap<LocationAttributeType, Object>();
		attrVals.put(mflCodeAttrType, mflCode);

		List<Location> locations = locationService.getLocations(null, null, attrVals, false, null, null);

		return locations.size() > 0 ? locations.get(0) : null;
	}

	/**
	 * @see org.openmrs.module.chaiemr.api.ChaiEmrService#getNextHivUniquePatientNumber(String)
	 */
	@Override
	public String getNextHivUniquePatientNumber(String comment) {
		if (comment == null) {
			comment = "ChaiEMR Service";
		}

		PatientIdentifierType upnType = MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
		IdentifierSource source = identifierManager.getIdentifierSource(upnType);

		String prefix = Context.getService(ChaiEmrService.class).getDefaultLocationMflCode();
		System.out.println("Prefix = " + prefix);
		String sequentialNumber = Context.getService(IdentifierSourceService.class).generateIdentifier(source, comment);
		System.out.println("sequential Number = " + sequentialNumber);
		return prefix + sequentialNumber;
	}

	/**
	 * @see ChaiEmrService#getVisitsByPatientAndDay(org.openmrs.Patient, java.util.Date)
	 */
	@Override
	public List<Visit> getVisitsByPatientAndDay(Patient patient, Date date) {
		Date startOfDay = OpenmrsUtil.firstSecondOfDay(date);
		Date endOfDay = OpenmrsUtil.getLastMomentOfDay(date);

		// look for visits that started before endOfDay and ended after startOfDay
		List<Visit> visits = Context.getVisitService().getVisits(null, Collections.singleton(patient), null, null, null, endOfDay, startOfDay, null, null, true, false);
		Collections.reverse(visits); // We want by date asc
		return visits;
	}

	/**
	 * @see ChaiEmrService#setupMrnIdentifierSource(String)
	 */
	@Override
	public void setupMrnIdentifierSource(String startFrom) {
		PatientIdentifierType idType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.OPENMRS_ID);
		setupIdentifierSource(idType, startFrom, OPENMRS_MEDICAL_RECORD_NUMBER_NAME, null, "M");
	}

	/**
	 * @see ChaiEmrService#setupHivUniqueIdentifierSource(String)
	 */
	@Override
	public void setupHivUniqueIdentifierSource(String startFrom) {
		PatientIdentifierType idType = MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
		setupIdentifierSource(idType, startFrom, HIV_UNIQUE_PATIENT_NUMBER_NAME, "0123456789", null);
	}

	/**
	 * Setup an identifier source
	 * @param idType the patient identifier type
	 * @param startFrom the base identifier to start from
	 * @param name the identifier source name
	 * @param baseCharacterSet the base character set
	 * @param prefix the prefix
	 */
	protected void setupIdentifierSource(PatientIdentifierType idType, String startFrom, String name, String baseCharacterSet, String prefix) {
		if (identifierManager.getIdentifierSource(idType) != null) {
			throw new APIException("Identifier source is already exists for " + idType.getName());
		}

		String validatorClass = idType.getValidator();
		LuhnModNIdentifierValidator validator = null;
		if (validatorClass != null) {
			try {
				validator = (LuhnModNIdentifierValidator) Context.loadClass(validatorClass).newInstance();
			} catch (Exception e) {
				throw new APIException("Unexpected Identifier Validator (" + validatorClass + ") for " + idType.getName(), e);
			}
		}

		if (startFrom == null) {
			if (validator != null) {
				startFrom = validator.getBaseCharacters().substring(0, 1);
			} else {
				throw new RuntimeException("startFrom is required if this isn't using a LuhnModNIdentifierValidator");
			}
		}

		if (baseCharacterSet == null) {
			baseCharacterSet = validator.getBaseCharacters();
		}

		IdentifierSourceService idService = Context.getService(IdentifierSourceService.class);

		SequentialIdentifierGenerator idGen = new SequentialIdentifierGenerator();
		idGen.setPrefix(prefix);
		idGen.setName(name);
		idGen.setDescription("Identifier Generator for " + idType.getName());
		idGen.setIdentifierType(idType);
		idGen.setBaseCharacterSet(baseCharacterSet);
		idGen.setFirstIdentifierBase(startFrom);
		idService.saveIdentifierSource(idGen);

		AutoGenerationOption auto = new AutoGenerationOption(idType, idGen, true, true);
		idService.saveAutoGenerationOption(auto);
	}

	@Override
	public List<Object> executeSqlQuery(String query, Map<String, Object> substitutions) {
		return dao.executeSqlQuery(query, substitutions);
	}

	@Override
	public List<Object> executeHqlQuery(String query, Map<String, Object> substitutions) {
		return dao.executeHqlQuery(query, substitutions);
	}
	
	public Encounter getFirstEncounterByDateTime(Patient patient,Visit visit) {
		return dao.getFirstEncounterByDateTime(patient,visit);
	}
	
	public Encounter getFirstEncounterByCreatedDateTime(Patient patient,Visit visit) {
		return dao.getFirstEncounterByCreatedDateTime(patient,visit);
	}
	
	public Encounter getLastEncounterByDateTime(Patient patient,Visit visit) {
		return dao.getLastEncounterByDateTime(patient,visit);
	}
	
	public Encounter getLastEncounterByCreatedDateTime(Patient patient,Visit visit) {
		return dao.getLastEncounterByCreatedDateTime(patient,visit);
	}
	
	public Encounter getLastEncounterByDateTime(Patient patient,Set<EncounterType> encounterTypes) {
		return dao.getLastEncounterByDateTime(patient,encounterTypes);
	}
	
	public Encounter getLastEncounterByCreatedDateTime(Patient patient,Set<EncounterType> encounterTypes) {
		return dao.getLastEncounterByCreatedDateTime(patient,encounterTypes);
	}
	
	public List<Order> getOrderByDateAndOrderType(Date date,OrderType orderType) {
		return dao.getOrderByDateAndOrderType(date,orderType);
	}
	
	public List<Obs> getObsGroupByDate(Date date) {
		return dao.getObsGroupByDate(date);
	}
	
	public List<Obs> getObsGroupByDateAndPerson(Date date,Person person) {
		return dao.getObsGroupByDateAndPerson(date,person);
	}
	
	public List<Obs> getObsByObsGroup(Obs obsGroup) {
		return dao.getObsByObsGroup(obsGroup);
	}
	
	public Obs saveOrUpdateObs(Obs obs) {
		return dao.saveOrUpdateObs(obs);
	}
	
	public DrugOrderProcessed saveDrugOrderProcessed(DrugOrderProcessed drugOrderProcessed) {
		return dao.saveDrugOrderProcessed(drugOrderProcessed);
	}
	
	public DrugObsProcessed saveDrugObsProcessed(DrugObsProcessed drugObsProcessed) {
		return dao.saveDrugObsProcessed(drugObsProcessed);
	}
	
	public DrugOrderProcessed getDrugOrderProcessed(DrugOrder drugOrder) {
		return dao.getDrugOrderProcessed(drugOrder);
	}
	
	public DrugOrderProcessed getLastDrugOrderProcessed(DrugOrder drugOrder) {
		return dao.getLastDrugOrderProcessed(drugOrder);
	}
	
	public DrugOrderProcessed getLastDrugOrderProcessedNotDiscontinued(DrugOrder drugOrder) {
		return dao.getLastDrugOrderProcessedNotDiscontinued(drugOrder);
	}
	
	public List<DrugOrderProcessed> getDrugOrderProcessedCompleted(DrugOrder drugOrder) {
		return dao.getDrugOrderProcessedCompleted(drugOrder);
	}
	
	public DrugOrderProcessed getDrugOrderProcesedById(Integer id) {
		return dao.getDrugOrderProcesedById(id);
	}
	
	public Encounter getLabbOrderEncounter(Visit visit) {
		
		FormService formService = Context.getService(FormService.class);
		EncounterType encounterType = Context.getEncounterService().getEncounterTypeByUuid("839cc70b-09cf-4d20-8bf5-b19dddde9e32");


		Form labOrderForm = formService.getFormByUuid("740308bd-1fe1-43b4-8b7d-51e1d513e205");
		if (labOrderForm == null) {
			throw new IllegalArgumentException("Cannot find form with uuid =  740308bd-1fe1-43b4-8b7d-51e1d513e205");
		}


		List<Encounter> encounters = Context.getEncounterService().getEncounters(visit.getPatient(), null, null, null, Collections.singleton(labOrderForm),  Collections.singleton(encounterType), null, null, Collections.singleton(visit), false);
		if (!encounters.isEmpty()) {
			// in case there are more than one, we pick the last one
			Encounter encounter = encounters.get(encounters.size() - 1);
			return encounter;
		}

		
		return null;
		
	}
	
	public List<Encounter> getLabResultEncounters(Visit visit) {
		EncounterType encType = Context.getEncounterService().getEncounterType("Lab Results");
		List<Encounter> encounters = Context.getEncounterService().getEncounters(visit.getPatient(), null, null, null, null, Collections.singleton(encType), null, null, Collections.singleton(visit), false);
		return encounters;
	}
	
	public List<DrugOrderProcessed> getDrugOrdersByProcessedDate(Date date) {
		return dao.getDrugOrdersByProcessedDate(date);
	}
	
	public List<DrugObsProcessed> getObsDrugOrdersByProcessedDate(Date date) {
		return dao.getObsDrugOrdersByProcessedDate(date);
	}
	
	public List<DrugOrderProcessed> getDrugOrdersByPatientAndProcessedDate(Patient patient,Date processedDate) {
		return dao.getDrugOrdersByPatientAndProcessedDate(patient,processedDate);
	}
	
	public List<DrugObsProcessed> getObsDrugOrdersByPatientAndProcessedDate(Patient patient,Date processedDate) {
		return dao.getObsDrugOrdersByPatientAndProcessedDate(patient,processedDate);
	}

	public List<DrugInfo> getDrugInfo() {
		return dao.getDrugInfo();
	}
	
	public DrugInfo getDrugInfo(String drugCode) {
		return dao.getDrugInfo(drugCode);
	}
	
	public DrugOrderProcessed getLastRegimenChangeType(Patient patient) {
		return dao.getLastRegimenChangeType(patient);
	}
	
	public List<ConceptAnswer> getConceptAnswerByAnsweConcept(Concept answerConcept){
		return dao.getConceptAnswerByAnsweConcept(answerConcept);
	}
		public List<DrugOrderProcessed> getAllfirstLine() {
		return dao.getAllfirstLine();
	}
		
	public List<PersonAddress> getPatientsByTownship(String township){
		return dao.getPatientsByTownship(township);
	}
	
	public List<Obs> getObsByScheduledDate(Date date){
		return dao.getObsByScheduledDate(date);
	}
	
	public Set<Patient> getPatientProgram(Program program,String startDate,String endDate){
		return dao.getPatientProgram(program,startDate,endDate);
	}
	
	public Set<Patient> getNoOfPatientTransferredIn(String startDate,String endDate){
		return dao.getNoOfPatientTransferredIn(startDate,endDate);
	}
	
	public Set<Patient> getNoOfPatientTransferredOut(String startDate,String endDate){
		return dao.getNoOfPatientTransferredOut(startDate,endDate);
	}
	
	public Visit getVisitsByPatient(Patient patient){
		return dao.getVisitsByPatient(patient);
	}
	
	public Set<Patient> getTotalNoOfCohort(String startDate,String endDate){
		return dao.getTotalNoOfCohort(startDate,endDate);
	}
	
	public Set<Patient> getCohortBasedOnGender(String gender,String startDate,String endDate){
		return dao.getCohortBasedOnGender(gender,startDate,endDate);
	}
	
	public Set<Patient> getCohortBasedOnAge(Integer age1,Integer age2,String startDate,String endDate){
		return dao.getCohortBasedOnAge(age1,age2,startDate,endDate);
	}
	
	public Set<Patient> getNoOfCohortAliveAndOnArt(Program program,String startDate,String endDate){
		return dao.getNoOfCohortAliveAndOnArt(program,startDate,endDate);
	}
	
	public Set<Patient> getOriginalFirstLineRegimen(Program program,String startDate,String endDate){
		return dao.getOriginalFirstLineRegimen(program,startDate,endDate);
	}
	
	public Set<Patient> getAlternateFirstLineRegimen(Program program,String startDate,String endDate){
		return dao.getAlternateFirstLineRegimen(program,startDate,endDate);
	}
	
	public Set<Patient> getSecondLineRegimen(Program program,String startDate,String endDate){
		return dao.getSecondLineRegimen(program,startDate,endDate);
	}
	
	public Set<Patient> getNoOfArtStoppedCohort(Program program,String startDate,String endDate){
		return dao.getNoOfArtStoppedCohort(program,startDate,endDate);
	}
	
	public Set<Patient> getNoOfArtDiedCohort(Program program,String startDate,String endDate){
		return dao.getNoOfArtDiedCohort(program,startDate,endDate);
	}
	
	public Set<Patient> getNoOfPatientLostToFollowUp(String startDate,String endDate){
		return dao.getNoOfPatientLostToFollowUp(startDate,endDate);
	}
	
	public List<Obs> getNoOfPatientWithCD4(String startDate,String endDate){
		return dao.getNoOfPatientWithCD4(startDate,endDate);
	}
	
	public List<Obs> getNoOfPatientNormalActivity(String startDate,String endDate){
		return dao.getNoOfPatientNormalActivity(startDate,endDate);
	}
	
	public List<Obs> getNoOfPatientBedriddenLessThanFifty(String startDate,String endDate){
		return dao.getNoOfPatientBedriddenLessThanFifty(startDate,endDate);
	}
	
	public List<Obs> getNoOfPatientBedriddenMoreThanFifty(String startDate,String endDate){
		return dao.getNoOfPatientBedriddenMoreThanFifty(startDate,endDate);
	}
	
	public Set<Patient> getNoOfPatientPickedUpArvForSixMonth(String startDate,String endDate){
		return dao.getNoOfPatientPickedUpArvForSixMonth(startDate,endDate);
	}
	
	public Set<Patient> getNoOfPatientPickedUpArvForTwelveMonth(String startDate,String endDate){
		return dao.getNoOfPatientPickedUpArvForTwelveMonth(startDate,endDate);
	}
	
	@Override
	public List<DrugOrderProcessed> getDrugOrderProcessedByPatient(Patient patient) {
		// TODO Auto-generated method stub
		return dao.getDrugOrderProcessedByPatient(patient);
	}
}
