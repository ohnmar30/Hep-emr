package org.openmrs.module.chaiemr.fragment.controller.dispensary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugObsProcessed;
import org.openmrs.module.chaiemr.model.DrugOrderObs;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class DrugOrderListFragmentController {
	public void controller(@FragmentParam("patient") Patient patient,FragmentModel model,HttpServletRequest request) {
        Person person=Context.getPersonService().getPerson(patient);
		List <DrugOrder> drugOrders=Context.getOrderService().getDrugOrdersByPatient(patient);
        List <DrugOrderProcessed> drugOrderProcessed=new LinkedList <DrugOrderProcessed>();
        ChaiEmrService kes = (ChaiEmrService) Context.getService(ChaiEmrService.class);
        HttpSession session = request.getSession();
        String dispensedDate=(String) session.getAttribute("dispensedDate");
        Date dispensedDatee = null;
        try {
			dispensedDatee  = parseDate(dispensedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        List<Obs> obss=kes.getObsGroupByDateAndPerson(dispensedDatee,person);
        List<DrugOrderObs> drugOrderObs=new LinkedList<DrugOrderObs>();
        for(Obs obs:obss){
        	obs.getObsGroupId();
        	List <Obs> obsDrugOrders=kes.getObsByObsGroup(obs);
        	DrugOrderObs drugOrder=new DrugOrderObs();
        	for(Obs obsDrugOrder:obsDrugOrders){
        		if(obsDrugOrder.getConcept().getUuid().equals("163079AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
        			drugOrder.setDrug(obsDrugOrder.getValueCoded().getName().toString());	
        		}
        		else if(obsDrugOrder.getConcept().getUuid().equals("163096AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
        			drugOrder.setDrug(obsDrugOrder.getValueCoded().getName().toString());		
        		}
                else if(obsDrugOrder.getConcept().getUuid().equals("163020AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugOrder.setDrug(obsDrugOrder.getValueText());			
        		}
        		
                if(obsDrugOrder.getConcept().getUuid().equals("162384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugOrder.setFormulation(obsDrugOrder.getValueCoded().getName().toString());	
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("1443AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	Integer strength=Integer.valueOf(obsDrugOrder.getValueNumeric().intValue());
                	drugOrder.setStrength(strength.toString());	
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("160855AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugOrder.setFrequency(obsDrugOrder.getValueCoded().getName().toString());		
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("162394AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugOrder.setRoute(obsDrugOrder.getValueCoded());		
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("159368AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	Integer duration=Integer.valueOf(obsDrugOrder.getValueNumeric().intValue());
                	drugOrder.setDuration(duration.toString());	
        		}
        	}
        	 drugOrder.setObsGroupId(obs.getObsId());
        	 drugOrderObs.add(drugOrder);
        }
        for(DrugOrder drugOrder:drugOrders){
        	DrugOrderProcessed drugOrderProcessedd=kes.getDrugOrderProcessed(drugOrder);
        	if(drugOrderProcessedd!=null){
        	drugOrderProcessed.add(drugOrderProcessedd);
        	}
        }
        
        String drugOrderProcessedId="";
        for(DrugOrderProcessed drugOrderProcess:drugOrderProcessed){
        	drugOrderProcessedId=drugOrderProcessedId+drugOrderProcess.getId()+"/";	
        }
        
        String drugOrderObsId="";
        for(DrugOrderObs drugOrderOb:drugOrderObs){
        	drugOrderObsId=drugOrderObsId+drugOrderOb.getObsGroupId()+"/";
        }
        
		
        Concept notDispensedConcept=Context.getConceptService().getConceptByUuid("1779AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        Collection<ConceptAnswer> notDispensedConceptAnswers=notDispensedConcept.getAnswers();
        model.addAttribute("count",1);
		model.addAttribute("drugOrderProcesseds",drugOrderProcessed);
		model.addAttribute("drugOrderObss",drugOrderObs);
		model.addAttribute("drugOrderProcessedId",drugOrderProcessedId);
		model.addAttribute("drugOrderObsId",drugOrderObsId);
		model.addAttribute("patient",patient);
		model.addAttribute("drugOrderSize",drugOrderProcessed.size()+drugOrderObs.size());
		model.addAttribute("notDispensedConceptAnswers",notDispensedConceptAnswers);
	}
	
	public String processDrugOrder(HttpServletRequest request,@RequestParam("patient") Patient patient,
			@RequestParam(value = "drugOrderProcessedIds", required = false) String[] drugOrderProcessedIds,
			@RequestParam(value = "obsGroupIds", required = false) String[] obsGroupIds,UiUtils ui) {
		ChaiEmrService kes = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		for (String drugOrderProcessedId : drugOrderProcessedIds) {
			Integer drugOrderProcessId = Integer.parseInt(drugOrderProcessedId);
			String issuedQuantity = request.getParameter(drugOrderProcessedId+"issueQuantity");	
			if(issuedQuantity!=null){
			DrugOrderProcessed drugOrderProces=kes.getDrugOrderProcesedById(drugOrderProcessId);
			Date curDate = new Date();
			Date date = new Date();
			SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
			Order order=Context.getOrderService().getOrder(drugOrderProces.getDrugOrder().getOrderId());
			String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(order.getEncounter().getEncounterDatetime());
				try {
					date = mysqlDateTimeFormatter.parse(modifiedDate
							+ " " + curDate.getHours() + ":" + curDate.getMinutes()
							+ ":" + curDate.getSeconds());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			drugOrderProces.setProcessedDate(date);
			drugOrderProces.setProcessedStatus(true);
			drugOrderProces.setQuantityPostProcess(Integer.parseInt(issuedQuantity));
			kes.saveDrugOrderProcessed(drugOrderProces);
			}
			else{
				Integer notDispensedReason= Integer.parseInt(request.getParameter(drugOrderProcessedId+"notDispensedReason"));	
				Concept notDispensedReasonConcept = Context.getConceptService().getConcept(notDispensedReason);
				DrugOrderProcessed drugOrderProces=kes.getDrugOrderProcesedById(drugOrderProcessId);
				Date curDate = new Date();
				Date date = new Date();
				SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
				Order order=Context.getOrderService().getOrder(drugOrderProces.getDrugOrder().getOrderId());
				String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(order.getEncounter().getEncounterDatetime());
					try {
						date = mysqlDateTimeFormatter.parse(modifiedDate
								+ " " + curDate.getHours() + ":" + curDate.getMinutes()
								+ ":" + curDate.getSeconds());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				drugOrderProces.setDiscontinuedDate(date);
				drugOrderProces.setDiscontinuedReason(notDispensedReasonConcept);
				kes.saveDrugOrderProcessed(drugOrderProces);	
			}
		}
		
		for (String obsGroupId : obsGroupIds) {
			Integer obsGrouppId = Integer.parseInt(obsGroupId);
			String issuedQuantity = request.getParameter(obsGroupId+"obsIssueQuantity");
			if(issuedQuantity!=null){
			DrugObsProcessed drugObsProcessed=new DrugObsProcessed();
			Obs obs=Context.getObsService().getObs(obsGrouppId);
			Date curDate = new Date();
			Date date = new Date();
			SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
			String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(obs.getEncounter().getEncounterDatetime());
				try {
					date = mysqlDateTimeFormatter.parse(modifiedDate
							+ " " + curDate.getHours() + ":" + curDate.getMinutes()
							+ ":" + curDate.getSeconds());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			obs.setComment("1");
			kes.saveOrUpdateObs(obs);

			drugObsProcessed.setObs(obs);
			drugObsProcessed.setPatient(patient);
			drugObsProcessed.setCreatedDate(obs.getEncounter().getEncounterDatetime());
			drugObsProcessed.setProcessedDate(date);
			drugObsProcessed.setQuantityPostProcess(Integer.parseInt(issuedQuantity));
			kes.saveDrugObsProcessed(drugObsProcessed);
			}
			else{
				String notDispensedReason= request.getParameter(obsGroupId+"notDispensedReason");
				//Integer notDispensedReason= Integer.parseInt(request.getParameter(obsGroupId+"notDispensedReason"));
				Concept notDispensedReasonConcept = Context.getConceptService().getConcept(notDispensedReason);
				Obs obs=Context.getObsService().getObs(obsGrouppId);
				//obs.setValueCoded(notDispensedReasonConcept);
				obs.setComment("0/"+notDispensedReason);
				kes.saveOrUpdateObs(obs);	
			}
		}
		return null;
		
	}
	
	private  Date parseDate(String s) throws ParseException {
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
}
