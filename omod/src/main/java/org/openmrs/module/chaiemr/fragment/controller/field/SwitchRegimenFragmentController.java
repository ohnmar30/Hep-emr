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

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.module.chaiemr.model.DrugInfo;
import org.openmrs.module.chaiemr.regimen.RegimenDefinition;
import org.openmrs.module.chaiemr.regimen.RegimenDefinitionGroup;
import org.openmrs.module.chaiemr.regimen.RegimenManager;
import org.openmrs.module.chaiemr.util.EmrUiUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 *
 */
public class SwitchRegimenFragmentController {

	public void controller(@FragmentParam("patient") Patient patient,
			               @FragmentParam("category") String category,
						   @FragmentParam(value = "includeGroups", required = false) Set<String> includeGroups,
						   FragmentModel model,
						   UiUtils ui,
						   @SpringBean RegimenManager regimenManager,
						   @SpringBean EmrUiUtils chaiUi) {

		List<RegimenDefinitionGroup> regimenGroups = regimenManager.getRegimenGroups(category);

		if (includeGroups != null) {
			regimenGroups = filterGroups(regimenGroups, includeGroups);
		}

		List<RegimenDefinition> regimenDefinitions = new ArrayList<RegimenDefinition>();
		for (RegimenDefinitionGroup group : regimenGroups) {
			regimenDefinitions.addAll(group.getRegimens());
		}
		
		ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
		Map<String, DrugInfo> drugInfoMap = new LinkedHashMap<String, DrugInfo>();
		for(DrugInfo drugInfo:chaiEmrService.getDrugInfo()){
			drugInfoMap.put(drugInfo.getDrugName().toString(), drugInfo);
		}
		
		model.addAttribute("maxComponents", 5);
		model.addAttribute("drugs", regimenManager.getDrugs(category));
		model.addAttribute("regimenGroups", regimenGroups);
		model.addAttribute("regimenDefinitions", chaiUi.simpleRegimenDefinitions(regimenDefinitions, ui));
		model.addAttribute("patient", patient);
		model.addAttribute("drugInfoMap", drugInfoMap);
	}

	/**
	 * Filter regimen groups by code
	 * @param groups the groups
	 * @param includeGroupCodes the group codes to include
	 * @return the filtered groups
	 */
	private static List<RegimenDefinitionGroup> filterGroups(List<RegimenDefinitionGroup> groups, Set<String> includeGroupCodes) {
		List<RegimenDefinitionGroup> filtered = new ArrayList<RegimenDefinitionGroup>();
		for (RegimenDefinitionGroup group : groups) {
			if (includeGroupCodes.contains(group.getCode())) {
				filtered.add(group);
			}
		}
		return filtered;
	}
}