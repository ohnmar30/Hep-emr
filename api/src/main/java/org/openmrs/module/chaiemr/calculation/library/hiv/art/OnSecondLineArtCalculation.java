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

package org.openmrs.module.chaiemr.calculation.library.hiv.art;

import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.calculation.BaseEmrCalculation;
import org.openmrs.module.chaiemr.calculation.EmrCalculationUtils;
import org.openmrs.module.chaiemr.calculation.library.hiv.LostToFollowUpCalculation;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.chaiemr.regimen.RegimenOrder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Calculates whether patients are on second-line ART regimens
 */
public class OnSecondLineArtCalculation extends AbstractPatientCalculation {

	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection, java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 * @should return null for patients who have never started ARVs
	 * @should return whether patients are currently taking a second-line regimen
	 */
	@Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> arg1, PatientCalculationContext context) {

		// Get active ART regimen of each patient
		CalculationResultMap ret = new CalculationResultMap();
		for (Integer ptId : cohort) {
			boolean onOrigFirstLine = false;
			 ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		 	   List<DrugOrderProcessed> drugorderprocess = chaiEmrService.getAllfirstLine();
		 	  DrugOrderProcessed drugorder = new DrugOrderProcessed();
		 	   {
		 	  for(DrugOrderProcessed order:drugorderprocess)
		 	  { 
		 		
		 		
		 			if((ptId.equals(order.getPatient().getPatientId())&&(!order.getDrugRegimen().equals("AZT/3TC+TDF+LPV/r"))&&(order.getRegimenChangeType().equals("Switch")) &&(order.getTypeOfRegimen().equals("Fixed dose combinations (FDCs)"))))
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
		}
			

			
		}
		return ret;
    }
}