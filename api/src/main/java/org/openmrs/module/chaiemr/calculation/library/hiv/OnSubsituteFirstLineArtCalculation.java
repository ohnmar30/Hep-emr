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

public class OnSubsituteFirstLineArtCalculation extends AbstractPatientCalculation {

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
			boolean onOrigFirstLines = false;
			 ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		 	   List<DrugOrderProcessed> drugorderprocess = chaiEmrService.getAllfirstLine();
		 	  DrugOrderProcessed drugorder = new DrugOrderProcessed();
		 	   {
		 	  for(DrugOrderProcessed order:drugorderprocess)
		 	  {
		 		 if(order.getPatient().getAge()<=14) 
		 		 {
		 	 
		 	  if((ptId.equals(order.getPatient().getPatientId())&&(order.getRegimenChangeType().equals("Substitute")) &&(order.getTypeOfRegimen().equals("Fixed dose combinations (FDCs)"))))
		 	  { if(order.getDrugRegimen().equals(drugorder.getDrugRegimen()))
		 		  { 
		 			 onOrigFirstLines = false;
		 			
		 		  }
		 		  else
		 		  {  onOrigFirstLines = true;
		 			drugorder=order;
		 			ret.put(ptId, new BooleanResult(onOrigFirstLines, this, context));
		 		  }
		 		 if(order.getDiscontinuedDate()!=null)
			 	  { 
			 		 onOrigFirstLines=false; 
			 		ret.put(ptId, new BooleanResult(onOrigFirstLines, this, context));
			 	  }
		 		
		 	  }
		 		 }
		 		 else
		 		 {
		 			if((ptId.equals(order.getPatient().getPatientId()) &&(order.getRegimenChangeType().equals("Substitute")) &&(order.getTypeOfRegimen().equals("First line Anti-retoviral drugs"))))
			 		 {  
			 			onOrigFirstLines=true; 
			 			ret.put(ptId, new BooleanResult(onOrigFirstLines, this, context));
			 			 if(order.getDiscontinuedDate()!=null)
					 	  { 
					 		 onOrigFirstLines=false; 
					 		ret.put(ptId, new BooleanResult(onOrigFirstLines, this, context));
					 	  }
			 		 }
		 			else if((ptId.equals(order.getPatient().getPatientId())&&(order.getRegimenChangeType().equals("Substitute")) &&(order.getTypeOfRegimen().equals("Fixed dose combinations (FDCs)"))))
			 	  { if(order.getDrugRegimen().equals(drugorder.getDrugRegimen()))
			 		  { 
			 			 onOrigFirstLines = false;
			 			
			 		  }
			 		  else
			 		  {  onOrigFirstLines = true;
			 			drugorder=order;
			 			ret.put(ptId, new BooleanResult(onOrigFirstLines, this, context));
			 		  }
			 		 if(order.getDiscontinuedDate()!=null)
				 	  { 
				 		 onOrigFirstLines=false; 
				 		ret.put(ptId, new BooleanResult(onOrigFirstLines, this, context));
				 	  }
			 		
			 	  }
		 		 }
		 	  }
		}
			
		
    }return ret;
}
}

