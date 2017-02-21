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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.metadata.CommonMetadata._EncounterType;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.chaiemr.regimen.Regimen;
import org.openmrs.module.chaiemr.regimen.RegimenChange;
import org.openmrs.module.chaiemr.regimen.RegimenChangeHistory;
import org.openmrs.module.chaiemr.regimen.RegimenComponent;
import org.openmrs.module.chaiemr.regimen.RegimenManager;
import org.openmrs.module.chaiemr.regimen.RegimenOrder;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.form.ValidatingCommandObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Various actions for regimen related functions
 */
public class RegimenUtilFragmentController {

	protected static final Log log = LogFactory.getLog(RegimenUtilFragmentController.class);

	/**
	 * Changes the patient's current regimen
	 * @param command the command object
	 * @param ui the UI utils
	 * @return the patient's current regimen
	 */
	public void changeRegimen(@MethodParam("newRegimenChangeCommandObject") @BindParams RegimenChangeCommandObject command, UiUtils ui,
			@RequestParam(value = "durgList", required = false) String[] durgList,
			@RequestParam(value = "srNo", required = false) String[] srNo,
			HttpServletRequest request){
		ui.validate(command, command, null);
		Encounter encounter=command.apply(request,srNo);
		//command.saveExtraRowForArv(durgList,request,command.getPatient(),encounter);
	}

	/**
	 * Undoes the last regimen change for the given patient
	 * @param patient the patient
	 * @return the patient's current regimen
	 */
	public void undoLastChange(@RequestParam("patient") Patient patient, HttpSession session, @RequestParam("category") String category, @SpringBean RegimenManager regimenManager, @SpringBean ChaiUiUtils chaiUi) {
		Concept masterSet = regimenManager.getMasterSetConcept(category);
		RegimenChangeHistory history = RegimenChangeHistory.forPatient(patient, masterSet);
		history.undoLastChange();

		chaiUi.notifySuccess(session, "Removed last regimen change");
	}

	/**
	 * Helper method to create a new form object
	 * @return the form object
	 */
	public RegimenChangeCommandObject newRegimenChangeCommandObject(@SpringBean RegimenManager regimenManager) {
		return new RegimenChangeCommandObject(regimenManager);
	}

	/**
	 * Change types
	 */
	public enum RegimenChangeType {
		Start,
		Change,
		Substitute,
		Switch,
		//STOP,
		Continue,
		Restart
	}

	/**
	 * Command object for regimen changes
	 */
	public class RegimenChangeCommandObject extends ValidatingCommandObject {

		private RegimenManager regimenManager;

		private Patient patient;

		private String category;

		private RegimenChangeType changeType;
		
		private Date changeDate;

		private Concept changeReason;
		
		private String changeReasonNonCoded;

		private Regimen regimen;

		public RegimenChangeCommandObject(RegimenManager regimenManager) {
			this.regimenManager = regimenManager;
		}

		/**
		 * @see org.springframework.validation.Validator#validate(java.lang.Object,org.springframework.validation.Errors)
		 */
		@Override
		public void validate(Object target, Errors errors) {
			require(errors, "patient");
			require(errors, "category");
			require(errors, "changeType");
			require(errors, "changeDate");

			// Reason is only required for stopping or changing/
			/*
			if (changeType == RegimenChangeType.Change || changeType == RegimenChangeType.Switch || changeType == RegimenChangeType.Substitute) {
				require(errors, "changeReason");

				if (changeReason != null) {
					Concept otherNonCoded = Dictionary.getConcept(Dictionary.OTHER_NON_CODED);

					if (changeReason.equals(otherNonCoded)) {
						require(errors, "changeReasonNonCoded");
					}
				}
			}

			if (category != null && changeDate != null) {
				// Get patient regimen history
				Concept masterSet = regimenManager.getMasterSetConcept(category);
				RegimenChangeHistory history = RegimenChangeHistory.forPatient(patient, masterSet);
				RegimenChange lastChange = history.getLastChange();
				boolean onRegimen = lastChange != null && lastChange.getStarted() != null;

				// Can't start if already started
				if ((changeType == RegimenChangeType.Start || changeType == RegimenChangeType.Restart) && onRegimen) {
					errors.reject("Can't start regimen for patient who is already on a regimen");
				}

				// Changes must be in order
				if (lastChange != null && OpenmrsUtil.compare(changeDate, lastChange.getDate()) <= 0) {
					errors.rejectValue("changeDate", "Change date must be after all other changes");
				}

				// Don't allow future dates
				if (OpenmrsUtil.compare(changeDate, new Date()) > 0) {
					errors.rejectValue("changeDate", "Change date can't be in the future");
				}
			}

			// Validate the regimen
			if (changeType != RegimenChangeType.Continue) {
				try {
					errors.pushNestedPath("regimen");
					ValidationUtils.invokeValidator(new RegimenValidator(), regimen, errors);
				} finally {
					errors.popNestedPath();
				}
			}*/
		}
		
