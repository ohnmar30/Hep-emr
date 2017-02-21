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

package org.openmrs.module.chaiemr.reporting.library.shared.hiv;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Program;
import org.openmrs.api.PatientSetService;
import org.openmrs.module.chaicore.report.ReportUtils;
import org.openmrs.module.chaicore.report.cohort.definition.CalculationCohortDefinition;
import org.openmrs.module.chaicore.report.cohort.definition.DateObsValueBetweenCohortDefinition;
import org.openmrs.module.chaiemr.Dictionary;
import org.openmrs.module.chaiemr.calculation.library.DeceasedPatientsCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABC3TCATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABC3TCDTGCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABC3TCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABC3TCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABCFTCDTGCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABCFTCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.ABCFTCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCATVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCLPVrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.AZT3TCplusNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.D4T3TCEFVCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.D4T3TCLrCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.D4T3TCNVPCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.FirstLineCalculation;
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
import org.openmrs.module.chaiemr.calculation.library.hiv.OnCtxWithinDurationCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.PatientWaitinglistArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.TBCasesAmongPLHIVSixMonthCalculation;
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
import org.openmrs.module.chaiemr.calculation.library.hiv.art.LevelOfAdherenceCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.NewHIVPatientEnrolledCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.OnAlternateFirstLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.hiv.art.OnOriginalFirstLineArtCalculation;
import org.openmrs.module.chaiemr.calculation.library.tb.TbPatientClassificationCalculation;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.reporting.library.shared.common.CommonCohortLibrary;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.art.ArtCohortLibrary;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.openmrs.module.chaiemr.Metadata;

/**
 * Library of ART related cohort definitions
 */
@Component
public class HivCohortLibrary {

	@Autowired
	private CommonCohortLibrary commonCohorts;
    
