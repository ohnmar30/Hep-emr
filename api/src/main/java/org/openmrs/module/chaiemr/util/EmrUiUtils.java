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

package org.openmrs.module.chaiemr.util;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.DrugOrder;
import org.openmrs.module.chaicore.CoreConstants;
import org.openmrs.module.chaiemr.regimen.DrugReference;
import org.openmrs.module.chaiemr.regimen.RegimenChange;
import org.openmrs.module.chaiemr.regimen.RegimenChangeHistory;
import org.openmrs.module.chaiemr.regimen.RegimenDefinition;
import org.openmrs.module.chaiemr.regimen.RegimenOrder;
import org.openmrs.module.chaiui.ChaiUiUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * UI utility methods for web pages
 */
@Component
public class EmrUiUtils {

	@Autowired
	private ChaiUiUtils chaiUi;

	/**
	 * Formats a drug reference
	 * @param drugRef the drug reference
	 * @return the string value
	 */
	public String formatDrug(DrugReference drugRef, UiUtils ui) {
		return drugRef.isConceptOnly() ? drugRef.getConcept().getPreferredName(CoreConstants.LOCALE).getName() : drugRef.getDrug().getName();
	}

	/**
	 * Formats a regimen in long format
	 * @param regimen the regimen
	 * @param ui the UI utils
	 * @return the string value
	 */
	public String formatRegimenShort(RegimenOrder regimen, UiUtils ui) {
		if (CollectionUtils.isEmpty(regimen.getDrugOrders())) {
			return "Empty";
		}
		List<String> components = new ArrayList<String>();
		for (DrugOrder o : regimen.getDrugOrders()) {
			ConceptName cn = o.getConcept().getPreferredName(CoreConstants.LOCALE);
			if (cn == null) {
				cn = o.getConcept().getName(CoreConstants.LOCALE);
			}
			components.add(cn.getName());
		}
		return OpenmrsUtil.join(components, ", ");
	}

	/**
	 * Formats a regimen in long format
	 * @param regimen the regimen
	 * @param ui the UI utils
	 * @return the string value
	 */
	public String formatRegimenLong(RegimenOrder regimen, UiUtils ui) {
		if (CollectionUtils.isEmpty(regimen.getDrugOrders())) {
			return "Empty";
		}
		List<String> components = new ArrayList<String>();
		for (DrugOrder o : regimen.getDrugOrders()) {
			StringBuilder sb = new StringBuilder();

			ConceptName cn = o.getConcept().getShortNameInLocale(CoreConstants.LOCALE);
			if (cn == null) {
				cn = o.getConcept().getName(CoreConstants.LOCALE);
			}
			sb.append(cn.getName());

			if (o.getDrug() != null) {
				sb.append(" " + o.getDrug().getDoseStrength() + o.getDrug().getUnits());
			}

			if (o.getDose() != null) {
				// If dose is a whole number, don't format with decimals... e.g. 3.0tabs looks weird
				boolean hasDecimals = Math.floor(o.getDose()) != o.getDose();
				String dose = hasDecimals ? ui.format(o.getDose()) : ui.format(o.getDose().intValue());
				
				sb.append(" " + dose + o.getUnits());
			}
			if (o.getFrequency() != null) {
				sb.append(" " + o.getFrequency());
			}
			components.add(sb.toString());
		}
		return OpenmrsUtil.join(components, " + ");
	}

	/**
	 * Creates a simple regimen object
	 * @param regimen the regimen
	 * @param ui the UI utils
	 * @return the simple object with { shortDisplay, longDisplay }
	 */
	public SimpleObject simpleRegimen(RegimenOrder regimen, UiUtils ui) {
		if (regimen == null) {
			return SimpleObject.create("shortDisplay", "None", "longDisplay", "None");
		} else {
			return SimpleObject.create("shortDisplay", formatRegimenShort(regimen, ui), "longDisplay", formatRegimenLong(regimen, ui));
		}
	}

	/**
	 * Converts the given regimen history to simple objects
	 * @param history the regimen history
	 * @param ui the UI utils
	 * @return a list of objects with { startDate, endDate, shortDisplay, longDisplay, changeReasons[] }
	 */
	public List<SimpleObject> simpleRegimenHistory(RegimenChangeHistory history, UiUtils ui) {
		List<RegimenChange> changes = history.getChanges();
		List<SimpleObject> ret = new ArrayList<SimpleObject>();

		if (changes.size() == 0) {
			return ret;
		}
		
		Date now = new Date();

		for (int i = 0; i < changes.size(); ++i) {
			RegimenChange change = changes.get(i);
			Date startDate = change.getDate();
			RegimenOrder regimen = change.getStarted();
			Date endDate = null;
			List<String> changeReasons = new ArrayList<String>();
			if (i + 1 < changes.size()) {
				RegimenChange next = changes.get(i + 1);
				endDate = next.getDate();
				if (next.getChangeReasons() != null) {
					for (Concept c : next.getChangeReasons()) {
						changeReasons.add(ui.format(c));
					}
				}
				if (next.getChangeReasonsNonCoded() != null) {
					changeReasons.addAll(next.getChangeReasonsNonCoded());
				}
			}

			boolean current = OpenmrsUtil.compare(startDate, now) <= 0 && (endDate == null || OpenmrsUtil.compare(endDate, now) > 0);
			ret.add(SimpleObject.create(
				"startDate", chaiUi.formatDate(startDate),
				"endDate", chaiUi.formatDate(endDate),
				"regimen", simpleRegimen(regimen, ui),
				"changeReasons", changeReasons,
				"origin", regimen,
				"current", current
			));
		}

		return ret;
	}

	/**
	 * Converts regimen definitions to simple objects
	 * @param definitions the regimen definitions
	 * @param ui the UI utils
	 * @return a list of objects with { name, suitability, components.conceptId, components.dose, components.units }
	 */
	public List<SimpleObject> simpleRegimenDefinitions(Collection<RegimenDefinition> definitions, UiUtils ui) {
		return SimpleObject.fromCollection(definitions, ui,
				"name", "group.code", "components.drugRef", "components.dose", "components.units", "components.frequency","components.duration"
		);
	}
}