		/**
		 * Applies this regimen change
		 */
		public Encounter apply(HttpServletRequest request,String[] srNo) {
			Concept masterSet = regimenManager.getMasterSetConcept(category);
			RegimenChangeHistory history = RegimenChangeHistory.forPatient(patient, masterSet);
			RegimenChange lastChange = history.getLastChange();
			RegimenOrder baseline = lastChange != null ? lastChange.getStarted() : null;
			Encounter encounter=null;
			ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
			
			List<Visit> visits=Context.getVisitService().getActiveVisitsByPatient(patient);
			Visit visit=new Visit();
			for(Visit vst:visits){
				visit=vst;
			}
			
			
			Date curDate = new Date();
			SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
					"dd-MMM-yy HH:mm:ss");
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(changeDate);
			try {
				date = mysqlDateTimeFormatter.parse(modifiedDate
						+ " " + curDate.getHours() + ":" + curDate.getMinutes()
						+ ":" + curDate.getSeconds());
			} catch (ParseException e) {
				date = curDate;
				e.printStackTrace();
			}
			
			String drugRegimenn="";
			String dosee="";
			if (changeType == RegimenChangeType.Start || changeType == RegimenChangeType.Substitute || changeType == RegimenChangeType.Switch) {
			if (srNo != null) {
				for (String srn : srNo) {
					Concept drugConcept=null;
					String drugRegimen=request.getParameter("drugKey"+srn);	
					String dose=request.getParameter("strength"+srn);
					dose=dose.substring(0,dose.length() - 3);
					if(drugRegimen!=null){
						 drugConcept=Context.getConceptService().getConceptByName(drugRegimen);
					}
					List<ConceptAnswer> conceptAnswers=chaiEmrService.getConceptAnswerByAnsweConcept(drugConcept);
					for(ConceptAnswer conceptAnswer:conceptAnswers){
						//Fixed dose combinations
						if(conceptAnswer.getConcept().getUuid().equals("b8d460f8-0563-4416-9e0a-d77aafa7e5c3")){
							drugRegimenn=drugRegimenn+drugRegimen+"+";
							dosee=dosee+dose+"+";
						}
					}
				}
			}
			
			if(!drugRegimenn.equals("")){
			String lastCharacter=drugRegimenn.substring(drugRegimenn.length() - 1);
			if(lastCharacter.equals("+")){
			drugRegimenn=drugRegimenn.substring(0,drugRegimenn.length() - 1);
			dosee=dosee.substring(0,dosee.length() - 1)+" "+"mg";
			    }
			  }
			}
			