	@Autowired
	private ArtCohortLibrary artCohorts;
	/**
	 * Patients referred from the given entry point onto the HIV program
	 * @param entryPoints the entry point concepts
	 * @return the cohort definition
	 */
	public CohortDefinition referredFrom(Concept... entryPoints) {
		EncounterType hivEnrollEncType = MetadataUtils.existing(EncounterType.class, HivMetadata._EncounterType.HIV_ENROLLMENT);
		Concept methodOfEnrollment = Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);

		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.setName("referred from");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.setQuestion(methodOfEnrollment);
		cd.setValueList(Arrays.asList(entryPoints));
		cd.setOperator(SetComparator.IN);
		cd.setEncounterTypeList(Collections.singletonList(hivEnrollEncType));
		return cd;
	}

	/**
	 * Patients referred from the given entry point onto the HIV program
	 * @param entryPoints the entry point concepts
	 * @return the cohort definition
	 */
	public CohortDefinition referredNotFrom(Concept... entryPoints) {
		EncounterType hivEnrollEncType = MetadataUtils.existing(EncounterType.class, HivMetadata._EncounterType.HIV_ENROLLMENT);
		Concept methodOfEnrollment = Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);

		CodedObsCohortDefinition cd = new CodedObsCohortDefinition();
		cd.setName("referred not from");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.setTimeModifier(PatientSetService.TimeModifier.ANY);
		cd.setQuestion(methodOfEnrollment);
		cd.setValueList(Arrays.asList(entryPoints));
		cd.setOperator(SetComparator.NOT_IN);
		cd.setEncounterTypeList(Collections.singletonList(hivEnrollEncType));
		return cd;
	}

	/**
	 * Patients who were enrolled in HIV care (including transfers) between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition enrolled() {
		return commonCohorts.enrolled(MetadataUtils.existing(Program.class, HivMetadata._Program.HIV));
	}

	/**
	 * Patients who were enrolled in HIV care (excluding transfers) between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition enrolledExcludingTransfers() {
		return commonCohorts.enrolledExcludingTransfers(MetadataUtils.existing(Program.class, HivMetadata._Program.HIV));
	}

	/**
	 * Patients who were enrolled in HIV care (excluding transfers) from the given entry points between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition enrolledExcludingTransfersAndReferredFrom(Concept... entryPoints) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("enrolled excluding transfers in HIV care from entry points");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("enrolledExcludingTransfers", ReportUtils.map(enrolledExcludingTransfers(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("referredFrom", ReportUtils.map(referredFrom(entryPoints), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(), "completedOnOrBefore=${onOrBefore}"));
		cd.setCompositionString("enrolledExcludingTransfers AND referredFrom AND NOT completeProgram");
		return cd;
	}

	//2016-2-26====	
		/**
		 * Patients who who got level of adherence on ${onDate}
		 * @return the cohort definition
		 */
		public CohortDefinition levelOfAdherence(int minPercentage,int maxPercentage) {
			CalculationCohortDefinition cd = new CalculationCohortDefinition(new LevelOfAdherenceCalculation(minPercentage,maxPercentage));
			cd.setName("on ART on date");
			cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
			cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			return cd;
		}
	//=============
		public CohortDefinition hivCohort() {
			CalculationCohortDefinition cd = new CalculationCohortDefinition(new TBCasesAmongPLHIVSixMonthCalculation());
			                                       
			cd.setName("on Hiv on date");
			cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
			
			return cd;
		}

	/**
	 * Patients who were enrolled in HIV care (excluding transfers) not from the given entry points between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition enrolledExcludingTransfersAndNotReferredFrom(Concept... entryPoints) {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("enrolled excluding transfers in HIV care not from entry points");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("enrolledExcludingTransfers", ReportUtils.map(enrolledExcludingTransfers(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("referredNotFrom", ReportUtils.map(referredNotFrom(entryPoints), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(), "completedOnOrBefore=${onOrBefore}"));
		cd.setCompositionString("enrolledExcludingTransfers AND referredNotFrom AND NOT completeProgram");
		return cd;
	}
	public CohortDefinition stoppArt() { 
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("Started ART excluding transfer ins on date in this facility");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("startedArt", ReportUtils.map(artCohorts.startArt(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrBefore=${onOrBefore}"));
		cd.setCompositionString("startedArt AND completeProgram");
		return  cd;
	}
        
        public CohortDefinition restartedProgram(){
        	Concept entrypoint = Dictionary.getConcept(Dictionary.METHOD_OF_ENROLLMENT);
        	Concept reason = Dictionary.getConcept(Dictionary.PATIENT_TRANSFERED_PRE_ART);
        	Concept reason1 = Dictionary.getConcept(Dictionary.PATIENT_TRANSFERED_ON_ART);
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("enrolled excluding transfers in HIV care not from entry points");
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
           
            cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
            cd.addSearch("entrypoint", ReportUtils.map(commonCohorts.hasObs(entrypoint ,reason), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.addSearch("entrypointOne", ReportUtils.map(commonCohorts.hasObs(entrypoint ,reason1), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
          //  cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrBefore=${onOrBefore}"));
            cd.setCompositionString("enrolled AND (entrypointOne OR entrypoint)");
            return cd;
        }

        public CohortDefinition reasonOfoutcome(){
         	//Concept outcome = Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
        //	Concept reason = Dictionary.getConcept(Dictionary.DIED);
        	CalculationCohortDefinition cd = new CalculationCohortDefinition(new DeceasedPatientsCalculation());
    		cd.setName("deceases patients on date");
    		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
    	//	cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            CompositionCohortDefinition comp = new CompositionCohortDefinition();
            comp.setName("outcome result of patient due to death");
            comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
           // cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
            comp.addSearch("outcomeReason", ReportUtils.map(artCohorts.startedArtExcludingTransferinsOnDates(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
         //   cd.addSearch("deceasedprev", ReportUtils.map(commonCohorts.deceasedPatients(), "onOrAfter=${onOrAfter-1m},onOrBefore=${onOrAfter-1d}"));
            comp.addSearch("deceased", ReportUtils.map(cd, "onDate=${onOrBefore}"));
            comp.setCompositionString("deceased AND outcomeReason");
            return comp;
        }
        
        public CohortDefinition reasonOfoutcometransfer(){
        	Concept outcome = Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
        	Concept reason = Dictionary.getConcept(Dictionary.TRANSFERRED_OUT);

            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("outcome result of patient due to death");
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
            cd.addSearch("outcomeReason", ReportUtils.map(commonCohorts.hasObs(outcome ,reason), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("enrolled AND  outcomeReason");
            return cd;
        }
        
        public CohortDefinition reasonOfoutcomeMissing(){
        	Concept outcome = Dictionary.getConcept(Dictionary.REASON_FOR_PROGRAM_DISCONTINUATION);
        	Concept reason = Dictionary.getConcept(Dictionary.LOST_MISSING_FOLLOW);

            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("outcome result of patient due to death");
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
            cd.addSearch("outcomeReason", ReportUtils.map(commonCohorts.hasObs(outcome ,reason), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("enrolled AND  outcomeReason");
            return cd;
        }
        
        public CohortDefinition totalArtpatient(){
        	
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("outcome result of patient due to death");
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
         //   cd.addSearch("enrolled", ReportUtils.map(artCohortsstartedArtExcludingTransferinsOnDates,"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
            cd.addSearch("outcomeReason1", ReportUtils.map(reasonOfoutcome(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.addSearch("outcomeReason2", ReportUtils.map(reasonOfoutcometransfer(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.addSearch("outcomeReason3", ReportUtils.map(reasonOfoutcomeMissing(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.addSearch("stop", ReportUtils.map(stoppArt(),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.addSearch("completeprog",ReportUtils.map(artCohorts.startedArtExcludingTransferinsOnDates(),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("completeprog AND NOT (outcomeReason1 OR outcomeReason2 OR outcomeReason3 OR stop)");
            return cd;
        }

       public CohortDefinition onCTX(){
    	   Concept oi = Dictionary.getConcept(Dictionary.OI_TREATMENT_DRUG);
    	   Concept ctx = Dictionary.getConcept(Dictionary.SULFAMETHOXAZOLE_TRIMETHOPRIM);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("hiv patient on CTX");
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addSearch("enrolledHIV", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.HIV)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
            cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrBefore=${onOrBefore}"));
            cd.addSearch("hasctx", ReportUtils.map(commonCohorts.hasObs(oi,ctx), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("enrolledHIV AND NOT enrolled AND hasctx");
            return cd;
        }
       
       public CohortDefinition startedTB(){
           Concept tbPatients = Dictionary.getConcept(Dictionary.TB_PATIENT);
           Concept tbstarted = Dictionary.getConcept(Dictionary.TUBERCULOSIS_DRUG_TREATMENT_START_DATE);
           CompositionCohortDefinition cd = new CompositionCohortDefinition();
           cd.setName("Number of HIV-infected patients with incident TB diagnosed and started on TB treatment during the reporting period");
           cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
	        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
           cd.addSearch("tbpat", ReportUtils.map(commonCohorts.hasObs(tbPatients), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("tbstart", ReportUtils.map(commonCohorts.hasObs(tbstarted), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.HIV)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
           cd.setCompositionString("tbpat AND tbstart AND enrolled");
           
           
           return cd;
        }
       
       public CohortDefinition incidentTB(){
           Concept tbPatients = Dictionary.getConcept(Dictionary.TB_PATIENT);
           Concept tbstarted = Dictionary.getConcept(Dictionary.TUBERCULOSIS_DRUG_TREATMENT_START_DATE);
           CompositionCohortDefinition cd = new CompositionCohortDefinition();
           cd.setName("PLHIV with incident TB");
           cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
	        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
           cd.addSearch("tbpat", ReportUtils.map(commonCohorts.hasObs(tbPatients), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("tbstart", ReportUtils.map(commonCohorts.hasObs(tbstarted), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.HIV)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
           cd.setCompositionString("tbpat AND NOT tbstart AND enrolled");
           
           
           return cd;
        }
        
       public CohortDefinition isoniazidTB(){
    	   Concept prophyl= Dictionary.getConcept(Dictionary.PROPHYLAXIS);
           Concept isionizd= Dictionary.getConcept(Dictionary.ISONIAZID);
           CompositionCohortDefinition cd = new CompositionCohortDefinition();
           cd.setName("PLHIV on IPT");
           cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
	        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
           cd.addSearch("isionizd", ReportUtils.map(commonCohorts.hasObs(prophyl,isionizd), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.HIV)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
           cd.setCompositionString("isionizd AND enrolled");
           
           
           return cd;
        }
       
       public CohortDefinition screenedTB(){
           Concept tbPatients = Dictionary.getConcept(Dictionary.TB_SCREENING);
           Concept assessed1 = Dictionary.getConcept(Dictionary. COUGH_LASTING_MORE_THAN_TWO_WEEKS);
           Concept assessed2 = Dictionary.getConcept(Dictionary.FEVER_TB);
           Concept assessed3 = Dictionary.getConcept(Dictionary.NIGHT_SWEATS);
           Concept assessed4 = Dictionary.getConcept(Dictionary.WEIGHT_LOSS);
           Concept assessed5 = Dictionary.getConcept(Dictionary.LYMPH_NODE);
           Concept assessed6 = Dictionary.getConcept(Dictionary.NONE);
           CompositionCohortDefinition cd = new CompositionCohortDefinition();
           cd.setName("PPLHIV assessed for TB");
           cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
	        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
           cd.addSearch("tbpatcogh", ReportUtils.map(commonCohorts.hasObs(tbPatients,assessed1), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("tbpatfev", ReportUtils.map(commonCohorts.hasObs(tbPatients,assessed2), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("tbpatsweat", ReportUtils.map(commonCohorts.hasObs(tbPatients,assessed3), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("tbpatweight", ReportUtils.map(commonCohorts.hasObs(tbPatients,assessed4), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("tbpatlymph", ReportUtils.map(commonCohorts.hasObs(tbPatients,assessed5), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("tbpatnone", ReportUtils.map(commonCohorts.hasObs(tbPatients,assessed6), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
           cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.HIV)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
           cd.setCompositionString("tbpatcogh OR tbpatfev OR tbpatsweat OR tbpatweight OR tbpatlymph OR tbpatnone AND enrolled");
           
           
           return cd;
        }
       
       public CohortDefinition onOriginalFirstLine() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FirstLineCalculation());
   		cd.setName("on original first line regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   		return cd;
   	}
      
       
       public CohortDefinition onAZT3TCEFV() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCEFVCalculation());
      		cd.setName("on AZT3TCEFV regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onAZT3TCNVP() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCNVPCalculation());
     		cd.setName("on AZT3TCNVP regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		    cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		  CompositionCohortDefinition comp = new CompositionCohortDefinition();
 		  comp.setName("patients who are in this mnth and before");
 		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 			
 		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
 		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
 		comp.setCompositionString("art AND regimen");
 			return comp;
     	}
       
       public CohortDefinition onTDF3TCNVP() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCNVPCalculation());
    		cd.setName("on TDF3TCNVP regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       
       public CohortDefinition onTDF3TCEFV() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCEFVCalculation());
   		cd.setName("on TDF3TCEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       
       public CohortDefinition onTDFFTCNVP() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCNVPCalculation());
      		cd.setName("on TDFFTCNVP regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onTDFFTCEFV() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCEFVCalculation());
     		cd.setName("on TDFFTCEFV regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       public CohortDefinition onTDFFTCEF() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCEFVSixhundredCalculation());
    		cd.setName("on TDFFTCEFV regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		    cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		   CompositionCohortDefinition comp = new CompositionCohortDefinition();
 		  comp.setName("patients who are in this mnth and before");
 		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 			
 		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
 		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
 		comp.setCompositionString("art AND regimen");
 			return comp;
    		
    	}
       
       public CohortDefinition onD4T3TCNVP() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new D4T3TCNVPCalculation());
    		cd.setName("on d4T3TCNVP regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition onD4T3TCEFV() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new D4T3TCEFVCalculation());
   		cd.setName("on d4T3TCEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       
       public CohortDefinition onRegimenNotDose() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FirstLineCalculation());
      		cd.setName("on d4T3TCEFV regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onAZT3TCLPVr() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCLPVrCalculation());
     		cd.setName("on AZT3TCLPVr regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition onTDF3TCLPVr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCLPVrCalculation());
    		cd.setName("on TDF3TCLPVr regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       
       public CohortDefinition onTDFFTCLPVr() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCLPVrCalculation());
   		cd.setName("on TDFFTCLPVr regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}  
       public CohortDefinition onTDFABCLPVr() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFABCLPVrCalculation());
      		cd.setName("on TDFABCLPVr regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onabc3tcatvr() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABC3TCATVrCalculation());
     		cd.setName("on regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       public CohortDefinition onazt3tcatvr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new AZT3TCATVrCalculation());
    		cd.setName("on regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition ontdf3tcatvr() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCATVrCalculation());
   		cd.setName("on regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       public CohortDefinition onD4T3TCplusNVP() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseCalculation());
     		cd.setName("on d4T3TCplusNVP regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       public CohortDefinition onD4T3TCLr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new D4T3TCLrCalculation());
    		cd.setName("on d4T3TCLr regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       
       public CohortDefinition onD4T3TCplusEFV() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedD4T3TCplusEFVCalculation());
   		cd.setName("on d4T3TCplusEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       public CohortDefinition onD4T3TCplusABC() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedD4T3TCplusABCCalculation());
      		cd.setName("on d4T3TCplusABC regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
      
       public CohortDefinition onAZT3TCplusRAL() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedAZT3TCplusRALCalculation());
     		cd.setName("on AZT3TCplusRAL regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition onAZT3TCplusATVr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedAZT3TCplusATVrCalculation());
    		cd.setName("on AZT3TCplusATVr regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       
       public CohortDefinition onABC3TCplusRAL() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedABC3TCplusRALCalculation());
   		cd.setName("on ABC3TCplusRAL regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       public CohortDefinition onTDF3TCplusEFV() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusEFVCalculation());
      		cd.setName("on TDF3TCplusEFV regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onTDF3TCplusNVP() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusNVPCalculation());
     		cd.setName("on TDF3TCplusNVP regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       public CohortDefinition onTDF3TCplusLPVr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusLPVrCalculation());
    		cd.setName("on TDF3TCplusLPVr regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition onTDF3TCplusRAL() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusRALCalculation());
   		cd.setName("on TDF3TCplusRAL regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       public CohortDefinition onTDF3TCplusATVr() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedTDF3TCplusATVrCalculation());
      		cd.setName("on TDF3TCplusATVr regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       public CohortDefinition onABC3TCplusATVr() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedABC3TCplusATVrCalculation());
     		cd.setName("on ABC3TCplusATVr regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       public CohortDefinition onAZT3TCplusNVP() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusNVPCalculation());
    		cd.setName("on AZT3TCplusNVP regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition onAZT3TCplusLPVr() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusLPVrCalculation());
   		cd.setName("on AZT3TCplusLPVr regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       public CohortDefinition onAZT3TCplusEFVtwo() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusEFVTwoCalculation());
      		cd.setName("on AZT3TCplusEFV regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       public CohortDefinition onAZT3TCplusEFVsix() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusEFVSixCalculation());
      		cd.setName("on AZT3TCplusEFV regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onABC3TCplusEFVtwo() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusEFVTwoCalculation());
     		cd.setName("on ABC3TCplusEFV Two hundred EFV regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition onABC3TCplusEFVsix() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusEFVSixCalculation());
    		cd.setName("on ABC3TCplusEFV Six hundred EFV regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition onAZT3TCplusABC() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusABCCalculation());
   		cd.setName("on AZT3TCplusABC regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       
       public CohortDefinition onABC3TCplusNVP() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusNVPCalculation());
      		cd.setName("on ABC3TCplusNVP regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onTdfFtcDtg() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDFFTCDTGCalculation());
     		cd.setName("on TDFFTCDTG regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       public CohortDefinition onTdf3tcDtg() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new TDF3TCDTGCalculation());
    		cd.setName("on TDF3TCDTG regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition onAbcFtcEfv() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABCFTCEFVCalculation());
   		cd.setName("on ABCFTCEFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       
       public CohortDefinition onAbc3tcDtg() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABC3TCDTGCalculation());
      		cd.setName("on ABC3TCDTG regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       public CohortDefinition onAbcftcDtg() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABCFTCDTGCalculation());
     		cd.setName("on ABCFTCDTG regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition onAbc3tcNvp() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABC3TCNVPCalculation());
    		cd.setName("on ABC3TCNVP regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       
       public CohortDefinition onAbcftcNvp() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABCFTCNVPCalculation());
   		cd.setName("on ABCFTCNVP regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       public CohortDefinition onAbc3tcEfv() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new ABC3TCEFVCalculation());
      		cd.setName("on ABC3TCEFV regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onTdf3tcNvp() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusNVPCalculation());
     		cd.setName("on TDF3TC+NVP regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition onTdf3tcplusefvtwo() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusEFVTwoCalculation());
    		cd.setName("on TDF3TC+EFV regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition onTdf3tcplusefvsix() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusEFVSixCalculation());
   		cd.setName("on TDF3TC+EFV regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       
       public CohortDefinition onTdfftcplusnvp() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusNVPCalculation());
      		cd.setName("on TDFFTC+NVP regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       
       public CohortDefinition onTdfftcplusefv() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusEFVFourCalculation());
     		cd.setName("on TDFFTC+EFV regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition onTdfftcplusefvsix() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusEFVCalculation());
    		cd.setName("on TDFFTC+EFV regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       public CohortDefinition ond4t3tcplusnvp() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseD4T3TCplusNVPCalculation());
   		cd.setName("on d4T3TC+NVP regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       public CohortDefinition onD4t3tcplusefvsix() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseD4T3TCplusEFVCalculation());
      		cd.setName("on d4T3TC+EFV regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
       public CohortDefinition onabc3tcplusefv() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusEFVCalculation());
     		cd.setName("on ABC3TC+EFV regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition ontdf3tcpluslpvr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDF3TCplusLPVrCalculation());
    		cd.setName("on TDF/3TC + LPV/r regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       
       public CohortDefinition ontdfftcpluslpvr() {
     		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFFTCplusLPVrCalculation());
     		cd.setName("on TDF/FTC + LPV/r regimen");
     		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
  		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
  		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
     	}
       
       public CohortDefinition ontdfabcpluslpvr() {
    		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseTDFABCplusLPVrCalculation());
    		cd.setName("on TDF+ABC+ LPV/r  regimen");
    		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
 		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
 		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
    	}
       
       public CohortDefinition onabc3tcpluslpvr() {
   		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseABC3TCplusLPVrCalculation());
   		cd.setName("on ABC/3TC+ LPV/r regimen");
   		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		 CompositionCohortDefinition comp = new CompositionCohortDefinition();
		  comp.setName("patients who are in this mnth and before");
		 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
			
		comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		comp.setCompositionString("art AND regimen");
			return comp;
   	}
       
       public CohortDefinition onazt3tcplustdfpluslpvr() {
      		CalculationCohortDefinition cd = new CalculationCohortDefinition(new FixedDoseAZT3TCplusTDFplusLPVrCalculation());
      		cd.setName("on AZT/3TC+ TDF+ LPV/r regimen");
      		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
   		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
   	 CompositionCohortDefinition comp = new CompositionCohortDefinition();
	  comp.setName("patients who are in this mnth and before");
	 comp.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
	comp.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		
	comp.addSearch("art", ReportUtils.map(totalArtpatient(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.addSearch("regimen", ReportUtils.map(cd, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	comp.setCompositionString("art AND regimen");
		return comp;
      	}
	/**
	 * Patients with a CD4 result between {onOrAfter} and {onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition hasCd4Result() {
		Concept cd4Count = Dictionary.getConcept(Dictionary.CD4_COUNT);
		Concept cd4Percent = Dictionary.getConcept(Dictionary.CD4_PERCENT);

		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("patients with CD4 results");
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addSearch("hasCdCount", ReportUtils.map(commonCohorts.hasObs(cd4Count), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("hasCd4Percent", ReportUtils.map(commonCohorts.hasObs(cd4Percent), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("hasCdCount OR hasCd4Percent");
		return cd;
	}
        
        public CohortDefinition givenOIDrugs(){
                Concept oiTxDrugs = Dictionary.getConcept(Dictionary.OI_TREATMENT_DRUG);
                
                CompositionCohortDefinition cd = new CompositionCohortDefinition();
                cd.setName("Patients Treated for Opportunistic Infections");
                cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		       cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
                cd.addSearch("givenDrugs", ReportUtils.map(commonCohorts.hasObs(oiTxDrugs), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
                cd.setCompositionString("givenDrugs");
                return cd;
        }
        
        public CohortDefinition receivedART(){
            Concept tbPatients = Dictionary.getConcept(Dictionary.TB_PATIENT);
            Concept tbPatientsstatus = Dictionary.getConcept(Dictionary.TB_Rx);
                CompositionCohortDefinition cd = new CompositionCohortDefinition();
                cd.setName("Patients HIV positive TB patients who have received ART");
                cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		        cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		        cd.addSearch("dead", ReportUtils.map(reasonOfoutcome(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	    		cd.addSearch("transferout", ReportUtils.map(reasonOfoutcometransfer(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
	    		cd.addSearch("lostmissing", ReportUtils.map(reasonOfoutcomeMissing(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));

                cd.addSearch("givenDrugs", ReportUtils.map(commonCohorts.hasObs(tbPatients,tbPatientsstatus), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
                cd.addSearch("enrolled", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.ART)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
        		cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrBefore=${onOrBefore}"));
        		cd.addSearch("completedProgram", ReportUtils.map(commonCohorts.compltedProgram(), "completedOnOrBefore=${onOrBefore}"));
                cd.setCompositionString("givenDrugs AND enrolled AND NOT (completeProgram OR completedProgram) AND NOT (dead OR transferout OR lostmissing)");
                
                
                return cd;
        }
        public CohortDefinition initializedHIV(){
        
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("waiting list for ART");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            
            cd.addSearch("enrolledHIV", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.HIV)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
          
            cd.setCompositionString("enrolledHIV ");
            return cd;
        }
        public CohortDefinition notinitializedART(){
             	CalculationCohortDefinition comp = new CalculationCohortDefinition(new PatientWaitinglistArtCalculation());
    		comp.setName("On CTX on date");
    		comp.addParameter(new Parameter("onDate", "On Date", Date.class));
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("waiting list for ART");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("onwaitinglistArt", ReportUtils.map(comp, "onDate=${onOrBefore}"));
            cd.addSearch("completeProgram", ReportUtils.map(commonCohorts.compltedProgram(MetadataUtils.existing(Program.class, Metadata.Program.ART)), "completedOnOrBefore=${onOrBefore}"));
      //      cd.addSearch("enrolledHIV", ReportUtils.map(commonCohorts.enrolled(MetadataUtils.existing(Program.class, Metadata.Program.HIV)),"enrolledOnOrAfter=${onOrAfter},enrolledOnOrBefore=${onOrBefore}"));
            cd.setCompositionString("onwaitinglistArt AND NOT completeProgram");
            return cd;
        }
        
        public CohortDefinition testedForCDFour(){
        	Concept laboratoryOrder = Dictionary.getConcept(Dictionary.lABORATORY_ORDER);
        	Concept cd4Count = Dictionary.getConcept(Dictionary.CD4_COUNT);
        	
        	CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("tested for CD 4 count");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("testedForCD4count", ReportUtils.map(commonCohorts.hasObs(laboratoryOrder,cd4Count), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("testedForCD4count");
            return cd;
        }
        
        public CohortDefinition testedForViralLoad(){
        	Concept laboratoryOrder = Dictionary.getConcept(Dictionary.lABORATORY_ORDER);
        	Concept viralLoad = Dictionary.getConcept(Dictionary.HIV_VIRAL_LOAD);
        	
        	CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("tested for viral load");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("testedForViralLoad", ReportUtils.map(commonCohorts.hasObs(laboratoryOrder,viralLoad), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("testedForViralLoad");
            return cd;
        }
        
        public CohortDefinition performanceScaleA(){
        	Concept performance = Dictionary.getConcept(Dictionary.PERFORMANCE);
        	Concept scaleA = Dictionary.getConcept(Dictionary.PERFSCALE_A);
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with performance scale A");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("scaleA", ReportUtils.map(commonCohorts.hasObs(performance,scaleA), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("scaleA ");
            return cd;
        } 
        public CohortDefinition performanceScaleb(){
        	Concept performance = Dictionary.getConcept(Dictionary.PERFORMANCE);
        	Concept scaleB = Dictionary.getConcept(Dictionary.PERFSCALE_B);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("Patient with performance scale B");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("scaleB", ReportUtils.map(commonCohorts.hasObs(performance,scaleB), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("scaleB");
            return cd;
        } 
        public CohortDefinition performanceScalec(){
        	Concept performance = Dictionary.getConcept(Dictionary.PERFORMANCE);
        	Concept scaleC = Dictionary.getConcept(Dictionary.PERFSCALE_C);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with performance scale A");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("scaleC", ReportUtils.map(commonCohorts.hasObs(performance,scaleC), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("scaleC");
            return cd;
        } 
        
        
        
        public CohortDefinition riskFactors1(){
        	Concept risk = Dictionary.getConcept(Dictionary.HIV_RISK_FACTOR);
        	Concept risk1 = Dictionary.getConcept(Dictionary.RISK_FACTOR_MSM);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with risk 1");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("risk1", ReportUtils.map(commonCohorts.hasObs(risk,risk1), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("risk1 ");
            return cd;
        }
        
        public CohortDefinition riskFactors2(){
        	Concept risk = Dictionary.getConcept(Dictionary.HIV_RISK_FACTOR);
        	Concept risk2 = Dictionary.getConcept(Dictionary.RISK_FACTOR_SW);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with risk 2");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("risk2", ReportUtils.map(commonCohorts.hasObs(risk,risk2), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("risk2");
            return cd;
        }
        
        public CohortDefinition riskFactors3(){
        	Concept risk = Dictionary.getConcept(Dictionary.HIV_RISK_FACTOR);
        	Concept risk3 = Dictionary.getConcept(Dictionary.RISK_FACTOR_HETRO);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with risk 3");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("risk3", ReportUtils.map(commonCohorts.hasObs(risk,risk3), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString(" risk3 ");
            return cd;
        }
        
        
        public CohortDefinition riskFactors4(){
        	Concept risk = Dictionary.getConcept(Dictionary.HIV_RISK_FACTOR);
        	Concept risk4 = Dictionary.getConcept(Dictionary.RISK_FACTOR_IDU);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with risk 4");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class)); 
            cd.addSearch("risk4", ReportUtils.map(commonCohorts.hasObs(risk,risk4), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString("risk4 ");
            return cd;
        }
        
        public CohortDefinition riskFactors5(){
        	Concept risk = Dictionary.getConcept(Dictionary.HIV_RISK_FACTOR);
        	Concept risk5 = Dictionary.getConcept(Dictionary.RISK_FACTOR_BLOODTRANS);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with risk 5");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("risk5", ReportUtils.map(commonCohorts.hasObs(risk,risk5), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString(" risk5");
            return cd;
        }
        
        public CohortDefinition riskFactors6(){
        	Concept risk = Dictionary.getConcept(Dictionary.HIV_RISK_FACTOR);
        	Concept risk6 = Dictionary.getConcept(Dictionary.RISK_FACTOR_MOTHERTOCHILD);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with risk 6");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("risk6", ReportUtils.map(commonCohorts.hasObs(risk,risk6), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString(" risk6 ");
            return cd;
        }
        
        
        public CohortDefinition riskFactors7(){
        	Concept risk = Dictionary.getConcept(Dictionary.HIV_RISK_FACTOR);
        	Concept risk7 = Dictionary.getConcept(Dictionary.RISK_FACTOR_UNKNOWN);
        	
            CompositionCohortDefinition cd = new CompositionCohortDefinition();
            cd.setName("patient with risk 7");
            cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
            cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
            cd.addSearch("risk7", ReportUtils.map(commonCohorts.hasObs(risk,risk7), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
            cd.setCompositionString(" risk7 ");
            return cd;
        }
        
   
	/**
	 * Patients with a HIV care visit between {onOrAfter} and {onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition hasHivVisit() {
		EncounterType hivEnrollment = MetadataUtils.existing(EncounterType.class, HivMetadata._EncounterType.HIV_ENROLLMENT);
		EncounterType hivConsultation = MetadataUtils.existing(EncounterType.class, HivMetadata._EncounterType.HIV_CONSULTATION);
		return commonCohorts.hasEncounter(hivEnrollment, hivConsultation);
	}

	/**
	 * Patients who took CTX prophylaxis between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public CohortDefinition onCtxProphylaxis() {
		CodedObsCohortDefinition onCtx = new CodedObsCohortDefinition();
		onCtx.setName("on CTX prophylaxis");
		onCtx.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		onCtx.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		onCtx.setTimeModifier(PatientSetService.TimeModifier.LAST);
		onCtx.setQuestion(Dictionary.getConcept(Dictionary.COTRIMOXAZOLE_DISPENSED));
		onCtx.setValueList(Arrays.asList(Dictionary.getConcept(Dictionary.YES)));
		onCtx.setOperator(SetComparator.IN);

		//we need to include those patients who have either ctx in the med orders
		//that was not captured coded obs

		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("Having CTX either dispensed");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("onCtx", ReportUtils.map(onCtx, "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("onMedCtx", ReportUtils.map(commonCohorts.medicationDispensed(Dictionary.getConcept(Dictionary.SULFAMETHOXAZOLE_TRIMETHOPRIM)),"onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("onCtxOnDuration", ReportUtils.map(onCtxOnDuration(), "onDate=${onOrBefore}"));
		cd.setCompositionString("onCtx OR onMedCtx OR onCtxOnDuration");

		return cd;
	}

	/**
	 * Patients who are in HIV care and are taking CTX prophylaxis between ${onOrAfter} and ${onOrBefore}
	 * @return
	 */
	public CohortDefinition inHivProgramAndOnCtxProphylaxis() {
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("in HIV program and on CTX prophylaxis");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("inProgram", ReportUtils.map(commonCohorts.inProgram(hivProgram), "onDate=${onOrBefore}"));
		cd.addSearch("onCtxProphylaxis", ReportUtils.map(onCtxProphylaxis(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("inProgram AND onCtxProphylaxis");
		return cd;
	}

	/**
	 * Patients who are in HIV care and are taking Fluconazole prophylaxis between ${onOrAfter} and ${onOrBefore}
	 * @return
	 */
	public CohortDefinition inHivProgramAndOnFluconazoleProphylaxis() {
		Concept flucanozole = Dictionary.getConcept(Dictionary.FLUCONAZOLE);
		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("in HIV program and on Fluconazole prophylaxis");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("inProgram", ReportUtils.map(commonCohorts.inProgram(hivProgram), "onDate=${onOrBefore}"));
		cd.addSearch("onMedication", ReportUtils.map(commonCohorts.medicationDispensed(flucanozole), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("inProgram AND onMedication");
		return cd;
	}

	/**
	 * Patients who are in HIV care and are on Flucanzole or CTX prophylaxis
	 * @return
	 */
	public CohortDefinition inHivProgramAndOnAnyProphylaxis() {
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("in HIV program and on any prophylaxis");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("onCtx", ReportUtils.map(inHivProgramAndOnCtxProphylaxis(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("onFlucanozole", ReportUtils.map(inHivProgramAndOnFluconazoleProphylaxis(), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("onCtx OR onFlucanozole");
		return cd;
	}

	/**
	 * Patients tested for HIV between ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public  CohortDefinition testedForHiv() {
		Concept hivStatus = Dictionary.getConcept(Dictionary.HIV_STATUS);
		Concept indeterminate = Dictionary.getConcept(Dictionary.INDETERMINATE);
		Concept hivInfected = Dictionary.getConcept(Dictionary.HIV_INFECTED);
		Concept unknown = Dictionary.getConcept(Dictionary.UNKNOWN);
		Concept positive = Dictionary.getConcept(Dictionary.POSITIVE);
		Concept negative = Dictionary.getConcept(Dictionary.NEGATIVE);
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("tested for HIV");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("resultOfHivTest", ReportUtils.map(commonCohorts.hasObs(hivStatus, unknown, positive, negative), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("testedForHivHivInfected", ReportUtils.map(commonCohorts.hasObs(hivInfected, indeterminate,positive,negative), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("resultOfHivTest OR testedForHivHivInfected");
		return cd;
	}

	/**
	 * Patients tested for HIV and turn to be positive ${onOrAfter} and ${onOrBefore}
	 * @return the cohort definition
	 */
	public  CohortDefinition testedHivStatus(Concept status) {
		Concept hivStatus = Dictionary.getConcept(Dictionary.HIV_STATUS);
		Concept hivInfected = Dictionary.getConcept(Dictionary.HIV_INFECTED);
		CompositionCohortDefinition cd = new CompositionCohortDefinition();
		cd.setName("tested for positive for HIV");
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addSearch("resultOfHivTestPositive", ReportUtils.map(commonCohorts.hasObs(hivStatus, status), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.addSearch("testedForHivHivInfectedPositive", ReportUtils.map(commonCohorts.hasObs(hivInfected ,status), "onOrAfter=${onOrAfter},onOrBefore=${onOrBefore}"));
		cd.setCompositionString("resultOfHivTestPositive OR testedForHivHivInfectedPositive");
		return cd;
	}

	/**
	 * Patients who started art from the transfer facility
	 * @return the cohort definition
	 */
	public CohortDefinition startedArtFromTransferringFacilityOnDate() {
		Concept starteArtFromTransferringFacility = Dictionary.getConcept(Dictionary.ANTIRETROVIRAL_TREATMENT_START_DATE);
		DateObsValueBetweenCohortDefinition cd = new DateObsValueBetweenCohortDefinition();
		cd.setName("Patients Who Started ART From the Transferring Facility between date");
		cd.setQuestion(starteArtFromTransferringFacility);
		cd.addParameter(new Parameter("onOrBefore", "Before Date", Date.class));
		cd.addParameter(new Parameter("onOrAfter", "After Date", Date.class));
		return cd;

	}

	/**
	 * Patients who are on ctx on ${onDate}
	 * @return the cohort definition
	 */
	public CohortDefinition onCtxOnDuration() {
		CalculationCohortDefinition cd = new CalculationCohortDefinition(new OnCtxWithinDurationCalculation());
		cd.setName("On CTX on date");
		cd.addParameter(new Parameter("onDate", "On Date", Date.class));
		return cd;
	}

	
}