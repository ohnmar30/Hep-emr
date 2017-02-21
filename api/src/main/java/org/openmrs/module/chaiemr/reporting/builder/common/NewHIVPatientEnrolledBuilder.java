/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.chaiemr.reporting.builder.common;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.ReportUtils;
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
@Builds({"chaiemr.common.report.newHIVPatientEnrolled"})
public class NewHIVPatientEnrolledBuilder extends AbstractReportBuilder{

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
				ReportUtils.map(createNewHIVEnrolledDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHIVEnrolledwithPerformanceADataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHIVEnrolledwithRiskDataSet(), "startDate=${startDate},endDate=${endDate}")
		);
	}

	
	/**
	 * Creates the HIV enrolled data set
	 * @return the data set
	 */
	private DataSetDefinition createNewHIVEnrolledDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("P");
		dsd.setDescription("New patients enrolled in HIV care during this month");
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
                
		EmrReportingUtils.addRow(dsd, "P1", "No. of ", ReportUtils.map(hivIndicators.notInART(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition createHIVEnrolledwithPerformanceADataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("S");
		dsd.setDescription("Performance scale (A, B, C)");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));
		dsd.addDimension("parameter", map(commonDimensions.performanceScales()));
		
		
		List<ColumnParameters> columnsA = new ArrayList<ColumnParameters>();
		columnsA.add(new ColumnParameters("FP", "Performance scale A,0-14 years, female", "gender=F|age=<15|parameter=A"));
		columnsA.add(new ColumnParameters("MP", "Performance scale A,0-14 years, male", "gender=M|age=<15|parameter=A"));
		columnsA.add(new ColumnParameters("FA", "Performance scale A,>14 years, female", "gender=F|age=15+|parameter=A"));
		columnsA.add(new ColumnParameters("MA", "Performance scale A,>14 years, male", "gender=M|age=15+|parameter=A"));
		columnsA.add(new ColumnParameters("T", "Performance scale A,total", ""));
		
		List<ColumnParameters> columnsB = new ArrayList<ColumnParameters>();
		columnsB.add(new ColumnParameters("FP1", "Performance scale B,0-14 years, female", "gender=F|age=<15|parameter=B"));
		columnsB.add(new ColumnParameters("MP1", "Performance scale B,0-14 years, male", "gender=M|age=<15|parameter=B"));
		columnsB.add(new ColumnParameters("FA1", "Performance scale B,>14 years, female", "gender=F|age=15+|parameter=B"));
		columnsB.add(new ColumnParameters("MA1", "Performance scale B,>14 years, male", "gender=M|age=15+|parameter=B"));
		columnsB.add(new ColumnParameters("T1", "Performance scale B,total", ""));
		
		List<ColumnParameters> columnsC = new ArrayList<ColumnParameters>();
		columnsC.add(new ColumnParameters("FP2", "Performance scale C,0-14 years, female", "gender=F|age=<15|parameter=C"));
		columnsC.add(new ColumnParameters("MP2", "Performance scale C,0-14 years, male", "gender=M|age=<15|parameter=C"));
		columnsC.add(new ColumnParameters("FA2", "Performance scale C,>14 years, female", "gender=F|age=15+|parameter=C"));
		columnsC.add(new ColumnParameters("MA2", "Performance scale C,>14 years, male", "gender=M|age=15+|parameter=C"));
		columnsC.add(new ColumnParameters("T2", "Performance scale C,total", ""));
		String indParams = "startDate=${startDate},endDate=${endDate}";
                
		EmrReportingUtils.addRow(dsd, "S1", "No. of detected case (Performance Scale A) ", ReportUtils.map(hivIndicators.performanceScaleA(), indParams), columnsA);
		EmrReportingUtils.addRow(dsd, "S2", "No. of detected case (Performance Scale B) ", ReportUtils.map(hivIndicators.performanceScaleB(), indParams), columnsB);
		EmrReportingUtils.addRow(dsd, "S3", "No. of detected case (Performance Scale C) ", ReportUtils.map(hivIndicators.performanceScaleC(), indParams), columnsC);
		return dsd;
	}
	
	private DataSetDefinition createHIVEnrolledwithRiskDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("T");
		dsd.setDescription("Risk Factors Code ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("parameter", map(commonDimensions.riskFactor()));
		dsd.addDimension("gender", map(commonDimensions.gender()));
		
		
		ColumnParameters MSMfemale=new ColumnParameters("T1", " risk factor for HIV as MSM ,female", "parameter=1|gender=F");
		ColumnParameters MSMmale=new ColumnParameters("T11", " risk factor for HIV as MSM ,male", "parameter=1|gender=M");
		ColumnParameters MSMtotal=new ColumnParameters("T12", "risk factor for HIV as MSM,total", "");
		
		ColumnParameters SWfemale=new ColumnParameters("T2", "risk factor for HIV as SW,female", "parameter=2|gender=F");
		ColumnParameters SWmale=new ColumnParameters("T22", "risk factor for HIV as SW,male", "parameter=2|gender=M");
		ColumnParameters SWtotal=new ColumnParameters("T23", "risk factor for HIV as SW,total", "");
		
		ColumnParameters heterosexualfemale=new ColumnParameters("T3", "risk factor for HIV as heterosexual,female", "parameter=3|gender=F");
		ColumnParameters heterosexualmale=new ColumnParameters("T33", "risk factor for HIV as heterosexual,male", "parameter=3|gender=M");
		ColumnParameters heterosexualtotal=new ColumnParameters("T34", "risk factor for HIV as heterosexual,total", "");
		
		ColumnParameters HIVfemale=new ColumnParameters("T4", "risk factor for HIV as IDU,female", "parameter=4|gender=F");
		ColumnParameters HIVmale=new ColumnParameters("T44", "risk factor for HIV as IDU,male", "parameter=4|gender=M");
		ColumnParameters HIVtotal=new ColumnParameters("T45", "risk factor for HIV as IDU,total", "");
		
		ColumnParameters Bloodtransfusionfemale=new ColumnParameters("T5", "risk factor for HIV as Blood transfusion,female", "parameter=5|gender=F");
		ColumnParameters Bloodtransfusionmale=new ColumnParameters("T55", "risk factor for HIV as Blood transfusion,male", "parameter=5|gender=M");
		ColumnParameters Bloodtransfusiontotal=new ColumnParameters("T56", "risk factor for HIV as Blood transfusion,total", "");
		
		ColumnParameters Motfemale=new ColumnParameters("T6", "risk factor for HIV as Mother to child,female", "parameter=6|gender=F");
		ColumnParameters Motmale=new ColumnParameters("T66", "risk factor for HIV as Mother to child,male", "parameter=6|gender=M");
		ColumnParameters Mottotal=new ColumnParameters("T67", "risk factor for HIV as Mother to child,total", "");
		
		ColumnParameters unknwnfemale=new ColumnParameters("T7", "risk factor for HIV as unknown,female", "parameter=7|gender=F");
		ColumnParameters unknwnmale=new ColumnParameters("T77", "risk factor for HIV as unknown,male", "parameter=7|gender=M");
		ColumnParameters unknwntotal=new ColumnParameters("T78", "risk factor for HIV as unknown,total", "");
		List<ColumnParameters> allColumns1 = Arrays.asList(MSMfemale, MSMmale, MSMtotal);
		List<ColumnParameters> allColumns2 = Arrays.asList(SWfemale, SWmale, SWtotal);
		List<ColumnParameters> allColumns3 = Arrays.asList(heterosexualfemale, heterosexualmale, heterosexualtotal);
		List<ColumnParameters> allColumns4 = Arrays.asList(HIVfemale, HIVmale, HIVtotal);
		List<ColumnParameters> allColumns5 = Arrays.asList(Bloodtransfusionfemale, Bloodtransfusionmale, Bloodtransfusiontotal);
		List<ColumnParameters> allColumns6 = Arrays.asList(Motfemale, Motmale, Mottotal);
		List<ColumnParameters> allColumns7 = Arrays.asList(unknwnfemale, unknwnmale, unknwntotal);
		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<String> indSuffixes = Arrays.asList("FM","MA", "TT");  
		EmrReportingUtils.addRow(dsd, "T1", "Total No. of detected case   ", ReportUtils.map(hivIndicators.riskFactor1(), indParams), allColumns1,indSuffixes);
		EmrReportingUtils.addRow(dsd, "T2", "Total No. of detected case ", ReportUtils.map(hivIndicators.riskFactor2(), indParams), allColumns2,indSuffixes);
		EmrReportingUtils.addRow(dsd, "T3", "Total No. of detected case  ", ReportUtils.map(hivIndicators.riskFactor3(), indParams), allColumns3,indSuffixes);
		EmrReportingUtils.addRow(dsd, "T4", "Total No. of detected case  ", ReportUtils.map(hivIndicators.riskFactor4(), indParams), allColumns4,indSuffixes);
		EmrReportingUtils.addRow(dsd, "T5", "Total No. of detected case  ", ReportUtils.map(hivIndicators.riskFactor5(), indParams), allColumns5,indSuffixes);
		EmrReportingUtils.addRow(dsd, "T6", "Total No. of detected case ", ReportUtils.map(hivIndicators.riskFactor6(), indParams), allColumns6,indSuffixes);
		EmrReportingUtils.addRow(dsd, "T7", "Total No. of detected case ", ReportUtils.map(hivIndicators.riskFactor7(), indParams), allColumns7,indSuffixes);
		return dsd;
	}

}