			if (baseline == null) {
				encounter=createEncounterForBaseLine(patient);
					for (String srn : srNo) {
				Concept drugConcept=null;
				String drugRegimen=request.getParameter("drugKey"+srn);
				//Double dose=Double.parseDouble(request.getParameter("strength"+srn));
				String dose=request.getParameter("strength"+srn);
				String doseRegimen="";
				Integer noOfTablet=Integer.parseInt(request.getParameter("noOfTablet"+srn));
				String units=request.getParameter("type"+srn);
				String frequency=request.getParameter("frequncy"+srn);
				Integer route=Integer.parseInt(request.getParameter("route"+srn));
				Integer duration=Integer.parseInt(request.getParameter("duration"+srn));

				if(drugRegimen!=null){
					 drugConcept=Context.getConceptService().getConceptByName(drugRegimen);
				}
				
				List<ConceptAnswer> conceptAnswers=chaiEmrService.getConceptAnswerByAnsweConcept(drugConcept);
				String typeOfRegimen="";
				Map<String,Concept> conMap=new LinkedHashMap<String,Concept>();
				for(ConceptAnswer conceptAnswer:conceptAnswers){
					//Fixed dose combinations
					if(conceptAnswer.getConcept().getUuid().equals("b8d460f8-0563-4416-9e0a-d77aafa7e5c3")){
						typeOfRegimen=conceptAnswer.getConcept().getName().getName();
						drugRegimen=drugRegimenn;
						doseRegimen=dosee;
					}
					else{
						//ARV drugs for child
						if(patient.getAge()<=14 && conceptAnswer.getConcept().getUuid().equals("4c132bde-0e0f-4586-a874-fe6335945144")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();
							doseRegimen=dose;
						}
						//First line Anti-retoviral drugs
						else if(conceptAnswer.getConcept().getUuid().equals("363c2193-f3d1-4e97-9dd3-09361bbcc233")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();	
							doseRegimen=dose;
						}
						//Second line ART
						else if(conceptAnswer.getConcept().getUuid().equals("f7693b07-789f-46d6-892a-fcf499b97228")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();	
							doseRegimen=dose;
						}
						//HIV/HBV co-infection
						else if(conceptAnswer.getConcept().getUuid().equals("ab9b1c9a-9acb-4e7f-b696-ac6870083117")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();	
							doseRegimen=dose;
						}
					}
				}
				
				DrugOrder drugOrder = new DrugOrder();
				drugOrder.setOrderType(Context.getOrderService().getOrderType(OpenmrsConstants.ORDERTYPE_DRUG));
				drugOrder.setEncounter(encounter);
				drugOrder.setPatient(patient);
				drugOrder.setStartDate(date);
				drugOrder.setConcept(drugConcept);
				//drugoOrder.setDrug();
				//drugoOrder.setDose(dose);
				drugOrder.setUnits(units);
				drugOrder.setFrequency(frequency);
				
				Order order=Context.getOrderService().saveOrder(drugOrder);
				
				DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
				drugOrderProcessed.setDrugOrder(Context.getOrderService().getDrugOrder(order.getOrderId()));
				drugOrderProcessed.setPatient(patient);
				drugOrderProcessed.setCreatedDate(new Date());
				drugOrderProcessed.setProcessedStatus(false);
				drugOrderProcessed.setDose(dose);
				drugOrderProcessed.setDoseRegimen(doseRegimen);
				drugOrderProcessed.setNoOfTablet(noOfTablet);
				drugOrderProcessed.setRoute(Context.getConceptService().getConcept(route));
				drugOrderProcessed.setDurationPreProcess(duration);	
				drugOrderProcessed.setDrugRegimen(drugRegimen);
				drugOrderProcessed.setRegimenChangeType(changeType.name());
				drugOrderProcessed.setTypeOfRegimen(typeOfRegimen);	
				drugOrderProcessed.setStartDate(date);
				drugOrderProcessed.setVisit(visit);
				chaiEmrService.saveDrugOrderProcessed(drugOrderProcessed);
			   }
			}
			else {
				if (changeType == RegimenChangeType.Continue) {
					List<DrugOrder> continu = new ArrayList<DrugOrder>(baseline.getDrugOrders());	
					for (DrugOrder drugOrder : continu){
						    DrugOrderProcessed dop=new DrugOrderProcessed();
						    DrugOrderProcessed drugOrderProcess=chaiEmrService.getDrugOrderProcessed(drugOrder);
						    if(drugOrderProcess!=null){
						    	dop=drugOrderProcess;
							drugOrderProcess.setDiscontinuedDate(date);
							chaiEmrService.saveDrugOrderProcessed(drugOrderProcess);
							}
						    else{
						    	List<DrugOrderProcessed> drugOrderProcessCompleted=chaiEmrService.getDrugOrderProcessedCompleted(drugOrder);
						    	for(DrugOrderProcessed drugOrderProcessCompletd:drugOrderProcessCompleted){
						    		dop=drugOrderProcessCompletd;
						    	}
						    }
							DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
							drugOrderProcessed.setDrugOrder(dop.getDrugOrder());
							drugOrderProcessed.setPatient(patient);
							drugOrderProcessed.setCreatedDate(new Date());
							drugOrderProcessed.setProcessedStatus(false);
							drugOrderProcessed.setDose(dop.getDose());
							drugOrderProcessed.setDoseRegimen(dop.getDoseRegimen());
							drugOrderProcessed.setNoOfTablet(dop.getNoOfTablet());
							drugOrderProcessed.setRoute(dop.getRoute());
							Integer duration=Integer.parseInt(request.getParameter("duration"+drugOrder.getConcept().getName()));
							drugOrderProcessed.setDurationPreProcess(duration);
							drugOrderProcessed.setDrugRegimen(dop.getDrugRegimen());
							drugOrderProcessed.setRegimenChangeType(changeType.name());
							drugOrderProcessed.setTypeOfRegimen(dop.getTypeOfRegimen());
							drugOrderProcessed.setStartDate(date);
							drugOrderProcessed.setVisit(visit);
							chaiEmrService.saveDrugOrderProcessed(drugOrderProcessed);
					}	
				}
				else if(changeType == RegimenChangeType.Substitute || changeType == RegimenChangeType.Switch){
				encounter=createEncounterForBaseLine(patient);
				Set<DrugOrder> drugOrders=baseline.getDrugOrders();
				Set<Concept> drugOrderConceptList=new LinkedHashSet<Concept>();
				Set<DrugOrder> commonDrugOrderList=new LinkedHashSet<DrugOrder>();
				for(DrugOrder drugOrder:drugOrders){
					drugOrderConceptList.add(drugOrder.getConcept());	
				}
				
				if (srNo != null) {
					for (String srn : srNo) {
				Concept drugConcept=null;
				String drugRegimen=request.getParameter("drugKey"+srn);
				//Double dose=Double.parseDouble(request.getParameter("strength"+srn));
				String dose=request.getParameter("strength"+srn);
				String doseRegimen="";
				Integer noOfTablet=Integer.parseInt(request.getParameter("noOfTablet"+srn));
				String units=request.getParameter("type"+srn);
				String frequency=request.getParameter("frequncy"+srn);
				Integer route=Integer.parseInt(request.getParameter("route"+srn));
				Integer duration=Integer.parseInt(request.getParameter("duration"+srn));

				if(drugRegimen!=null){
					 drugConcept=Context.getConceptService().getConceptByName(drugRegimen);
				}
				
				List<ConceptAnswer> conceptAnswers=chaiEmrService.getConceptAnswerByAnsweConcept(drugConcept);
				String typeOfRegimen="";
				Map<String,Concept> conMap=new LinkedHashMap<String,Concept>();
				for(ConceptAnswer conceptAnswer:conceptAnswers){
					//Fixed dose combinations
					if(conceptAnswer.getConcept().getUuid().equals("b8d460f8-0563-4416-9e0a-d77aafa7e5c3")){
						typeOfRegimen=conceptAnswer.getConcept().getName().getName();
						drugRegimen=drugRegimenn;
						doseRegimen=dosee;
					}
					else{
						//ARV drugs for child
						if(patient.getAge()<=14 && conceptAnswer.getConcept().getUuid().equals("4c132bde-0e0f-4586-a874-fe6335945144")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();	
							doseRegimen=dose;
						}
						//First line Anti-retoviral drugs
						else if(conceptAnswer.getConcept().getUuid().equals("363c2193-f3d1-4e97-9dd3-09361bbcc233")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();	
							doseRegimen=dose;
						}
						//Second line ART
						else if(conceptAnswer.getConcept().getUuid().equals("f7693b07-789f-46d6-892a-fcf499b97228")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();
							doseRegimen=dose;
						}
						//HIV/HBV co-infection
						else if(conceptAnswer.getConcept().getUuid().equals("ab9b1c9a-9acb-4e7f-b696-ac6870083117")){
							typeOfRegimen=conceptAnswer.getConcept().getName().getName();	
							doseRegimen=dose;
						}
					}
				}

								
				if(drugOrderConceptList.contains(drugConcept)){
					for(DrugOrder drugOrder:baseline.getDrugOrders()){
						DrugOrderProcessed dop=chaiEmrService.getLastDrugOrderProcessedNotDiscontinued(drugOrder);
						if(dop!=null){
							if(dop.getDrugOrder().getConcept().equals(drugConcept)){
								commonDrugOrderList.add(drugOrder);
								if(dop.getDose().equals(dose) && dop.getDrugOrder().getFrequency().equals(frequency)){
									dop.setDiscontinuedDate(new Date());
									chaiEmrService.saveDrugOrderProcessed(dop);
									DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
									drugOrderProcessed.setDrugOrder(dop.getDrugOrder());
									drugOrderProcessed.setPatient(patient);
									drugOrderProcessed.setCreatedDate(new Date());
									drugOrderProcessed.setProcessedStatus(false);
									drugOrderProcessed.setDose(dose);
									drugOrderProcessed.setDoseRegimen(doseRegimen);
									drugOrderProcessed.setNoOfTablet(noOfTablet);
									drugOrderProcessed.setRoute(Context.getConceptService().getConcept(route));
									drugOrderProcessed.setDurationPreProcess(duration);	
									drugOrderProcessed.setDrugRegimen(drugRegimen);
									drugOrderProcessed.setRegimenChangeType(changeType.name());
									drugOrderProcessed.setTypeOfRegimen(typeOfRegimen);	
									drugOrderProcessed.setStartDate(date);
									drugOrderProcessed.setVisit(visit);
									chaiEmrService.saveDrugOrderProcessed(drugOrderProcessed);	
								}
								else{
									dop.setDiscontinuedDate(date);
									chaiEmrService.saveDrugOrderProcessed(dop);
									drugOrder.setDiscontinued(true);
									drugOrder.setDiscontinuedDate(date);
									drugOrder.setDiscontinuedBy(Context.getAuthenticatedUser());
									drugOrder.setDiscontinuedReason(changeReason);
									drugOrder.setDiscontinuedReasonNonCoded(changeReasonNonCoded);
									Context.getOrderService().saveOrder(drugOrder);	
									
									DrugOrder drugOder = new DrugOrder();
									drugOder.setOrderType(Context.getOrderService().getOrderType(OpenmrsConstants.ORDERTYPE_DRUG));
									drugOder.setEncounter(encounter);
									drugOder.setPatient(patient);
									drugOder.setStartDate(date);
									drugOder.setConcept(drugConcept);
									drugOder.setUnits(units);
									drugOder.setFrequency(frequency);
									
									Order order=Context.getOrderService().saveOrder(drugOder);
									
									DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
									drugOrderProcessed.setDrugOrder(Context.getOrderService().getDrugOrder(order.getOrderId()));
									drugOrderProcessed.setPatient(patient);
									drugOrderProcessed.setCreatedDate(new Date());
									drugOrderProcessed.setProcessedStatus(false);
									drugOrderProcessed.setDose(dose);
									drugOrderProcessed.setDoseRegimen(doseRegimen);
									drugOrderProcessed.setNoOfTablet(noOfTablet);
									drugOrderProcessed.setRoute(Context.getConceptService().getConcept(route));
									drugOrderProcessed.setDurationPreProcess(duration);	
									drugOrderProcessed.setDrugRegimen(drugRegimen);
									drugOrderProcessed.setRegimenChangeType(changeType.name());
									drugOrderProcessed.setTypeOfRegimen(typeOfRegimen);
									drugOrderProcessed.setStartDate(date);
									drugOrderProcessed.setVisit(visit);
									chaiEmrService.saveDrugOrderProcessed(drugOrderProcessed);
								}
							}
						}
					}
				}
				else{
				DrugOrder drugOder = new DrugOrder();
				drugOder.setOrderType(Context.getOrderService().getOrderType(OpenmrsConstants.ORDERTYPE_DRUG));
				drugOder.setEncounter(encounter);
				drugOder.setPatient(patient);
				drugOder.setStartDate(date);
				drugOder.setConcept(drugConcept);
				//drugOder.setDrug();
				//drugOder.setDose(dose);
				drugOder.setUnits(units);
				drugOder.setFrequency(frequency);
				
				Order order=Context.getOrderService().saveOrder(drugOder);
				
				DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
				drugOrderProcessed.setDrugOrder(Context.getOrderService().getDrugOrder(order.getOrderId()));
				drugOrderProcessed.setPatient(patient);
				drugOrderProcessed.setCreatedDate(new Date());
				drugOrderProcessed.setProcessedStatus(false);
				drugOrderProcessed.setDose(dose);
				drugOrderProcessed.setDoseRegimen(doseRegimen);
				drugOrderProcessed.setNoOfTablet(noOfTablet);
				drugOrderProcessed.setRoute(Context.getConceptService().getConcept(route));
				drugOrderProcessed.setDurationPreProcess(duration);	
				drugOrderProcessed.setDrugRegimen(drugRegimen);
				drugOrderProcessed.setRegimenChangeType(changeType.name());
				drugOrderProcessed.setTypeOfRegimen(typeOfRegimen);	
				drugOrderProcessed.setStartDate(date);
				drugOrderProcessed.setVisit(visit);
				chaiEmrService.saveDrugOrderProcessed(drugOrderProcessed);
				}
			  }
			 }
				
				for(DrugOrder commonDrugOrder:commonDrugOrderList){
					drugOrders.remove(commonDrugOrder);	
				}
				
				for(DrugOrder drugOrder:drugOrders){
					DrugOrderProcessed dop=chaiEmrService.getLastDrugOrderProcessedNotDiscontinued(drugOrder);	
					dop.setDiscontinuedDate(date);
					drugOrder.setDiscontinued(true);
					drugOrder.setDiscontinuedDate(date);
					drugOrder.setDiscontinuedBy(Context.getAuthenticatedUser());
					drugOrder.setDiscontinuedReason(changeReason);
					drugOrder.setDiscontinuedReasonNonCoded(changeReasonNonCoded);
					Context.getOrderService().saveOrder(drugOrder);	
				}
			}
		   }
			return encounter;
		}
		
