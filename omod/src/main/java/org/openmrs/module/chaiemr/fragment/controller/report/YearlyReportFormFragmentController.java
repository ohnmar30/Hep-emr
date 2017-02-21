package org.openmrs.module.chaiemr.fragment.controller.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class YearlyReportFormFragmentController {
	public void controller(FragmentModel model, UiUtils ui) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String year = df.format(date);
		int currYear = Integer.parseInt(year);
		List<Integer> listOfYear = new ArrayList<Integer>();
		for (Integer i = currYear; i > currYear - 11; i--) {
			listOfYear.add(i);
		}
		model.addAttribute("listOfYear", listOfYear);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		model.addAttribute("currDate", formatter.format(new Date()));
	}
}
