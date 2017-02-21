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
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.openmrs.module.chaicore.report.ReportUtils.map;
import static org.openmrs.module.chaiemr.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Library of ART Drugs related indicator definitions. All indicators require parameters ${startDate} and ${endDate}
 */
@Component
public class ArtIndicatorLibrary {

	@Autowired
	private ArtCohortLibrary artCohorts;

	/**
	 * Number of patients who are eligible for ART
	 * @return the indicator
	 */
	public CohortIndicator eligibleForArt() {
		return cohortIndicator("patients eligible for ART", map(artCohorts.eligibleForArt(), "onDate=${endDate}"));
	}

	/**
	 * Number of patients who are on ART
	 * @return the indicator
	 */
	public CohortIndicator onArt() {
		return cohortIndicator("patients on ART", map(artCohorts.onArt(), "onDate=${endDate}"));
	}

	/**
	 * Number of patients who are on ART and pregnant
	 * @return the indicator
	 */
	public CohortIndicator onArtAndPregnant() {
		return cohortIndicator("patients on ART and pregnant", map(artCohorts.onArtAndPregnant(), "onDate=${endDate}"));
	}

	/**
	 * Number of patients who are on ART and pregnant
	 * @return the indicator
	 */
	public CohortIndicator onArtAndNotPregnant() {
		return cohortIndicator("patients on ART and not pregnant", map(artCohorts.onArtAndNotPregnant(), "onDate=${endDate}"));
	}

	/**
	 * Number of patients having the given ART regimen
	 * @return indicator
	 */
	public CohortIndicator onRegimen(List<Concept> regimen) {
		return cohortIndicator("", map(artCohorts.inHivProgramAndOnRegimen(regimen), "onDate=${endDate}"));
	}