		public void saveExtraRowForArv(String[] durgList,HttpServletRequest request,Patient patient,Encounter encounter) {
			int count=6;
			
			if (changeType == RegimenChangeType.Substitute) {
			for(String drug:durgList){
				String drugConceptId=request.getParameter("drug"+count);
				double dose=Integer.parseInt(request.getParameter("dose"+count));
				String unit=request.getParameter("unit"+count);
				String frequency=request.getParameter("frequency"+count);
				Integer duration=Integer.parseInt(request.getParameter("duration"+count));
				
				if(drugConceptId!=null){
				DrugOrder order = new DrugOrder();
				order.setOrderType(Context.getOrderService().getOrderType(OpenmrsConstants.ORDERTYPE_DRUG));
				order.setEncounter(encounter);
				order.setPatient(patient);
				order.setStartDate(new Date());
				order.setConcept(Context.getConceptService().getConceptByUuid(drugConceptId.substring(2)));
				order.setDose(dose);
				order.setUnits(unit);
				order.setFrequency(frequency);
				Context.getOrderService().saveOrder(order);
				
				ChaiEmrService kes = (ChaiEmrService) Context.getService(ChaiEmrService.class);
				
				DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
				drugOrderProcessed.setDrugOrder(order);
				drugOrderProcessed.setPatient(patient);
				drugOrderProcessed.setCreatedDate(new Date());
				drugOrderProcessed.setProcessedStatus(false);
				drugOrderProcessed.setDurationPreProcess(duration);
				drugOrderProcessed.setDrugRegimen("");
				kes.saveDrugOrderProcessed(drugOrderProcessed);
				count++;
				}
			  }
		   }
			
			if (changeType == RegimenChangeType.Switch) {
				for(String drug:durgList){
					String drugConceptId=request.getParameter("drugSwitch"+count);
					double dose=Integer.parseInt(request.getParameter("doseSwitch"+count));
					String unit=request.getParameter("unitSwitch"+count);
					String frequency=request.getParameter("frequencySwitch"+count);
					Integer duration=Integer.parseInt(request.getParameter("durationSwitch"+count));
					
					if(drugConceptId!=null){
					DrugOrder order = new DrugOrder();
					order.setOrderType(Context.getOrderService().getOrderType(OpenmrsConstants.ORDERTYPE_DRUG));
					order.setEncounter(encounter);
					order.setPatient(patient);
					order.setStartDate(new Date());
					order.setConcept(Context.getConceptService().getConceptByUuid(drugConceptId.substring(2)));
					order.setDose(dose);
					order.setUnits(unit);
					order.setFrequency(frequency);
					Context.getOrderService().saveOrder(order);
					
					ChaiEmrService kes = (ChaiEmrService) Context.getService(ChaiEmrService.class);
					
					DrugOrderProcessed drugOrderProcessed=new DrugOrderProcessed();
					drugOrderProcessed.setDrugOrder(order);
					drugOrderProcessed.setPatient(patient);
					drugOrderProcessed.setCreatedDate(new Date());
					drugOrderProcessed.setProcessedStatus(false);
					drugOrderProcessed.setDurationPreProcess(duration);
					drugOrderProcessed.setDrugRegimen("");
					kes.saveDrugOrderProcessed(drugOrderProcessed);
					count++;
					}
				 }
			 }
		}
		
