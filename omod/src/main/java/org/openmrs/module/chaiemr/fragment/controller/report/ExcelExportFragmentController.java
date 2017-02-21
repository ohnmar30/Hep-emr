package org.openmrs.module.chaiemr.fragment.controller.report;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.export.DownloadService;
import org.openmrs.module.chaiemr.export.ExportAttributeDetailsApi;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ExcelExportFragmentController{

	public void controller(@RequestParam("year") String year,
			FragmentModel model, UiUtils ui,
			HttpServletRequest request,HttpServletResponse response) {
    ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
    ExportAttributeDetailsApi adts = new ExportAttributeDetailsApi();
    adts.setDateStr(year);
    DownloadService downloadService=new DownloadService();
    try {
		downloadService.downloadXLS(adts, request, response);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
