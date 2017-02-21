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

public class OnSwitchLineArtCalculation extends AbstractPatientCalculation {

	
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
		 	  for(DrugOrderProcessed order:drugorderprocess)
		 	  { 
		 		if(order.getPatient().getAge()<=14) 
		 		{
		 	  if((ptId.equals(order.getPatient().getPatientId())&&(order.getRegimenChangeType().equals("Switch")) &&(order.getTypeOfRegimen().equals("Fixed dose combinations (FDCs)"))))
		 	  { if(order.getDrugRegimen().equals(drugorder.getDrugRegimen()))
		 		  { 
		 			 onOrigFirstLine = false;
		 			
		 		  }
		 		  else
		 		  {  onOrigFirstLine = true;
		 			drugorder=order;
		 			ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
		 		  }
		 		 if(order.getDiscontinuedDate()!=null)
			 	  { 
			 		 onOrigFirstLine=false; 
			 		ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
			 	  }
		 		
		 	  }
		 		}
		 		else
		 		{  
		 			if((ptId.equals(order.getPatient().getPatientId())&&(order.getRegimenChangeType().equals("Switch")) &&(order.getTypeOfRegimen().equals("Fixed dose combinations (FDCs)"))&&(!(order.getDrugRegimen().equals("AZT/3TC+TDF+LPV/r")))))
				 	  { if(order.getDrugRegimen().equals(drugorder.getDrugRegimen()))
				 		  { 
				 			 onOrigFirstLine = false;
				 			
				 		  }
				 		  else
				 		  {  onOrigFirstLine = true;
				 			drugorder=order;
				 			
				 			ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
				 		  }
				 		 if((ptId.equals(order.getPatient().getPatientId()))&&order.getDiscontinuedDate()!=null)
					 	  { 
					 		 onOrigFirstLine=false; 
					 		ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
					 	  }
				 		
				 	  }
		 			else
		 				{ 
		 				
		 				if((ptId.equals(order.getPatient().getPatientId())&&(order.getRegimenChangeType().equals("Switch")) &&(order.getTypeOfRegimen().equals("Second line ART"))&&(!(order.getDrugRegimen().equals("AZT/3TC+TDF+LPV/r")))))
		 				
					 	  { if(order.getDrugRegimen().equals(drugorder.getDrugRegimen()))
					 		  { 
					 			 onOrigFirstLine = false;
					 			
					 		  }
					 		  else
					 		  {  onOrigFirstLine = true;
					 			drugorder=order;
					 		
					 			ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
					 		  }
					 		 if((ptId.equals(order.getPatient().getPatientId()))&&order.getDiscontinuedDate()!=null)
						 	  { 
						 		 onOrigFirstLine=false; 
						 		ret.put(ptId, new BooleanResult(onOrigFirstLine, this, context));
						 	  }
					 		
					 	  }
		 				
		 				}
		 			
		 		}
		 	  }
		}
			

			
		}
		
		return ret;
    }
}