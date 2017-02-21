package org.openmrs.module.chaiemr.reporting.calculation.converter;

import org.openmrs.Obs;
import org.openmrs.module.chaiemr.reporting.RDQAReportUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converter to get obsDatetime from an observation
 */
public class ObsDatetimeConverter implements DataConverter {
	@Override
	public Object convert(Object original) {
		Obs o = (Obs) original;

		if (o == null)
			return "Missing";

		return RDQAReportUtils.formatdates(o.getObsDatetime(), RDQAReportUtils.DATE_FORMAT);
	}

	@Override
	public Class<?> getInputDataType() {
		return Obs.class;
	}

	@Override
	public Class<?> getDataType() {
		return String.class;
	}
}
