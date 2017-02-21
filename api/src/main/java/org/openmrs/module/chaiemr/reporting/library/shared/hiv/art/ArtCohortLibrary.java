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

package org.openmrs.module.chaiemr.reporting.library.shared.hiv.art;

import org.openmrs.Concept;
import org.openmrs.Program;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.chaicore.report.ReportUtils;
import org.openmrs.module.chaicore.report.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.chaicore.report.cohort.definition.DateCalculationCohortDefinition;
import org.openmrs.module.chaicore.report.data.patient.evaluator.CalculationDataEvaluator;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.Metadata;
import org.openmrs.module.chaiemr.calculation.library.MissedLastAppointmentCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABC3TCATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABC3TCDTGCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABC3TCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABCFTCDTGCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABCFTCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABCFTCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.D4T3TCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.D4T3TCLrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.D4T3TCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedABC3TCplusATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedABC3TCplusRALCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedAZT3TCplusATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedAZT3TCplusRALCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedD4T3TCplusABCCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedD4T3TCplusEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseABC3TCplusEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseABC3TCplusEFVSixCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseABC3TCplusEFVTwoCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseABC3TCplusLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseABC3TCplusNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseAZT3TCplusABCCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseAZT3TCplusEFVSixCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseAZT3TCplusEFVTwoCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseAZT3TCplusLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseAZT3TCplusNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseAZT3TCplusTDFplusLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseD4T3TCplusEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseD4T3TCplusNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDF3TCplusEFVSixCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDF3TCplusEFVTwoCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDF3TCplusLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDF3TCplusNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDFABCplusLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDFFTCplusEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDFFTCplusEFVFourCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDFFTCplusLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedDoseTDFFTCplusNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedTDF3TCplusATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedTDF3TCplusEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedTDF3TCplusLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedTDF3TCplusNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FixedTDF3TCplusRALCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.LostToFollowUpCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.OnSubsituteFirstLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.OnSwitchLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.OnSwitchThirdLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDF3TCATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDF3TCDTGCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDF3TCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDF3TCLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDF3TCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDFABCLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDFFTCDTGCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDFFTCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDFFTCEFVSixhundredCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDFFTCLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TDFFTCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.ABC3TCATVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.ABC3TCDTGStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.ABC3TCNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.ABCFTCDTGStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.ABCFTCEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.ABCFTCNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.AZT3TCATVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.AZT3TCEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.AZT3TCLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.AZT3TCNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.D4T3TCEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.D4T3TCLrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.D4T3TCNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.EligibleForArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.EligibleForArtExclusiveCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedABC3TCplusATVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedABC3TCplusRALStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedAZT3TCplusATVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedAZT3TCplusRALStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedD4T3TCplusABCStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedD4T3TCplusEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseABC3TCplusEFVSixStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseABC3TCplusEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseABC3TCplusEFVTwoStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseABC3TCplusLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseABC3TCplusNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseAZT3TCplusABCStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseAZT3TCplusEFVSixStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseAZT3TCplusEFVTwoStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseAZT3TCplusLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseAZT3TCplusNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseAZT3TCplusTDFplusLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseD4T3TCplusEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseD4T3TCplusNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDF3TCplusEFVSixStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDF3TCplusEFVTwoStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDF3TCplusLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDF3TCplusNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDFABCplusLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDFFTCplusEFVFourStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDFFTCplusEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDFFTCplusLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedDoseTDFFTCplusNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedTDF3TCplusATVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedTDF3TCplusEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedTDF3TCplusLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedTDF3TCplusNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.FixedTDF3TCplusRALStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.InitialArtStartDateCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.OnAlternateFirstLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.OnArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.OnOriginalFirstLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.OnSecondLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.PregnantAtArtStartCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDF3TCATVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDF3TCDTGStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDF3TCEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDF3TCLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDF3TCNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDFABCLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDFFTCDTGStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDFFTCEFVSixhundredStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDFFTCEFVStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDFFTCLPVrStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TDFFTCNVPStockDispensedCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.TbPatientAtArtStartCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.WhoStageAtArtStartCalculation;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.regimen.RegimenManager;
import org.openmrs.module.chaiemr.reporting.cohort.definition.RegimenOrderCohortDefinition;
import org.openmrs.module.chaiemr.reporting.cohort.definition.evaluator.RegimenOrderCohortDefinitionEvaluator;
import org.openmrs.module.chaiemr.reporting.library.shared.common.CommonCohortLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.HivCohortLibrary;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.NumericObsCohortDefinition;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Library of ART related cohort definitions
 */
