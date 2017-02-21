package org.openmrs.module.chaiemr.reporting.builder.common;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.ReportUtils;
import org.openmrs.module.chaicore.report.builder.AbstractReportBuilder;
import org.openmrs.module.chaicore.report.builder.Builds;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.reporting.ColumnParameters;
import org.openmrs.module.chaiemr.reporting.EmrReportingUtils;
import org.openmrs.module.chaiemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.HivIndicatorLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.art.ArtIndicatorLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Builds({"chaiemr.common.report.patientOutcomeART"})
public class ArtOutcomeBuilder extends AbstractReportBuilder{
	@Autowired
	private CommonDimensionLibrary commonDimensions;

	@Autowired
	private HivIndicatorLibrary hivIndicators;
	
	@Autowired
	private ArtIndicatorLibrary artIndicators;
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
				ReportUtils.map(creatArtOutcomeDeathDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(creatArtOutcomeTransferredOutDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(creatArtOutcomeLostMissingDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(creatArtOutcomeCompletedArtDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(creatArtOutcomeDataSet(), "startDate=${startDate},endDate=${endDate}")
				
				
		);
	}
	
	/**
	 * Creates the HIV enrolled data set
	 * @return the data set
	 */
	private DataSetDefinition creatArtOutcomeDeathDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("P");
		dsd.setDescription("No. of death reported at the end of this month ");
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
                
		EmrReportingUtils.addRow(dsd, "P1", "No. of ", ReportUtils.map(hivIndicators.outcomeArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
    
	private DataSetDefinition creatArtOutcomeTransferredOutDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("Q");
		dsd.setDescription("No. of death reported at the end of this month ");
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
                
                
		EmrReportingUtils.addRow(dsd, "Q1", "No. of ", ReportUtils.map(hivIndicators.outcometransferredArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition creatArtOutcomeLostMissingDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("R");
		dsd.setDescription("No. of death reported at the end of this month ");
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
                
		
		EmrReportingUtils.addRow(dsd, "R1", "No. of ", ReportUtils.map(hivIndicators.outcomelostmissingArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition creatArtOutcomeCompletedArtDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("S");
		dsd.setDescription("No. of patients stopping ART at the end of this month  ");
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
                
                
		EmrReportingUtils.addRow(dsd, "S1", "No. of ", ReportUtils.map(hivIndicators.restartART(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition creatArtOutcomeDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("T");
		dsd.setDescription("No. of patients on ART at the end of this month ");
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
                
                
		EmrReportingUtils.addRow(dsd, "T1", "No. of ", ReportUtils.map(hivIndicators.startArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	

	
}
