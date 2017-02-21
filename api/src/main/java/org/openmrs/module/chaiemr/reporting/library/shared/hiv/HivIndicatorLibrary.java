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
import org.openmrs.Program;
import org.openmrs.module.chaiemr.Metadata;
import org.openmrs.module.chaiemr.reporting.library.shared.hiv.art.ArtCohortLibrary;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.chaiemr.metadata.HivMetadata;
import org.openmrs.module.chaiemr.reporting.library.shared.common.CommonIndicatorLibrary;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.chaicore.report.ReportUtils.map;
import static org.openmrs.module.chaiemr.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Library of HIV related indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class HivIndicatorLibrary {

	@Autowired
	private CommonIndicatorLibrary commonIndicators;

	@Autowired
	private HivCohortLibrary hivCohorts;

	/**
	 * Number of new patients enrolled in HIV care (excluding transfers)
	 * @return the indicator
	 */
	public CohortIndicator enrolledExcludingTransfers() {
		return commonIndicators.enrolledExcludingTransfers(MetadataUtils.existing(Program.class, HivMetadata._Program.HIV));
	}

	/**
	 * Number of patients ever enrolled in HIV care (including transfers) up to ${endDate}
	 * @return the indicator
	 */
	public CohortIndicator enrolledCumulative() {
		return commonIndicators.enrolledCumulative(MetadataUtils.existing(Program.class, HivMetadata._Program.HIV));
	}
	/**
	 * Number of patients  enrolled in HIV positiveTB received ART up to ${endDate}
	 * @return the indicator
	 */
	public CohortIndicator enrolledCumulativeTB() {
		return commonIndicators.enrolledCumulativeTB(MetadataUtils.existing(Program.class,HivMetadata._Programs.ART));
	}
	/**
	 * Number of patients who were enrolled (excluding transfers) after referral from the given entry points
	 * @return the indicator
	 */
	public CohortIndicator enrolledExcludingTransfersAndReferredFrom(Concept... entryPoints) {
		return cohortIndicator("newly enrolled patients referred from",
				map(hivCohorts.enrolledExcludingTransfersAndReferredFrom(entryPoints), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	//2016-2-26====
		/**
		 * Number of patients who got level of adherence 
		 * @return the indicator
		 */
		public CohortIndicator levelOfAdherence(int minPercentage,int maxPercentage) {
			return cohortIndicator("patients on ART", map(hivCohorts.levelOfAdherence(minPercentage,maxPercentage), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}
	//==============
		public CohortIndicator hivCohort() {  
			return cohortIndicator("patients on hiv", map(hivCohorts.hivCohort(), "onOrAfter=${startDate}"));
		}
	/**
	 * Number of patients who were enrolled (excluding transfers) after referral from services other than the given entry points
	 * @return the indicator
	 */
	public CohortIndicator enrolledExcludingTransfersAndNotReferredFrom(Concept... entryPoints) {
		return cohortIndicator("newly enrolled patients referred from",
				map(hivCohorts.enrolledExcludingTransfersAndNotReferredFrom(entryPoints), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	/**
	 * Number of patients who are on Cotrimoxazole prophylaxis
	 * @return the indicator
	 */
	public CohortIndicator onCotrimoxazoleProphylaxis() {
		return cohortIndicator("patients on CTX prophylaxis", map(hivCohorts.inHivProgramAndOnCtxProphylaxis(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	
	/**
	 * Number of patients who are on Fluconazole prophylaxis
	 * @return the indicator
	 */
	public CohortIndicator onFluconazoleProphylaxis() {
		return cohortIndicator("patients on Fluconazole prophylaxis", map(hivCohorts.inHivProgramAndOnFluconazoleProphylaxis(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	/**
	 * Number of patients who are on any form of prophylaxis
	 * @return the indicator
	 */
	public CohortIndicator onProphylaxis() {
		return cohortIndicator("patients on any prophylaxis", map(hivCohorts.inHivProgramAndOnAnyProphylaxis(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
        
        public CohortIndicator givenDrugsForOI() {
                return cohortIndicator("patients treated for Opportunistic Infection", map(hivCohorts.givenOIDrugs(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
        }
        
        public CohortIndicator initiatedARTandTB(){ 
                return cohortIndicator("TB patients received ART", map(hivCohorts.receivedART(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
        }
        
        public CohortIndicator stopART(){
            return cohortIndicator("patients Restarted ART", map(hivCohorts.stoppArt(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
        public CohortIndicator restartART(){
                return cohortIndicator("patients Restarted ART", map(hivCohorts.restartedProgram(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
        }
        public CohortIndicator inHIV(){
            return cohortIndicator("patients initalized for HIV", map(hivCohorts.initializedHIV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
        public CohortIndicator notInART(){
                return cohortIndicator("patients not initialized for ART", map(hivCohorts.notinitializedART(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
        }
        
        public CohortIndicator performanceScaleA(){
            return cohortIndicator("patients with performance scale A", map(hivCohorts.performanceScaleA(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
        }
        
            public CohortIndicator performanceScaleB(){
                return cohortIndicator("patients with performance scale B", map(hivCohorts.performanceScaleb(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
            public CohortIndicator performanceScaleC(){
                return cohortIndicator("patients with performance scale B", map(hivCohorts.performanceScalec(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
  
        public CohortIndicator riskFactor1(){
                return cohortIndicator("patients with risk Factor 1", map(hivCohorts.riskFactors1(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }    
            
       public CohortIndicator riskFactor2(){
                return cohortIndicator("patients with risk Factor 2", map(hivCohorts.riskFactors2(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }  
            
       public CohortIndicator riskFactor3(){
           return cohortIndicator("patients with risk Factor 3", map(hivCohorts.riskFactors3(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }    
       
       public CohortIndicator riskFactor4(){
           return cohortIndicator("patients with risk Factor 4", map(hivCohorts.riskFactors4(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }       
            
       public CohortIndicator riskFactor5(){
           return cohortIndicator("patients with risk Factor 5", map(hivCohorts.riskFactors5(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }    
       
       public CohortIndicator riskFactor6(){
           return cohortIndicator("patients with risk Factor 6", map(hivCohorts.riskFactors6(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }      
       
       public CohortIndicator riskFactor7(){
           return cohortIndicator("patients with risk Factor 7", map(hivCohorts.riskFactors7(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator cdFourTest(){
           return cohortIndicator("patients tested for CD 4 count", map(hivCohorts.testedForCDFour(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
      }
       
       public CohortIndicator viralLoadTest(){
           return cohortIndicator("patients tested for viral load", map(hivCohorts.testedForViralLoad(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
      }
       public CohortIndicator outcomeArt(){
           return cohortIndicator("out come art reason died", map(hivCohorts.reasonOfoutcome(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }  
       
       public CohortIndicator outcometransferredArt(){
           return cohortIndicator("out come art reason transferred", map(hivCohorts.reasonOfoutcometransfer(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       } 
       
       public CohortIndicator outcomelostmissingArt(){
           return cohortIndicator("out come art reason reason lost", map(hivCohorts.reasonOfoutcomeMissing(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       } 
       
       public CohortIndicator startArt(){
           return cohortIndicator("out come art reason reason lost", map(hivCohorts.totalArtpatient(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       } 
       
       public CohortIndicator hivonCTX(){
           return cohortIndicator("patient on ctx", map(hivCohorts.onCTX(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }  
       
       public CohortIndicator hivstartedTB(){
           return cohortIndicator("patient strated TB", map(hivCohorts.startedTB(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator hivincidentTB(){
           return cohortIndicator("patient initiated TB", map(hivCohorts.incidentTB(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator hivisoniazidTB(){
           return cohortIndicator("patient initiated TB", map(hivCohorts.isoniazidTB(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator hivscreenedTB(){
           return cohortIndicator("patient initiated TB", map(hivCohorts.screenedTB(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onoriginal(){
           return cohortIndicator("patient on original first line", map(hivCohorts.onOriginalFirstLine(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenNVP(){
           return cohortIndicator("Patients having (AZT/3TC/NVP)(300/150/200)mg regimen", map(hivCohorts.onAZT3TCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimenEFV(){
           return cohortIndicator("Patients having (AZT/3TC/EFV) (300/150/600)mg regimen", map(hivCohorts.onAZT3TCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenTDF(){
           return cohortIndicator("Patients having (TDF/3TC/NVP) (300/300/200)mg regimen", map(hivCohorts.onTDF3TCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenTDFEFV(){
           return cohortIndicator("Patients having (TDF/3TC/EFV) (300/300/600)mg regimen", map(hivCohorts.onTDF3TCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenTDFFTC(){
           return cohortIndicator("Patients having (TDF/FTC/NVP) (300/200/200)mg regimen", map(hivCohorts.onTDFFTCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimenTDFFTCEFV(){
           return cohortIndicator("Patients having (TDF/FTC/EFV) ((300/200/200)mg regimen", map(hivCohorts.onTDFFTCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimenTDFFTCE(){
           return cohortIndicator("Patients having (TDF/FTC/EFV) ((300/200/600)mg regimen", map(hivCohorts.onTDFFTCEF(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenDT(){
           return cohortIndicator("Patients having (d4T/3TC/NVP)  (30/150/200)mg regimen", map(hivCohorts.onD4T3TCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenDTEFV(){
           return cohortIndicator("Patients having (d4T/3TC/EFV) (30/150/600)mg regimen", map(hivCohorts.onD4T3TCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimenNotDose(){
           return cohortIndicator("Patients having  regimen", map(hivCohorts.onRegimenNotDose(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenAzt(){
           return cohortIndicator("Patients having (AZT/3TC/LPV/r) (300/150/200/50)mg regimen", map(hivCohorts.onAZT3TCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenTdf(){
           return cohortIndicator("Patients having (TDF/3TC/LPV/r) (300/300/200/50)mg regimen", map(hivCohorts.onTDF3TCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenTdfftc(){
           return cohortIndicator("Patients having  (TDF/FTC/LPV/r) (300/200/200/50)mg regimen", map(hivCohorts.onTDFFTCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenTdfabc(){
           return cohortIndicator("Patients having (TDF/ABC/LPV/r) (300/300/200/50)mg regimen", map(hivCohorts.onTDFABCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       
       public CohortIndicator onregimenazt(){
           return cohortIndicator("Patients having (d4T+3TC+NVP) regimen", map(hivCohorts.onD4T3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimend4t(){
           return cohortIndicator("Patients having (d4T+3TC+L/r)  regimen", map(hivCohorts.onD4T3TCLr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimend4tefv(){
           return cohortIndicator("Patients having (d4T+3TC+EFV)  regimen", map(hivCohorts.onD4T3TCplusEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimend4tabc(){
           return cohortIndicator("Patients having (d4T+3TC+ABC)  regimen", map(hivCohorts.onD4T3TCplusABC(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimenaztral(){
           return cohortIndicator("Patients having (AZT+3TC+RAL)  regimen", map(hivCohorts.onAZT3TCplusRAL(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimenatv(){
           return cohortIndicator("Patients having (AZT+3TC+ATV/r)  regimen", map(hivCohorts.onAZT3TCplusATVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenral(){
           return cohortIndicator("Patients having (ABC+3TC+RAL)  regimen", map(hivCohorts.onABC3TCplusRAL(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenefv(){
           return cohortIndicator("Patients having (TDF+3TC+EFV)  regimen", map(hivCohorts.onTDF3TCplusEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimennvp(){
           return cohortIndicator("Patients having (TDF+3TC+NVP)  regimen", map(hivCohorts.onTDF3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimenlpv(){
           return cohortIndicator("Patients having (TDF+3TC+LPV/r)  regimen", map(hivCohorts.onTDF3TCplusLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       
       public CohortIndicator onregimentdfral(){
           return cohortIndicator("Patients having (TDF+3TC+RAL)  regimen", map(hivCohorts.onTDF3TCplusRAL(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimentdfatv(){
           return cohortIndicator("Patients having (TDF+3TC+ATV/r)  regimen", map(hivCohorts.onTDF3TCplusATVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimenabc(){
           return cohortIndicator("Patients having (ABC+3TC+ATV/r)  regimen", map(hivCohorts.onABC3TCplusATVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimendosenvp(){
           return cohortIndicator("Patients having (AZT/3TC+NVP) (60/30+50) mg regimen", map(hivCohorts.onAZT3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }
       public CohortIndicator onregimendoselpvr(){
           return cohortIndicator("Patients having (AZT/3TC+LPV/r)(60/30+100/25) mg regimen", map(hivCohorts.onAZT3TCplusLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
       }

	    public CohortIndicator onregimendoseefvtwo() {
		return cohortIndicator("Patients having (AZT/3TC+EFV) (60/30+200) and (300/150+600)  mg regimen", map(hivCohorts.onAZT3TCplusEFVtwo(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	    }

	     public CohortIndicator onregimendoseefvsix() {
		return  cohortIndicator("Patients having (AZT/3TC+EFV) (60/30+600) mg regimen", map(hivCohorts.onAZT3TCplusEFVsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	     }

	     public CohortIndicator onregimendoseabcefvtwo() {
	 	  return  cohortIndicator("Patients having (ABC/3TC+EFV) (60/30+200)  mg regimen", map(hivCohorts.onABC3TCplusEFVtwo(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	    }
 
	    public CohortIndicator onregimendoseabcefvsix() {
		return  cohortIndicator("Patients having (ABC/3TC+EFV) (60/30+600) mg regimen", map(hivCohorts.onABC3TCplusEFVsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	    }

	    public CohortIndicator onregimendoseabc() {
		return  cohortIndicator("Patients having (AZT/3TC+ABC) (60/30+60) mg regimen", map(hivCohorts.onAZT3TCplusABC(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	    }

	    public CohortIndicator onregimendoseabcnvp() {
		return  cohortIndicator("Patients having (ABC/3TC+NVP) (60/30+50) mg regimen", map(hivCohorts.onABC3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	    }

		public CohortIndicator  onregimenftcdtg() {
			return  cohortIndicator("Patients having (TDF/FTC/DTG) regimen", map(hivCohorts.onTdfFtcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator  onregimen3tcdtg() {
			return  cohortIndicator("Patients having (TDF/3TC/DTG) regimen", map(hivCohorts.onTdf3tcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator  onregimenftcefv() {
			return  cohortIndicator("Patients having (ABC/FTC/EFV) regimen", map(hivCohorts.onAbcFtcEfv(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator  onregimenabc3tcdtg() {
			return  cohortIndicator("Patients having (ABC/3TC/DTG) regimen", map(hivCohorts.onAbc3tcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator  onregimenabcftcdtg() {
			return  cohortIndicator("Patients having (ABC/FTC/DTG) regimen", map(hivCohorts.onAbcftcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator  onregimen3tcnvp() {
			return  cohortIndicator("Patients having (ABC/3TC/NVP) regimen", map(hivCohorts.onAbc3tcNvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator  onregimenftcnvp() {
			return  cohortIndicator("Patients having (ABC/FTC/NVP) regimen", map(hivCohorts.onAbcftcNvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator  onregimen3tcefv() {
			return  cohortIndicator("Patients having (ABC/3TC/EFV) regimen", map(hivCohorts.onAbc3tcEfv(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimentdf3tcnvp() {
			return  cohortIndicator("Patients having (TDF/3TC+NVP) (300/300+200) mg regimen", map(hivCohorts.onTdf3tcNvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public  CohortIndicator onregimen3tcefvtwo() {
			
			return  cohortIndicator("Patients having (TDF/3TC+EFV) (300/300+400) mg regimen", map(hivCohorts.onTdf3tcplusefvtwo(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		}

		public  CohortIndicator onregimen3tcefvsix() {
			
			return  cohortIndicator("Patients having (TDF/3TC+EFV) (300/300+600) mg regimen", map(hivCohorts.onTdf3tcplusefvsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		}

		public  CohortIndicator onregimentdfftcnvp() {
			
			return  cohortIndicator("Patients having (TDF/FTC+NVP) (300/200+200) mg regimen", map(hivCohorts.onTdfftcplusnvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		
		}

		public CohortIndicator onregimenftcefvfour() {
			return  cohortIndicator("Patients having (TDF/FTC+EFV) (300/200+400) mg regimen", map(hivCohorts.onTdfftcplusefv(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimenftcefvsix() {
			return  cohortIndicator("Patients having (TDF/FTC+EFV) (300/200+600) mg regimen", map(hivCohorts.onTdfftcplusefvsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimendt3tcnvp() {
			return  cohortIndicator("Patients having (d4T/3TC+NVP) (30/150+200) mg regimen", map(hivCohorts.ond4t3tcplusnvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimendt3tcefv() {
			return  cohortIndicator("Patients having (d4T/3TC+EFV) (30/150+600) mg regimen", map(hivCohorts.onD4t3tcplusefvsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimenabcefv() {
			return  cohortIndicator("Patients having (ABC/3TC + EFV) (600/300+600) mg regimen", map(hivCohorts.onabc3tcplusefv(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimentdf3tc() {
			return  cohortIndicator("Patients having (TDF/3TC + LPV/r) (300/300+200/50) mg regimen", map(hivCohorts.ontdf3tcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimentdfftc() {
			return  cohortIndicator("Patients having (TDF/FTC+ LPV/r) (300/200+200/50) mg regimen", map(hivCohorts.ontdfftcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimentdf() {
			return  cohortIndicator("Patients having (TDF+ABC+ LPV/r )(300/300+200/50)mg mg regimen", map(hivCohorts.ontdfabcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimenabcdoselpvr() {
			return  cohortIndicator("Patients having (ABC/3TC+ LPV/r) (600/300+200/50)mg regimen", map(hivCohorts.onabc3tcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onRegimenazt3tc() {
			return  cohortIndicator("Patients having (AZT/3TC+ TDF+ LPV/r)(300/150+300+200/50)mg regimen", map(hivCohorts.onazt3tcplustdfpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimenabcatv() {
			return  cohortIndicator("Patients having (ABC/3TC/ATV/r) regimen", map(hivCohorts.onabc3tcatvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimenaztatv() {
			return  cohortIndicator("Patients having (AZT/3TC/ATV/r) regimen", map(hivCohorts.onazt3tcatvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		public CohortIndicator onregimentdfatvr() {
			return  cohortIndicator("Patients having (TDF/3TC/ATV/r) regimen", map(hivCohorts.ontdf3tcatvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}

		
		
}