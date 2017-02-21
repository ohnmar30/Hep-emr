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

package org.openmrs.module.chaiemr.page.controller.reports;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.common.Logger;

import org.openmrs.Program;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.chaicore.program.ProgramDescriptor;
import org.openmrs.module.chaicore.program.ProgramManager;
import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.ReportManager;
import org.openmrs.module.chaiemr.EmrConstants;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.annotation.AppPage;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;

/**
 * Homepage for the reports app
 */
@AppPage(EmrConstants.APP_REPORTS)
public class ReportsHomePageController {
	private static Logger logger = Logger.getLogger(ReportsHomePageController.class);

	public void controller(PageModel model, UiUtils ui, @SpringBean ReportManager reportManager, @SpringBean ProgramManager programManager, @SpringBean ChaiUiUtils chaiUi, PageRequest pageRequest) {
		logger.info("Reporting");
		AppDescriptor currentApp = chaiUi.getCurrentApp(pageRequest);

		Map<String, List<SimpleObject>> reportsByProgram = new LinkedHashMap<String, List<SimpleObject>>();

		List<SimpleObject> common = new ArrayList<SimpleObject>();
		for (ReportDescriptor report : reportManager.getCommonReports(currentApp)) {
			common.add(ui.simplifyObject(report));
		}

		reportsByProgram.put("", common);

		for (ProgramDescriptor programDescriptor : programManager.getAllProgramDescriptors()) {
			Program program = programDescriptor.getTarget();
			List<ReportDescriptor> reports = reportManager.getProgramReports(currentApp, program);

			if (reports.size() > 0) {
				List<SimpleObject> forProgram = new ArrayList<SimpleObject>();

				// We're not calling ui.simplifyCollection because it doesn't
				// play well with subclasses
				for (ReportDescriptor report : reports) {
					forProgram.add(ui.simplifyObject(report));
				}

				reportsByProgram.put(program.getName(), forProgram);
			}
		}

		model.addAttribute("reportsByProgram", reportsByProgram);
	}
}