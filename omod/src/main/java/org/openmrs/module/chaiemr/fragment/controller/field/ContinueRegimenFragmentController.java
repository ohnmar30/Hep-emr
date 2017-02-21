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

package org.openmrs.module.chaiemr.fragment.controller.field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugInfo;
import org.openmrs.module.chaiemr.model.DrugOrderProcessed;
import org.openmrs.module.chaiemr.regimen.RegimenChange;
import org.openmrs.module.chaiemr.regimen.RegimenChangeHistory;
import org.openmrs.module.chaiemr.regimen.RegimenDefinition;
import org.openmrs.module.chaiemr.regimen.RegimenDefinitionGroup;
import org.openmrs.module.chaiemr.regimen.RegimenManager;
import org.openmrs.module.chaiemr.regimen.RegimenOrder;
import org.openmrs.module.chaiemr.util.EmrUiUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 *
 */
public class ContinueRegimenFragmentController {

	public void controller(@FragmentParam("patient") Patient patient,
			               @FragmentParam("category") String category,
						   @FragmentParam(value = "includeGroups", required = false) Set<String> includeGroups,
						   FragmentModel model,
						   UiUtils ui,
						   @SpringBean RegimenManager regimenManager,
						   @SpringBean EmrUiUtils chaiUi) {
		ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		Map<String, DrugInfo> drugInfoMap = new LinkedHashMap<String, DrugInfo>();
		for(DrugInfo drugInfo:chaiEmrService.getDrugInfo()){
			drugInfoMap.put(drugInfo.getDrugName().toString(), drugInfo);
		}
		
		model.addAttribute("drugInfoMap", drugInfoMap);
		model.addAttribute("patient", patient);
		
		OrderService os = Context.getOrderService();
		Concept masterSet = regimenManager.getMasterSetConcept(category);
		RegimenChangeHistory history = RegimenChangeHistory.forPatient(patient, masterSet);
		RegimenChange lastChange = history.getLastChange();
		RegimenOrder baseline = lastChange != null ? lastChange.getStarted() : null;
		List<DrugOrder> continueRegimen = new ArrayList<DrugOrder>(baseline.getDrugOrders());
		List<DrugOrderProcessed> drugOrderProcessed = new ArrayList<DrugOrderProcessed>();
		for(DrugOrder continueRegim:continueRegimen){
			DrugOrderProcessed drugOrderProcess=chaiEmrService.getDrugOrderProcessed(continueRegim);
			drugOrderProcessed.add(drugOrderProcess);
		}
		model.addAttribute("continueRegimen", continueRegimen);
		model.addAttribute("drugOrderProcessed", drugOrderProcessed);
	}
}