		/**
		 * Gets the patient
		 * @return the patient
		 */
		public Patient getPatient() {
			return patient;
		}
		
		/**
		 * Sets the patient
		 * @param patient the patient
		 */
		public void setPatient(Patient patient) {
			this.patient = patient;
		}

		/**
		 * Gets the regimen category
		 * @return the regimen category
		 */
		public String getCategory() {
			return category;
		}

		/**
		 * Sets the regimen category
		 * @param category the category
		 */
		public void setCategory(String category) {
			this.category = category;
		}

		/**
		 * Gets the change type
		 * @return the change type
		 */
		public RegimenChangeType getChangeType() {
			return changeType;
		}

		/**
		 * Sets the change type
		 * @param changeType the change type
		 */
		public void setChangeType(RegimenChangeType changeType) {
			this.changeType = changeType;
		}

		/**
		 * Gets the change date
		 * @return the change date
		 */
		public Date getChangeDate() {
			return changeDate;
		}
		
		/**
		 * Set the change date
		 * @param changeDate the change date
		 */
		public void setChangeDate(Date changeDate) {
			this.changeDate = changeDate;
		}
		
		/**
		 * Gets the change reason
		 * @return the change reason
		 */
		public Concept getChangeReason() {
			return changeReason;
		}
		
		/**
		 * Sets the change reason
		 * @param changeReason the change reason
		 */
		public void setChangeReason(Concept changeReason) {
			this.changeReason = changeReason;
		}

