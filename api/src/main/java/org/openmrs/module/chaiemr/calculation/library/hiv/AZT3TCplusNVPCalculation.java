package org.openmrs.module.chaiemr.calculation.library.hiv;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;

public class AZT3TCplusNVPCalculation  extends AbstractPatientCalculation {

	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection, java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 * @should return null for patients who have never started ARVs
	 * @should return null for patients who aren't currently on ARVs
	 * @should return whether patients have changed regimens
	 */
	@Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> arg1, PatientCalculationContext context) {

		
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			boolean onOrigFirstLine = false;
			 ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		 	   List<DrugOrderProcessed> drugorderprocess = chaiEmrService.getAllfirstLine();
		 	 
		 	   {
		 	  for(DrugOrderProcessed orderprocess:drugorderprocess)
		 	  {   Order order = Context.getOrderService().getDrugOrder(orderprocess.getDrugOrder().getOrderId());
		 		  System.out.println("hellooooooooo"+order.getOrderId());
		 	  if((ptId.equals(orderprocess.getPatient().getPatientId()) && (orderprocess.getDrugOrder().getOrderId().equals(order.getOrderId())) ))
		 		 {  Encounter enc=Context.getEncounterService().getEncounter(order.getOrderId());
		 		 System.out.println("huytuytut"+enc.getId());
		 			onOrigFirstLine=true; 
		 		 }
		 		
		 	  }
		}
			
			ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
		}
		return ret;
    }
}
