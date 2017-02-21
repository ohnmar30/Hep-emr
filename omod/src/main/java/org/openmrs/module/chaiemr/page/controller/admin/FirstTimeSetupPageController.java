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

package org.openmrs.module.chaiemr.page.controller.admin;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.chaicore.identifier.IdentifierManager;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.annotation.AppPage;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Controller for the guided first-time configuration
 */
@AppPage(EmrConstants.APP_ADMIN)
public class FirstTimeSetupPageController {
	
	public String controller(HttpSession session, PageModel model, UiUtils ui,
							 @SpringBean ChaiUiUtils chaiUi,
							 @SpringBean IdentifierManager identifierManager,
	                         @RequestParam(required = false, value = "defaultLocation") Location defaultLocation,
	                         @RequestParam(required = false, value = "mrnIdentifierSourceStart") String mrnIdentifierSourceStart,
	                         @RequestParam(required = false, value = "hivIdentifierSourceStart") String hivIdentifierSourceStart) {
		
		ChaiEmrService service = Context.getService(ChaiEmrService.class);
		
		// handle submission
		if (defaultLocation != null || StringUtils.isNotEmpty(mrnIdentifierSourceStart) || StringUtils.isNotEmpty(hivIdentifierSourceStart)) {
			if (defaultLocation != null) {
				service.setDefaultLocation(defaultLocation);
			}
			if (StringUtils.isNotEmpty(mrnIdentifierSourceStart)) {
				service.setupMrnIdentifierSource(mrnIdentifierSourceStart);
			}
			if (StringUtils.isNotEmpty(hivIdentifierSourceStart)) {
				service.setupHivUniqueIdentifierSource(hivIdentifierSourceStart);
			}
			chaiUi.notifySuccess(session, "First-Time Setup Completed");

			return "redirect:" + ui.pageLink(EmrConstants.MODULE_ID, "home");
		}
		
		defaultLocation = service.getDefaultLocation();
		IdentifierSource mrnIdentifierSource = identifierManager.getIdentifierSource(MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.OPENMRS_ID));
		IdentifierSource hivIdentifierSource = identifierManager.getIdentifierSource(MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER));

		User authenticatedUser = Context.getAuthenticatedUser();
		
		model.addAttribute("isSuperUser", authenticatedUser != null ? Context.getAuthenticatedUser().isSuperUser() : false);
		model.addAttribute("defaultLocation", defaultLocation);
		model.addAttribute("mrnIdentifierSource", mrnIdentifierSource);
		model.addAttribute("hivIdentifierSource", hivIdentifierSource);
		return null;
	}
}