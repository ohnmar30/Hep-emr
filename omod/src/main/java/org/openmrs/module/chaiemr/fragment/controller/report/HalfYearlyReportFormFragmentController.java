package org.openmrs.module.chaiemr.fragment.controller.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class HalfYearlyReportFormFragmentController {
	public void controller(FragmentModel model, UiUtils ui) {
		List<String> listOfYear = new ArrayList<String>();
		listOfYear.add("First Half");
		listOfYear.add("Second Half");
		model.addAttribute("listOfYear", listOfYear);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		model.addAttribute("currDate", formatter.format(new Date()));
	}
	
}
