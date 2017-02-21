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

import org.openmrs.Encounter;
import org.openmrs.Visit;
import org.openmrs.module.chaicore.form.FormManager;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.annotation.AppPage;
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
public class EnterLabResultMainPageController {
	
	public void controller(
            @RequestParam(required = false, value = "encounterId") Encounter encounter,
            PageModel model,
            UiUtils ui,
            Session session,
		    PageRequest pageRequest,
		    @SpringBean ChaiUiUtils chaiUi,
		    @SpringBean FormManager formManager) {
		
		model.addAttribute("encounter", encounter);
		model.addAttribute("visit", encounter.getVisit());
	}
}