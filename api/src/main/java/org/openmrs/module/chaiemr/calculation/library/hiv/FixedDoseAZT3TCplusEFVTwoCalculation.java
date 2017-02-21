package org.openmrs.module.chaiemr.calculation.library.hiv;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;

public class FixedDoseAZT3TCplusEFVTwoCalculation extends AbstractPatientCalculation {

	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection, java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 * @should return null for patients who have never started ARVs
	 * @should return null for patients who aren't currently on ARVs
	 * @should return whether patients have changed regimens
	 */
	@Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> arg1, PatientCalculationContext context) {

		
		Set<Integer> alive = Filters.alive(cohort, context);
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : alive) {
			boolean onOrigFirstLine = false;
			 ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		 	   List<DrugOrderProcessed> drugorderprocess = chaiEmrService.getAllfirstLine(); 
		 	  DrugOrderProcessed drugorder = new DrugOrderProcessed();
		 	   {
		 	  for(DrugOrderProcessed orderprocess:drugorderprocess)
		 	  {  if(orderprocess.getPatient().getAge()<=14)
		 	     {
		 	  if((ptId.equals(orderprocess.getPatient().getPatientId()) && (orderprocess.getDrugRegimen().equals("AZT/3TC+EFV")) && (orderprocess.getDoseRegimen().equals("60/30+200 mg"))))
		 		 { 
		 		
		 		  if(orderprocess.getDrugRegimen().equals(drugorder.getDrugRegimen()))
		 		  {  
		 			 onOrigFirstLine = false;
		 			
		 		  }
		 		  else
		 		  { 
		 			 onOrigFirstLine = true;
		 			drugorder=orderprocess;
		 			ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
		 		  }
		 		 if(orderprocess.getDiscontinuedDate()!=null)
			 	  { 
			 		 onOrigFirstLine=false; 
			 		ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
			 	  }
		 		 }
		 	  }
		 	  else
		 	  {
		 		 if((ptId.equals(orderprocess.getPatient().getPatientId()) &&(orderprocess.getDrugRegimen().equals("AZT/3TC+EFV")) && (orderprocess.getDoseRegimen().equals("300/150+600 mg"))))
		 			 
		 		 { 
		 		  if(orderprocess.getDrugRegimen().equals(drugorder.getDrugRegimen()))
		 		  {  
		 			 onOrigFirstLine = false;
		 		  }
		 		  else
		 		  {  
		 			 onOrigFirstLine = true;
		 			drugorder=orderprocess;
		 			ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
		 		  }
		 		 if(orderprocess.getDiscontinuedDate()!=null)
			 	  { 
			 		 onOrigFirstLine=false; 
			 		ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
			 	  }
		 		 }   
		 	  }
		 	  }
		}
			
			
		}
		return ret;
    }
}