package org.openmrs.module.chaiemr.reporting.builder.common;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

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
@Builds({"chaiemr.hiv.report.incidentTBCasesAmongPLHIV"})
public class HIVIncidentTB extends AbstractReportBuilder{

	@Autowired
	private CommonDimensionLibrary commonDimensions;
	
	@Autowired
	private HivIndicatorLibrary hivIndicators;
	@Override
	protected List<Parameter> getParameters(ReportDescriptor descriptor) {
		return Arrays.asList(
				new Parameter("startDate", "Start Date", Date.class),
				new Parameter("endDate", "End Date", Date.class)
		);
	}
	@Override
	protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
		return Arrays.asList(
				ReportUtils.map(createHIVinitiatedTB(), "startDate=${startDate},endDate=${endDate}")
				
		);
	}
	private DataSetDefinition createHIVinitiatedTB() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		dsd.setName("C");
		dsd.setDescription("Number of incident TB cases among PLHIV");
		dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
		dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
		dsd.addDimension("gender", map(commonDimensions.gender()));

		
		
		ColumnParameters female=new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+");
		ColumnParameters male=new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+");
		ColumnParameters childfemale =new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15");
		ColumnParameters childmale =new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15");
		ColumnParameters total=new ColumnParameters("T", "total", "");


		String indParams = "startDate=${startDate},endDate=${endDate}";
		List<ColumnParameters> allColumns = Arrays.asList(female,male,childfemale, childmale ,total);
		List<String> indSuffixes = Arrays.asList("FM","MA","CF","CM", "TT");    
                
		EmrReportingUtils.addRow(dsd, "C1", "No. of ", ReportUtils.map(hivIndicators.hivincidentTB(), indParams), allColumns,indSuffixes);
		return dsd;
	}
}
