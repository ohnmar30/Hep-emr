package org.openmrs.module.chaiemr.fragment.controller.field;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.chaiemr.regimen.RegimenChange;
import org.openmrs.module.chaiemr.regimen.RegimenChangeHistory;
import org.openmrs.module.chaiemr.regimen.RegimenManager;
import org.openmrs.module.chaiemr.regimen.RegimenOrder;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.springframework.web.bind.annotation.RequestParam;

public class StopDrugRegimenFragmentController {
	public void stopRegimen(@RequestParam("patient") Patient patient,
			@RequestParam("reason") Concept reason,
			@RequestParam(value="otherReason",required = false) String otherReason,
			@RequestParam(value="discontinueDate",required = false) String discontinueDate,
			@SpringBean RegimenManager regimenManager) {
		OrderService os = Context.getOrderService();
		ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date changeDate=new Date();
		try {
			changeDate = dateFormat.parse(discontinueDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date curDate = new Date();
		SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat(
				"dd-MMM-yy HH:mm:ss");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd-MMM-yyyy").format(changeDate);
		try {
			date = mysqlDateTimeFormatter.parse(modifiedDate
					+ " " + curDate.getHours() + ":" + curDate.getMinutes()
					+ ":" + curDate.getSeconds());
		} catch (ParseException e) {
			date = curDate;
			e.printStackTrace();
		}
		
		String category="ARV";
		Concept masterSet = regimenManager.getMasterSetConcept(category);
		RegimenChangeHistory history = RegimenChangeHistory.forPatient(patient, masterSet);
		RegimenChange lastChange = history.getLastChange();
		RegimenOrder baseline = lastChange != null ? lastChange.getStarted() : null;
		List<DrugOrder> toStop = new ArrayList<DrugOrder>(baseline.getDrugOrders());
		for (DrugOrder o : toStop) {
			DrugOrderProcessed dop=chaiEmrService.getDrugOrderProcessed(o);
			if(dop!=null){
			dop.setDiscontinuedDate(new Date());
			chaiEmrService.saveDrugOrderProcessed(dop);
			}
			o.setDiscontinued(true);
			o.setDiscontinuedDate(date);
			o.setDiscontinuedBy(Context.getAuthenticatedUser());
			o.setDiscontinuedReason(reason);
			o.setDiscontinuedReasonNonCoded(otherReason);
			os.saveOrder(o);
		}
	}
}
