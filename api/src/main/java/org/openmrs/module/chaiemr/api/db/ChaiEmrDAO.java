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

package org.openmrs.module.chaiemr.api.db;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.module.chaiemr.model.DrugInfo;
import org.openmrs.module.chaiemr.model.DrugObsProcessed;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;

/**
 * Database access functions
 */
public interface ChaiEmrDAO {
	public List<Object> executeSqlQuery(String query, Map<String, Object> substitutions);
	public List<Object> executeHqlQuery(String query, Map<String, Object> substitutions);
	/**
	 * Get last encounter
	 * @param patient
	 * @return
	 */
	public Encounter getFirstEncounterByDateTime(Patient patient,Visit visit);
	public Encounter getFirstEncounterByCreatedDateTime(Patient patient,Visit visit);
	public Encounter getLastEncounterByDateTime(Patient patient,Visit visit);
	public Encounter getLastEncounterByCreatedDateTime(Patient patient,Visit visit);
	public Encounter getLastEncounterByDateTime(Patient patient,Set<EncounterType> encounterTypes);
	public Encounter getLastEncounterByCreatedDateTime(Patient patient,Set<EncounterType> encounterTypes);
	public List<Order> getOrderByDateAndOrderType(Date date,OrderType orderType);
	public List<Obs> getObsGroupByDate(Date date);
	public List<Obs> getObsGroupByDateAndPerson(Date date,Person person);
	public List<Obs> getObsByObsGroup(Obs obsGroup);
	public Obs saveOrUpdateObs(Obs obs);
	public DrugOrderProcessed saveDrugOrderProcessed(DrugOrderProcessed drugOrderProcessed);
	public DrugObsProcessed saveDrugObsProcessed(DrugObsProcessed drugObsProcessed);
	public DrugOrderProcessed getDrugOrderProcessed(DrugOrder drugOrder);
	public DrugOrderProcessed getLastDrugOrderProcessed(DrugOrder drugOrder);
	public DrugOrderProcessed getLastDrugOrderProcessedNotDiscontinued(DrugOrder drugOrder);
	public List<DrugOrderProcessed> getDrugOrderProcessedCompleted(DrugOrder drugOrder);
	public DrugOrderProcessed getDrugOrderProcesedById(Integer id);
	public List<DrugOrderProcessed> getDrugOrdersByProcessedDate(Date date);
	public List<DrugObsProcessed> getObsDrugOrdersByProcessedDate(Date date);
	public List<DrugOrderProcessed> getDrugOrdersByPatientAndProcessedDate(Patient patient,Date processedDate);
	public List<DrugObsProcessed> getObsDrugOrdersByPatientAndProcessedDate(Patient patient,Date processedDate);
	public List<DrugInfo> getDrugInfo();
	public DrugInfo getDrugInfo(String drugName);
	public DrugOrderProcessed getLastRegimenChangeType(Patient patient);
	public List<ConceptAnswer> getConceptAnswerByAnsweConcept(Concept answerConcept);
	public List<DrugOrderProcessed> getAllfirstLine();
	public List<PersonAddress> getPatientsByTownship(String township);
	public List<Obs> getObsByScheduledDate(Date date);
	public Set<Patient> getPatientProgram(Program program,String startDate,String endDate);
	public Set<Patient> getNoOfPatientTransferredIn(String startDate,String endDate);
	public Set<Patient> getNoOfPatientTransferredOut(String startDate,String endDate);
	public Visit getVisitsByPatient(Patient patient);
	public Set<Patient> getTotalNoOfCohort(String startDate,String endDate);
	public Set<Patient> getCohortBasedOnGender(String gender,String startDate,String endDate);
	public Set<Patient> getCohortBasedOnAge(Integer age1,Integer age2,String startDate,String endDate);
	public Set<Patient> getNoOfCohortAliveAndOnArt(Program program,String startDate,String endDate);
	public Set<Patient> getOriginalFirstLineRegimen(Program program,String startDate,String endDate);
	public Set<Patient> getAlternateFirstLineRegimen(Program program,String startDate,String endDate);
	public Set<Patient> getSecondLineRegimen(Program program,String startDate,String endDate);
	public Set<Patient> getNoOfArtStoppedCohort(Program program,String startDate,String endDate);
	public Set<Patient> getNoOfArtDiedCohort(Program program,String startDate,String endDate);
	public Set<Patient> getNoOfPatientLostToFollowUp(String startDate,String endDate);
	public List<Obs> getNoOfPatientWithCD4(String startDate,String endDate);
	public List<Obs> getNoOfPatientNormalActivity(String startDate,String endDate);
	public List<Obs> getNoOfPatientBedriddenLessThanFifty(String startDate,String endDate);
	public List<Obs> getNoOfPatientBedriddenMoreThanFifty(String startDate,String endDate);
	public Set<Patient> getNoOfPatientPickedUpArvForSixMonth(String startDate,String endDate);
	public Set<Patient> getNoOfPatientPickedUpArvForTwelveMonth(String startDate,String endDate);
	public List<DrugOrderProcessed> getDrugOrderProcessedByPatient(Patient patient);
}