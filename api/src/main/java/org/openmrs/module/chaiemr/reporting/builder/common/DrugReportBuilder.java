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
@Builds({"chaiemr.common.report.drugReport"})
public class DrugReportBuilder  extends AbstractReportBuilder{

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
				ReportUtils.map(creatDrugReportDataSet(), "startDate=${startDate},endDate=${endDate}"));
			
		}

		/**
		 * Creates the HIV enrolled data set
		 * @return the data set
		 */
		private DataSetDefinition creatDrugReportDataSet() {
			CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
			dsd.setName("P");
			dsd.setDescription("Drug Report");
			dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
			dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
			dsd.addDimension("age", map(commonDimensions.standardAgeGroups(), "onDate=${endDate}"));
			dsd.addDimension("gender", map(commonDimensions.gender()));

			List<ColumnParameters> columns = new ArrayList<ColumnParameters>();
			columns.add(new ColumnParameters("FP", "0-14 years, female", "gender=F|age=<15"));
			columns.add(new ColumnParameters("MP", "0-14 years, male", "gender=M|age=<15"));
			columns.add(new ColumnParameters("FA", ">14 years, female", "gender=F|age=15+"));
			columns.add(new ColumnParameters("MA", ">14 years, male", "gender=M|age=15+"));
			columns.add(new ColumnParameters("T", "total", ""));

			String indParams = "startDate=${startDate},endDate=${endDate}";
	                
			EmrReportingUtils.addRow(dsd, "P1", "No. of ", ReportUtils.map(hivIndicators.notInART(), indParams), columns);
			return dsd;
		}

}
