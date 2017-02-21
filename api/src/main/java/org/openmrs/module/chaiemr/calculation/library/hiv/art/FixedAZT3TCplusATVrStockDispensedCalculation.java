package org.openmrs.module.chaiemr.calculation.library.hiv.art;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;

public class FixedAZT3TCplusATVrStockDispensedCalculation extends AbstractPatientCalculation {

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
			Integer stock=0;
			 ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		 	   List<DrugOrderProcessed> drugorderprocess = chaiEmrService.getAllfirstLine() ;
		 	  DrugOrderProcessed drugorder = new DrugOrderProcessed();
		 	   
		 	  for(DrugOrderProcessed orderprocess:drugorderprocess)
		 	  {  
		 		 
		 		 if((ptId.equals(orderprocess.getPatient().getPatientId()) &&(orderprocess.getDrugRegimen().equals("AZT+3TC+ATV/r"))))
		 			 
		 		
		 		  { 
		 			 if( orderprocess.getProcessedStatus()==true)
		 		      {
		 			
		 			 stock=orderprocess.getQuantityPostProcess();
			 			ret.put(ptId, new SimpleResult(stock, this, context));
		 		  }
		 			
		 		  }
		 		 
		 		 }   
		 	  }
		 	  
		return ret;
    }
}



