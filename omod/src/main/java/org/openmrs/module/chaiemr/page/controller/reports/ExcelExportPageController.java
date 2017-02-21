package org.openmrs.module.chaiemr.page.controller.reports;

import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ExcelExportPageController {
	public void controller(@RequestParam("year") String year,
			PageModel model, UiUtils ui) {
    model.addAttribute("year", year);
	}
}
