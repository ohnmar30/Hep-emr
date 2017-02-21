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

package org.openmrs.module.chaiemr.fragment.controller.program;

import org.openmrs.Patient;
import org.openmrs.module.chaicore.UiResource;
import org.openmrs.module.chaicore.program.ProgramDescriptor;
import org.openmrs.module.chaicore.program.ProgramManager;
import org.openmrs.module.chaiemr.EmrWebConstants;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Program care summaries fragment
 */
public class ProgramCarePanelsFragmentController {

	public void controller(FragmentModel model,
						   @FragmentParam("patient") Patient patient,
						   @FragmentParam("complete") boolean complete,
						   @FragmentParam("activeOnly") boolean activeOnly,
						   @RequestParam(required = false, value = "startDate") String startDate,
						   @RequestParam(required = false, value = "endDate") String endDate,
						   @SpringBean ProgramManager programManager) {

		List<UiResource> carePanels = new ArrayList<UiResource>();

/*		Collection<ProgramDescriptor> programs = activeOnly
				? programManager.getPatientActivePrograms(patient)
				: programManager.getPatientPrograms(patient);
*/
		//Show all care panels 
		Collection<ProgramDescriptor> programs = programManager.getPatientPrograms(patient);
		for (ProgramDescriptor programDescriptor : programs) {
			carePanels.add(programDescriptor.getFragments().get(EmrWebConstants.PROGRAM_CARE_PANEL_FRAGMENT));
		}

		model.addAttribute("patient", patient);
		model.addAttribute("carePanels", carePanels);
		model.addAttribute("complete", complete);
	}
}