@Component
public class ArtCohortLibrary {

	@Autowired
	private RegimenManager regimenManager;

	@Autowired
	private CommonCohortLibrary commonCohorts;

	@Autowired
	private HivCohortLibrary hivCohortLibrary;

	/**
	 * Patients who are eligible for ART on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition eligibleForArt() {

		CalculationCohortDefinition eligibleForART = new CalculationCohortDefinition(new EligibleForArtCalculation());
		eligibleForART.setName("eligible for ART on date");
		eligibleForART.addParameter(new Parameter("onDate", "On Date", Date.class));
		return eligibleForART;
	}

	public CohortDefinition EligibleForArtExclusive() {
		CalculationCohortDefinition eligibleForARTExclusive = new CalculationCohortDefinition(new EligibleForArtExclusiveCalculation());
		eligibleForARTExclusive.setName("eligible for ART on date exclusively");
		eligibleForARTExclusive.addParameter(new Parameter("onDate", "On Date", Date.class));
		return eligibleForARTExclusive;
	}

	/**
	 * Patients who are LTFU
	 * @return the cohort definition
	 */
	public CohortDefinition lostToFollowUpPatients() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new LostToFollowUpCalculation());
		cd.setName("lost to follow on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}

	/**
	 * Patients who are on ART on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onArt() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnArtCalculation());
		cd.setName("on ART on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}

	/**
	 * Patients who missed appointments on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition missedAppointments() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new MissedLastAppointmentCalculation());
		cd.setName("missed appointment on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}

	/**
	 * Patients who are on art and  missed appointments on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onArtAndMissedAppointments() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("on ART and Missed last appointment");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addSearch("onArt", ReportUtils.map(onArt(), "onDate=${onDate}"));
		cd.addSearch("missedAppointments", ReportUtils.map(missedAppointments(), "onDate=${onDate}"));
		cd.setCompositionString("onArt AND NOT missedAppointments");
		return cd;

	}

	/**
	 * Patients who are on ART and pregnant on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onArtAndPregnant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("on ART and pregnant");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addSearch("onArt", ReportUtils.map(onArtAndMissedAppointments(), "onDate=${onDate}"));
		cd.addSearch("pregnant", ReportUtils.map(commonCohorts.pregnant(), "onDate=${onDate}"));
		cd.setCompositionString("onArt AND pregnant");
		return cd;
	}

	/**
	 * Patients who are on ART and not pregnant on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onArtAndNotPregnant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("on ART and not pregnant");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addSearch("onArt", ReportUtils.map(onArtAndMissedAppointments(), "onDate=${onDate}"));
		cd.addSearch("pregnant", ReportUtils.map(commonCohorts.pregnant(), "onDate=${onDate}"));
		cd.setCompositionString("onArt AND NOT pregnant");
		return cd;
	}

	/**
	 * Patients who are taking their original first line regimen on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onOriginalFirstLine() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnOriginalFirstLineArtCalculation());
		cd.setName("on original first line regimen");
		 cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
			cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
		
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(hivCohortLibrary.totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
	}
	public CohortDefinition onSubstitueFirstLine() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnSubsituteFirstLineArtCalculation());
		cd.setName("on subsitute first line regimen");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(hivCohortLibrary.totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
	}
	
	public CohortDefinition onSwitchSecondLine() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnSwitchLineArtCalculation());
		cd.setName("on subsitute first line regimen");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(hivCohortLibrary.totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
	}
	public CohortDefinition onSwitchThirdLine() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnSwitchThirdLineArtCalculation());
		cd.setName("on subsitute first line regimen");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(hivCohortLibrary.totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
	}
	
	/**
	 * Patients who are taking an alternate first line regimen on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onAlternateFirstLine() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnAlternateFirstLineArtCalculation());
		cd.setName("on alternate first line regimen");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}

	/**
	 * Patients who are taking a second line regimen on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onSecondLine() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnSecondLineArtCalculation());
		cd.setName("on second line regimen");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}

	/**
	 * Patients who are in the "month net cohort" on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition netCohortMonths(int months) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("in " + months + " month net cohort on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addSearch("startedArtMonthsAgo", ReportUtils.map(startedArt(), "onOrAfter=${onDate-"+ (months + 1) + "m},onOrBefore=${onDate-" + months + "m}"));
		cd.addSearch("transferredOut", ReportUtils.map(commonCohorts.transferredOut(), "onOrAfter=${onDate-" + (months + 1) + "m}"));
		cd.setCompositionString("startedArtMonthsAgo AND NOT transferredOut");
		return cd;
	}

	/**
	 * Patients who are in the "month net cohort" on ${onDate}
	 * Patients who started art between dates given months
	 * Used for art cohort analysis
	 * @return the cohort definition
	 */
	public CohortDefinition netCohortMonthsBetweenDatesGivenMonths() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("month net cohort on date given months");
		cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
		cd.addParameter(new Parameter("endDate", "End Date", Date.class));
		cd.addSearch("startedArtMonthsAgo", ReportUtils.map(startedArt(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		cd.setCompositionString("startedArtMonthsAgo");
		return cd;
	}

	/**
	 * Patients on the given regimen. In the future this should look at dispensing records during the reporting period
	 * which implicitly check whether a patient is active. As a workaround until we get to dispensing records, we
	 * explicitly check whether a patient is active here by looking for recent encounters.
	 *
	 * @return the cohort definition
	 */
	public CohortDefinition onRegimen(List<Concept> drugConcepts) {
		RegimenOrderCohortDefinition regCd = new RegimenOrderCohortDefinition();
		Set<Concept> drugConceptSet = new HashSet<Concept>(drugConcepts);
		regCd.setName("ART regimen");
		regCd.addParameter(new Parameter("onDate", "On Date", Date.class));
		regCd.setMasterConceptSet(regimenManager.getMasterSetConcept("ARV"));
		regCd.setConceptSet(drugConceptSet);

		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("Has an encounter in last 3 months and on regimen");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addSearch("onRegimen", ReportUtils.map(regCd, "onDate=${onDate}"));
		cd.addSearch("hasEncounterInLast3Months", ReportUtils.map(commonCohorts.hasEncounter(), "onOrBefore=${onDate},onOrAfter=${onDate-90d}"));
		cd.setCompositionString("onRegimen AND hasEncounterInLast3Months");
		return cd;
	}

	/**
	 * Patients who are in HIV Program and on a given regimen
	 * @return the cohort definition
	 */
	public CohortDefinition inHivProgramAndOnRegimen(List<Concept> drugConcepts) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("In Hiv program and on regimen");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		cd.addSearch("inHivProgram", ReportUtils.map(commonCohorts.inProgram(MetadataUtils.existing(Program.class, HivMetadata._Program.HIV)), "onDate=${onDate}"));
		cd.addSearch("onRegimen", ReportUtils.map(onRegimen(drugConcepts), "onDate=${onDate}"));
		
		cd.setCompositionString("inHivProgram AND onRegimen ");
		return cd;

	}

	/**
	 * Patients who were pregnant when they started ART
	 * @return the cohort definition
	 */
	public CohortDefinition pregnantAtArtStart() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new PregnantAtArtStartCalculation());
		cd.setName("pregnant at start of ART");
		return cd;
	}
	public CohortDefinition newAtArtStart() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new PregnantAtArtStartCalculation());
		cd.setName("new patient at start of ART");
		return cd;
	} 
	
	/**
	 * Patients who were TB patients when they started ART
	 * @return the cohort definition
	 */
	public CohortDefinition tbPatientAtArtStart() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TbPatientAtArtStartCalculation());
		cd.setName("TB patient at start of ART");
		return cd;
	}

	/**
	 * Patients with given WHO stage when started ART
	 * @return the cohort definition
	 */
	public CohortDefinition whoStageAtArtStart(int stage) {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new WhoStageAtArtStartCalculation());
		cd.setName("who stage " + stage + " at start of ART");
		cd.setWithResult(stage);
		return cd;
	}

	/**
	 * Patients who started ART between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition startedArt() {
		DateCalculationCohortDefinition cd = new DateCalculationCohortDefinition(new InitialArtStartDateCalculation());
		cd.setName("started ART");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
		return cd;
	}
	
	public CohortDefinition startsART() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("started ART");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("transferIn", ReportUtils.map(hivCohortLibrary.restartedProgram(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
	//	cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrBefore=${onOrBefore}"));
		//cd.addSearch("completehivProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.HIV)), "completedOnOrBefore=${onOrBefore}"));
		cd.setCompositionString("enrolled AND NOT transferIn");
		return cd;
	}
	public CohortDefinition startArt() {
	CompositionCohortDefinition cd = new CompositionCohortDefinition();
	cd.setName("started ART");
	cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
	cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
	cd.setCompositionString("enrolled");
	return cd;
}



	/**
	 * Patients who are eligible and started art during 6 months review period adults
	 * @return CohortDefinition
	 */
	public CohortDefinition eligibleAndStartedARTAdult() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("Eligible and started ART");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("eligible", ReportUtils.map(EligibleForArtExclusive(), "onDate=${onOrBefore}"));
		cd.addSearch("startART", ReportUtils.map(startedArt(), "onOrAfter=${onOrBefore-6m},onOrBefore=${onOrBefore}"));
		cd.addSearch("adult", ReportUtils.map(commonCohorts.agedAtLeast(15), "effectiveDate=${onOrBefore}"));
		cd.setCompositionString("eligible and startART and adult");
		return  cd;
	}

	/**
	 * Patients who are eligible and started art during 6 months review period children
	 * @return CohortDefinition
	 */
	public CohortDefinition eligibleAndStartedARTPeds() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("Eligible and started ART");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("eligible", ReportUtils.map(EligibleForArtExclusive(), "onDate=${onOrBefore}"));
		cd.addSearch("startART", ReportUtils.map(startedArt(), "onOrAfter=${onOrBefore-6m},onOrBefore=${onOrBefore}"));
		cd.addSearch("child", ReportUtils.map(commonCohorts.agedAtMost(15), "effectiveDate=${onOrBefore}"));
		cd.setCompositionString("eligible and startART and child");
		return  cd;
	}

	/**
	 * Patients who started ART on ${onOrBefore} excluding transfer ins
	 * @return the cohort definition
	 */
	public CohortDefinition startedArtOnDate() { 
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("Started ART excluding transfer ins on date in this facility");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("startedArt", ReportUtils.map(startsART(), "onOrAfter=${onOrAfter-1m},onOrBefore=${onOrAfter-1d}"));
		 cd.addSearch("deceased", ReportUtils.map(commonCohorts.deceasedPatients(), "onOrAfter=${onOrAfter-1m},onOrBefore=${onOrAfter-1d}"));
		 cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrAfter=${onOrAfter-1m},completedOnOrBefore=${onOrAfter-1d}"));
		cd.setCompositionString("startedArt AND NOT (deceased OR completeProgram)");
		return  cd;
	}
     
	public CohortDefinition startedArtExcludingTransferinsOnDates()
	{ 
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("Started ART total");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		//cd.addSearch("startedArt", ReportUtils.map( startsART(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("transferIn", ReportUtils.map(hivCohortLibrary.restartedProgram(), "onOrBefore=${onOrBefore}"));
		cd.addSearch("begining", ReportUtils.map(startsART(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("start", ReportUtils.map(startedArtOnDate(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		 //cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrAfter=${onOrAfter-1m},completedOnOrBefore=${onOrAfter-1d}"));

		//cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrBefore=${onOrBefore}"));
		cd.setCompositionString("begining OR transferIn OR start");
		return  cd;
	}
	
/*	public CohortDefinition includecompleteProgram()
	{ 
	CompositionCohortDefinition cd = new CompositionCohortDefinition();
	cd.setName("Include list of patients who have completed the program");
	cd.addParameter(new Parameter("completedBefore", "Complete Date", Date.class));
	
	cd.addSearch("completedProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrBefore=${onOrBefore}"));
	cd.setCompositionString("completedProgram");
	return  cd;
}*/
	
	/**
	 * Patients who started ART while pregnant between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definitione
	 */
	public CohortDefinition startedArtWhilePregnant() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("started ART while pregnant");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("startedArt", ReportUtils.map(startedArt(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("pregnantAtArtStart", ReportUtils.map(pregnantAtArtStart()));
		cd.setCompositionString("startedArt AND pregnantAtArtStart");
		return cd;
	}

	/**
	 * Patients who started ART while being a TB patient between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition startedArtWhileTbPatient() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("started ART while being TB patient");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("startedArt", ReportUtils.map(startedArt(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("tbPatientAtArtStart", ReportUtils.map(tbPatientAtArtStart()));
		cd.setCompositionString("startedArt AND tbPatientAtArtStart");
		return cd;
	}

	/**
	 * Patients who started ART with the given WHO stage between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition startedArtWithWhoStage(int stage) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("started ART with WHO stage " + stage);
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("startedArt", ReportUtils.map(startedArt(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("withWhoStage", ReportUtils.map(whoStageAtArtStart(stage)));
		cd.setCompositionString("startedArt AND withWhoStage");
		return cd;
	}

	 public CohortDefinition onAZT3TCNVP() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCNVPStockDispensedCalculation());
  		cd.setName("on AZT3TCNVP regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
	 public CohortDefinition onAZT3TCEFV() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCEFVStockDispensedCalculation());
   		cd.setName("on AZT3TCEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
	 public CohortDefinition onTDF3TCNVP() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCNVPStockDispensedCalculation());
 		cd.setName("on TDF3TCNVP regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onTDF3TCEFV() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCEFVStockDispensedCalculation());
   		cd.setName("on TDF3TCEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onTDFFTCNVP() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCNVPStockDispensedCalculation());
   		cd.setName("on TDFFTCNVP regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onTDFFTCEFV() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCEFVStockDispensedCalculation());
  		cd.setName("on TDFFTCEFV regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onTDFFTCEF() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCEFVSixhundredStockDispensedCalculation());
 		cd.setName("on TDFFTCEFV regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onAZT3TCplusNVP() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusNVPStockDispensedCalculation());
 		cd.setName("on AZT3TCplusNVP regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onAZT3TCplusEFVtwo() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusEFVTwoStockDispensedCalculation());
   		cd.setName("on AZT3TCplusEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onTdf3tcNvp() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusNVPStockDispensedCalculation());
  		cd.setName("on TDF3TC+NVP regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onTdf3tcplusefvtwo() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusEFVTwoStockDispensedCalculation());
 		cd.setName("on TDF3TC+EFV regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onTdf3tcplusefvsix() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusEFVSixStockDispensedCalculation());
    		cd.setName("on TDF3TC+EFV regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onTdfftcplusnvp() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusNVPStockDispensedCalculation());
   		cd.setName("on TDFFTC+NVP regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
    
     public CohortDefinition onTdfftcplusefv() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusEFVFourStockDispensedCalculation());
  		cd.setName("on TDFFTC+EFV regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onTdfftcplusefvsix() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusEFVStockDispensedCalculation());
 		cd.setName("on TDFFTC+EFV regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onD4T3TCNVP() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new D4T3TCNVPStockDispensedCalculation());
 		cd.setName("on d4T3TCNVP regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition ond4t3tcplusnvp() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseD4T3TCplusNVPStockDispensedCalculation());
    		cd.setName("on d4T3TC+NVP regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onD4T3TCEFV() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new D4T3TCEFVStockDispensedCalculation());
    		cd.setName("on d4T3TCEFV regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onD4t3tcplusefvsix() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseD4T3TCplusEFVStockDispensedCalculation());
   		cd.setName("on d4T3TC+EFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onTdfFtcDtg() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCDTGStockDispensedCalculation());
  		cd.setName("on TDFFTCDTG regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onTdf3tcDtg() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCDTGStockDispensedCalculation());
 		cd.setName("on TDF3TCDTG regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onabc3tcplusefv() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusEFVStockDispensedCalculation());
  		cd.setName("on ABC3TC+EFV regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onAbc3tcDtg() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABC3TCDTGStockDispensedCalculation());
   		cd.setName("on ABC3TCDTG regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onAbc3tcNvp() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABC3TCNVPStockDispensedCalculation());
 		cd.setName("on ABC3TCNVP regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onAbcFtcEfv() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABCFTCEFVStockDispensedCalculation());
   		cd.setName("on ABCFTCEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onAbcftcDtg() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABCFTCDTGStockDispensedCalculation());
  		cd.setName("on ABCFTCDTG regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onAbcftcNvp() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABCFTCNVPStockDispensedCalculation());
    		cd.setName("on ABCFTCNVP regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onAZT3TCLPVr() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCLPVrStockDispensedCalculation());
  		cd.setName("on AZT3TCLPVr regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onAZT3TCplusLPVr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusLPVrStockDispensedCalculation());
    		cd.setName("on AZT3TCplusLPVr regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onTDF3TCLPVr() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCLPVrStockDispensedCalculation());
 		cd.setName("on TDF3TCLPVr regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition ontdf3tcpluslpvr() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusLPVrStockDispensedCalculation());
 		cd.setName("on TDF/3TC + LPV/r regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onTDFFTCLPVr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCLPVrStockDispensedCalculation());
    		cd.setName("on TDFFTCLPVr regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	} 
     public CohortDefinition ontdfftcpluslpvr() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusLPVrStockDispensedCalculation());
  		cd.setName("on TDF/FTC + LPV/r regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onTDFABCLPVr() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFABCLPVrStockDispensedCalculation());
   		cd.setName("on TDFABCLPVr regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition ontdfabcpluslpvr() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFABCplusLPVrStockDispensedCalculation());
 		cd.setName("on TDF+ABC+ LPV/r  regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onabc3tcpluslpvr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusLPVrStockDispensedCalculation());
    		cd.setName("on ABC/3TC+ LPV/r regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onabc3tcatvr() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABC3TCATVrStockDispensedCalculation());
  		cd.setName("on regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onazt3tcatvr() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCATVrStockDispensedCalculation());
 		cd.setName("on regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition ontdf3tcatvr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCATVrStockDispensedCalculation());
    		cd.setName("on regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onazt3tcplustdfpluslpvr() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusTDFplusLPVrStockDispensedCalculation());
   		cd.setName("on AZT/3TC+ TDF+ LPV/r regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onAZT3TCplusEFVsix() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusEFVSixStockDispensedCalculation());
   		cd.setName("on AZT3TCplusEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onABC3TCplusNVP() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusNVPStockDispensedCalculation());
   		cd.setName("on ABC3TCplusNVP regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onABC3TCplusEFVtwo() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusEFVTwoStockDispensedCalculation());
  		cd.setName("on ABC3TCplusEFV Two hundred EFV regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     
     public CohortDefinition onABC3TCplusEFVsix() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusEFVSixStockDispensedCalculation());
  		cd.setName("on ABC3TCplusEFV Six hundred EFV regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onAZT3TCplusABC() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusABCStockDispensedCalculation());
    		cd.setName("on AZT3TCplusABC regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onD4T3TCplusNVP() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseStockDispensedCalculation());
  		cd.setName("on d4T3TCplusNVP regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onD4T3TCLr() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new D4T3TCLrStockDispensedCalculation());
 		cd.setName("on d4T3TCLr regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onD4T3TCplusEFV() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedD4T3TCplusEFVStockDispensedCalculation());
    		cd.setName("on d4T3TCplusEFV regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onD4T3TCplusABC() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedD4T3TCplusABCStockDispensedCalculation());
   		cd.setName("on d4T3TCplusABC regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onAZT3TCplusRAL() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedAZT3TCplusRALStockDispensedCalculation());
  		cd.setName("on AZT3TCplusRAL regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onAZT3TCplusATVr() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedAZT3TCplusATVrStockDispensedCalculation());
 		cd.setName("on AZT3TCplusATVr regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onABC3TCplusRAL() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedABC3TCplusRALStockDispensedCalculation());
    		cd.setName("on ABC3TCplusRAL regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onTDF3TCplusEFV() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusEFVStockDispensedCalculation());
   		cd.setName("on TDF3TCplusEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onTDF3TCplusNVP() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusNVPStockDispensedCalculation());
  		cd.setName("on TDF3TCplusNVP regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
     public CohortDefinition onTDF3TCplusLPVr() {
 		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusLPVrStockDispensedCalculation());
 		cd.setName("on TDF3TCplusLPVr regimen");
 		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		return cd;
 	}
     public CohortDefinition onTDF3TCplusRAL() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusRALStockDispensedCalculation());
    		cd.setName("on TDF3TCplusRAL regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
    		return cd;
    	}
     public CohortDefinition onTDF3TCplusATVr() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusATVrStockDispensedCalculation());
   		cd.setName("on TDF3TCplusATVr regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
     public CohortDefinition onABC3TCplusATVr() {
  		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedABC3TCplusATVrStockDispensedCalculation());
  		cd.setName("on ABC3TCplusATVr regimen");
  		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		return cd;
  	}
}