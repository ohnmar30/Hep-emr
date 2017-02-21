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

package org.openmrs.module.chaiemr.page.controller.intake;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.Program;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.chaicore.form.FormDescriptor;
import org.openmrs.module.chaicore.form.FormManager;
import org.openmrs.module.chaicore.program.ProgramDescriptor;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.EmrWebConstants;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.annotation.AppPage;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.session.Session;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * View patient page for intake app
 */
@AppPage(EmrConstants.APP_INTAKE)
public class IntakeViewPatientPageController {
	
	public void controller(@RequestParam(required = false, value = "visitId") Visit visit,
            @RequestParam(required = false, value = "formUuid") String formUuid,
            @RequestParam(required = false, value = "section") String section,
            PageModel model,
            UiUtils ui,
            Session session,
		    PageRequest pageRequest,
		    @SpringBean ChaiUiUtils chaiUi,
		    @SpringBean FormManager formManager) {
		
		
		if ("".equals(formUuid)) {
			formUuid = null;
		}

		Patient patient = (Patient) model.getAttribute(EmrWebConstants.MODEL_ATTR_CURRENT_PATIENT);

		AppDescriptor thisApp = chaiUi.getCurrentApp(pageRequest);

		List<FormDescriptor> oneTimeFormDescriptors = formManager.getCommonFormsForPatient(thisApp, patient);
		List<SimpleObject> oneTimeForms = new ArrayList<SimpleObject>();
		for (FormDescriptor formDescriptor : oneTimeFormDescriptors) {
			Form form = formDescriptor.getTarget();
			oneTimeForms.add(ui.simplifyObject(form));
		}
		model.addAttribute("oneTimeForms", oneTimeForms);

		model.addAttribute("visits", Context.getVisitService().getVisitsByPatient(patient));
		model.addAttribute("visitsCount", Context.getVisitService().getVisitsByPatient(patient).size());
		Form form = null;
		String selection = null;
		Encounter encounter = null;
		if (visit != null) {
			selection = "visit-" + visit.getVisitId();
		}
		else if (formUuid != null) {
			selection = "form-" + formUuid;
			form = Context.getFormService().getFormByUuid(formUuid);
			List<Encounter> encounters = Context.getEncounterService().getEncounters(patient, null, null, null, Collections.singleton(form), null, null, null, null, false);
			encounter = encounters.size() > 0 ? encounters.get(0) : null;
		}
		else {
			if (StringUtils.isEmpty(section)) {
				section = "overview";
			}
			selection = "section-" + section;
		}
		model.addAttribute("encounter", encounter);
		model.addAttribute("form", form);
		model.addAttribute("visit", visit);
		model.addAttribute("section", section);
		model.addAttribute("selection", selection);
	}
}