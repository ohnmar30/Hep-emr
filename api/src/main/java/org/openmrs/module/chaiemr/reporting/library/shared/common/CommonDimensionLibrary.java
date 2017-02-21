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

package org.openmrs.module.chaiemr.reporting.library.shared.common;

import static org.openmrs.module.chaicore.report.ReportUtils.map;

import java.util.Date;

import org.openmrs.module.reporting.cohort.definition.AgeCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Library of common dimension definitions
 */
@Component
public class CommonDimensionLibrary {

	@Autowired
	private CommonCohortLibrary commonCohortLibrary;

	/**
	 * Gender dimension
	 * 
	 * @return the dimension
	 */
	public CohortDefinitionDimension gender() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("gender");
		dim.addCohortDefinition("M", map(commonCohortLibrary.males()));
		dim.addCohortDefinition("F", map(commonCohortLibrary.females()));
		return dim;
	}

	public CohortDefinitionDimension addMale() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("gender");
		dim.addCohortDefinition("Male", map(commonCohortLibrary.males()));
		return dim;
	}

	/**
	 * Dimension of age using the 3 standard age groups
	 * 
	 * @return the dimension
	 */
	public CohortDefinitionDimension standardAgeGroups() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("age groups (<1, <15, 15+)");
		dim.addParameter(new Parameter("onDate", "Date", Date.class));
		dim.addCohortDefinition("<1", map(commonCohortLibrary.agedAtMost(0), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("<15", map(commonCohortLibrary.agedAtMost(14), "effectiveDate=${onDate}"));
		dim.addCohortDefinition("15+", map(commonCohortLibrary.agedAtLeast(15), "effectiveDate=${onDate}"));
		return dim;
	}

	public CohortDefinitionDimension addAdult() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.addCohortDefinition("15+", map(commonCohortLibrary.adult(), "effectiveDate=${onDate}"));
		// org.openmrs.module.reporting.cohort.definition.
		return dim;
	}

	public CohortDefinitionDimension performanceScales() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("performance scale (A,B,C)");

		dim.addCohortDefinition("A", map(commonCohortLibrary.scaleA()));
		dim.addCohortDefinition("B", map(commonCohortLibrary.scaleB()));
		dim.addCohortDefinition("C", map(commonCohortLibrary.scaleC()));
		return dim;
	}

	public CohortDefinitionDimension performanceSalceA() {
		CohortDefinitionDimension dimension = new CohortDefinitionDimension();
		dimension.setName("Performance scale A");
		dimension.addCohortDefinition("A", map(commonCohortLibrary.scaleA()));
		return dimension;
	}

	public CohortDefinitionDimension riskFactor() {
		CohortDefinitionDimension dim = new CohortDefinitionDimension();
		dim.setName("risk factor (1,2,3,4,5,6,7)");

		dim.addCohortDefinition("1", map(commonCohortLibrary.risk1()));
		dim.addCohortDefinition("2", map(commonCohortLibrary.risk2()));
		dim.addCohortDefinition("3", map(commonCohortLibrary.risk3()));
		dim.addCohortDefinition("4", map(commonCohortLibrary.risk4()));
		dim.addCohortDefinition("5", map(commonCohortLibrary.risk5()));
		dim.addCohortDefinition("6", map(commonCohortLibrary.risk6()));
		dim.addCohortDefinition("7", map(commonCohortLibrary.risk7()));
		return dim;
	}
}