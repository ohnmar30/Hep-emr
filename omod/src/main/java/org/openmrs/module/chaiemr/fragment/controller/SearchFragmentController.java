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

package org.openmrs.module.chaiemr.fragment.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptSearchResult;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitAttribute;
import org.openmrs.VisitType;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.module.chaicore.CoreConstants;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.calculation.library.ScheduledVisitOnDayCalculation;
import org.openmrs.module.chaiemr.calculation.library.VisitsOnDayCalculation;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.chaiemr.metadata.HcvMetadata;
import org.openmrs.module.chaiemr.model.DrugObsProcessed;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.PersonByNameComparator;
import org.openmrs.web.user.CurrentUsers;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Fragment actions specifically for searching for OpenMRS objects
 */
public class SearchFragmentController {

	protected static final Log log = LogFactory
			.getLog(SearchFragmentController.class);

	/**
	 * Gets a patient by their id
	 * 
	 * @param patient
	 *            the patient
	 * @param ui
	 *            the UI utils
	 * @return the simplified patient
	 */
	public SimpleObject patient(@RequestParam("id") Patient patient, UiUtils ui) {
		SimpleObject ret = ui.simplifyObject(patient);

		// Simplify and attach active visit to patient object
		List<Visit> activeVisits = Context.getVisitService()
				.getActiveVisitsByPatient(patient);
		ret.put("activeVisit",
				activeVisits.size() > 0 ? ui.simplifyObject(activeVisits.get(0))
						: null);
		return ret;
	}

	/**
	 * Searches for patients by name, identifier, age, visit status
	 * 
	 * @param query
	 *            the name or identifier
	 * @param which
	 *            all|checked-in|non-accounts
	 * @param ui
	 *            the UI utils
	 * @return the simple patients
	 */
	public List<SimpleObject> patients(
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "which", required = false, defaultValue = "all") String which,
			UiUtils ui) {

		//log.error("search normal");
		// Return empty list if we don't have enough input to search on
		if (StringUtils.isBlank(query) && "all".equals(which)) {
			return Collections.emptyList();
		}

		// Run main patient search query based on id/name
		List<Patient> matchedByNameOrID = Context.getPatientService()
				.getPatients(query);

		// Gather up active visits for all patients. These are attached to the
		// returned patient representations.
		Map<Patient, Visit> patientActiveVisits = getActiveVisitsByPatients();

		List<Patient> matched = new ArrayList<Patient>();

		// If query wasn't long enough to be searched on, and they've requested
		// checked-in patients, return the list
		// of checked in patients
		if (StringUtils.isBlank(query) && "checked-in".equals(which)) {
			matched.addAll(patientActiveVisits.keySet());
			Collections.sort(matched, new PersonByNameComparator()); // Sort by
																		// person
																		// name
		}
		else if (StringUtils.isBlank(query) && "screening-register".equals(which)) {
			
			for (Patient patient : patientActiveVisits.keySet())
			{
				List<Encounter> prevEncounters = Context
						.getEncounterService().getEncountersByPatient(patient);
				if (!prevEncounters.isEmpty())
				{
					Encounter lastEncounter = prevEncounters.get(prevEncounters.size()-1);
					EncounterType screeningEncType = MetadataUtils.existing(
							EncounterType.class,
							HcvMetadata._EncounterType.SCREENING_REGISTRATION);
					
					if (lastEncounter.getEncounterType().equals(screeningEncType))
						matched.add(patient);
				}
			}
			
			//matched.addAll(patientActiveVisits.keySet());
			
			
			Collections.sort(matched, new PersonByNameComparator()); // Sort by
																		// person
																		// name
		}
		else {
			if ("all".equals(which)) {
				matched = matchedByNameOrID;
			} else if ("checked-in".equals(which)) {
				for (Patient patient : matchedByNameOrID) {
					if (patientActiveVisits.containsKey(patient)) {
						matched.add(patient);
					}
				}
			}
			else if ("screening-register".equals(which))
			{
				for (Patient patient : matchedByNameOrID) 
				{
					if (!patientActiveVisits.containsKey(patient))
						continue;
					
					List<Encounter> prevEncounters = Context
							.getEncounterService().getEncountersByPatient(patient);
					if (!prevEncounters.isEmpty())
					{
						Encounter lastEncounter = prevEncounters.get(prevEncounters.size()-1);
						EncounterType screeningEncType = MetadataUtils.existing(
								EncounterType.class,
								HcvMetadata._EncounterType.SCREENING_REGISTRATION);
						
						if (lastEncounter.getEncounterType().equals(screeningEncType))
							matched.add(patient);
					}
				}
			}
			else if ("non-accounts".equals(which)) {
				Set<Person> accounts = new HashSet<Person>();
				accounts.addAll(getUsersByPersons(query).keySet());
				accounts.addAll(getProvidersByPersons(query).keySet());

				for (Patient patient : matchedByNameOrID) {
					if (!accounts.contains(patient)) {
						matched.add(patient);
					}
				}
			}
		}

		// Simplify and attach active visits to patient objects
		List<SimpleObject> simplePatients = new ArrayList<SimpleObject>();
		for (Patient patient : matched) {
			SimpleObject simplePatient = ui.simplifyObject(patient);

			Visit activeVisit = patientActiveVisits.get(patient);
			simplePatient
					.put("activeVisit",
							activeVisit != null ? ui
									.simplifyObject(activeVisit) : null);
			simplePatient.put("patientName", patient.getGivenName());

			simplePatients.add(simplePatient);
		}

		return simplePatients;
	}

	private Date parseDate(String s) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (s == null || s.length() == 0) {
			return null;
		} else {
			if (s.length() == 10) {
				s += " 00:00:00";
			}
			return df.parse(s);
		}
	}

	/**
	 * Searches for patients by name, identifier, age, visit status
	 * 
	 * @param query
	 *            the name or identifier
	 * @param which
	 *            all|checked-in|non-accounts
	 * @param ui
	 *            the UI utils
	 * @return the simple patients
	 */
	public List<SimpleObject> patientsWithDate(
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "which", required = false, defaultValue = "all") String which,
			@RequestParam(value = "townShip", required = false) String townShip,
			UiUtils ui) {
ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);

SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm:ss");
		Date scheduledDate = null;
		if(!date.equals("")){
		try {
			scheduledDate = mysqlDateTimeFormatter.parse(date+" "+"00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
		
		// Run main patient search query based on id/name
		List<Patient> matchedByNameOrID = Context.getPatientService()
				.getPatients(query);
		if (matchedByNameOrID.size() == 0) {
			List<Patient> matchedByID = Context.getPatientService()
					.getPatients(null, query, null, true);
			matchedByNameOrID.addAll(matchedByID);
		}

		// Gather up active visits for all patients. These are attached to the
		// returned patient representations.
		Map<Patient, Visit> patientActiveVisits = getActiveVisitsByPatients();

		List<Patient> matched = new ArrayList<Patient>();

		// If query wasn't long enough to be searched on, and they've requested
		// checked-in patients, return the list
		// of checked in patients
		if (StringUtils.isBlank(query) && "checked-in".equals(which)) {
			matched.addAll(patientActiveVisits.keySet());
			Collections.sort(matched, new PersonByNameComparator()); // Sort by	person name
		}
		else if (StringUtils.isBlank(query) && "screening-register".equals(which)) {
			//matched.addAll(patientActiveVisits.keySet());
			for (Patient patient : patientActiveVisits.keySet())
			{
				List<Encounter> prevEncounters = Context
						.getEncounterService().getEncountersByPatient(patient);
				if (!prevEncounters.isEmpty())
				{
					Encounter lastEncounter = prevEncounters.get(prevEncounters.size()-1);
					EncounterType screeningEncType = MetadataUtils.existing(
							EncounterType.class,
							HcvMetadata._EncounterType.SCREENING_REGISTRATION);
					
					if (lastEncounter.getEncounterType().equals(screeningEncType))
						matched.add(patient);
				}
			}
			Collections.sort(matched, new PersonByNameComparator()); // Sort by	person name
		}
			
		else if (StringUtils.isBlank(query) && "all".equals(which)) {
			matched = matchedByNameOrID;
			Collections.sort(matched, new PersonByNameComparator()); // Sort by	person name
		} 
		else if (StringUtils.isBlank(query) && "scheduled".equals(which)) {
			matched = Context.getPatientService().getAllPatients();
			Collections.sort(matched, new PersonByNameComparator()); // Sort by	person name
		} 
		else {
			if ("all".equals(which)) {
				matched = matchedByNameOrID;
			} 
			else if ("checked-in".equals(which)) 
			{
				for (Patient patient : matchedByNameOrID) {
					if (patientActiveVisits.containsKey(patient)) {
						matched.add(patient);
					}
				}
			} 
			else if ("screening-register".equals(which)) 
			{
				for (Patient patient : matchedByNameOrID) {
					if (!patientActiveVisits.containsKey(patient)) {
						continue;
					}
					
					List<Encounter> prevEncounters = Context
							.getEncounterService().getEncountersByPatient(patient);
					if (!prevEncounters.isEmpty())
					{
						Encounter lastEncounter = prevEncounters.get(prevEncounters.size()-1);
						EncounterType screeningEncType = MetadataUtils.existing(
								EncounterType.class,
								HcvMetadata._EncounterType.SCREENING_REGISTRATION);
						
						if (lastEncounter.getEncounterType().equals(screeningEncType))
							matched.add(patient);
					}
				}
			} 
			else if ("scheduled".equals(which)) {
				
			}
			else if ("non-accounts".equals(which)) {
				Set<Person> accounts = new HashSet<Person>();
				accounts.addAll(getUsersByPersons(query).keySet());
				accounts.addAll(getProvidersByPersons(query).keySet());

				for (Patient patient : matchedByNameOrID) {
					if (!accounts.contains(patient)) {
						matched.add(patient);
					}
				}
			}
		}
		
		Set<Patient> searchedPatients=new LinkedHashSet<Patient>();
		/*
		if (StringUtils.isNotEmpty(townShip) && StringUtils.isNotEmpty(date)) {
			List<PersonAddress> listPersonAddress=new ArrayList<PersonAddress>();
			if(!townShip.equals("")){
        	listPersonAddress=chaiEmrService.getPatientsByTownship(townShip);
			}
        	List<Obs> obsList=chaiEmrService.getObsByScheduledDate(scheduledDate);
        	for(PersonAddress personAddress:listPersonAddress){
        		Patient patient=Context.getPatientService().getPatientOrPromotePerson(personAddress.getPerson().getPersonId());
        		searchedPatients.add(patient);
        	}
        	for(Obs obs:obsList){
        		searchedPatients.add(obs.getPatient());
        	}	
		}
        else if(StringUtils.isNotEmpty(townShip) || StringUtils.isNotEmpty(date)){
        	List<PersonAddress> listPersonAddress=new ArrayList<PersonAddress>();
        	if(!townShip.equals("")){
        	listPersonAddress=chaiEmrService.getPatientsByTownship(townShip);
        	}
        	List<Obs> obsList=chaiEmrService.getObsByScheduledDate(scheduledDate);
        	for(PersonAddress personAddress:listPersonAddress){
        		Patient patient=Context.getPatientService().getPatientOrPromotePerson(personAddress.getPerson().getPersonId());
        		searchedPatients.add(patient);
        	}
        	for(Obs obs:obsList){
        		searchedPatients.add(obs.getPatient());
        	}		
        }
        else{
        	for(Patient match:matched){
    			searchedPatients.add(match);	
    		}	
        }*/
		if ("scheduled".equals(which)) {
			List<Obs> obsList=chaiEmrService.getObsByScheduledDate(scheduledDate);	
			for(Obs obs:obsList){
				if(!obs.getVoided()){
					searchedPatients.add(obs.getPatient());	
				}
				
        	}	
		}
		else{
        	for(Patient match:matched){
    			searchedPatients.add(match);	
    		}	
        }

		List<SimpleObject> simplePatients = new ArrayList<SimpleObject>();	
		// Simplify and attach active visits to patient objects
			for (Patient patient : searchedPatients) {
				SimpleObject simplePatient = ui.simplifyObject(patient);

				/*
				 * List<Visit> visits =
				 * Context.getVisitService().getActiveVisitsByPatient(patient);
				 * for(Visit v : visits) {
				 * if(v.getVisitType().getName().equalsIgnoreCase
				 * (EmrWebConstants.VISIT_TYPE_NEW_PATIENT)){
				 * simplePatient.put("newVisit", "true"); break; } }
				 */

				Visit activeVisit = patientActiveVisits.get(patient);
				simplePatient
						.put("activeVisit", ui.simplifyObject(activeVisit));
				simplePatient.put("patientName", patient.getGivenName());
				if (activeVisit != null) {
					Collection<VisitAttribute> attrs = activeVisit
							.getActiveAttributes();
					if (attrs != null && attrs.size() > 0) {
						for (VisitAttribute attr : attrs) {
							if (attr.getAttributeType()
									.getUuid()
									.equals(CommonMetadata._VisitAttributeType.NEW_PATIENT)) {
								simplePatient.put("newVisit", attr.getValue()
										.toString());
								break;
							}
						}
					}
				}
		simplePatients.add(simplePatient);
		}

		return simplePatients;
	}

	public List<SimpleObject> patientsWithDispensingDate(
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "which", required = false, defaultValue = "all") String which,
			UiUtils ui, HttpServletRequest request) {
		Date dispensedDate = null;
		try {
			dispensedDate = parseDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Run main patient search query based on id/name
		List<Patient> matchedByNameOrID = Context.getPatientService()
				.getPatients(query);
		if (matchedByNameOrID.size() == 0) {
			List<Patient> matchedByID = Context.getPatientService()
					.getPatients(null, query, null, true);
			matchedByNameOrID.addAll(matchedByID);
		}

		List<Patient> matched = new ArrayList<Patient>();
		List<SimpleObject> simplePatientsJason = new ArrayList<SimpleObject>();

		ChaiEmrService chaiEmrService = (ChaiEmrService) Context
				.getService(ChaiEmrService.class);

		if (query != "" && date != "") {
			Map<Patient, Order> drugOrders1 = getDrugOrders(dispensedDate);
			Map<Patient, Obs> drugOrders2 = getObsDrugOrders(dispensedDate);
			for (Patient patient : matchedByNameOrID) {
				if (drugOrders1.containsKey(patient)) {
					matched.add(patient);
				}
				if (drugOrders2.containsKey(patient)) {
					matched.add(patient);
				}
			}
		} else if (query != "") {
			matched = matchedByNameOrID;
		} else if (date != "") {
			List<Order> drugOrders1 = chaiEmrService
					.getOrderByDateAndOrderType(
							dispensedDate,
							Context.getOrderService().getOrderTypeByUuid(
									"131168f4-15f5-102d-96e4-000c29c2a5d7"));
			List<Obs> obss = chaiEmrService.getObsGroupByDate(dispensedDate);
			for (Order drugOrder : drugOrders1) {
				matched.add(drugOrder.getPatient());
			}
			for (Obs obs : obss) {
				if(!obs.getVoided()){
					matched.add(obs.getPatient());	
				}
			}
		}

		Collections.sort(matched, new PersonByNameComparator());

		Set<Patient> matchedd = new LinkedHashSet<Patient>();
		
		for (Patient patient : matched) {
			matchedd.add(patient);
		}
		
		for (Patient patient : matchedd) {
			SimpleObject simplePatientt = ui.simplifyObject(patient);
			simplePatientt.put("patientName", patient.getGivenName());
			simplePatientsJason.add(simplePatientt);
		}

		HttpSession session = request.getSession();
		session.setAttribute("dispensedDate", date);

		return simplePatientsJason;
	}

	public List<SimpleObject> patientsWithPastDispensingDate(
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "which", required = false, defaultValue = "all") String which,
			UiUtils ui, HttpServletRequest request) {
		Date processedDate = null;
		try {
			processedDate = parseDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Run main patient search query based on id/name
		List<Patient> matchedByNameOrID = Context.getPatientService()
				.getPatients(query);
		if (matchedByNameOrID.size() == 0) {
			List<Patient> matchedByID = Context.getPatientService()
					.getPatients(null, query, null, true);
			matchedByNameOrID.addAll(matchedByID);
		}

		List<Patient> matched = new ArrayList<Patient>();
		List<SimpleObject> simplePatientsJasonn = new ArrayList<SimpleObject>();

		ChaiEmrService chaiEmrService = (ChaiEmrService) Context
				.getService(ChaiEmrService.class);

		if (query != "" && date != "") {
			List<DrugOrderProcessed> dops = chaiEmrService.getDrugOrdersByProcessedDate(processedDate);
			List<DrugObsProcessed> dobps = chaiEmrService.getObsDrugOrdersByProcessedDate(processedDate);
			
			Map<Patient, DrugOrderProcessed> drugOrderProcessed = new HashMap<Patient, DrugOrderProcessed>();
			Map<Patient, DrugObsProcessed> drugObsProcessed = new HashMap<Patient, DrugObsProcessed>();
			
			for (DrugOrderProcessed dop : dops) {
				drugOrderProcessed.put(dop.getPatient(), dop);
			}
			
			for (DrugObsProcessed dobp : dobps) {
				drugObsProcessed.put(dobp.getPatient(), dobp);
			}
			
			for (Patient patient : matchedByNameOrID) {
				if (drugOrderProcessed.containsKey(patient)) {
					matched.add(patient);
				}
				if (drugObsProcessed.containsKey(patient)) {
					matched.add(patient);
				}
			}
		} else if (query != "") {
			matched = matchedByNameOrID;
		} else if (date != "") {
			List<DrugOrderProcessed> dops = chaiEmrService.getDrugOrdersByProcessedDate(processedDate);
			List<DrugObsProcessed> dobps = chaiEmrService.getObsDrugOrdersByProcessedDate(processedDate);
			for (DrugOrderProcessed dop : dops) {
				matched.add(dop.getPatient());
			}
			for (DrugObsProcessed dobp : dobps) {
				matched.add(dobp.getPatient());
			}
		}

		Collections.sort(matched, new PersonByNameComparator());
		
        Set<Patient> matchedd = new LinkedHashSet<Patient>();
		
		for (Patient patient : matched) {
			matchedd.add(patient);
		}
		
		Integer count=0;
		for (Patient patient : matchedd) {
			SimpleObject simplePatientt = ui.simplifyObject(patient);
			simplePatientt.put("patientName", patient.getGivenName());
			//simplePatientt.put("patientId", patient.getPatientId());
			
			if(patient.getPatientIdentifier("Patient ID")!=null){
			simplePatientt.put("patientIdentifier", patient.getPatientIdentifier("Patient ID").getIdentifier());
			}
			else{
				simplePatientt.put("patientIdentifier", " ");
			}
			simplePatientt.put("count", ++count);
			simplePatientsJasonn.add(simplePatientt);
		}

		HttpSession session = request.getSession();
		session.setAttribute("processedDate", date);

		return simplePatientsJasonn;
	}

	/**
	 * Gets a location by it's id
	 * 
	 * @param location
	 *            the location
	 * @param ui
	 *            the UI utils
	 * @return the simplified location
	 */
	public SimpleObject location(@RequestParam("id") Location location,
			UiUtils ui) {
		return ui.simplifyObject(location);
	}

	/**
	 * Searches for locations by name or MFL code
	 * 
	 * @param query
	 *            the search query
	 * @param ui
	 *            the UI utils
	 * @return the simplified locations
	 */
	public SimpleObject[] locations(@RequestParam("q") String query, UiUtils ui) {
		LocationService svc = Context.getLocationService();

		// Results will be sorted by name
		Set<Location> results = new TreeSet<Location>(
				new Comparator<Location>() {
					@Override
					public int compare(Location location1, Location location2) {
						return location1.getName().compareTo(
								location2.getName());
					}
				});

		// If term looks like an MFL code, add location with that code
		if (StringUtils.isNumeric(query) && query.length() >= 5) {
			Location locationByMflCode = Context.getService(
					ChaiEmrService.class).getLocationByMflCode(query);
			if (locationByMflCode != null) {
				results.add(locationByMflCode);
			}
		}

		// Add first 20 results of search by name
		if (StringUtils.isNotBlank(query)) {
			results.addAll(svc.getLocations(query, true, 0, 20));
		}

		// Convert to simple objects
		return ui.simplifyCollection(results);
	}

	/**
	 * Gets a person by their id
	 * 
	 * @param person
	 *            the person
	 * @param ui
	 *            the UI utils
	 * @return the simplified person
	 */
	public SimpleObject person(@RequestParam("id") Person person, UiUtils ui) {
		return ui.simplifyObject(person);
	}

	/**
	 * Searches for persons by name
	 * 
	 * @param query
	 *            the name query
	 * @param ui
	 *            the UI utils
	 * @return the simplified persons
	 */
	public SimpleObject[] persons(
			@RequestParam(value = "q", required = false) String query,
			UiUtils ui) {
		Collection<Person> results = Context.getPersonService().getPeople(
				query, null);

		// Convert to simple objects
		return ui.simplifyCollection(results);
	}

	/**
	 * Gets a provider by their id
	 * 
	 * @param provider
	 *            the provider
	 * @param ui
	 *            the UI utils
	 * @return the simplified provider
	 */
	public SimpleObject provider(@RequestParam("id") Provider provider,
			UiUtils ui) {
		return ui.simplifyObject(provider);
	}

	/**
	 * Searches for providers by name
	 * 
	 * @param query
	 *            the name query
	 * @param ui
	 *            the UI utils
	 * @return the simplified providers
	 */
	public SimpleObject[] providers(
			@RequestParam(value = "q", required = false) String query,
			UiUtils ui) {
		Collection<Provider> results = Context.getProviderService()
				.getProviders(query, null, null, null);

		// Convert to simple objects
		return ui.simplifyCollection(results);
	}

	/**
	 * Searches for accounts by name
	 * 
	 * @param query
	 *            the name query
	 * @param which
	 *            all|providers|users|non-patients
	 * @param ui
	 * @return
	 */
	public List<SimpleObject> accounts(
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "which", required = false, defaultValue = "all") String which,
			HttpSession session, UiUtils ui) {

		Map<Person, User> userAccounts = new HashMap<Person, User>();
		Map<Person, Provider> providerAccounts = new HashMap<Person, Provider>();

		if (!"providers".equals(which)) {
			userAccounts = getUsersByPersons(query);
		}

		if (!"users".equals(which)) {
			providerAccounts = getProvidersByPersons(query);
		}

		Set<Person> persons = new TreeSet<Person>(new PersonByNameComparator());
		persons.addAll(userAccounts.keySet());
		persons.addAll(providerAccounts.keySet());

		Set<String> onlineUsers = new HashSet<String>(
				CurrentUsers.getCurrentUsernames(session));

		List<SimpleObject> ret = new ArrayList<SimpleObject>();
		for (Person p : persons) {
			if ("non-patients".equals(which) && p.isPatient()) {
				continue;
			}

			// Simplify person first
			SimpleObject account = ui.simplifyObject(p);

			User user = userAccounts.get(p);
			if (user != null) {
				boolean online;
				String username = user.getUsername();

				// Admin account doesn't have a username
				if (StringUtils.isBlank(username)) {
					online = onlineUsers.contains("systemid:"
							+ user.getSystemId());
					username = user.getSystemId();
				} else {
					online = onlineUsers.contains(username);
				}

				account.put("user", SimpleObject.create("id", user.getId(),
						"username", username, "online", online));
			}

			Provider provider = providerAccounts.get(p);
			if (provider != null) {
				account.put("provider",
						SimpleObject.fromObject(provider, ui, "identifier"));
			}

			ret.add(account);
		}

		return ret;
	}

	/**
	 * Gets a concept by it's id
	 * 
	 * @param concept
	 *            the concept
	 * @param ui
	 *            the UI utils
	 * @return the simplified concept
	 */
	public SimpleObject concept(@RequestParam("id") Concept concept, UiUtils ui) {
		return ui.simplifyObject(concept);
	}

	/**
	 * Searches for concept by name and class
	 * 
	 * @param query
	 *            the name query
	 * @param conceptClass
	 *            the concept class
	 * @param ui
	 *            the UI utils
	 * @return the simplified concepts
	 */
	public List<SimpleObject> concepts(
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "class", required = false) ConceptClass conceptClass,
			@RequestParam(value = "answerTo", required = false) Concept answerTo,
			@RequestParam(value = "size", required = false) Integer size,
			UiUtils ui) {

		ConceptService conceptService = Context.getConceptService();

		List<ConceptClass> conceptClasses = conceptClass != null ? Collections
				.singletonList(conceptClass) : null;

		List<ConceptSearchResult> results = conceptService.getConcepts(query,
				Collections.singletonList(CoreConstants.LOCALE), false,
				conceptClasses, null, null, null, answerTo, 0, size);

		// Simplify results
		List<SimpleObject> simpleConcepts = new ArrayList<SimpleObject>();
		for (ConceptSearchResult result : results) {
			simpleConcepts.add(ui.simplifyObject(result.getConcept()));
		}

		return simpleConcepts;
	}

	/**
	 * Helper method to get all active visits organised by patient
	 * 
	 * @return the map of patients to active visits
	 */
	protected Map<Patient, Visit> getActiveVisitsByPatients() {
		Collection<VisitType> visitType = Context.getVisitService()
				.getVisitTypes("Follow up visit");
		List<Visit> activeVisits = Context.getVisitService().getVisits(
				visitType, null, null, null, null, null, null, null, null,
				false, false);
		Map<Patient, Visit> patientToVisits = new HashMap<Patient, Visit>();
		for (Visit visit : activeVisits) {
			patientToVisits.put(visit.getPatient(), visit);
		}
		return patientToVisits;
	}

	protected Map<Patient, Order> getDrugOrders(Date date) {
		ChaiEmrService chaiEmrService = (ChaiEmrService) Context
				.getService(ChaiEmrService.class);
		List<Order> orders = chaiEmrService.getOrderByDateAndOrderType(
				date,
				Context.getOrderService().getOrderTypeByUuid(
						"131168f4-15f5-102d-96e4-000c29c2a5d7"));
		Map<Patient, Order> drugOrders = new HashMap<Patient, Order>();
		for (Order order : orders) {
			drugOrders.put(order.getPatient(), order);
		}
		return drugOrders;
	}

	protected Map<Patient, Obs> getObsDrugOrders(Date date) {
		ChaiEmrService chaiEmrService = (ChaiEmrService) Context
				.getService(ChaiEmrService.class);
		List<Obs> orders = chaiEmrService.getObsGroupByDate(date);
		Map<Patient, Obs> drugOrders = new HashMap<Patient, Obs>();
		for (Obs order : orders) {
			drugOrders.put(order.getPatient(), order);
		}
		return drugOrders;
	}

	/**
	 * Helper method to get users organised by person
	 * 
	 * @param query
	 *            the name query
	 * @return the map of persons to users
	 */
	protected Map<Person, User> getUsersByPersons(String query) {
		Map<Person, User> personToUsers = new HashMap<Person, User>();
		for (User user : Context.getUserService().getUsers(query, null, true)) {
			if (!"daemon".equals(user.getUsername())) {
				personToUsers.put(user.getPerson(), user);
			}
		}
		return personToUsers;
	}

	/**
	 * Helper method to get all providers organised by person
	 * 
	 * @param query
	 *            the name query
	 * @return the map of persons to providers
	 */
	protected Map<Person, Provider> getProvidersByPersons(String query) {
		Map<Person, Provider> personToProviders = new HashMap<Person, Provider>();
		List<Provider> providers = Context.getProviderService().getProviders(
				query, null, null, null);
		for (Provider p : providers) {
			if (p.getPerson() != null) {
				personToProviders.put(p.getPerson(), p);
			}
		}
		return personToProviders;
	}

	/**
	 * Gets the minimum number of query characters required for a service search
	 * method
	 * 
	 * @return the value of min search characters
	 */
	protected static int getMinSearchCharacters() {
		int minSearchCharacters = OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_MIN_SEARCH_CHARACTERS;
		String minSearchCharactersStr = Context.getAdministrationService()
				.getGlobalProperty(
						OpenmrsConstants.GLOBAL_PROPERTY_MIN_SEARCH_CHARACTERS);

		try {
			minSearchCharacters = Integer.valueOf(minSearchCharactersStr);
		} catch (NumberFormatException e) {
			// do nothing
		}
		return minSearchCharacters;
	}
}