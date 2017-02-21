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

package org.openmrs.module.chaiemr.reporting.builder.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.ReportUtils;
import org.openmrs.module.chaicore.report.builder.AbstractReportBuilder;
import org.openmrs.module.chaicore.report.builder.Builds;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.reporting.EmrReportingUtils;
import org.openmrs.module.chaiemr.reporting.ColumnParameters;
import org.openmrs.module.chaiemr.reporting.library.moh731.Moh731IndicatorLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.HivIndicatorLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.art.ArtIndicatorLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.tb.TbIndicatorLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

/**
 * MOH 711 report
 */
@Component
@Builds({"chaiemr.common.report.levelOfAdherence"})
public class LevelOfAdherenceReportBuilder extends AbstractReportBuilder {

	protected static final Log log = LogFactory.getLog(LevelOfAdherenceReportBuilder.class);

	@Autowired
	private CommonDimensionLibrary commonDimensions;

	@Autowired
	private HivIndicatorLibrary hivIndicators;

	/**
	 * @see org.openmrs.module.chaicore.report.builder.AbstractReportBuilder#getParameters(org.openmrs.module.chaicore.report.ReportDescriptor)
	 */
	@Override
	protected List<Parameter> getParameters(ReportDescriptor descriptor) {
		return Arrays.asList(
				new Parameter("startDate", "Start Date", Date.class),
				new Parameter("endDate", "End Date", Date.class)
		);
	}

	/**
	 * @see org.openmrs.module.chaicore.report.builder.AbstractReportBuilder#buildDataSets(org.openmrs.module.chaicore.report.ReportDescriptor, org.openmrs.module.reporting.report.definition.ReportDefinition)
	 */
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		return Arrays.asList(
				ReportUtils.map(createHIVAdherenceDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createLevelAdherenceDataSet(), "startDate=${startDate},endDate=${endDate}")
		);
	}



	/**
	 * Creates the in hiv, got levels of adherence data set
	 * @return the data set
	 */
	private DataSetDefinition createHIVAdherenceDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("P");
		dsd.setDescription("HIV Levels of Adherence");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));

		ColumnParameters colFPeds = new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters colMPeds = new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters colFAdults = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters colMAdults = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters colFTotal = new ColumnParameters("F", "totals, female", "gender=F");
		ColumnParameters colMTotal = new ColumnParameters("M", "totals, male", "gender=M");
		ColumnParameters colTotal = new ColumnParameters("T", "grand total", "");

		List<ColumnParameters> allColumns = Arrays.asList(colFPeds, colMPeds, colFAdults, colMAdults, colFTotal, colMTotal, colTotal);

		String indParams = "startDate=${startDate},endDate=${endDate}";

		EmrReportingUtils.addRow(dsd, "K1-1", "< 5% of doses missed in a period of 30 days (>95%)", ReportUtils.map(hivIndicators.levelOfAdherence(0,5), indParams), allColumns);
		EmrReportingUtils.addRow(dsd, "K1-2", "(5-20)% of doses missed in a period of 30 days (80-95%)", ReportUtils.map(hivIndicators.levelOfAdherence(5,20), indParams), allColumns);
		EmrReportingUtils.addRow(dsd, "K1-3", ">20% of doses missed in a period of 30 days (<80%)", ReportUtils.map(hivIndicators.levelOfAdherence(20,100), indParams), allColumns);


		return dsd;
	}
	
	private DataSetDefinition createLevelAdherenceDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("Q");
		dsd.setDescription("No. of patients assessed for adherence during this month");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));

		ColumnParameters colFPeds = new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters colMPeds = new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters colFAdults = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters colMAdults = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters colFTotal = new ColumnParameters("F", "totals, female", "gender=F");
		ColumnParameters colMTotal = new ColumnParameters("M", "totals, male", "gender=M");
		ColumnParameters colTotal = new ColumnParameters("T", "grand total", "");

		List<ColumnParameters> allColumns = Arrays.asList(colFPeds, colMPeds, colFAdults, colMAdults, colFTotal, colMTotal, colTotal);

		String indParams = "startDate=${startDate},endDate=${endDate}";

		
		EmrReportingUtils.addRow(dsd, "Q1", "No. of patients assessed for adherence during this month", ReportUtils.map(hivIndicators.levelOfAdherence(0,100), indParams), allColumns);


		return dsd;
	}
}