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

package org.openmrs.module.chaiemr.page.controller;

import org.apache.log4j.Logger;
import org.openmrs.module.chaicore.CoreUtils;
import org.openmrs.module.chaicore.report.IndicatorReportDescriptor;
import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.ReportManager;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.module.chaiui.annotation.SharedPage;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Report view page
 */
@SharedPage
public class ReportViewPageController {

	private static Logger logger = Logger.getLogger(ReportViewPageController.class);

	public void get(@RequestParam("request") ReportRequest reportRequest, @RequestParam("returnUrl") String returnUrl, PageRequest pageRequest, PageModel model, @SpringBean ReportManager reportManager, @SpringBean ChaiUiUtils chaiUi, @SpringBean ReportService reportService) throws Exception {

		ReportDefinition definition = reportRequest.getReportDefinition().getParameterizable();
		ReportDescriptor report = reportManager.getReportDescriptor(definition);

		CoreUtils.checkAccess(report, chaiUi.getCurrentApp(pageRequest));

		ReportData reportData = reportService.loadReportData(reportRequest);
		model.addAttribute("reportRequest", reportRequest);
		model.addAttribute("definition", definition);
		model.addAttribute("isIndicator", report instanceof IndicatorReportDescriptor);
		model.addAttribute("reportData", reportData);
		model.addAttribute("returnUrl", returnUrl);
	}
}