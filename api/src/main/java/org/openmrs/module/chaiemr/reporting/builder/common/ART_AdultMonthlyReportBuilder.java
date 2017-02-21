package org.openmrs.module.chaiemr.reporting.builder.common;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openmrs.module.chaicore.report.ReportDescriptor;
import org.openmrs.module.chaicore.report.ReportUtils;
import org.openmrs.module.chaicore.report.builder.AbstractReportBuilder;
import org.openmrs.module.chaicore.report.builder.Builds;
import org.openmrs.module.chaiemr.reporting.ColumnParameters;
import org.openmrs.module.chaiemr.reporting.EmrReportingUtils;
import org.openmrs.module.chaiemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.HivIndicatorLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.art.ArtIndicatorLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.openmrs.module.reporting.indicator.dimension.CohortDimension;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Builds({ "chaiemr.common.report.artAdultmonthlyReport" })
public class ART_AdultMonthlyReportBuilder extends AbstractReportBuilder {
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
		return Arrays.asList(new Parameter("startDate", "Start Date", Date.class), new Parameter("endDate", "End Date", Date.class));
	}

	/**
	 * @see org.openmrs.module.chaicore.report.builder.AbstractReportBuilder#buildDataSets(org.openmrs.module.chaicore.report.ReportDescriptor,
	 *      org.openmrs.module.reporting.report.definition.ReportDefinition)
	 */
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		return Arrays.asList(ReportUtils.map(creatArtOutcomeDeathDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(creatArtOutcomeTransferredOutDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(creatArtOutcomeLostMissingDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(creatArtOutcomeCompletedArtDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(creatArtOutcomeDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createCumulativeactiveTbDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createNewInitiatedPatientTbDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createTbDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createCumulativeTbDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHivDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createCumulativeHivDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(creatDataSetForCdFourCount(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(creatDataSetForViralLoad(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createNewHIVEnrolledDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createHIVEnrolledwithPerformanceADataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createHIVEnrolledwithRiskDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createOIDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createHIVAdherenceDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createLevelAdherenceDataSet(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(createElligibleARTDataSet(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(firstregimensDataset(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(secondregimensDataset(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(hivhbvregimensDataset(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(originalDataset(), "startDate=${startDate},endDate=${endDate}"),
				ReportUtils.map(onsubsituteDataset(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(onswitchsecondDataset(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(onswitchthirdDataset(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(onstockDispensedDataset(), "startDate=${startDate},endDate=${endDate}"), ReportUtils.map(createAllPatientDataSet()),
				ReportUtils.map(createHIVEnrolledwithPerformanceADataSet1(), "startDate=${startDate},endDate=${endDate}")

		);
	}

	/**
	 * Creates the HIV enrolled data set
	 * 
	 * @return the data set
	 */
	private DataSetDefinition creatArtOutcomeDeathDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("A");
		dsd.setDescription("No. of death reported at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "  Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender(),""));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "A1", "No. of ", ReportUtils.map(hivIndicators.outcomeArt(), indParams), allColumns, indSuffixes);
		return dsd;
	}

	private DataSetDefinition creatArtOutcomeTransferredOutDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("B");
		dsd.setDescription("No. of patients transferred out under ARV at the end of this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "B1", "No. of ", ReportUtils.map(hivIndicators.outcometransferredArt(), indParams), allColumns, indSuffixes);
		return dsd;
	}

	private DataSetDefinition creatArtOutcomeLostMissingDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("C");
		dsd.setDescription("No. of patients missing/lost to follow-up at the end of this month");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "C1", "No. of ", ReportUtils.map(hivIndicators.outcomelostmissingArt(), indParams), allColumns, indSuffixes);
		return dsd;
	}

	private DataSetDefinition creatArtOutcomeCompletedArtDataSet() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("D");
		dsd.setDescription("No. of patients stopping ART at the end of this month  ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "D1", "No. of ", ReportUtils.map(hivIndicators.stopART(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "E1", "No. of ", ReportUtils.map(hivIndicators.startArt(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "F1", "No. of detected cases (Cumulative no. of active follow up  patients ever started on ART )", ReportUtils.map(artIndicators.startedArtCumulative(), indParams), allColumns, indSuffixes);

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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "G1", "  No. of detected cases (New patients started on ART)", ReportUtils.map(artIndicators.startedArt(), indParams), allColumns, indSuffixes);

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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "H1", "No. of detected cases (Transferred in)", ReportUtils.map(hivIndicators.restartART(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "I1", "  No. of detected cases (Cumulative no. of patients ever started on ART )", ReportUtils.map(artIndicators.startedArtCumulativeResult(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "J1", "No. of detected cases (TB patients on ART)", ReportUtils.map(hivIndicators.initiatedARTandTB(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MM", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "K1", "Cumulative No. of detected cases (TB patients on ART)", ReportUtils.map(hivIndicators.enrolledCumulativeTB(), indParams), allColumns, indSuffixes);

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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "L1", "No. of patients tested for CD4 count ", ReportUtils.map(hivIndicators.cdFourTest(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "M1", "No. of patients tested for viral load", ReportUtils.map(hivIndicators.viralLoadTest(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "N1", "No. of ", ReportUtils.map(hivIndicators.inHIV(), indParams), allColumns, indSuffixes);
		return dsd;
	}

	private DataSetDefinition createHIVEnrolledwithPerformanceADataSet1() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("AB");
		dsd.setDescription("Performance scale (A, B, C) -------");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.addMale()));
		dsd.addDimension("parameter", map(commonDimensions.performanceScales()));

		List<ColumnParameters> columnsB = new ArrayList<ColumnParameters>();
		columnsB.add(new ColumnParameters("AB1", "Performance scale B,>14 years, male", "age=15+"));
		columnsB.add(new ColumnParameters("AB1", "Performance scale B,>14 years, male", "gender=Male"));
		String indParams = "startDate=${startDate},endDate=${endDate}";
		
		EmrReportingUtils.addRow(dsd, "AB1", "No. of detected case (Performance Scale B) ", ReportUtils.map(hivIndicators.performanceScaleB(), indParams), columnsB);
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
		columnsA.add(new ColumnParameters("FA", "Performance scale A,>14 years, female", "gender=F|age=15+|parameter=A"));
		columnsA.add(new ColumnParameters("MA", "Performance scale A,>14 years, male", "gender=M|age=15+|parameter=A"));
		columnsA.add(new ColumnParameters("T", "Performance scale A ,>14 years,total", "age=15+"));

		List<ColumnParameters> columnsB = new ArrayList<ColumnParameters>();
		columnsB.add(new ColumnParameters("FA1", "Performance scale B,>14 years, female", "gender=F|age=15+|parameter=B"));
		columnsB.add(new ColumnParameters("MA1", "Performance scale B,>14 years, male", "gender=M|age=15+|parameter=B"));
		columnsB.add(new ColumnParameters("T1", "Performance scale B ,>14 years,total", "age=15+"));

		List<ColumnParameters> columnsC = new ArrayList<ColumnParameters>();
		columnsC.add(new ColumnParameters("FA2", "Performance scale C,>14 years, female", "gender=F|age=15+|parameter=C"));
		columnsC.add(new ColumnParameters("MA2", "Performance scale C,>14 years, male", "gender=M|age=15+|parameter=C"));
		columnsC.add(new ColumnParameters("T2", "Performance scale C, >14 years,total", "age=15+"));
		String indParams = "startDate=${startDate},endDate=${endDate}";

		EmrReportingUtils.addRow(dsd, "O1", "No. of detected case (Performance Scale A) ", ReportUtils.map(hivIndicators.performanceScaleA(), indParams), columnsA);
		EmrReportingUtils.addRow(dsd, "O2", "No. of detected case (Performance Scale B) ", ReportUtils.map(hivIndicators.performanceScaleB(), indParams), columnsB);
		EmrReportingUtils.addRow(dsd, "O3", "No. of detected case (Performance Scale C) ", ReportUtils.map(hivIndicators.performanceScaleC(), indParams), columnsC);
		return dsd;
	}

	private DataSetDefinition createAllPatientDataSet() {
		CohortIndicatorDataSetDefinition definition = new CohortIndicatorDataSetDefinition();
		definition.setName("AB");
		definition.setDescription("All patient report");
		definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
		definition.addParameter(new Parameter("endDate", "End Date", Date.class));
		String indParams = "startDate=${startDate},endDate=${endDate}";
		// CohortDimension dmension = new CohortDefinitionDimension();
		CohortIndicator cohortIndicator = new CohortIndicator();
		definition.addColumn("age", "Label ", ReportUtils.map(hivIndicators.performanceScaleA(), indParams), "");
		return definition;
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
		MSM.add(new ColumnParameters("T1", " Risk factor for HIV as MSM ,female", "gender=F|age=15+|parameter=1"));
		MSM.add(new ColumnParameters("T11", "Risk factor for HIV as MSM ,male", "gender=M|age=15+|parameter=1"));

		List<ColumnParameters> SW = new ArrayList<ColumnParameters>();
		SW.add(new ColumnParameters("T2", " Risk factor for HIV as SW ,female", "gender=F|age=15+|parameter=2"));
		SW.add(new ColumnParameters("T22", "Risk factor for HIV as SW ,male", "gender=M|age=15+|parameter=2"));

		List<ColumnParameters> heterosexual = new ArrayList<ColumnParameters>();
		heterosexual.add(new ColumnParameters("T3", " Risk factor for HIV as heterosexual ,female", "gender=F|age=15+|parameter=3"));
		heterosexual.add(new ColumnParameters("T33", "Risk factor for HIV as heterosexual ,male", "gender=M|age=15+|parameter=3"));

		List<ColumnParameters> HIV = new ArrayList<ColumnParameters>();
		HIV.add(new ColumnParameters("T4", " Risk factor for HIV as IDU ,female", "gender=F|age=15+|parameter=4"));
		HIV.add(new ColumnParameters("T44", "Risk factor for HIV as IDU ,male", "gender=M|age=15+|parameter=4"));

		List<ColumnParameters> Bloodtransfusion = new ArrayList<ColumnParameters>();
		Bloodtransfusion.add(new ColumnParameters("T5", " Risk factor for HIV as Blood transfusion ,female", "gender=F|age=15+|parameter=5"));
		Bloodtransfusion.add(new ColumnParameters("T55", "Risk factor for HIV as Blood transfusion ,male", "gender=M|age=15+|parameter=5"));

		List<ColumnParameters> Mot = new ArrayList<ColumnParameters>();
		Mot.add(new ColumnParameters("T6", " Risk factor for HIV as Mother to child ,female", "gender=F|age=15+|parameter=6"));
		Mot.add(new ColumnParameters("T66", "Risk factor for HIV as Mother to child ,male", "gender=M|age=15+|parameter=6"));

		List<ColumnParameters> unknown = new ArrayList<ColumnParameters>();
		unknown.add(new ColumnParameters("T7", " Risk factor for HIV as unknown ,female", "gender=F|age=15+|parameter=7"));
		unknown.add(new ColumnParameters("T77", "Risk factor for HIV as unknown ,male", "gender=M|age=15+|parameter=7"));

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
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
		dsd.setDescription(" No. of patients treated for  Opportunistic Infections during this month ");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("gender", map(commonDimensions.gender()));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "Q1", "No. of detected cases (Treated for OI)", ReportUtils.map(hivIndicators.givenDrugsForOI(), indParams), allColumns, indSuffixes);
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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "grand total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "R1-1", "< 5% of doses missed in a period of 30 days (>95%)", ReportUtils.map(hivIndicators.levelOfAdherence(0, 5), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "R1-2", "(5-20)% of doses missed in a period of 30 days (80-95%)", ReportUtils.map(hivIndicators.levelOfAdherence(5, 20), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "R1-3", ">20% of doses missed in a period of 30 days (<80%)", ReportUtils.map(hivIndicators.levelOfAdherence(20, 100), indParams), allColumns, indSuffixes);

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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "grand total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "S1", "No. of patients assessed for adherence during this month", ReportUtils.map(hivIndicators.levelOfAdherence(0, 100), indParams), allColumns, indSuffixes);

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

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");

		EmrReportingUtils.addRow(dsd, "T1", "No. of detected cases (Eligible for ART)", ReportUtils.map(hivIndicators.notInART(), indParams), allColumns, indSuffixes);
		return dsd;
	}

	private DataSetDefinition firstregimensDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("U");
		dsd.setDescription(" First Line regimen at the end of month");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));

		ColumnParameters adults = new ColumnParameters("AP", "Adults", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";

		List<ColumnParameters> allColumns = Arrays.asList(adults);
		List<String> indSuffixes = Arrays.asList("AD");
		EmrReportingUtils.addRow(dsd, "U1", "Patients having (AZT/3TC/NVP)(300/150/200)mg regimen", ReportUtils.map(hivIndicators.onregimenNVP(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U2", "Patients having (AZT/3TC+NVP) (300/150+200) mg regimen", ReportUtils.map(hivIndicators.onregimendosenvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U3", "Patients having (AZT/3TC/EFV) (300/150/600)mg regimen", ReportUtils.map(hivIndicators.onregimenEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U4", "Patients having (AZT/3TC+EFV) (300/150+600) mg regimen", ReportUtils.map(hivIndicators.onregimendoseefvtwo(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U5", "Patients having (TDF/3TC/NVP) (300/300/200)mg regimen", ReportUtils.map(hivIndicators.onregimenTDF(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U6", "Patients having (TDF/3TC+NVP) (300/300+200) mg regimen", ReportUtils.map(hivIndicators.onregimentdf3tcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U7", "Patients having (TDF/3TC/EFV) (300/300/600)mg regimen", ReportUtils.map(hivIndicators.onregimenTDFEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U8", "Patients having (TDF/3TC+EFV) (300/300+400) mg regimen", ReportUtils.map(hivIndicators.onregimen3tcefvtwo(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U9", "Patients having (TDF/3TC+EFV) (300/300+600) mg regimen", ReportUtils.map(hivIndicators.onregimen3tcefvsix(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U10", "Patients having (TDF/FTC/NVP) (300/200/200)mg regimen", ReportUtils.map(hivIndicators.onregimenTDFFTC(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U11", "Patients having (TDF/FTC+NVP) (300/200+200) mg regimen", ReportUtils.map(hivIndicators.onregimentdfftcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U12", "Patients having (TDF/FTC/EFV) (300/200/200)mg regimen", ReportUtils.map(hivIndicators.onregimenTDFFTCEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U13", "Patients having (TDF/FTC+EFV) (300/200+400) mg regimen", ReportUtils.map(hivIndicators.onregimenftcefvfour(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U14", "Patients having (TDF/FTC/EFV) (300/200/600)mg regimen", ReportUtils.map(hivIndicators.onregimenTDFFTCE(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U15", "Patients having (TDF/FTC+EFV) (300/200+600) mg regimen", ReportUtils.map(hivIndicators.onregimenftcefvsix(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U16", "Patients having (d4T/3TC/NVP)  (30/150/200)mg regimen", ReportUtils.map(hivIndicators.onregimenDT(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U17", "Patients having (d4T/3TC+NVP)  (30/150+200)mg regimen", ReportUtils.map(hivIndicators.onregimendt3tcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U18", "Patients having (d4T/3TC/EFV) (30/150/600)mg regimen", ReportUtils.map(hivIndicators.onregimenDTEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U19", "Patients having (d4T/3TC+EFV) (30/150+600) mg regimen", ReportUtils.map(hivIndicators.onregimendt3tcefv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U20", "Patients having (TDF/FTC/DTG) regimen", ReportUtils.map(hivIndicators.onregimenftcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U21", "Patients having (TDF/3TC/DTG) regimen", ReportUtils.map(hivIndicators.onregimen3tcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U22", "Patients having (ABC/3TC+EFV) (600/300+600) mg regimen", ReportUtils.map(hivIndicators.onregimenabcefv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U23", "Patients having (ABC/3TC/DTG) regimen", ReportUtils.map(hivIndicators.onregimenabc3tcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U24", "Patients having (ABC/3TC/NVP) regimen", ReportUtils.map(hivIndicators.onregimen3tcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U25", "Patients having (ABC/FTC/EFV) regimen", ReportUtils.map(hivIndicators.onregimenftcefv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U26", "Patients having (ABC/FTC/DTG) regimen", ReportUtils.map(hivIndicators.onregimenabcftcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "U27", "Patients having (ABC/FTC/NVP) regimen", ReportUtils.map(hivIndicators.onregimenftcnvp(), indParams), allColumns, indSuffixes);
		return dsd;
	}

	private DataSetDefinition secondregimensDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("V");
		dsd.setDescription("Second line Regimen at the end of month");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));

		ColumnParameters adults = new ColumnParameters("AP", "Adults", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";

		List<ColumnParameters> allColumns = Arrays.asList(adults);
		List<String> indSuffixes = Arrays.asList("AD");
		EmrReportingUtils.addRow(dsd, "V1", "Patients having (AZT/3TC/LPV/r) (300/150/200/50)mg regimen", ReportUtils.map(hivIndicators.onregimenAzt(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V2", "Patients having (AZT/3TC+LPV/r)(300/150+200/50)mg regimen", ReportUtils.map(hivIndicators.onregimendoselpvr(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V3", "Patients having (TDF/3TC/LPV/r) (300/300/200/50)mg regimen", ReportUtils.map(hivIndicators.onregimenTdf(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V4", "Patients having (TDF/3TC+LPV/r)(300/300+200/50)mg regimen", ReportUtils.map(hivIndicators.onregimentdf3tc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V5", "Patients having (TDF/FTC/LPV/r) (300/200/200/50)mg regimen", ReportUtils.map(hivIndicators.onregimenTdfftc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V6", "Patients having (TDF/FTC+LPV/r)(300/200+200/50)mg regimen", ReportUtils.map(hivIndicators.onregimentdfftc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V7", "Patients having (TDF/ABC/LPV/r) (300/300/200/50)mg regimen", ReportUtils.map(hivIndicators.onregimenTdfabc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V8", "Patients having (TDF+ABC+LPV/r )(300+300+200/50)mg regimen", ReportUtils.map(hivIndicators.onregimentdf(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V9", "Patients having (ABC/3TC+LPV/r)(600/300+200/50)mg regimen", ReportUtils.map(hivIndicators.onregimenabcdoselpvr(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V10", "Patients having (ABC/3TC/ATV/r) regimen", ReportUtils.map(hivIndicators.onregimenabcatv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V11", "Patients having (AZT/3TC/ATV/r) regimen", ReportUtils.map(hivIndicators.onregimenaztatv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "V12", "Patients having (TDF/3TC/ATV/r) regimen", ReportUtils.map(hivIndicators.onregimentdfatvr(), indParams), allColumns, indSuffixes);
		return dsd;
	}

	private DataSetDefinition hivhbvregimensDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("W");
		dsd.setDescription("Groups Patients HIV and HBV co-infection ART Regimen and Age");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));

		ColumnParameters adults = new ColumnParameters("AP", "Adults", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(adults);
		List<String> indSuffixes = Arrays.asList("AD");

		EmrReportingUtils.addRow(dsd, "W1", "Patients having (AZT/3TC+TDF+LPV/r)(300/150+300+200/50) mg regimen", ReportUtils.map(hivIndicators.onRegimenazt3tc(), indParams), allColumns, indSuffixes);

		return dsd;
	}

	private DataSetDefinition originalDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("X");
		dsd.setDescription(" No. on original 1st line regimen");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "grand total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "X1", "No. on original 1st line regimen", ReportUtils.map(artIndicators.onoriginal(), indParams), allColumns, indSuffixes);

		return dsd;
	}

	private DataSetDefinition onsubsituteDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("Y");
		dsd.setDescription("No. on substituted 1st line regimen");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "grand total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "Y1", "No. on substituted 1st line regimen", ReportUtils.map(artIndicators.onsubsitute(), indParams), allColumns, indSuffixes);

		return dsd;
	}

	private DataSetDefinition onswitchsecondDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("Z");
		dsd.setDescription("No. switched to 2nd line regimen");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "grand total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "Z1", "No. switched to 2nd line regimen", ReportUtils.map(artIndicators.onswitchsecond(), indParams), allColumns, indSuffixes);

		return dsd;
	}

	private DataSetDefinition onswitchthirdDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("AA");
		dsd.setDescription("No. switched to 3rd line regimen");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		ColumnParameters female = new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male = new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters total = new ColumnParameters("T", "grand total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female, male, total);
		List<String> indSuffixes = Arrays.asList("FM", "MA", "TT");
		EmrReportingUtils.addRow(dsd, "AA1", "No. switched to 3rd line regimen", ReportUtils.map(artIndicators.onswitchthird(), indParams), allColumns, indSuffixes);

		return dsd;
	}

	private DataSetDefinition onstockDispensedDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("BB");
		dsd.setDescription("Stock dispensed this mnth");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", ReportUtils.map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));

		ColumnParameters total = new ColumnParameters("T", "grand total", "age=15+");

		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(total);
		List<String> indSuffixes = Arrays.asList("TT");
		EmrReportingUtils.addRow(dsd, "BB1", "Patients having (AZT/3TC/NVP)(300/150/200)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenNVP(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB2", "Patients having (AZT/3TC+NVP) (300/150+200) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimendosenvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB3", "Patients having (AZT/3TC/EFV) (300/150/600)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB4", "Patients having (AZT/3TC+EFV) (300/150+600) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimendoseefvtwo(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB5", "Patients having (TDF/3TC/NVP) (300/300/200)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTDF(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB6", "Patients having (TDF/3TC+NVP) (300/300+200) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimentdf3tcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB7", "Patients having (TDF/3TC/EFV) (300/300/600)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTDFEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB8", "Patients having (TDF/3TC+EFV) (300/300+400) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimen3tcefvtwo(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB9", "Patients having (TDF/3TC+EFV) (300/300+600) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimen3tcefvsix(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB10", "Patients having (TDF/FTC/NVP) (300/200/200)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTDFFTC(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB11", "Patients having (TDF/FTC+NVP) (300/200+200) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimentdfftcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB12", "Patients having (TDF/FTC/EFV) (300/200/200)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTDFFTCEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB13", "Patients having (TDF/FTC+EFV) (300/200+400) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenftcefvfour(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB14", "Patients having (TDF/FTC/EFV) (300/200/600)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTDFFTCE(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB15", "Patients having (TDF/FTC+EFV) (300/200+600) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenftcefvsix(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB16", "Patients having (d4T/3TC/NVP)  (30/150/200)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenDT(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB17", "Patients having (d4T/3TC+NVP)  (30/150+200)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimendt3tcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB18", "Patients having (d4T/3TC/EFV) (30/150/600)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenDTEFV(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB19", "Patients having (d4T/3TC+EFV) (30/150+600) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimendt3tcefv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB20", "Patients having (TDF/FTC/DTG) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenftcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB21", "Patients having (TDF/3TC/DTG) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimen3tcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB22", "Patients having (ABC/3TC+EFV) (600/300+600) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenabcefv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB23", "Patients having (ABC/3TC/DTG) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenabc3tcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB24", "Patients having (ABC/3TC/NVP) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimen3tcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB25", "Patients having (ABC/FTC/EFV) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenftcefv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB26", "Patients having (ABC/FTC/DTG) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenabcftcdtg(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB27", "Patients having (ABC/FTC/NVP) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenftcnvp(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB28", "Patients having (AZT/3TC/LPV/r) (300/150/200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenAzt(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB29", "Patients having (AZT/3TC+LPV/r)(300/150+200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimendoselpvr(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB30", "Patients having (TDF/3TC/LPV/r) (300/300/200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTdf(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB31", "Patients having (TDF/3TC+LPV/r)(300/300+200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimentdf3tc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB32", "Patients having (TDF/FTC/LPV/r) (300/200/200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTdfftc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB33", "Patients having (TDF/FTC+LPV/r)(300/200+200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimentdfftc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB34", "Patients having (TDF/ABC/LPV/r) (300/300/200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenTdfabc(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB35", "Patients having (TDF+ABC+LPV/r )(300+300+200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimentdf(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB36", "Patients having (ABC/3TC+LPV/r)(600/300+200/50)mg regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenabcdoselpvr(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB37", "Patients having (ABC/3TC/ATV/r) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenabcatv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB38", "Patients having (AZT/3TC/ATV/r) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimenaztatv(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB39", "Patients having (TDF/3TC/ATV/r) regimen dispensed For this month", ReportUtils.map(artIndicators.onregimentdfatvr(), indParams), allColumns, indSuffixes);
		EmrReportingUtils.addRow(dsd, "BB40", "Patients having (AZT/3TC+TDF+LPV/r)(300/150+300+200/50) mg regimen dispensed For this month", ReportUtils.map(artIndicators.onRegimenazt3tc(), indParams), allColumns, indSuffixes);
		return dsd;
	}
}
