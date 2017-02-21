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

package org.openmrs.module.chaiemr.api.db.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.chaiemr.api.db.ChaiEmrDAO;
import org.openmrs.module.chaiemr.model.DrugInfo;
import org.openmrs.module.chaiemr.model.DrugObsProcessed;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;

/**
 * Hibernate specific data access functions. This class should not be used directly.
 */
@SuppressWarnings("deprecation")
public class HibernateChaiEmrDAO implements ChaiEmrDAO {

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	SimpleDateFormat formatterExt = new SimpleDateFormat("yyyy-MM-dd");
	
	private SessionFactory sessionFactory;

	/**
	 * Sets the session factory
	 * @param sessionFactory the session factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Convenience method to get current session
	 * @return the session
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Object> executeSqlQuery(String query, Map<String, Object> substitutions) {
		SQLQuery q = sessionFactory.getCurrentSession().createSQLQuery(query);

		for (Map.Entry<String, Object> e : substitutions.entrySet()) {
			if (e.getValue() instanceof Collection) {
				q.setParameterList(e.getKey(), (Collection) e.getValue());
			} else if (e.getValue() instanceof Object[]) {
				q.setParameterList(e.getKey(), (Object[]) e.getValue());
			} else if (e.getValue() instanceof Cohort) {
				q.setParameterList(e.getKey(), ((Cohort) e.getValue()).getMemberIds());
			} else if (e.getValue() instanceof Date) {
				q.setDate(e.getKey(), (Date) e.getValue());
			} else {
				q.setParameter(e.getKey(), e.getValue());
			}


		}

		q.setReadOnly(true);

		List<Object> r = q.list();
		return r;
	}

	@Override
	public List<Object> executeHqlQuery(String query, Map<String, Object> substitutions) {
		Query q = sessionFactory.getCurrentSession().createQuery(query);

		applySubstitutions(q, substitutions);

		// optimizations go here
		q.setReadOnly(true);

		return q.list();
	}

	private void applySubstitutions(Query q, Map<String, Object> substitutions) {
		for (Map.Entry<String, Object> e : substitutions.entrySet()) {
			if (e.getValue() instanceof Collection) {
				q.setParameterList(e.getKey(), (Collection) e.getValue());
			} else if (e.getValue() instanceof Object[]) {
				q.setParameterList(e.getKey(), (Object[]) e.getValue());
			} else if (e.getValue() instanceof Cohort) {
				q.setParameterList(e.getKey(), ((Cohort) e.getValue()).getMemberIds());
			} else if (e.getValue() instanceof Date) {
				q.setDate(e.getKey(), (Date) e.getValue());
			} else {
				q.setParameter(e.getKey(), e.getValue());
			}
		}
	}
	
	/*
	 * ENCOUNTER
	 */
	public Encounter getFirstEncounterByDateTime(Patient patient,Visit visit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Encounter.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.eq("visit", visit));
		criteria.addOrder(Order.asc("encounterDatetime"));
		criteria.setMaxResults(1);
		return (Encounter) criteria.uniqueResult();
	}
	
	public Encounter getFirstEncounterByCreatedDateTime(Patient patient,Visit visit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Encounter.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.eq("visit", visit));
		criteria.addOrder(Order.asc("dateCreated"));
		criteria.setMaxResults(1);
		return (Encounter) criteria.uniqueResult();
	}
	
	public Encounter getLastEncounterByDateTime(Patient patient,Visit visit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Encounter.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.eq("visit", visit));
		criteria.addOrder(Order.desc("encounterDatetime"));
		criteria.setMaxResults(1);
		return (Encounter) criteria.uniqueResult();
	}
	
	public Encounter getLastEncounterByCreatedDateTime(Patient patient,Visit visit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Encounter.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.eq("visit", visit));
		criteria.addOrder(Order.desc("dateCreated"));
		criteria.setMaxResults(1);
		return (Encounter) criteria.uniqueResult();
	}
	
	public Encounter getLastEncounterByDateTime(Patient patient,Set<EncounterType> encounterTypes) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Encounter.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.in("encounterType", encounterTypes));
		criteria.addOrder(Order.desc("encounterDatetime"));
		criteria.setMaxResults(1);
		return (Encounter) criteria.uniqueResult();
	}
	
	public Encounter getLastEncounterByCreatedDateTime(Patient patient,Set<EncounterType> encounterTypes) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Encounter.class);
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.in("encounterType", encounterTypes));
		criteria.addOrder(Order.desc("dateCreated"));
		criteria.setMaxResults(1);
		return (Encounter) criteria.uniqueResult();
	}
	
	public List<org.openmrs.Order> getOrderByDateAndOrderType(Date date,OrderType orderType) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(org.openmrs.Order.class,"order");
		criteria.add(Restrictions.eq("order.orderType", orderType));
		if(date!=null){
		String datee = formatterExt.format(date);
		String startFromDate = datee + " 00:00:00";
		String endFromDate = datee + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("order.startDate", formatter.parse(startFromDate)),
				    Restrictions.le("order.startDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
		return criteria.list();
	}
	
	public List<Obs> getObsGroupByDate(Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		if(date!=null){
		String dat = formatterExt.format(date);
		String startFromDate = dat + " 00:00:00";
		String endFromDate = dat + " 23:59:59";
		Concept concept1=Context.getConceptService().getConceptByUuid("163021AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept concept2=Context.getConceptService().getConceptByUuid("163022AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept concept3=Context.getConceptService().getConceptByUuid("163023AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		List<Concept> obsGroupCollection=new LinkedList<Concept>();
		obsGroupCollection.add(concept1);
		obsGroupCollection.add(concept2);
		obsGroupCollection.add(concept3);
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obs.dateCreated", formatter.parse(startFromDate)),
				    Restrictions.le("obs.dateCreated", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.add(Restrictions.in("obs.concept", obsGroupCollection));
		}
		criteria.add(Restrictions.isNull("comment"));
		return criteria.list();
	}
	
	public List<Obs> getObsGroupByDateAndPerson(Date date,Person person) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		criteria.add(Restrictions.eq("obs.person", person));
		Concept concept1=Context.getConceptService().getConceptByUuid("163021AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept concept2=Context.getConceptService().getConceptByUuid("163022AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept concept3=Context.getConceptService().getConceptByUuid("163023AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		List<Concept> obsGroupCollection=new LinkedList<Concept>();
		obsGroupCollection.add(concept1);
		obsGroupCollection.add(concept2);
		obsGroupCollection.add(concept3);
		if(date!=null){
		String dat = formatterExt.format(date);
		String startFromDate = dat + " 00:00:00";
		String endFromDate = dat + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obs.dateCreated", formatter.parse(startFromDate)),
				    Restrictions.le("obs.dateCreated", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
		criteria.add(Restrictions.in("obs.concept", obsGroupCollection));
		criteria.add(Restrictions.isNull("comment"));
		return criteria.list();
	}
	
	public List<Obs> getObsByObsGroup(Obs obsGroup) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		criteria.add(Restrictions.eq("obs.obsGroup", obsGroup));
		return criteria.list();
	}
	
	public Obs saveOrUpdateObs(Obs obs) throws DAOException {
		return (Obs) sessionFactory.getCurrentSession().merge(obs);
	}
	
	public DrugOrderProcessed saveDrugOrderProcessed(DrugOrderProcessed drugOrderProcessed) throws DAOException {
		return (DrugOrderProcessed) sessionFactory.getCurrentSession().merge(drugOrderProcessed);
	}
	
	public DrugObsProcessed saveDrugObsProcessed(DrugObsProcessed drugObsProcessed) throws DAOException {
		return (DrugObsProcessed) sessionFactory.getCurrentSession().merge(drugObsProcessed);
	}
	
	public DrugOrderProcessed getDrugOrderProcessed(DrugOrder drugOrder) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("drugOrder", drugOrder));
		criteria.add(Restrictions.eq("processedStatus", false));
		criteria.add(Restrictions.isNull("discontinuedDate"));
		return (DrugOrderProcessed) criteria.uniqueResult();
	}
	
	public DrugOrderProcessed getLastDrugOrderProcessed(DrugOrder drugOrder) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("drugOrder", drugOrder));
		criteria.addOrder(Order.desc("createdDate"));
		criteria.setMaxResults(1);
		return (DrugOrderProcessed) criteria.uniqueResult();
	}
	
	public DrugOrderProcessed getLastDrugOrderProcessedByPatient(Patient patient) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("patient", patient));
		criteria.addOrder(Order.desc("createdDate"));
		criteria.setMaxResults(1);
		return (DrugOrderProcessed) criteria.uniqueResult();
	}
	
	public DrugOrderProcessed getLastDrugOrderProcessedNotDiscontinued(DrugOrder drugOrder) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("drugOrder", drugOrder));
		criteria.add(Restrictions.isNull("discontinuedDate"));
		criteria.addOrder(Order.desc("createdDate"));
		criteria.setMaxResults(1);
		return (DrugOrderProcessed) criteria.uniqueResult();
	}
	
	public List<DrugOrderProcessed> getDrugOrderProcessedCompleted(DrugOrder drugOrder) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("drugOrder", drugOrder));
		criteria.add(Restrictions.eq("processedStatus", true));
		return criteria.list();
	}
	
	public DrugOrderProcessed getDrugOrderProcesedById(Integer id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("id", id));
		return (DrugOrderProcessed) criteria.uniqueResult();
	}
	
	public List<DrugOrderProcessed> getDrugOrdersByProcessedDate(Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		String dat = formatterExt.format(date);
		String startFromDate = dat + " 00:00:00";
		String endFromDate = dat + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("processedDate", formatter.parse(startFromDate)),
					Restrictions.le("processedDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return criteria.list();
	}
	
	public List<DrugObsProcessed> getObsDrugOrdersByProcessedDate(Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugObsProcessed.class,"DrugObsProcessed");
		String dat = formatterExt.format(date);
		String startFromDate = dat + " 00:00:00";
		String endFromDate = dat + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("processedDate", formatter.parse(startFromDate)),
				    Restrictions.le("processedDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return criteria.list();
	}
	
	public List<DrugOrderProcessed> getDrugOrdersByPatientAndProcessedDate(Patient patient,Date processedDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("patient", patient));
		criteria.add(Restrictions.eq("processedStatus", true));
		if(processedDate!=null){
		String dat = formatterExt.format(processedDate);
		String startFromDate = dat + " 00:00:00";
		String endFromDate = dat + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("processedDate", formatter.parse(startFromDate)),
					Restrictions.le("processedDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
		return criteria.list();
	}
	
	public List<DrugObsProcessed> getObsDrugOrdersByPatientAndProcessedDate(Patient patient,Date processedDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugObsProcessed.class,"DrugObsProcessed");
		criteria.add(Restrictions.eq("patient", patient));
		if(processedDate!=null){
		String dat = formatterExt.format(processedDate);
		String startFromDate = dat + " 00:00:00";
		String endFromDate = dat + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("processedDate", formatter.parse(startFromDate)),
				    Restrictions.le("processedDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
		return criteria.list();
	}
	
	public List<DrugInfo> getDrugInfo() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugInfo.class,"drugInfo");
		return criteria.list();
	}
	
	public DrugInfo getDrugInfo(String drugCode) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugInfo.class,"drugInfo");
		criteria.add(Restrictions.eq("drugCode", drugCode));
		return (DrugInfo) criteria.uniqueResult();
	}
	
	public DrugOrderProcessed getLastRegimenChangeType(Patient patient) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("patient", patient));
		//criteria.add(Restrictions.isNotNull("regimenChangeType"));
		//criteria.add(Restrictions.isNotNull("discontinuedDate"));
		//criteria.addOrder(Order.desc("discontinuedDate"));
		criteria.addOrder(Order.desc("createdDate"));
		criteria.setMaxResults(1);
		return (DrugOrderProcessed) criteria.uniqueResult();
	}
	
	public List<ConceptAnswer> getConceptAnswerByAnsweConcept(Concept answerConcept) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConceptAnswer.class,"conceptAnswer");
		criteria.add(Restrictions.eq("answerConcept", answerConcept));
		return criteria.list();
	}
		@Override
	public List<DrugOrderProcessed> getAllfirstLine() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		
		criteria.add(Restrictions.isNotNull("typeOfRegimen"));
		
		return criteria.list();
	}
	
	public List<PersonAddress> getPatientsByTownship(String township) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PersonAddress.class,"personAddress");
		criteria.add(Restrictions.ilike("countyDistrict", township+"%"));
		return criteria.list();
	}
	
	public List<Obs> getObsByScheduledDate(Date date) {
	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
	Collection<Concept> conList=new	ArrayList<Concept>();
	conList.add(Context.getConceptService().getConceptByUuid("5096AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
	conList.add(Context.getConceptService().getConceptByUuid("1879AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
	criteria.add(Restrictions.in("concept", conList));
	criteria.add(Restrictions.eq("valueDatetime", date));
	return criteria.list();
	}
	
	public Set<Patient> getPatientProgram(Program program,String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PatientProgram.class,"patientProgram");
		criteria.add(Restrictions.eq("program", program));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("dateEnrolled", formatter.parse(startFromDate)),
				    Restrictions.le("dateEnrolled", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Set<Patient> patients=new HashSet<Patient>();
		List<PatientProgram> ppgms=criteria.list();
		for(PatientProgram ppgm:ppgms){
			patients.add(ppgm.getPatient());
		}
		
		return patients;
		}
	
	public Set<Patient> getNoOfPatientTransferredIn(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Collection<Concept> conList=new	ArrayList<Concept>();
		conList.add(Context.getConceptService().getConceptByUuid("4b73234a-15db-49a0-b089-c26c239fe90d"));
		conList.add(Context.getConceptService().getConceptByUuid("feee14d1-6cd6-4f5d-a3f6-056ed91526e5"));
		criteria.add(Restrictions.in("valueCoded", conList));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("obsDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Set<Patient> patients=new HashSet<Patient>();
		List<Obs> obss=criteria.list();
		for(Obs obs:obss){
			patients.add(obs.getPatient());
		}
		return patients;
		}
	
	public Set<Patient> getNoOfPatientTransferredOut(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Concept conceptTransferredOut=Context.getConceptService().getConceptByUuid("159492AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		criteria.add(Restrictions.eq("valueCoded", conceptTransferredOut));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("obsDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Set<Patient> patients=new HashSet<Patient>();
		List<Obs> obss=criteria.list();
		for(Obs obs:obss){
			patients.add(obs.getPatient());
		}
		return patients;
		}
	
	public Visit getVisitsByPatient(Patient patient) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Visit.class,"visit");
		criteria.add(Restrictions.eq("patient", patient));
		criteria.addOrder(Order.asc("startDatetime"));
		criteria.setMaxResults(1);
		return (Visit) criteria.uniqueResult();
		}
	
	public Set<Patient> getTotalNoOfCohort(String startDate,String endDate) {
		Program program=Context.getProgramWorkflowService().getProgramByUuid("96ec813f-aaf0-45b2-add6-e661d5bf79d6");
		Set<Patient> paientOnART=getPatientProgram(program,startDate,endDate);
		Set<Patient> paientTransferredOut=getNoOfPatientTransferredOut(startDate,endDate);
		Set<Patient> totalCohort=new LinkedHashSet<Patient>();
		for(Patient patient:paientOnART){
		if(paientTransferredOut.contains(patient)){
			
		}
		else{
			totalCohort.add(patient);	
		}
		}
		return totalCohort;
		}
	
	public Set<Patient> getCohortBasedOnGender(String gender,String startDate,String endDate) {
		Program program=Context.getProgramWorkflowService().getProgramByUuid("96ec813f-aaf0-45b2-add6-e661d5bf79d6");
		Set<Patient> patientOnCohort=getPatientProgram(program,startDate,endDate);
		Set<Patient> paientTransferredOut=getNoOfPatientTransferredOut(startDate,endDate);
		
		List<Person> personList=getListOfPatient(gender);
		List<Patient> patientList=new LinkedList<Patient>();
		for(Person person:personList){
			Patient patient=(Patient) person;
			patientList.add(patient);
		}
		
		Set<Patient> cohortAfterTransferredOut=new LinkedHashSet<Patient>();
		Set<Patient> cohortBasedOnGender=new LinkedHashSet<Patient>();
		for(Patient patient:patientOnCohort){
		if(paientTransferredOut.contains(patient)){
			
		}
		else{
			cohortAfterTransferredOut.add(patient);	
		}
		}
		
		for(Patient patient:cohortAfterTransferredOut){
		if(patientList.contains(patient)){
			cohortBasedOnGender.add(patient);			
		}
		}
		return cohortBasedOnGender;
		}
	
	public Set<Patient> getCohortBasedOnAge(Integer age1,Integer age2,String startDate,String endDate) {
		Program program=Context.getProgramWorkflowService().getProgramByUuid("96ec813f-aaf0-45b2-add6-e661d5bf79d6");
		Set<Patient> patientOnCohort=getPatientProgram(program,startDate,endDate);
		Set<Patient> paientTransferredOut=getNoOfPatientTransferredOut(startDate,endDate);
		
		List<Person> personList=getListOfPatient(age1,age2);
		List<Patient> patientList=new LinkedList<Patient>();
		for(Person person:personList){
			if(person.getAge()>=age1 && person.getAge()<=age2){
			Patient patient=(Patient) person;
			patientList.add(patient);
			}
		}
		
		Set<Patient> cohortAfterTransferredOut=new LinkedHashSet<Patient>();
		Set<Patient> cohortBasedOnAge=new LinkedHashSet<Patient>();
		for(Patient patient:patientOnCohort){
		if(paientTransferredOut.contains(patient)){
			
		}
		else{
			cohortAfterTransferredOut.add(patient);	
		}
		}
		
		for(Patient patient:cohortAfterTransferredOut){
		if(patientList.contains(patient)){
			cohortBasedOnAge.add(patient);			
		}
		}
		return cohortBasedOnAge;
		}
	
	public Set<Patient> getNoOfCohortAliveAndOnArt(Program program,String startDate,String endDate) {
		Set<Patient> patients=new LinkedHashSet<Patient>();
		Set<Patient> noOfArtStoppedCohorts=getNoOfArtStoppedCohort(program,startDate,endDate);
		Set<Patient> noOfArtDiedCohorts=getNoOfArtDiedCohort(program,startDate,endDate);
		Set<Patient> noOfPatientLostToFollowUps=getNoOfPatientLostToFollowUp(startDate,endDate);
		Set<Patient> transferredOutPatient=getNoOfPatientTransferredOut(startDate,endDate);
		Set<Patient> noOfHIVStoppedCohorts=getNoOfHIVStoppedCohort(startDate,endDate);
		
		patients.addAll(noOfArtStoppedCohorts);
		patients.addAll(noOfArtDiedCohorts);
		patients.addAll(noOfPatientLostToFollowUps);
		patients.addAll(transferredOutPatient);
		patients.addAll(noOfHIVStoppedCohorts);
		
		Set<Patient> totalCohort=getTotalNoOfCohort(startDate,endDate);
		Set<Patient> patientSet=new LinkedHashSet<Patient>();
		
		for(Patient patient:totalCohort){
			if(patients.contains(patient)){
				
			}
			else{
				patientSet.add(patient);	
			}	
		}
	
		return patientSet;
		}
	
	public Set<Patient> getOriginalFirstLineRegimen(Program program,String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("regimenChangeType", "Start"));
		criteria.add(Restrictions.eq("typeOfRegimen", "First line Anti-retoviral drugs"));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("startDate", formatter.parse(startFromDate)),
				    Restrictions.le("startDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DrugOrderProcessed> drugOrderProcesseds=criteria.list();
		Set<Patient> afasr=getAlternateFirstLineRegimen(program,startDate,endDate);
		afasr.addAll(getSecondLineRegimen(program,startDate,endDate));
		Set<Patient> dops=new LinkedHashSet<Patient>();
		for(DrugOrderProcessed drugOrderProcessed:drugOrderProcesseds){
			
			if(afasr.contains(drugOrderProcessed.getPatient())){
			
			}
			else{
				dops.add(drugOrderProcessed.getPatient());		
			}
		}
		
		Set<Patient> patients=new LinkedHashSet<Patient>();
		Set<Patient> noOfArtStoppedCohorts=getNoOfArtStoppedCohort(program,startDate,endDate);
		Set<Patient> noOfArtDiedCohorts=getNoOfArtDiedCohort(program,startDate,endDate);
		Set<Patient> noOfPatientLostToFollowUps=getNoOfPatientLostToFollowUp(startDate,endDate);
		Set<Patient> transferredOutPatient=getNoOfPatientTransferredOut(startDate,endDate);
		Set<Patient> noOfHIVStoppedCohorts=getNoOfHIVStoppedCohort(startDate,endDate);
		
		patients.addAll(noOfArtStoppedCohorts);
		patients.addAll(noOfArtDiedCohorts);
		patients.addAll( noOfPatientLostToFollowUps);
		patients.addAll(transferredOutPatient);
		patients.addAll(noOfHIVStoppedCohorts);
		
		Set<Patient> patientSet=new LinkedHashSet<Patient>();
		
		for(Patient dop:dops){
			if(patients.contains(dop)){
				
			}
			else{
				patientSet.add(dop);	
			}	
		}
		return patientSet;
	}
	
	public Set<Patient> getAlternateFirstLineRegimen(Program program,String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("regimenChangeType", "Substitute"));
		criteria.add(Restrictions.eq("typeOfRegimen", "First line Anti-retoviral drugs"));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("startDate", formatter.parse(startFromDate)),
				    Restrictions.le("startDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DrugOrderProcessed> drugOrderProcesseds=criteria.list();
		Set<Patient> dops=new LinkedHashSet<Patient>();
		for(DrugOrderProcessed drugOrderProcessed:drugOrderProcesseds){
			DrugOrderProcessed dop=getLastDrugOrderProcessedByPatient(drugOrderProcessed.getPatient());
			if(dop.getRegimenChangeType().equals("Switch")){
				
			}
			else{
				dops.add(drugOrderProcessed.getPatient());			
			}
		}
		
		Set<Patient> patients=new LinkedHashSet<Patient>();
		Set<Patient> noOfArtStoppedCohorts=getNoOfArtStoppedCohort(program,startDate,endDate);
		Set<Patient> noOfArtDiedCohorts=getNoOfArtDiedCohort(program,startDate,endDate);
		Set<Patient> noOfPatientLostToFollowUps=getNoOfPatientLostToFollowUp(startDate,endDate);
		Set<Patient> transferredOutPatient=getNoOfPatientTransferredOut(startDate,endDate);
		Set<Patient> noOfHIVStoppedCohorts=getNoOfHIVStoppedCohort(startDate,endDate);
		
		patients.addAll(noOfArtStoppedCohorts);
		patients.addAll(noOfArtDiedCohorts);
		patients.addAll( noOfPatientLostToFollowUps);
		patients.addAll(transferredOutPatient);
		patients.addAll(noOfHIVStoppedCohorts);
		
		Set<Patient> patientSet=new LinkedHashSet<Patient>();
		
		for(Patient dop:dops){
			if(patients.contains(dop)){
				
			}
			else{
				patientSet.add(dop);		
			}	
		}
		return patientSet;
	}
	
	public Set<Patient> getSecondLineRegimen(Program program,String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		List<String> typeOfRegimen=new ArrayList<String>();
		typeOfRegimen.add("Second line ART");
		typeOfRegimen.add("Fixed dose combinations (FDCs)");
		
		criteria.add(Restrictions.eq("regimenChangeType", "Switch"));
		criteria.add(Restrictions.in("typeOfRegimen", typeOfRegimen));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("startDate", formatter.parse(startFromDate)),
				    Restrictions.le("startDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DrugOrderProcessed> drugOrderProcesseds=criteria.list();
		Set<Patient> dops=new LinkedHashSet<Patient>();
		for(DrugOrderProcessed drugOrderProcessed:drugOrderProcesseds){
			DrugOrderProcessed dop=getLastDrugOrderProcessedByPatient(drugOrderProcessed.getPatient());
			if(dop.getRegimenChangeType().equals("Substitute")){
				
			}
			else{
				dops.add(drugOrderProcessed.getPatient());			
			}
		}
		
		Set<Patient> patients=new LinkedHashSet<Patient>();
		Set<Patient> noOfArtStoppedCohorts=getNoOfArtStoppedCohort(program,startDate,endDate);
		Set<Patient> noOfArtDiedCohorts=getNoOfArtDiedCohort(program,startDate,endDate);
		Set<Patient> noOfPatientLostToFollowUps=getNoOfPatientLostToFollowUp(startDate,endDate);
		Set<Patient> transferredOutPatient=getNoOfPatientTransferredOut(startDate,endDate);
		Set<Patient> noOfHIVStoppedCohorts=getNoOfHIVStoppedCohort(startDate,endDate);
		
		patients.addAll(noOfArtStoppedCohorts);
		patients.addAll(noOfArtDiedCohorts);
		patients.addAll( noOfPatientLostToFollowUps);
		patients.addAll(transferredOutPatient);
		patients.addAll(noOfHIVStoppedCohorts);
		
		Set<Patient> patientSet=new LinkedHashSet<Patient>();
		
		for(Patient dop:dops){
			if(patients.contains(dop)){
				
			}
			else{
				patientSet.add(dop);
			}	
		}
		return patientSet;
	}
	
	public Set<Patient> getNoOfArtStoppedCohort(Program program,String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PatientProgram.class,"patientProgram");
		criteria.add(Restrictions.eq("program", program));
		criteria.add(Restrictions.isNotNull("dateCompleted"));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("dateCompleted", formatter.parse(startFromDate)),
				    Restrictions.le("dateCompleted", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<PatientProgram> ppgms=criteria.list();
		Set<Patient> artStoppedCohort=new LinkedHashSet<Patient>();
		
		for(PatientProgram ppgm:ppgms){
		Obs obs=getOutCome(ppgm.getPatient(),startFromDate,endFromDate);
		if(obs!=null){
			Date date1=ppgm.getDateCompleted();
			Date date2=obs.getObsDatetime();
			if(date1.compareTo(date2)>0){
				artStoppedCohort.add(ppgm.getPatient());	
			}
		}
		else{
			artStoppedCohort.add(ppgm.getPatient());	
		}
		}
		return artStoppedCohort;
		}
	
	public Set<Patient> getNoOfHIVStoppedCohort(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PatientProgram.class,"patientProgram");
		Program program=Context.getProgramWorkflowService().getProgramByUuid("dfdc6d40-2f2f-463d-ba90-cc97350441a8");
		criteria.add(Restrictions.eq("program", program));
		criteria.add(Restrictions.isNotNull("dateCompleted"));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("dateCompleted", formatter.parse(startFromDate)),
				    Restrictions.le("dateCompleted", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<PatientProgram> ppgms=criteria.list();
		Set<Patient> patients=new LinkedHashSet<Patient>();
		for(PatientProgram ppgm:ppgms){
		patients.add(ppgm.getPatient());	
		}
		return patients;
		}
	
	
	public Set<Patient> getNoOfArtDiedCohort(Program program,String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PatientProgram.class,"patientProgram");
		criteria.add(Restrictions.eq("program", program));
		criteria.add(Restrictions.isNull("dateCompleted"));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
	
		List<Person> personList=getListOfDiedPatient(startDate,endDate);
		List<Patient> patientList=new LinkedList<Patient>();
		for(Person person:personList){
			Patient patient=(Patient) person;
			patientList.add(patient);
		}
		
		List<PatientProgram> ppgms=new ArrayList<PatientProgram>();
		Set<Patient> patients=new LinkedHashSet<Patient>();
		if(patientList.size()!=0){
			criteria.add(Restrictions.in("patient", patientList));
			ppgms=criteria.list();	
		}
		
		for(PatientProgram ppgm:ppgms){
		patients.add(ppgm.getPatient());	
		}
		
		return patients;
		}
	
	public Set<Patient> getNoOfPatientLostToFollowUp(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Concept conceptLostToFollowUp=Context.getConceptService().getConceptByUuid("5240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

		criteria.add(Restrictions.eq("valueCoded", conceptLostToFollowUp));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("obsDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.add(Restrictions.eq("voided",false));
		List<Obs> obss=criteria.list();
		Set<Patient> patients=new LinkedHashSet<Patient>();
		for(Obs obs:obss){
			patients.add(obs.getPatient());	
		}
		return patients;
		}
	
	public List<Obs> getNoOfPatientWithCD4(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Concept cd4Concept=Context.getConceptService().getConceptByUuid("5497AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Integer obj = new Integer(200);
		double doub = obj.doubleValue();

		criteria.add(Restrictions.and(Restrictions.eq("concept", cd4Concept),Restrictions.ge("valueNumeric", doub)));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("obsDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.add(Restrictions.eq("voided",false));
		return criteria.list();
		}
		
		public List<Obs> getNoOfPatientNormalActivity(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Concept scaleA=Context.getConceptService().getConceptByUuid("e8a480a7-1f05-402c-9adf-9acbd6ff446f");

		criteria.add(Restrictions.eq("valueCoded", scaleA));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("obsDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.add(Restrictions.eq("voided",false));
		return criteria.list();
		}
		
		public List<Obs> getNoOfPatientBedriddenLessThanFifty(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Concept scaleB=Context.getConceptService().getConceptByUuid("585dcf92-c42f-42af-ac44-fdd2fb66ae3a");

		criteria.add(Restrictions.eq("valueCoded", scaleB));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("obsDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.add(Restrictions.eq("voided",false));
		return criteria.list();
		}
		
		public List<Obs> getNoOfPatientBedriddenMoreThanFifty(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Concept scaleC=Context.getConceptService().getConceptByUuid("a70cd549-aa63-4310-9a38-715dfc3ebbd2");

		criteria.add(Restrictions.eq("valueCoded", scaleC));
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("obsDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.add(Restrictions.eq("voided",false));
		return criteria.list();
		}
		
		public Set<Patient> getNoOfPatientPickedUpArvForSixMonth(String startDate,String endDate) {
		Set<Patient> listOfVisitedPatients=getListOfVisitedPatient(startDate,endDate);
		Set<Patient> patients=new HashSet<Patient>();
		for(Patient patient:listOfVisitedPatients){
		List<Visit> visits=Context.getVisitService().getVisitsByPatient(patient);
		if(visits.size()>5){
			Integer count=1;
			Integer token=1;
			Integer startVisit=visits.size()-5;
			Date startVisitDate=new Date();
			Date endVisitDate=new Date();
			for(Visit visit:visits){
			if(count==1){
		         endVisitDate=visit.getStartDatetime();	
			}
			if(count==startVisit){
				startVisitDate=visit.getStartDatetime();	
			}
			count++;
			}
			
			for(Visit visit:visits){
				List<DrugOrderProcessed> drugOrderProcesseds=getDrugOrderProcessedByVisit(visit);	
				if(drugOrderProcesseds.size()>0){
				token++;	
				}
				else{
				token=1;	
				}
			}
			
			for(Visit visit:visits){
			List<Obs> lostToFollowUp=getLostToFollowUpObs(visit.getPatient(),startVisitDate,endVisitDate);
			List<DrugOrderProcessed> drugOrderProcesseds=getDrugOrderProcessedByVisit(visit);
			if(lostToFollowUp.size()==0 && drugOrderProcesseds.size()>0 && token==6){
				patients.add(visit.getPatient());
			}
		   }
		  }
		}
		return patients;
	   }
	   
		public Set<Patient> getNoOfPatientPickedUpArvForTwelveMonth(String startDate,String endDate) {
		Set<Patient> listOfVisitedPatients=getListOfVisitedPatient(startDate,endDate);
		Set<Patient> patients=new HashSet<Patient>();
		for(Patient patient:listOfVisitedPatients){
		List<Visit> visits=Context.getVisitService().getVisitsByPatient(patient);
		if(visits.size()>11){
			Integer count=1;
			Integer token=1;
			Integer startVisit=visits.size()-11;
			Date startVisitDate=new Date();
			Date endVisitDate=new Date();
			for(Visit visit:visits){
			if(count==1){
				endVisitDate=visit.getStartDatetime();	
			}
	        if(count==startVisit){
	        	startVisitDate=visit.getStartDatetime();	
			}
			count++;
			}
			
			for(Visit visit:visits){
				List<DrugOrderProcessed> drugOrderProcesseds=getDrugOrderProcessedByVisit(visit);	
				if(drugOrderProcesseds.size()>0){
				token++;	
				}
				else{
				token=1;	
				}
			}
				
			for(Visit visit:visits){
			List<Obs> lostToFollowUp=getLostToFollowUpObs(visit.getPatient(),startVisitDate,endVisitDate);
			List<DrugOrderProcessed> drugOrderProcesseds=getDrugOrderProcessedByVisit(visit);
			if(lostToFollowUp.size()==0 && drugOrderProcesseds.size()>0 && token==12){
				patients.add(visit.getPatient());
			}
		   }
		  }
		 }
		return patients;
	   }
		
	public List<DrugOrderProcessed> getDrugOrderProcessedByPatient(Patient patient) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class);
		criteria.add(Restrictions.eq("patient", patient));
		return criteria.list();
	}
	
	public List<Person> getListOfDiedPatient(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Person.class,"person");
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
			
		try {
			criteria.add(Restrictions.and(Restrictions.ge("deathDate", formatter.parse(startFromDate)),
					    Restrictions.le("deathDate", formatter.parse(endFromDate))));
		} catch (ParseException e) {
				e.printStackTrace();
		}
			
		criteria.add(Restrictions.eq("dead", true));
		return criteria.list();	
	}
	
	public List<Person> getListOfDiedPatient() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Person.class,"person");
		criteria.add(Restrictions.eq("dead", true));
		criteria.add(Restrictions.ge("personId", 38));
		return criteria.list();	
	}
	
	public List<Person> getListOfAlivePatient() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Person.class,"person");
		criteria.add(Restrictions.eq("dead", false));
		criteria.add(Restrictions.ge("personId", 38));
		return criteria.list();	
	}
	
	public List<Person> getListOfPatient(String gender) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Person.class,"person");
		criteria.add(Restrictions.eq("gender", gender));
		criteria.add(Restrictions.ge("personId", 38));
		return criteria.list();	
	}
	
	public List<Person> getListOfPatient(Integer age1,Integer age2) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Person.class,"person");
		criteria.add(Restrictions.ge("personId", 38));
		return criteria.list();	
	}
	
	public Set<Patient> getListOfVisitedPatient(String startDate,String endDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Visit.class,"visit");
		String startFromDate = startDate + " 00:00:00";
		String endFromDate = endDate + " 23:59:59";
		try {
			criteria.add(Restrictions.and(Restrictions.ge("startDatetime", formatter.parse(startFromDate)),
				    Restrictions.le("startDatetime", formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Visit> visits=criteria.list();
		Set<Patient> patients=new LinkedHashSet<Patient>();
		for(Visit visit:visits){
			patients.add(visit.getPatient());
		}
		return patients;
	}
	
	public List<DrugOrderProcessed> getDrugOrderProcessedByVisit(Visit visit) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DrugOrderProcessed.class,"drugOrderProcessed");
		criteria.add(Restrictions.eq("visit", visit));
		//criteria.addOrder(Order.asc("createdDate"));
		return criteria.list();
	}
	
	public List<Obs> getLostToFollowUpObs(Patient patient,Date startVisitDate,Date endVisitDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		Concept conceptLostToFollowUp=Context.getConceptService().getConceptByUuid("5240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Person person=patient;
		criteria.add(Restrictions.eq("person", person));
		criteria.add(Restrictions.eq("valueCoded", conceptLostToFollowUp));
		criteria.add(Restrictions.and(Restrictions.ge("obsDatetime",startVisitDate),Restrictions.le("obsDatetime",endVisitDate)));
		return criteria.list();
	}
	
	public Obs getOutCome(Patient patient,String startFromDate,String endFromDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Obs.class,"obs");
		List<Concept> conceptList=new ArrayList<Concept>();
		Concept conceptDied=Context.getConceptService().getConceptByUuid("160034AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept conceptLostToFollowUp=Context.getConceptService().getConceptByUuid("5240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		Concept conceptTransferredOut=Context.getConceptService().getConceptByUuid("159492AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		conceptList.add(conceptDied);
		conceptList.add(conceptLostToFollowUp);
		conceptList.add(conceptTransferredOut);
		Person person=patient;
		criteria.add(Restrictions.eq("person", person));
		criteria.add(Restrictions.in("valueCoded", conceptList));
		try {
			criteria.add(Restrictions.and(Restrictions.ge("obsDatetime",formatter.parse(startFromDate)),Restrictions.le("obsDatetime",formatter.parse(endFromDate))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		criteria.addOrder(Order.desc("obsDatetime"));
		return (Obs) criteria.uniqueResult();
	}
}