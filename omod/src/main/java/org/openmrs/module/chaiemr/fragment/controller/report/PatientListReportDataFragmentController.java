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

package org.openmrs.module.chaiemr.fragment.controller.report;

import org.apache.commons.collections.map.HashedMap;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaicore.report.CalculationReportDescriptor;
import org.openmrs.module.chaicore.report.ReportManager;
import org.openmrs.module.chaiemr.metadata.CommonMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.data.converter.PropertyConverter;
import org.openmrs.module.reporting.data.person.definition.PersonAttributeDataDefinition;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.Map;

/**
 * Patient list report fragment
 */
public class PatientListReportDataFragmentController {

	public void controller(@FragmentParam("reportData") ReportData reportData, FragmentModel model) {

		// We assume that this kind of report produces a single SimpleDataSet
		SimpleDataSet dataSet = (SimpleDataSet) reportData.getDataSets().entrySet().iterator().next().getValue();

		model.addAttribute("definition", reportData.getDefinition());
		model.addAttribute("dataSet", dataSet);
		model.addAttribute("summary", createSummary(dataSet));
	}

	/**
	 * Creates a summary of a patient data set
	 * @param dataSet the data set
	 * @return the summary
	 */
	protected Map<String, Integer> createSummary(SimpleDataSet dataSet) {
		Map<String, Integer> summary = new HashedMap();
		 
		
		int males = 0, females = 0;
		int child=0;
		for (DataSetRow row : dataSet.getRows()) {
			String gender = (String) row.getColumnValue("Sex");
			String age =   row.getColumnValue("Age").toString();
			
			
			if(Integer.parseInt(age) > 12){
				if (gender.equals("M")) {
					++males;
				} else if (gender.equals("F")) {
					++females;
				}
			}
			else{
				++child;
			}
				
		}

		summary.put("total", dataSet.getRows().size());
		summary.put("males", males);
		summary.put("females", females);
		summary.put("child", child);
		return summary;
	}

	private int parseInt(String age) {
		// TODO Auto-generated method stub
		return 0;
	}
}