	/**
	 * Number of patients who started ART
	 * @return the indicator
	 */
	public CohortIndicator startedArt() {
		return cohortIndicator("patients who started ART", map(artCohorts.startsART(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	/**
	 * Number of patients who started ART while pregnant
	 * @return the indicator
	 */
	public CohortIndicator startedArtWhilePregnant() {
		return cohortIndicator("patients who started ART while pregnant", map(artCohorts.startedArtWhilePregnant(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	/**
	 * Number of patients who started ART while being a TB patient
	 * @return the indicator
	 */
	public CohortIndicator startedArtWhileTbPatient() {
		return cohortIndicator("patients who started ART while being a TB patient", map(artCohorts.startedArtWhileTbPatient(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	/**
	 * Number of patients who started ART with given WHO stage
	 * @return the indicator
	 */
	public CohortIndicator startedArtWithWhoStage(int stage) {
		return cohortIndicator("patients who started ART with WHO stage " + stage, map(artCohorts.startedArtWithWhoStage(stage), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

	/**
	 * Number of patients who have ever started ART
	 * @return the indicator
	 */
	public CohortIndicator startedArtCumulative() { 
		return cohortIndicator("patients who have ever started ART", map(artCohorts.startedArtOnDate(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	
	public CohortIndicator startedArtCumulativeResult() { 
	return cohortIndicator("patients who have ever started ART", map(artCohorts.startedArtExcludingTransferinsOnDates(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
	
	public CohortIndicator onoriginal() { 
	return cohortIndicator("patients who have first line", map(artCohorts.onOriginalFirstLine(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
	public CohortIndicator onsubsitute() { 
	return cohortIndicator("patients who have subsitute line", map(artCohorts.onSubstitueFirstLine(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}    
	public CohortIndicator onswitchsecond() { 
	return cohortIndicator("patients who have switched to second line", map(artCohorts.onSwitchSecondLine(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
	public CohortIndicator onswitchthird() { 
		return cohortIndicator("patients who have switched to third line", map(artCohorts.onSwitchThirdLine(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}

    public CohortIndicator onregimenNVP(){
        return cohortIndicator("Patients having (AZT/3TC/NVP)(300/150/200)mg regimen", map(artCohorts.onAZT3TCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    public CohortIndicator onregimenEFV(){
        return cohortIndicator("Patients having (AZT/3TC/EFV) (300/150/600)mg regimen", map(artCohorts.onAZT3TCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    
    public CohortIndicator onregimenTDF(){
        return cohortIndicator("Patients having (TDF/3TC/NVP) (300/300/200)mg regimen", map(artCohorts.onTDF3TCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    
    public CohortIndicator onregimenTDFEFV(){
        return cohortIndicator("Patients having (TDF/3TC/EFV) (300/300/600)mg regimen", map(artCohorts.onTDF3TCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    
    public CohortIndicator onregimenTDFFTC(){
        return cohortIndicator("Patients having (TDF/FTC/NVP) (300/200/200)mg regimen", map(artCohorts.onTDFFTCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    public CohortIndicator onregimenTDFFTCEFV(){
        return cohortIndicator("Patients having (TDF/FTC/EFV) ((300/200/200)mg regimen", map(artCohorts.onTDFFTCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    public CohortIndicator onregimenTDFFTCE(){
        return cohortIndicator("Patients having (TDF/FTC/EFV) ((300/200/600)mg regimen", map(artCohorts.onTDFFTCEF(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    public CohortIndicator onregimendosenvp(){
        return cohortIndicator("Patients having (AZT/3TC+NVP) (60/30+50) mg regimen", map(artCohorts.onAZT3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    public CohortIndicator onregimendoseefvtwo() {
		return cohortIndicator("Patients having (AZT/3TC+EFV) (60/30+200) and (300/150+600)  mg regimen", map(artCohorts.onAZT3TCplusEFVtwo(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	    }
    public CohortIndicator onregimentdf3tcnvp() {
		return  cohortIndicator("Patients having (TDF/3TC+NVP) (300/300+200) mg regimen", map(artCohorts.onTdf3tcNvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    public  CohortIndicator onregimen3tcefvtwo() {
		
		return  cohortIndicator("Patients having (TDF/3TC+EFV) (300/300+400) mg regimen", map(artCohorts.onTdf3tcplusefvtwo(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	
	}
    public  CohortIndicator onregimen3tcefvsix() {
		
		return  cohortIndicator("Patients having (TDF/3TC+EFV) (300/300+600) mg regimen", map(artCohorts.onTdf3tcplusefvsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	
	}
    public  CohortIndicator onregimentdfftcnvp() {
		
		return  cohortIndicator("Patients having (TDF/FTC+NVP) (300/200+200) mg regimen", map(artCohorts.onTdfftcplusnvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	
	}
    public CohortIndicator onregimenftcefvfour() {
		return  cohortIndicator("Patients having (TDF/FTC+EFV) (300/200+400) mg regimen", map(artCohorts.onTdfftcplusefv(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    public CohortIndicator onregimenftcefvsix() {
		return  cohortIndicator("Patients having (TDF/FTC+EFV) (300/200+600) mg regimen", map(artCohorts.onTdfftcplusefvsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    public CohortIndicator onregimenDT(){
        return cohortIndicator("Patients having (d4T/3TC/NVP)  (30/150/200)mg regimen", map(artCohorts.onD4T3TCNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    public CohortIndicator onregimendt3tcnvp() {
		return  cohortIndicator("Patients having (d4T/3TC+NVP) (30/150+200) mg regimen", map(artCohorts.ond4t3tcplusnvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    public CohortIndicator onregimenDTEFV(){
        return cohortIndicator("Patients having (d4T/3TC/EFV) (30/150/600)mg regimen", map(artCohorts.onD4T3TCEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
    
    public CohortIndicator onregimendt3tcefv() {
		return  cohortIndicator("Patients having (d4T/3TC+EFV) (30/150+600) mg regimen", map(artCohorts.onD4t3tcplusefvsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    public CohortIndicator  onregimenftcdtg() {
		return  cohortIndicator("Patients having (TDF/FTC/DTG) regimen", map(artCohorts.onTdfFtcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    public CohortIndicator  onregimen3tcdtg() {
		return  cohortIndicator("Patients having (TDF/3TC/DTG) regimen", map(artCohorts.onTdf3tcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    
    public CohortIndicator onregimenabcefv() {
		return  cohortIndicator("Patients having (ABC/3TC + EFV) (600/300+600) mg regimen", map(artCohorts.onabc3tcplusefv(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    public CohortIndicator  onregimenabc3tcdtg() {
		return  cohortIndicator("Patients having (ABC/3TC/DTG) regimen", map(artCohorts.onAbc3tcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	public CohortIndicator  onregimen3tcnvp() {
		return  cohortIndicator("Patients having (ABC/3TC/NVP) regimen", map(artCohorts.onAbc3tcNvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	public CohortIndicator  onregimenftcefv() {
		return  cohortIndicator("Patients having (ABC/FTC/EFV) regimen", map(artCohorts.onAbcFtcEfv(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	public CohortIndicator  onregimenabcftcdtg() {
		return  cohortIndicator("Patients having (ABC/FTC/DTG) regimen", map(artCohorts.onAbcftcDtg(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    
public CohortIndicator  onregimenftcnvp() {
		return  cohortIndicator("Patients having (ABC/FTC/NVP) regimen", map(artCohorts.onAbcftcNvp(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
    
public CohortIndicator onregimenAzt(){
          return cohortIndicator("Patients having (AZT/3TC/LPV/r) (300/150/200/50)mg regimen", map(artCohorts.onAZT3TCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
      }
public CohortIndicator onregimendoselpvr(){
          return cohortIndicator("Patients having (AZT/3TC+LPV/r)(60/30+100/25) mg regimen", map(artCohorts.onAZT3TCplusLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
      }
public CohortIndicator onregimenTdf(){
        return cohortIndicator("Patients having (TDF/3TC/LPV/r) (300/300/200/50)mg regimen", map(artCohorts.onTDF3TCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
public CohortIndicator onregimentdf3tc() {
		return  cohortIndicator("Patients having (TDF/3TC+LPV/r) (300/300+200/50) mg regimen", map(artCohorts.ontdf3tcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
public CohortIndicator onregimenTdfftc(){
          return cohortIndicator("Patients having  (TDF/FTC/LPV/r) (300/200/200/50)mg regimen", map(artCohorts.onTDFFTCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
      }
	  
public CohortIndicator onregimentdfftc() {
			return  cohortIndicator("Patients having (TDF/FTC+ LPV/r) (300/200+200/50) mg regimen", map(artCohorts.ontdfftcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}
public CohortIndicator onregimenTdfabc(){
          return cohortIndicator("Patients having (TDF/ABC/LPV/r) (300/300/200/50)mg regimen", map(artCohorts.onTDFABCLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
      }
public CohortIndicator onregimentdf() {
			return  cohortIndicator("Patients having (TDF+ABC+ LPV/r )(300/300+200/50)mg mg regimen", map(artCohorts.ontdfabcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}
public CohortIndicator onregimenabcdoselpvr() {
			return  cohortIndicator("Patients having (ABC/3TC+ LPV/r) (600/300+200/50)mg regimen", map(artCohorts.onabc3tcpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}
public CohortIndicator onregimenabcatv() {
			return  cohortIndicator("Patients having (ABC/3TC/ATV/r) regimen", map(artCohorts.onabc3tcatvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}
public CohortIndicator onregimenaztatv() {
			return  cohortIndicator("Patients having (AZT/3TC/ATV/r) regimen", map(artCohorts.onazt3tcatvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}
public CohortIndicator onregimentdfatvr() {
			return  cohortIndicator("Patients having (TDF/3TC/ATV/r) regimen", map(artCohorts.ontdf3tcatvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
		}
public CohortIndicator onRegimenazt3tc() {
	return  cohortIndicator("Patients having (AZT/3TC+ TDF+ LPV/r)(300/150+300+200/50)mg regimen", map(artCohorts.onazt3tcplustdfpluslpvr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}

public CohortIndicator onregimendoseefvsix() {
	return  cohortIndicator("Patients having (AZT/3TC+EFV) (60/30+600) mg regimen", map(artCohorts.onAZT3TCplusEFVsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
     }
public CohortIndicator onregimendoseabcnvp() {
	return  cohortIndicator("Patients having (ABC/3TC+NVP) (60/30+50) mg regimen", map(artCohorts.onABC3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
public CohortIndicator onregimendoseabcefvtwo() {
	  return  cohortIndicator("Patients having (ABC/3TC+EFV) (60/30+200)  mg regimen", map(artCohorts.onABC3TCplusEFVtwo(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
  }
public CohortIndicator onregimendoseabcefvsix() {
	return  cohortIndicator("Patients having (ABC/3TC+EFV) (60/30+600) mg regimen", map(artCohorts.onABC3TCplusEFVsix(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
public CohortIndicator onregimendoseabc() {
	return  cohortIndicator("Patients having (AZT/3TC+ABC) (60/30+60) mg regimen", map(artCohorts.onAZT3TCplusABC(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
    }
public CohortIndicator onregimenazt(){
    return cohortIndicator("Patients having (d4T+3TC+NVP) regimen", map(artCohorts.onD4T3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimend4t(){
    return cohortIndicator("Patients having (d4T+3TC+L/r)  regimen", map(artCohorts.onD4T3TCLr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}

public CohortIndicator onregimend4tefv(){
    return cohortIndicator("Patients having (d4T+3TC+EFV)  regimen", map(artCohorts.onD4T3TCplusEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimend4tabc(){
    return cohortIndicator("Patients having (d4T+3TC+ABC)  regimen", map(artCohorts.onD4T3TCplusABC(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimenaztral(){
    return cohortIndicator("Patients having (AZT+3TC+RAL)  regimen", map(artCohorts.onAZT3TCplusRAL(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimenatv(){
    return cohortIndicator("Patients having (AZT+3TC+ATV/r)  regimen", map(artCohorts.onAZT3TCplusATVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimenral(){
    return cohortIndicator("Patients having (ABC+3TC+RAL)  regimen", map(artCohorts.onABC3TCplusRAL(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimenefv(){
    return cohortIndicator("Patients having (TDF+3TC+EFV)  regimen", map(artCohorts.onTDF3TCplusEFV(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimennvp(){
    return cohortIndicator("Patients having (TDF+3TC+NVP)  regimen", map(artCohorts.onTDF3TCplusNVP(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimenlpv(){
    return cohortIndicator("Patients having (TDF+3TC+LPV/r)  regimen", map(artCohorts.onTDF3TCplusLPVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimentdfral(){
    return cohortIndicator("Patients having (TDF+3TC+RAL)  regimen", map(artCohorts.onTDF3TCplusRAL(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimentdfatv(){
    return cohortIndicator("Patients having (TDF+3TC+ATV/r)  regimen", map(artCohorts.onTDF3TCplusATVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
public CohortIndicator onregimenabc(){
    return cohortIndicator("Patients having (ABC+3TC+ATV/r)  regimen", map(artCohorts.onABC3TCplusATVr(), "onOrAfter=${startDate},onOrBefore=${endDate}"));
}
}