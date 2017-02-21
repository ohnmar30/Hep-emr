/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.chaiemr.reporting.builder.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.ReportUtils;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

import org.openmrs.module.chaicore.report.builder.AbstractReportBuilder;
import org.openmrs.module.chaicore.report.builder.Builds;
import org.openmrs.module.chaiemr.reporting.ColumnParameters;
import org.openmrs.module.chaiemr.reporting.EmrReportingUtils;
import org.openmrs.module.chaiemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.HivIndicatorLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Builds({"chaiemr.common.report.patientsTBreceivedART"})
public class HIVPositiveTBReceivedARTBuilder extends AbstractReportBuilder{
       protected static final Log log = LogFactory.getLog(HIVPositiveTBReceivedARTBuilder.class);

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
				ReportUtils.map(createTbDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createCumulativeTbDataSet(), "startDate=${startDate},endDate=${endDate}")
			    
		);
	}
	
	/**
	 * Creates the ART data set
	 * @return the data set
	 */
	private DataSetDefinition createTbDataSet() { 
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("P");
		dsd.setDescription("No. of  HIV positive TB patients who have received ART during this month");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		

		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters female=new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male=new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total=new ColumnParameters("T", "total", "");


		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,female,male,total);
		List<String> indSuffixes = Arrays.asList("CF","CM","FM","MA", "TT");       
                
		EmrReportingUtils.addRow(dsd, "P1", "No. of detected cases (TB patients on ART)", ReportUtils.map(hivIndicators.initiatedARTandTB(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition createCumulativeTbDataSet() { 
	CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
	dsd.setName("S");
	dsd.setDescription("Cumulative no. of HIV positive TB patients who received ART at the end of this month ");
	dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
	dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
	dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
	dsd.addDimension("gender", map(commonDimensions.gender()));

	
	

	ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
	ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
	ColumnParameters female=new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
	ColumnParameters male=new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
	ColumnParameters total=new ColumnParameters("T", "total", "");


	String indParams = "startDate=${startDate},endDate=${endDate}";
	List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,female,male,total);
	List<String> indSuffixes = Arrays.asList("CF","CM","FM","MA", "TT");    
	
	EmrReportingUtils.addRow(dsd, "S1", "Cumulative No. of detected cases (TB patients on ART)", ReportUtils.map(hivIndicators.enrolledCumulativeTB(), indParams),  allColumns ,indSuffixes);
	
	return dsd;
}
	
}
