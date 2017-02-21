package org.openmrs.module.chaiemr.fragment.controller.intake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.CommonUtils;
import org.openmrs.module.chaiemr.EmrWebConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ListLabOrdersFragmentController {
	
	protected static final Log log = LogFactory.getLog(ListLabOrdersFragmentController.class);
	
	public void controller(
			@RequestParam(required = false, value = "patientId") Patient patient,
            UiUtils ui,
            PageModel model) {
			EncounterType encType = Context.getEncounterService().getEncounterType("Lab Results");
		
			List<Visit> visits = Context.getVisitService().getVisitsByPatient(patient, true, false);

			List<LabOrder> listLabOrder = new ArrayList<LabOrder>();
			for (Visit v : visits) 	{
				for (Encounter enc : v.getEncounters()) {
					if (enc.getEncounterType().getUuid().equals(EmrWebConstants.ENCOUNTER_TYPE_LAB_ORDER_UUID)) {
						List<Encounter> encs = Context.getEncounterService().getEncounters(patient, null, null, null, null, Collections.singleton(encType), null, null, Collections.singleton(v), false);
						Encounter resultEncounter = null;
						if (encs != null && !encs.isEmpty()) {
							resultEncounter = encs.get(encs.size() - 1);
						}
						LabOrder order = new LabOrder(enc, resultEncounter);
						listLabOrder.add(order);
						
					}
				}
			}
			Collections.sort(listLabOrder);
			model.addAttribute("listLabOrder", listLabOrder);
			model.addAttribute("returnUrl", ui.thisUrl());
	}
	
	public class LabOrder implements Comparable<LabOrder>{
		public String visitDate;
		public String orderDate;
		public String updateDate;
		public String encounterId;
		public Date dateOrdered;
		
		public LabOrder(Encounter order, Encounter result) {
			dateOrdered = order.getDateCreated();
			visitDate = CommonUtils.format(order.getVisit().getStartDatetime());
			orderDate = CommonUtils.format(order.getDateCreated());
			updateDate = result != null ? CommonUtils.format(result.getDateCreated()) : "";
			encounterId = order.getEncounterId().toString();
		}

		@Override
		public int compareTo(LabOrder other) {
			return dateOrdered.compareTo(other.dateOrdered);
		}
	}
	
}
