package org.openmrs.module.chaiemr.reporting.builder.common;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
@Builds({"chaiemr.common.report.artChildmonthlyReport"})
public class ART_ChildMonthlyReportBuilder extends AbstractReportBuilder{
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
				ReportUtils.map(creatArtOutcomeDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createCumulativeactiveTbDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createNewInitiatedPatientTbDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createTbDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createCumulativeTbDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHivDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createCumulativeHivDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map( creatDataSetForCdFourCount(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(creatDataSetForViralLoad(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createNewHIVEnrolledDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHIVEnrolledwithPerformanceADataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHIVEnrolledwithRiskDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createOIDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHIVAdherenceDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createLevelAdherenceDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createElligibleARTDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(childregimensDataset(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(originalDataset(), "startDate=${startDate},endDate=${endDate}"), 
				ReportUtils.map(onsubsituteDataset(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(onswitchsecondDataset(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(onswitchthirdDataset(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(onstockDispensedDataset(), "startDate=${startDate},endDate=${endDate}")
				
		);
	}
	
	/**
	 * Creates the HIV enrolled data set
	 * @return the data set
	 */
	private DataSetDefinition creatArtOutcomeDeathDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("A");
		dsd.setDescription("No. of death reported at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
                
		EmrReportingUtils.addRow(dsd, "A1", "No. of ", ReportUtils.map(hivIndicators.outcomeArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
    
	private DataSetDefinition creatArtOutcomeTransferredOutDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("B");
		dsd.setDescription("No. of patients transferred out under ARV at the end of this month  ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
                
		EmrReportingUtils.addRow(dsd, "B1", "No. of ", ReportUtils.map(hivIndicators.outcometransferredArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition creatArtOutcomeLostMissingDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("C");
		dsd.setDescription("No. of patients missing/lost to follow-up at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
                
		EmrReportingUtils.addRow(dsd, "C1", "No. of ", ReportUtils.map(hivIndicators.outcomelostmissingArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition creatArtOutcomeCompletedArtDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("D");
		dsd.setDescription("No. of patients stopping ART at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
                
		EmrReportingUtils.addRow(dsd, "D1", "No. of ", ReportUtils.map(hivIndicators.stopART(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
	private DataSetDefinition creatArtOutcomeDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("E");
		dsd.setDescription("No. of patients on ART at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
                
		EmrReportingUtils.addRow(dsd, "E1", "No. of ", ReportUtils.map(hivIndicators.startArt(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	private DataSetDefinition createCumulativeactiveTbDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("F");
		dsd.setDescription("Cumulative no. of active follow up  patients ever started on ART at the beginning of this month");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));
		
		
		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		
		ColumnParameters total =new ColumnParameters("T", "total", "age=<15");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns =Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM", "TT"); 
		EmrReportingUtils.addRow(dsd, "F1", "No. of detected cases (Cumulative no. of active follow up  patients ever started on ART )", ReportUtils.map(artIndicators.startedArtCumulative(), indParams), allColumns,indSuffixes);
		
		return dsd;
	}
		
		private DataSetDefinition createNewInitiatedPatientTbDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("G");
		dsd.setDescription("New patients started on ART during this month");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		
		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		
		ColumnParameters total =new ColumnParameters("T", "total", "age=<15");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns =Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM", "TT");     
		EmrReportingUtils.addRow(dsd, "G1", "  No. of detected cases (New patients started on ART)", ReportUtils.map(artIndicators.startedArt(), indParams), allColumns,indSuffixes);
		
		return dsd;
		}
		
		private DataSetDefinition createTbDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("H");
			dsd.setDescription("No. of patients on ART transferred in this month");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));

			
			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			
			ColumnParameters total =new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters>allColumns =Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM", "TT");   
	                
			EmrReportingUtils.addRow(dsd, "H1", "No. of detected cases (Transferred in)", ReportUtils.map(hivIndicators.restartART(), indParams), allColumns,indSuffixes);
			return dsd;
		}
		

		
		private DataSetDefinition createCumulativeTbDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("I");
		dsd.setDescription("Cumulative no. of active follow up patients ever started on ART at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total =new ColumnParameters("T", "total", "age=<15");


		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns =Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM", "TT");   
	            
	            
		EmrReportingUtils.addRow(dsd, "I1", "  No. of detected cases (Cumulative no. of patients ever started on ART )", ReportUtils.map(artIndicators.startedArtCumulativeResult(), indParams), allColumns,indSuffixes);
		return dsd;
	}
	
		private DataSetDefinition createHivDataSet() { 
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("J");
			dsd.setDescription("No. of  HIV positive TB patients who have received ART during this month");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));

			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");
			
			
			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
	                
			EmrReportingUtils.addRow(dsd, "J1", "No. of detected cases (TB patients on ART)", ReportUtils.map(hivIndicators.initiatedARTandTB(), indParams), allColumns,indSuffixes);
			return dsd;
		}
		
		private DataSetDefinition createCumulativeHivDataSet() { 
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("K");
		dsd.setDescription("Cumulative no. of HIV positive TB patients who received ART at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		
		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total=new ColumnParameters("T", "total", "age=<15");
		
		
		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
		EmrReportingUtils.addRow(dsd, "K1", "Cumulative No. of detected cases (TB patients on ART)", ReportUtils.map(hivIndicators.enrolledCumulativeTB(), indParams),  allColumns ,indSuffixes);
		
		return dsd;
	}
		private DataSetDefinition creatDataSetForCdFourCount() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("L");
			dsd.setDescription("No. of patients tested for CD4 count");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			
			
			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
	                
			EmrReportingUtils.addRow(dsd, "L1", "No. of patients tested for CD4 count ", ReportUtils.map(hivIndicators.cdFourTest(), indParams), allColumns,indSuffixes);
			return dsd;
		}
		
		private DataSetDefinition creatDataSetForViralLoad() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("M");
			dsd.setDescription("No. of patients tested for viral load");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));

			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");    
	                
			EmrReportingUtils.addRow(dsd, "M1", "No. of patients tested for viral load", ReportUtils.map(hivIndicators.viralLoadTest(), indParams), allColumns,indSuffixes);
			return dsd;
		}
		
		private DataSetDefinition createNewHIVEnrolledDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("N");
			dsd.setDescription("New patients enrolled in HIV care during this month");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			
			
			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");  
	                
			EmrReportingUtils.addRow(dsd, "N1", "No. of ", ReportUtils.map(hivIndicators.inHIV(), indParams), allColumns,indSuffixes);
			return dsd;
		}
		
		private DataSetDefinition createHIVEnrolledwithPerformanceADataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("O");
			dsd.setDescription("Performance scale (A, B, C)");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			dsd.addDimension("parameter", map(commonDimensions.performanceScales()));
			
			
			List<ColumnParameters> columnsA = new ArrayList<ColumnParameters>();
			columnsA.add(new ColumnParameters("FP", "Performance scale A,0-14 years, female", "gender=F|age=<15|parameter=A"));
			columnsA.add(new ColumnParameters("MP", "Performance scale A,0-14 years, male", "gender=M|age=<15|parameter=A"));
			columnsA.add(new ColumnParameters("T", "Performance scale A 0-14 years,total", "age=<15"));
			
			List<ColumnParameters> columnsB = new ArrayList<ColumnParameters>();
			columnsB.add(new ColumnParameters("FP1", "Performance scale B,0-14 years, female", "gender=F|age=<15|parameter=B"));
			columnsB.add(new ColumnParameters("MP1", "Performance scale B,0-14 years, male", "gender=M|age=<15|parameter=B"));
			columnsB.add(new ColumnParameters("T1", "Performance scale B 0-14 years,total", "age=<15"));
			
			List<ColumnParameters> columnsC = new ArrayList<ColumnParameters>();
			columnsC.add(new ColumnParameters("FP2", "Performance scale C,0-14 years, female", "gender=F|age=<15|parameter=C"));
			columnsC.add(new ColumnParameters("MP2", "Performance scale C,0-14 years, male", "gender=M|age=<15|parameter=C"));
			columnsC.add(new ColumnParameters("T2", "Performance scale C 0-14 years,total", "age=<15"));
			String indParams = "startDate=${startDate},endDate=${endDate}";
	                
			EmrReportingUtils.addRow(dsd, "O1", "No. of detected case (Performance Scale A) ", ReportUtils.map(hivIndicators.performanceScaleA(), indParams), columnsA);
			EmrReportingUtils.addRow(dsd, "O2", "No. of detected case (Performance Scale B) ", ReportUtils.map(hivIndicators.performanceScaleB(), indParams), columnsB);
			EmrReportingUtils.addRow(dsd, "O3", "No. of detected case (Performance Scale C) ", ReportUtils.map(hivIndicators.performanceScaleC(), indParams), columnsC);
			return dsd;
		}
		
		private DataSetDefinition createHIVEnrolledwithRiskDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("P");
			dsd.setDescription("Risk Factors Code ");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("parameter", map(commonDimensions.riskFactor()));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			
			List<ColumnParameters> MSM = new ArrayList<ColumnParameters>();
			MSM.add(new ColumnParameters("T1", " Risk factor for HIV as MSM ,female", "gender=F|age=<15|parameter=1"));
			MSM.add(new ColumnParameters("T11", "Risk factor for HIV as MSM ,male", "gender=M|age=<15|parameter=1"));
			
		    
			List<ColumnParameters> SW = new ArrayList<ColumnParameters>();
			SW.add(new ColumnParameters("T2", " Risk factor for HIV as SW ,female", "gender=F|age=<15|parameter=2"));
			SW.add(new ColumnParameters("T22", "Risk factor for HIV as SW ,male", "gender=M|age=<15|parameter=2"));
			
			List<ColumnParameters> heterosexual = new ArrayList<ColumnParameters>();
			heterosexual.add(new ColumnParameters("T3", " Risk factor for HIV as heterosexual ,female", "gender=F|age=<15|parameter=3"));
			heterosexual.add(new ColumnParameters("T33", "Risk factor for HIV as heterosexual ,male", "gender=M|age=<15|parameter=3"));

			List<ColumnParameters> HIV = new ArrayList<ColumnParameters>();
			HIV.add(new ColumnParameters("T4", " Risk factor for HIV as IDU ,female", "gender=F|age=<15|parameter=4"));
			HIV.add(new ColumnParameters("T44", "Risk factor for HIV as IDU ,male", "gender=M|age=<15|parameter=4"));
		
			List<ColumnParameters> Bloodtransfusion = new ArrayList<ColumnParameters>();
			Bloodtransfusion.add(new ColumnParameters("T5", " Risk factor for HIV as Blood transfusion ,female", "gender=F|age=<15|parameter=5"));
			Bloodtransfusion.add(new ColumnParameters("T55", "Risk factor for HIV as Blood transfusion ,male", "gender=M|age=<15|parameter=5"));
		    
			List<ColumnParameters> Mot = new ArrayList<ColumnParameters>();
			Mot.add(new ColumnParameters("T6", " Risk factor for HIV as Mother to child ,female", "gender=F|age=<15|parameter=6"));
			Mot.add(new ColumnParameters("T66", "Risk factor for HIV as Mother to child ,male", "gender=M|age=<15|parameter=6"));
		
			List<ColumnParameters> unknown = new ArrayList<ColumnParameters>();
			unknown.add(new ColumnParameters("T7", " Risk factor for HIV as unknown ,female", "gender=F|age=<15|parameter=7"));
			unknown.add(new ColumnParameters("T77", "Risk factor for HIV as unknown ,male", "gender=M|age=<15|parameter=7"));
		
			String indParams = "startDate=${startDate},endDate=${endDate}";
			
			EmrReportingUtils.addRow(dsd, "P1", "Total No. of detected case   ", ReportUtils.map(hivIndicators.riskFactor1(), indParams), MSM);
			EmrReportingUtils.addRow(dsd, "P2", "Total No. of detected case ", ReportUtils.map(hivIndicators.riskFactor2(), indParams), SW);
			EmrReportingUtils.addRow(dsd, "P3", "Total No. of detected case  ", ReportUtils.map(hivIndicators.riskFactor3(), indParams), heterosexual);
			EmrReportingUtils.addRow(dsd, "P4", "Total No. of detected case  ", ReportUtils.map(hivIndicators.riskFactor4(), indParams), HIV);
			EmrReportingUtils.addRow(dsd, "P5", "Total No. of detected case  ", ReportUtils.map(hivIndicators.riskFactor5(), indParams), Bloodtransfusion);
			EmrReportingUtils.addRow(dsd, "P6", "Total No. of detected case ", ReportUtils.map(hivIndicators.riskFactor6(), indParams), Mot);
			EmrReportingUtils.addRow(dsd, "P7", "Total No. of detected case ", ReportUtils.map(hivIndicators.riskFactor7(), indParams), unknown);
			return dsd;
		}
		private DataSetDefinition createOIDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("Q");
			dsd.setDescription("No. of patients treated for  Opportunistic Infections during this month ");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			
			
			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");  
			
			EmrReportingUtils.addRow(dsd, "Q1", "No. of detected cases (Treated for OI)", ReportUtils.map(hivIndicators.givenDrugsForOI(), indParams), allColumns,indSuffixes);
			return dsd;
		}
		
		private DataSetDefinition createHIVAdherenceDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("R");
			dsd.setDescription("Of those assessed for adherence, level of adherence in the last month");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));

			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");  

			EmrReportingUtils.addRow(dsd, "R1-1", "< 5% of doses missed in a period of 30 days (>95%)", ReportUtils.map(hivIndicators.levelOfAdherence(0,5), indParams), allColumns,indSuffixes);
			EmrReportingUtils.addRow(dsd, "R1-2", "(5-20)% of doses missed in a period of 30 days (80-95%)", ReportUtils.map(hivIndicators.levelOfAdherence(5,20), indParams), allColumns,indSuffixes);
			EmrReportingUtils.addRow(dsd, "R1-3", ">20% of doses missed in a period of 30 days (<80%)", ReportUtils.map(hivIndicators.levelOfAdherence(20,100), indParams), allColumns,indSuffixes);


			return dsd;
		}
		private DataSetDefinition createLevelAdherenceDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("S");
			dsd.setDescription(" No. of patients assessed for adherence during this month");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));

			
			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");  
			EmrReportingUtils.addRow(dsd, "S1", "No. of patients assessed for adherence during this month", ReportUtils.map(hivIndicators.levelOfAdherence(0,100), indParams), allColumns,indSuffixes);
			


			return dsd;
		}
		
		private DataSetDefinition createElligibleARTDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("T");
			dsd.setDescription("No. of medically eligible patients currently remaining on waiting list for ART at the end of this month");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));

			ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
			ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
			ColumnParameters total=new ColumnParameters("T", "total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(childfemale, childmale ,total);
			List<String> indSuffixes = Arrays.asList("CF","CM","TT");  
	                
			EmrReportingUtils.addRow(dsd, "T1", "No. of detected cases (Eligible for ART)", ReportUtils.map(hivIndicators.notInART(), indParams), allColumns,indSuffixes);
			return dsd;
		}
		
		private DataSetDefinition childregimensDataset() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("U");
			dsd.setDescription(" Regimen at the end of month");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));

			
			ColumnParameters child =new ColumnParameters("CP", "Child", "age=<15");
			

			String indParams = "startDate=${startDate},endDate=${endDate}";

			List<ColumnParameters> allColumns = Arrays.asList(child);
			List<String> indSuffixes = Arrays.asList("CD");
			
			EmrReportingUtils.addRow(dsd, "U1", "Patients having (AZT/3TC+NVP) (60/30+50) mg regimen", ReportUtils.map(hivIndicators.onregimendosenvp(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U2", "Patients having (AZT/3TC+LPV/r)(60/30+100/25) mg regimen", ReportUtils.map(hivIndicators.onregimendoselpvr(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U3", "Patients having (AZT/3TC+EFV) (60/30+200) mg regimen", ReportUtils.map(hivIndicators.onregimendoseefvtwo(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U4", "Patients having (AZT/3TC+EFV) (60/30+600) mg regimen", ReportUtils.map(hivIndicators.onregimendoseefvsix(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U5", "Patients having (ABC/3TC+NVP) (60/30+50) mg regimen", ReportUtils.map(hivIndicators.onregimendoseabcnvp(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U6", "Patients having (ABC/3TC+EFV) (60/30+200) mg regimen", ReportUtils.map(hivIndicators.onregimendoseabcefvtwo(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U7", "Patients having (ABC/3TC+EFV) (60/30+600) mg regimen", ReportUtils.map(hivIndicators.onregimendoseabcefvsix(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U8", "Patients having (ABC/3TC+LPV/r) (60/30+100/25) mg regimen", ReportUtils.map(hivIndicators.onregimenabcdoselpvr(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U9", "Patients having (AZT/3TC+ABC) (60/30+60) mg regimen", ReportUtils.map(hivIndicators.onregimendoseabc(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U10", "Patients having (d4T+3TC+NVP) regimen", ReportUtils.map(hivIndicators.onregimenazt(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U11", "Patients having (d4T+3TC+LPV/r) regimen", ReportUtils.map(hivIndicators.onregimend4t(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U12", "Patients having (d4T+3TC+EFV) regimen", ReportUtils.map(hivIndicators.onregimend4tefv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U13", "Patients having (d4T+3TC+ABC) regimen", ReportUtils.map(hivIndicators.onregimend4tabc(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U14", "Patients having (AZT+3TC+RAL) regimen", ReportUtils.map(hivIndicators.onregimenaztral(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U15", "Patients having (AZT+3TC+ATV/r) regimen", ReportUtils.map(hivIndicators.onregimenatv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U16", "Patients having (ABC+3TC+RAL) regimen", ReportUtils.map(hivIndicators.onregimenral(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U17", "Patients having (TDF+3TC+EFV) regimen", ReportUtils.map(hivIndicators.onregimenefv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U18", "Patients having (TDF+3TC+NVP) regimen", ReportUtils.map(hivIndicators.onregimennvp(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U19", "Patients having (TDF+3TC+LPV/r) regimen", ReportUtils.map(hivIndicators.onregimenlpv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U20", "Patients having (TDF+3TC+RAL) regimen", ReportUtils.map(hivIndicators.onregimentdfral(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U21", "Patients having (TDF+3TC+ATV/r) regimen", ReportUtils.map(hivIndicators.onregimentdfatv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "U22", "Patients having (ABC+3TC+ATV/r) regimen", ReportUtils.map(hivIndicators.onregimenabc(), indParams), allColumns, indSuffixes);
			
			return dsd;
		}
		
		private DataSetDefinition originalDataset() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("V");
			dsd.setDescription(" No. on original 1st line regimen");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			
			ColumnParameters female = new ColumnParameters("FA", "<14 years, female", "gender=F|age=<15");
			ColumnParameters male = new ColumnParameters("MA", "<14 years, male", "gender=M|age=<15");
			ColumnParameters total = new ColumnParameters("T", "grand total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(female,male,total);
			List<String> indSuffixes = Arrays.asList("CF","CM", "TT"); 
			EmrReportingUtils.addRow(dsd, "V1", "No. on original 1st line regimen", ReportUtils.map(artIndicators.onoriginal(), indParams), allColumns, indSuffixes);
			
			
			return dsd;
		}
		private DataSetDefinition onsubsituteDataset() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("W");
			dsd.setDescription("No. on substituted 1st line regimen");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			
			ColumnParameters female = new ColumnParameters("FA", "<14 years, female", "gender=F|age=<15");
			ColumnParameters male = new ColumnParameters("MA", "<14 years, male", "gender=M|age=<15");
			ColumnParameters total = new ColumnParameters("T", "grand total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(female,male,total);
			List<String> indSuffixes = Arrays.asList("CM","CF", "TT"); 
			EmrReportingUtils.addRow(dsd, "W1", "No. on substituted 1st line regimen", ReportUtils.map(artIndicators.onsubsitute(), indParams), allColumns, indSuffixes);
			
			
			return dsd;
		}
		
		private DataSetDefinition onswitchsecondDataset() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("X");
			dsd.setDescription("No. switched to 2nd line regimen");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			
			ColumnParameters female = new ColumnParameters("FA", "<14 years, female", "gender=F|age=<15");
			ColumnParameters male = new ColumnParameters("MA", "<14 years, male", "gender=M|age=<15");
			ColumnParameters total = new ColumnParameters("T", "grand total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(female,male,total);
			List<String> indSuffixes = Arrays.asList("CM","CF", "TT"); 
			EmrReportingUtils.addRow(dsd, "X1", "No. switched to 2nd line regimen", ReportUtils.map(artIndicators.onswitchsecond(), indParams), allColumns, indSuffixes);
			
			
			return dsd;
		}
		
		private DataSetDefinition onswitchthirdDataset() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("Y");
			dsd.setDescription("No. switched to 3rd line regimen");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));
			
			ColumnParameters female = new ColumnParameters("FA", "<14 years, female", "gender=F|age=<15");
			ColumnParameters male = new ColumnParameters("MA", "<14 years, male", "gender=M|age=<15");
			ColumnParameters total = new ColumnParameters("T", "grand total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(female,male,total);
			List<String> indSuffixes = Arrays.asList("CM","CF", "TT"); 
			EmrReportingUtils.addRow(dsd, "Y1", "No. switched to 3rd line regimen", ReportUtils.map(artIndicators.onswitchthird(), indParams), allColumns, indSuffixes);
			
			
			return dsd;
		}
		
		private DataSetDefinition onstockDispensedDataset() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("Z");
			dsd.setDescription("Stock dispensed this mnth");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			
			
			
			ColumnParameters total = new ColumnParameters("T", "grand total", "age=<15");

			String indParams = "startDate=${startDate},endDate=${endDate}";
			List<ColumnParameters> allColumns = Arrays.asList(total);
			List<String> indSuffixes = Arrays.asList("TT"); 
			EmrReportingUtils.addRow(dsd, "Z1", "Patients having (AZT/3TC+NVP) (60/30+50) mg regimen", ReportUtils.map(artIndicators.onregimendosenvp(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z2", "Patients having (AZT/3TC+LPV/r)(60/30+100/25) mg regimen", ReportUtils.map(artIndicators.onregimendoselpvr(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z3", "Patients having (AZT/3TC+EFV) (60/30+200) mg regimen", ReportUtils.map(artIndicators.onregimendoseefvtwo(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z4", "Patients having (AZT/3TC+EFV) (60/30+600) mg regimen", ReportUtils.map(artIndicators.onregimendoseefvsix(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z5", "Patients having (ABC/3TC+NVP) (60/30+50) mg regimen", ReportUtils.map(artIndicators.onregimendoseabcnvp(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z6", "Patients having (ABC/3TC+EFV) (60/30+200) mg regimen", ReportUtils.map(artIndicators.onregimendoseabcefvtwo(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z7", "Patients having (ABC/3TC+EFV) (60/30+600) mg regimen", ReportUtils.map(artIndicators.onregimendoseabcefvsix(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z8", "Patients having (ABC/3TC+LPV/r) (60/30+100/25) mg regimen", ReportUtils.map(artIndicators.onregimenabcdoselpvr(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z9", "Patients having (AZT/3TC+ABC) (60/30+60) mg regimen", ReportUtils.map(artIndicators.onregimendoseabc(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z10", "Patients having (d4T+3TC+NVP) regimen", ReportUtils.map(artIndicators.onregimenazt(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z11", "Patients having (d4T+3TC+LPV/r) regimen", ReportUtils.map(artIndicators.onregimend4t(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z12", "Patients having (d4T+3TC+EFV) regimen", ReportUtils.map(artIndicators.onregimend4tefv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z13", "Patients having (d4T+3TC+ABC) regimen", ReportUtils.map(artIndicators.onregimend4tabc(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z14", "Patients having (AZT+3TC+RAL) regimen", ReportUtils.map(artIndicators.onregimenaztral(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z15", "Patients having (AZT+3TC+ATV/r) regimen", ReportUtils.map(artIndicators.onregimenatv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z16", "Patients having (ABC+3TC+RAL) regimen", ReportUtils.map(artIndicators.onregimenral(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z17", "Patients having (TDF+3TC+EFV) regimen", ReportUtils.map(artIndicators.onregimenefv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z18", "Patients having (TDF+3TC+NVP) regimen", ReportUtils.map(artIndicators.onregimennvp(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z19", "Patients having (TDF+3TC+LPV/r) regimen", ReportUtils.map(artIndicators.onregimenlpv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z20", "Patients having (TDF+3TC+RAL) regimen", ReportUtils.map(artIndicators.onregimentdfral(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z21", "Patients having (TDF+3TC+ATV/r) regimen", ReportUtils.map(artIndicators.onregimentdfatv(), indParams), allColumns, indSuffixes);
			EmrReportingUtils.addRow(dsd, "Z22", "Patients having (ABC+3TC+ATV/r) regimen", ReportUtils.map(artIndicators.onregimenabc(), indParams), allColumns, indSuffixes);
			return dsd;
		}	
	
}
