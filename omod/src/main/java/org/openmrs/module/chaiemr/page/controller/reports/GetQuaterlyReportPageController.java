package org.openmrs.module.chaiemr.page.controller.reports;

import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class GetQuaterlyReportPageController {
	public void controller(@RequestParam("quaterly") String quaterly,
			PageModel model, UiUtils ui) {
    model.addAttribute("quaterly", quaterly);
	}
}