		/**
		 * Gets the non-coded change reason
		 * @return the non-coded change reason
		 */
		public String getChangeReasonNonCoded() {
			return changeReasonNonCoded;
		}

		/**
		 * Sets the non-coded change reason
		 * @param changeReasonNonCoded the non-coded change reason
		 */
		public void setChangeReasonNonCoded(String changeReasonNonCoded) {
			this.changeReasonNonCoded = changeReasonNonCoded;
		}

		/**
		 * Gets the regimen
		 * @return the regimen
		 */
		public Regimen getRegimen() {
			return regimen;
		}

		/**
		 * Sets the regimen
		 * @param regimen the regimen
		 */
		public void setRegimen(Regimen regimen) {
			this.regimen = regimen;
		}
	}

	/**
	 * Analyzes the current regimen order and the new regimen component to decide which orders must be changed
	 * @param baseline the current regimen order
	 * @param component the new regimen component
	 * @param noChanges
	 * @param toChangeDose
	 * @param toStart
	 */
	private void changeRegimenHelper(RegimenOrder baseline, RegimenComponent component, List<DrugOrder> noChanges, List<DrugOrder> toChangeDose,
									 List<DrugOrder> toStart) {

		List<DrugOrder> sameGeneric = baseline.getDrugOrders(component.getDrugRef());

		boolean anyDoseChanges = false;
		for (DrugOrder o : sameGeneric) {
			if (o.getDose().equals(component.getDose()) && o.getUnits().equals(component.getUnits()) && OpenmrsUtil.nullSafeEquals(o.getFrequency(), component.getFrequency())) {
				noChanges.add(o);
			} else {
				toChangeDose.add(o);
				anyDoseChanges = true;
			}
		}
		if (anyDoseChanges || sameGeneric.size() == 0) {
			toStart.add(component.toDrugOrder(null, null,null));
		}
	}
	
    public Encounter createEncounterForBaseLine(Patient patient){
    	Encounter encounter = new Encounter();
		Location location = new Location(1);
		User user = Context.getAuthenticatedUser();
		List<Visit> visits=Context.getVisitService().getActiveVisitsByPatient(patient);
		int visitSize=visits.size();
		
		encounter.setPatient(patient);
		encounter.setCreator(user);
		encounter.setProvider(user);
//		encounter.setEncounterDatetime(new Date());
		encounter.setEncounterType(Context.getEncounterService().getEncounterTypeByUuid(_EncounterType.REGIMEN_ORDER));
		encounter.setLocation(location);
		if(visitSize==1){
			for(Visit visit:visits){
		encounter.setVisit(visit);
		Date curDate = new Date();
		SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
				"dd-MMM-yy HH:mm:ss");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(visit.getStartDatetime());
		try {
			date = mysqlDateTimeFormatter.parse(modifiedDate
					+ " " + curDate.getHours() + ":" + curDate.getMinutes()
					+ ":" + curDate.getSeconds());
		} catch (ParseException e) {
			date = curDate;
			e.printStackTrace();
		}
		encounter.setEncounterDatetime(date);
			}
		}
		else{
			encounter.setEncounterDatetime(new Date());
		}
		
		encounter=Context.getEncounterService().saveEncounter(encounter);
		return encounter;
    }
}