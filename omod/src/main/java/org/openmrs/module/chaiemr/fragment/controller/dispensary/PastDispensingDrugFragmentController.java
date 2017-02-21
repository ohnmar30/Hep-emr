package org.openmrs.module.chaiemr.fragment.controller.dispensary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugDetails;
import org.openmrs.module.chaiemr.model.DrugObsProcessed;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.ui.framework.SimpleObject;
import org.springframework.web.bind.annotation.RequestParam;

public class PastDispensingDrugFragmentController {
	public SimpleObject dispensedDetails(@RequestParam("patient") Patient patient,
			HttpServletRequest request){
		ChaiEmrService chaiEmrService = (ChaiEmrService) Context
				.getService(ChaiEmrService.class);
		HttpSession session = request.getSession();
        String processedDate=(String) session.getAttribute("processedDate");
        Date processedDatee = null;
        try {
        	processedDatee  = parseDate(processedDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleObject simplePatientt= new SimpleObject();
		List<DrugOrderProcessed> dops = chaiEmrService.getDrugOrdersByPatientAndProcessedDate(patient,processedDatee);
		List<DrugObsProcessed> dobps = chaiEmrService.getObsDrugOrdersByPatientAndProcessedDate(patient,processedDatee);
		List<DrugDetails> drugDetailsList=new LinkedList<DrugDetails>();
		
		for(DrugOrderProcessed dop:dops){
			DrugDetails drugDetails=new DrugDetails();
			drugDetails.setDrug(dop.getDrugOrder().getConcept().getName().toString());
			drugDetails.setStrength(dop.getDose().toString());
			drugDetails.setFormulation(dop.getDrugOrder().getUnits().toString());
			drugDetails.setFrequency(dop.getDrugOrder().getFrequency().toString());
			drugDetails.setQuantity(dop.getQuantityPostProcess().toString());
			drugDetails.setDuration(dop.getDurationPreProcess().toString());
			drugDetailsList.add(drugDetails);
		}
		
        for(DrugObsProcessed dobp:dobps){
        	List<Obs> obsDrugOrders=chaiEmrService.getObsByObsGroup(dobp.getObs());
        	DrugDetails drugDetailss=new DrugDetails();
        	for(Obs obsDrugOrder:obsDrugOrders){
        		if(obsDrugOrder.getConcept().getUuid().equals("163079AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
        			drugDetailss.setDrug(obsDrugOrder.getValueCoded().getName().toString());	
        		}
        		else if(obsDrugOrder.getConcept().getUuid().equals("163096AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
        			drugDetailss.setDrug(obsDrugOrder.getValueCoded().getName().toString());		
        		}
                else if(obsDrugOrder.getConcept().getUuid().equals("163020AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugDetailss.setDrug(obsDrugOrder.getValueText());		
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("1443AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugDetailss.setStrength(obsDrugOrder.getValueNumeric().toString());	
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("162384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugDetailss.setFormulation(obsDrugOrder.getValueCoded().getName().toString());	
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("160855AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugDetailss.setFrequency(obsDrugOrder.getValueCoded().getName().toString());			
        		}
                
                if(obsDrugOrder.getConcept().getUuid().equals("159368AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")){
                	drugDetailss.setDuration(obsDrugOrder.getValueNumeric().toString());	
        		}
        	}
        	drugDetailss.setQuantity(dobp.getQuantityPostProcess().toString());
        	drugDetailsList.add(drugDetailss);
		}
        simplePatientt.put("drugDetailsList", drugDetailsList);
	
	return simplePatientt;	
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
