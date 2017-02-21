package org.openmrs.module.chaiemr.fragment.controller.intake;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class CompletedLabResultFragmentController {
	
	Log log = LogFactory.getLog(CompletedLabResultFragmentController.class);
	
	public void controller(@RequestParam(value="visitId",required=false) Visit visit,
			PageModel model) {
	
//	List<Encounter> encounters = Context.getService(ChaiEmrService.class).getLabResultEncounters(visit);
	

	EncounterType encType = Context.getEncounterService().getEncounterType("Lab Results");
	List<Encounter> encs = Context.getEncounterService().getEncounters(visit.getPatient(), null, null, null, null, Collections.singleton(encType), null, null, Collections.singleton(visit), false);
	Encounter enc = null;
	if (encs != null && !encs.isEmpty()) {
		enc = encs.get(encs.size() - 1);
	}
	
	model.addAttribute("encounter", enc);
	
	}

}
