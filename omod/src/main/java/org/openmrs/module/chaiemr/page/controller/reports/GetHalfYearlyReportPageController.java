package org.openmrs.module.chaiemr.page.controller.reports;

import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class GetHalfYearlyReportPageController {
	public void controller(@RequestParam("halfYearly") String halfYearly,
			PageModel model, UiUtils ui) {
    model.addAttribute("halfYearly", halfYearly);
	}
}
