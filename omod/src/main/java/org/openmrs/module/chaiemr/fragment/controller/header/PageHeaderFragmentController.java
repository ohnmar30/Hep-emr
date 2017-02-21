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

package org.openmrs.module.chaiemr.fragment.controller.header;

import org.jfree.util.Log;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.util.ServerInformation;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.util.OpenmrsUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller for page header
 */
public class PageHeaderFragmentController {

	public void controller(FragmentModel model,PageRequest pageRequest,
						   @SpringBean ChaiUiUtils chaiui) {

		Map<String, Object> chaiemrInfo = ServerInformation.getChaiemrInformation();

		String moduleVersion = (String) chaiemrInfo.get("version");
		boolean isSnapshot = moduleVersion.endsWith("SNAPSHOT");

		if (isSnapshot) {
			Date moduleBuildDate = (Date) chaiemrInfo.get("buildDate");
			moduleVersion += " (" + chaiui.formatDateTime(moduleBuildDate) + ")";
		}

		model.addAttribute("moduleVersion", moduleVersion);

		model.addAttribute("systemLocation", Context.getService(ChaiEmrService.class).getDefaultLocation());
		model.addAttribute("systemLocationCode", Context.getService(ChaiEmrService.class).getDefaultLocationMflCode());
		
		// Get apps for the current user
		List<AppDescriptor> apps = Context.getService(AppFrameworkService.class).getAppsForCurrentUser();
		
		

		// Sort by order property
		Collections.sort(apps, new Comparator<AppDescriptor>() {
			@Override
			public int compare(AppDescriptor left, AppDescriptor right) {
				return OpenmrsUtil.compareWithNullAsGreatest(left.getOrder(), right.getOrder());
			}
		});

		model.addAttribute("apps", apps);
		AppDescriptor currentApp = chaiui.getCurrentApp(pageRequest);
		model.addAttribute("currentApp", currentApp);
		
		Log.info("Ohnmar: inside controller");
		for (AppDescriptor app : apps)
		{
			Log.info("Ohnmar app: " + app.getDescription() + "|" + app.getId());
			Log.info(app.toString());
			
		}
	}
}