package org.openmrs.module.chaiemr.calculation.library.hiv;

import static org.openmrs.module.chaiemr.calculation.EmrCalculationUtils.daysSince;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Program;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.chaicore.calculation.AbstractPatientCalculation;
import org.openmrs.module.chaicore.calculation.BooleanResult;
import org.openmrs.module.chaicore.calculation.CalculationUtils;
import org.openmrs.module.chaicore.calculation.Filters;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.HivConstants;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.metadatadeploy.MetadataUtils;

public class PatientWaitinglistArtCalculation  extends AbstractPatientCalculation {

	/**
	 * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(java.util.Collection, java.util.Map, org.openmrs.calculation.patient.PatientCalculationContext)
	 * @should return null for patients who have never started ARVs
	 * @should return null for patients who aren't currently on ARVs
	 * @should return whether patients have changed regimens
	 */
	@Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> arg1, PatientCalculationContext context) {

		boolean inArtlist=false;
		CalculationResultMap ret = new CalculationResultMap();
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
        Set<Integer> alive = Filters.alive(cohort, context);
        Set<Integer> inHIV = Filters.inProgram(hivProgram, alive, context);
        Program artProgram = MetadataUtils.existing(Program.class, HivMetadata._Programs.ART);
        Set<Integer> inArt = Filters.inProgram(artProgram, alive, context);
        CalculationResultMap finalList = new CalculationResultMap();
        
        ObsService obs = Context.getObsService();
        for (Integer ptId : cohort) {
            if (inHIV.contains(ptId)) {
               
                  if((inArt.contains(ptId)))
                  {
                	  inArtlist=false;
                  }
                  else
                  {
                	  inArtlist=true;
                  }
                
            }
            else
            {
            	inArtlist=false;
            }
            ret.put(ptId, new BooleanResult(inArtlist, this, context));
        }
        return ret;
    }

}