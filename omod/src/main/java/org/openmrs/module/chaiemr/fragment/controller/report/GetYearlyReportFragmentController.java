package org.openmrs.module.chaiemr.fragment.controller.report;

import java.util.List;
import java.util.Set;

import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.chaiemr.api.ChaiEmrService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class GetYearlyReportFragmentController {
	public void controller(@RequestParam("year") String year,
			FragmentModel model, UiUtils ui) {
    ChaiEmrService chaiEmrService = (ChaiEmrService) Context.getService(ChaiEmrService.class);
	Program program=Context.getProgramWorkflowService().getProgramByUuid("96ec813f-aaf0-45b2-add6-e661d5bf79d6");
	
	if(year!=null){
		String janStartDate=year+"-"+"01"+"-"+"01";
		String janEndDate=year+"-"+"01"+"-"+"31";
		
		String febStartDate=year+"-"+"02"+"-"+"01";
		String febEndDate=year+"-"+"02"+"-"+"28";
		
		String marchStartDate=year+"-"+"03"+"-"+"01";
		String marchEndDate=year+"-"+"03"+"-"+"31";
		
		String aprilStartDate=year+"-"+"04"+"-"+"01";
		String aprilEndDate=year+"-"+"04"+"-"+"30";
		
		String mayStartDate=year+"-"+"05"+"-"+"01";
		String mayEndDate=year+"-"+"05"+"-"+"31";
		
		String juneStartDate=year+"-"+"06"+"-"+"01";
		String juneEndDate=year+"-"+"06"+"-"+"30";
		
		String julyStartDate=year+"-"+"07"+"-"+"01";
		String julyEndDate=year+"-"+"07"+"-"+"31";
		
		String augustStartDate=year+"-"+"08"+"-"+"01";
		String augustEndDate=year+"-"+"08"+"-"+"31";
		
		String septemberStartDate=year+"-"+"09"+"-"+"01";
		String septemberEndDate=year+"-"+"09"+"-"+"30";
		
		String octoberStartDate=year+"-"+"10"+"-"+"01";
		String octoberEndDate=year+"-"+"10"+"-"+"31";
		
		String novemberStartDate=year+"-"+"11"+"-"+"01";
		String novemberEndDate=year+"-"+"11"+"-"+"30";
		
		String decemberStartDate=year+"-"+"12"+"-"+"01";
		String decemberEndDate=year+"-"+"12"+"-"+"31";
		
		Set<Patient> patientProgramForJan=chaiEmrService.getPatientProgram(program,janStartDate,janEndDate);
		Set<Patient> patientProgramForFeb=chaiEmrService.getPatientProgram(program,febStartDate,febEndDate);
		Set<Patient> patientProgramForMarch=chaiEmrService.getPatientProgram(program,marchStartDate,marchEndDate);
		Set<Patient> patientProgramForApril=chaiEmrService.getPatientProgram(program,aprilStartDate,aprilEndDate);
		Set<Patient> patientProgramForMay=chaiEmrService.getPatientProgram(program,mayStartDate,mayEndDate);
		Set<Patient> patientProgramForJune=chaiEmrService.getPatientProgram(program,juneStartDate,juneEndDate);
		Set<Patient> patientProgramForJuly=chaiEmrService.getPatientProgram(program,julyStartDate,julyEndDate);
		Set<Patient> patientProgramForAugust=chaiEmrService.getPatientProgram(program,augustStartDate,augustEndDate);
		Set<Patient> patientProgramForSeptember=chaiEmrService.getPatientProgram(program,septemberStartDate,septemberEndDate);
		Set<Patient> patientProgramForOctober=chaiEmrService.getPatientProgram(program,octoberStartDate,octoberEndDate);
		Set<Patient> patientProgramForNovember=chaiEmrService.getPatientProgram(program,novemberStartDate,novemberEndDate);
		Set<Patient> patientProgramForDecember=chaiEmrService.getPatientProgram(program,decemberStartDate,decemberEndDate);
		
		Set<Patient> patientTransferInForJan=chaiEmrService.getNoOfPatientTransferredIn(janStartDate,janEndDate);
		Set<Patient> patientTransferInForFeb=chaiEmrService.getNoOfPatientTransferredIn(febStartDate,febEndDate);
		Set<Patient> patientTransferInForMarch=chaiEmrService.getNoOfPatientTransferredIn(marchStartDate,marchEndDate);
		Set<Patient> patientTransferInForApril=chaiEmrService.getNoOfPatientTransferredIn(aprilStartDate,aprilEndDate);
		Set<Patient> patientTransferInForMay=chaiEmrService.getNoOfPatientTransferredIn(mayStartDate,mayEndDate);
		Set<Patient> patientTransferInForJune=chaiEmrService.getNoOfPatientTransferredIn(juneStartDate,juneEndDate);
		Set<Patient> patientTransferInForJuly=chaiEmrService.getNoOfPatientTransferredIn(julyStartDate,julyEndDate);
		Set<Patient> patientTransferInForAugust=chaiEmrService.getNoOfPatientTransferredIn(augustStartDate,augustEndDate);
		Set<Patient> patientTransferInForSeptember=chaiEmrService.getNoOfPatientTransferredIn(septemberStartDate,septemberEndDate);
		Set<Patient> patientTransferInForOctober=chaiEmrService.getNoOfPatientTransferredIn(octoberStartDate,octoberEndDate);
		Set<Patient> patientTransferInForNovember=chaiEmrService.getNoOfPatientTransferredIn(novemberStartDate,novemberEndDate);
		Set<Patient> patientTransferInForDecember=chaiEmrService.getNoOfPatientTransferredIn(decemberStartDate,decemberEndDate);
		
		Set<Patient> patientTransferOutForJan=chaiEmrService.getNoOfPatientTransferredOut(janStartDate,janEndDate);
		Set<Patient> patientTransferOutForFeb=chaiEmrService.getNoOfPatientTransferredOut(febStartDate,febEndDate);
		Set<Patient> patientTransferOutForMarch=chaiEmrService.getNoOfPatientTransferredOut(marchStartDate,marchEndDate);
		Set<Patient> patientTransferOutForApril=chaiEmrService.getNoOfPatientTransferredOut(aprilStartDate,aprilEndDate);
		Set<Patient> patientTransferOutForMay=chaiEmrService.getNoOfPatientTransferredOut(mayStartDate,mayEndDate);
		Set<Patient> patientTransferOutForJune=chaiEmrService.getNoOfPatientTransferredOut(juneStartDate,juneEndDate);
		Set<Patient> patientTransferOutForJuly=chaiEmrService.getNoOfPatientTransferredOut(julyStartDate,julyEndDate);
		Set<Patient> patientTransferOutForAugust=chaiEmrService.getNoOfPatientTransferredOut(augustStartDate,augustEndDate);
		Set<Patient> patientTransferOutForSeptember=chaiEmrService.getNoOfPatientTransferredOut(septemberStartDate,septemberEndDate);
		Set<Patient> patientTransferOutForOctober=chaiEmrService.getNoOfPatientTransferredOut(octoberStartDate,octoberEndDate);
		Set<Patient> patientTransferOutForNovember=chaiEmrService.getNoOfPatientTransferredOut(novemberStartDate,novemberEndDate);
		Set<Patient> patientTransferOutForDecember=chaiEmrService.getNoOfPatientTransferredOut(decemberStartDate,decemberEndDate);
		
		Set<Patient> totalCohortForJan=chaiEmrService.getTotalNoOfCohort(janStartDate, janEndDate);
		Set<Patient> totalCohortForFeb=chaiEmrService.getTotalNoOfCohort(febStartDate,febEndDate);
		Set<Patient> totalCohortForMarch=chaiEmrService.getTotalNoOfCohort(marchStartDate,marchEndDate);
		Set<Patient> totalCohortForApril=chaiEmrService.getTotalNoOfCohort(aprilStartDate,aprilEndDate);
		Set<Patient> totalCohortForMay=chaiEmrService.getTotalNoOfCohort(mayStartDate,mayEndDate);
		Set<Patient> totalCohortForJune=chaiEmrService.getTotalNoOfCohort(juneStartDate,juneEndDate);
		Set<Patient> totalCohortForJuly=chaiEmrService.getTotalNoOfCohort(julyStartDate,julyEndDate);
		Set<Patient> totalCohortForAugust=chaiEmrService.getTotalNoOfCohort(augustStartDate,augustEndDate);
		Set<Patient> totalCohortForSeptember=chaiEmrService.getTotalNoOfCohort(septemberStartDate,septemberEndDate);
		Set<Patient> totalCohortForOctober=chaiEmrService.getTotalNoOfCohort(octoberStartDate,octoberEndDate);
		Set<Patient> totalCohortForNovember=chaiEmrService.getTotalNoOfCohort(novemberStartDate,novemberEndDate);
		Set<Patient> totalCohortForDecember=chaiEmrService.getTotalNoOfCohort(decemberStartDate,decemberEndDate);
		
		Set<Patient> maleCohortForJan=chaiEmrService.getCohortBasedOnGender("M",janStartDate, janEndDate);
		Set<Patient> maleCohortForFeb=chaiEmrService.getCohortBasedOnGender("M",febStartDate,febEndDate);
		Set<Patient> maleCohortForMarch=chaiEmrService.getCohortBasedOnGender("M",marchStartDate,marchEndDate);
		Set<Patient> maleCohortForApril=chaiEmrService.getCohortBasedOnGender("M",aprilStartDate,aprilEndDate);
		Set<Patient> maleCohortForMay=chaiEmrService.getCohortBasedOnGender("M",mayStartDate,mayEndDate);
		Set<Patient> maleCohortForJune=chaiEmrService.getCohortBasedOnGender("M",juneStartDate,juneEndDate);
		Set<Patient> maleCohortForJuly=chaiEmrService.getCohortBasedOnGender("M",julyStartDate,julyEndDate);
		Set<Patient> maleCohortForAugust=chaiEmrService.getCohortBasedOnGender("M",augustStartDate,augustEndDate);
		Set<Patient> maleCohortForSeptember=chaiEmrService.getCohortBasedOnGender("M",septemberStartDate,septemberEndDate);
		Set<Patient> maleCohortForOctober=chaiEmrService.getCohortBasedOnGender("M",octoberStartDate,octoberEndDate);
		Set<Patient> maleCohortForNovember=chaiEmrService.getCohortBasedOnGender("M",novemberStartDate,novemberEndDate);
		Set<Patient> maleCohortForDecember=chaiEmrService.getCohortBasedOnGender("M",decemberStartDate,decemberEndDate);
		
		Set<Patient> femaleCohortForJan=chaiEmrService.getCohortBasedOnGender("F",janStartDate, janEndDate);
		Set<Patient> femaleCohortForFeb=chaiEmrService.getCohortBasedOnGender("F",febStartDate,febEndDate);
		Set<Patient> femaleCohortForMarch=chaiEmrService.getCohortBasedOnGender("F",marchStartDate,marchEndDate);
		Set<Patient> femaleCohortForApril=chaiEmrService.getCohortBasedOnGender("F",aprilStartDate,aprilEndDate);
		Set<Patient> femaleCohortForMay=chaiEmrService.getCohortBasedOnGender("F",mayStartDate,mayEndDate);
		Set<Patient> femaleCohortForJune=chaiEmrService.getCohortBasedOnGender("F",juneStartDate,juneEndDate);
		Set<Patient> femaleCohortForJuly=chaiEmrService.getCohortBasedOnGender("F",julyStartDate,julyEndDate);
		Set<Patient> femaleCohortForAugust=chaiEmrService.getCohortBasedOnGender("F",augustStartDate,augustEndDate);
		Set<Patient> femaleCohortForSeptember=chaiEmrService.getCohortBasedOnGender("F",septemberStartDate,septemberEndDate);
		Set<Patient> femaleCohortForOctober=chaiEmrService.getCohortBasedOnGender("F",octoberStartDate,octoberEndDate);
		Set<Patient> femaleCohortForNovember=chaiEmrService.getCohortBasedOnGender("F",novemberStartDate,novemberEndDate);
		Set<Patient> femaleCohortForDecember=chaiEmrService.getCohortBasedOnGender("F",decemberStartDate,decemberEndDate);
		
		Integer age1=0;
		Integer age2=14;
		Set<Patient> cohortFor0_14AgeForJan=chaiEmrService.getCohortBasedOnAge(age1,age2,janStartDate, janEndDate);
		Set<Patient> cohortFor0_14AgeForFeb=chaiEmrService.getCohortBasedOnAge(age1,age2,febStartDate,febEndDate);
		Set<Patient> cohortFor0_14AgeForMarch=chaiEmrService.getCohortBasedOnAge(age1,age2,marchStartDate,marchEndDate);
		Set<Patient> cohortFor0_14AgeForApril=chaiEmrService.getCohortBasedOnAge(age1,age2,aprilStartDate,aprilEndDate);
		Set<Patient> cohortFor0_14AgeForMay=chaiEmrService.getCohortBasedOnAge(age1,age2,mayStartDate,mayEndDate);
		Set<Patient> cohortFor0_14AgeForJune=chaiEmrService.getCohortBasedOnAge(age1,age2,juneStartDate,juneEndDate);
		Set<Patient> cohortFor0_14AgeForJuly=chaiEmrService.getCohortBasedOnAge(age1,age2,julyStartDate,julyEndDate);
		Set<Patient> cohortFor0_14AgeForAugust=chaiEmrService.getCohortBasedOnAge(age1,age2,augustStartDate,augustEndDate);
		Set<Patient> cohortFor0_14AgeForSeptember=chaiEmrService.getCohortBasedOnAge(age1,age2,septemberStartDate,septemberEndDate);
		Set<Patient> cohortFor0_14AgeForOctober=chaiEmrService.getCohortBasedOnAge(age1,age2,octoberStartDate,octoberEndDate);
		Set<Patient> cohortFor0_14AgeForNovember=chaiEmrService.getCohortBasedOnAge(age1,age2,novemberStartDate,novemberEndDate);
		Set<Patient> cohortFor0_14AgeForDecember=chaiEmrService.getCohortBasedOnAge(age1,age2,decemberStartDate,decemberEndDate);
		
		age1=15;
		age2=24;
		Set<Patient> cohortFor15_24AgeForJan=chaiEmrService.getCohortBasedOnAge(age1,age2,janStartDate, janEndDate);
		Set<Patient> cohortFor15_24AgeForFeb=chaiEmrService.getCohortBasedOnAge(age1,age2,febStartDate,febEndDate);
		Set<Patient> cohortFor15_24AgeForMarch=chaiEmrService.getCohortBasedOnAge(age1,age2,marchStartDate,marchEndDate);
		Set<Patient> cohortFor15_24AgeForApril=chaiEmrService.getCohortBasedOnAge(age1,age2,aprilStartDate,aprilEndDate);
		Set<Patient> cohortFor15_24AgeForMay=chaiEmrService.getCohortBasedOnAge(age1,age2,mayStartDate,mayEndDate);
		Set<Patient> cohortFor15_24AgeForJune=chaiEmrService.getCohortBasedOnAge(age1,age2,juneStartDate,juneEndDate);
		Set<Patient> cohortFor15_24AgeForJuly=chaiEmrService.getCohortBasedOnAge(age1,age2,julyStartDate,julyEndDate);
		Set<Patient> cohortFor15_24AgeForAugust=chaiEmrService.getCohortBasedOnAge(age1,age2,augustStartDate,augustEndDate);
		Set<Patient> cohortFor15_24AgeForSeptember=chaiEmrService.getCohortBasedOnAge(age1,age2,septemberStartDate,septemberEndDate);
		Set<Patient> cohortFor15_24AgeForOctober=chaiEmrService.getCohortBasedOnAge(age1,age2,octoberStartDate,octoberEndDate);
		Set<Patient> cohortFor15_24AgeForNovember=chaiEmrService.getCohortBasedOnAge(age1,age2,novemberStartDate,novemberEndDate);
		Set<Patient> cohortFor15_24AgeForDecember=chaiEmrService.getCohortBasedOnAge(age1,age2,decemberStartDate,decemberEndDate);
		
		age1=25;
		age2=60;
		Set<Patient> cohortFor25_60AgeForJan=chaiEmrService.getCohortBasedOnAge(age1,age2,janStartDate, janEndDate);
		Set<Patient> cohortFor25_60AgeForFeb=chaiEmrService.getCohortBasedOnAge(age1,age2,febStartDate,febEndDate);
		Set<Patient> cohortFor25_60AgeForMarch=chaiEmrService.getCohortBasedOnAge(age1,age2,marchStartDate,marchEndDate);
		Set<Patient> cohortFor25_60AgeForApril=chaiEmrService.getCohortBasedOnAge(age1,age2,aprilStartDate,aprilEndDate);
		Set<Patient> cohortFor25_60AgeForMay=chaiEmrService.getCohortBasedOnAge(age1,age2,mayStartDate,mayEndDate);
		Set<Patient> cohortFor25_60AgeForJune=chaiEmrService.getCohortBasedOnAge(age1,age2,juneStartDate,juneEndDate);
		Set<Patient> cohortFor25_60AgeForJuly=chaiEmrService.getCohortBasedOnAge(age1,age2,julyStartDate,julyEndDate);
		Set<Patient> cohortFor25_60AgeForAugust=chaiEmrService.getCohortBasedOnAge(age1,age2,augustStartDate,augustEndDate);
		Set<Patient> cohortFor25_60AgeForSeptember=chaiEmrService.getCohortBasedOnAge(age1,age2,septemberStartDate,septemberEndDate);
		Set<Patient> cohortFor25_60AgeForOctober=chaiEmrService.getCohortBasedOnAge(age1,age2,octoberStartDate,octoberEndDate);
		Set<Patient> cohortFor25_60AgeForNovember=chaiEmrService.getCohortBasedOnAge(age1,age2,novemberStartDate,novemberEndDate);
		Set<Patient> cohortFor25_60AgeForDecember=chaiEmrService.getCohortBasedOnAge(age1,age2,decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfCohortAliveAndOnArtForJan=chaiEmrService.getNoOfCohortAliveAndOnArt(program,janStartDate,janEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForFeb=chaiEmrService.getNoOfCohortAliveAndOnArt(program,febStartDate,febEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForMarch=chaiEmrService.getNoOfCohortAliveAndOnArt(program,marchStartDate,marchEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForApril=chaiEmrService.getNoOfCohortAliveAndOnArt(program,aprilStartDate,aprilEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForMay=chaiEmrService.getNoOfCohortAliveAndOnArt(program,mayStartDate,mayEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForJune=chaiEmrService.getNoOfCohortAliveAndOnArt(program,juneStartDate,juneEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForJuly=chaiEmrService.getNoOfCohortAliveAndOnArt(program,julyStartDate,julyEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForAugust=chaiEmrService.getNoOfCohortAliveAndOnArt(program,augustStartDate,augustEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForSeptember=chaiEmrService.getNoOfCohortAliveAndOnArt(program,septemberStartDate,septemberEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForOctober=chaiEmrService.getNoOfCohortAliveAndOnArt(program,octoberStartDate,octoberEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForNovember=chaiEmrService.getNoOfCohortAliveAndOnArt(program,novemberStartDate,novemberEndDate);
		Set<Patient> noOfCohortAliveAndOnArtForDecember=chaiEmrService.getNoOfCohortAliveAndOnArt(program,decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfOriginalFirstLineRegimenForJan=chaiEmrService.getOriginalFirstLineRegimen(program,janStartDate,janEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForFeb=chaiEmrService.getOriginalFirstLineRegimen(program,febStartDate,febEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForMarch=chaiEmrService.getOriginalFirstLineRegimen(program,marchStartDate,marchEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForApril=chaiEmrService.getOriginalFirstLineRegimen(program,aprilStartDate,aprilEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForMay=chaiEmrService.getOriginalFirstLineRegimen(program,mayStartDate,mayEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForJune=chaiEmrService.getOriginalFirstLineRegimen(program,juneStartDate,juneEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForJuly=chaiEmrService.getOriginalFirstLineRegimen(program,julyStartDate,julyEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForAugust=chaiEmrService.getOriginalFirstLineRegimen(program,augustStartDate,augustEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForSeptember=chaiEmrService.getOriginalFirstLineRegimen(program,septemberStartDate,septemberEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForOctober=chaiEmrService.getOriginalFirstLineRegimen(program,octoberStartDate,octoberEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForNovember=chaiEmrService.getOriginalFirstLineRegimen(program,novemberStartDate,novemberEndDate);
		Set<Patient> noOfOriginalFirstLineRegimenForDecember=chaiEmrService.getOriginalFirstLineRegimen(program,decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfAlternateFirstLineRegimenForJan=chaiEmrService.getAlternateFirstLineRegimen(program,janStartDate,janEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForFeb=chaiEmrService.getAlternateFirstLineRegimen(program,febStartDate,febEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForMarch=chaiEmrService.getAlternateFirstLineRegimen(program,marchStartDate,marchEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForApril=chaiEmrService.getAlternateFirstLineRegimen(program,aprilStartDate,aprilEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForMay=chaiEmrService.getAlternateFirstLineRegimen(program,mayStartDate,mayEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForJune=chaiEmrService.getAlternateFirstLineRegimen(program,juneStartDate,juneEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForJuly=chaiEmrService.getAlternateFirstLineRegimen(program,julyStartDate,julyEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForAugust=chaiEmrService.getAlternateFirstLineRegimen(program,augustStartDate,augustEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForSeptember=chaiEmrService.getAlternateFirstLineRegimen(program,septemberStartDate,septemberEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForOctober=chaiEmrService.getAlternateFirstLineRegimen(program,octoberStartDate,octoberEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForNovember=chaiEmrService.getAlternateFirstLineRegimen(program,novemberStartDate,novemberEndDate);
		Set<Patient> noOfAlternateFirstLineRegimenForDecember=chaiEmrService.getAlternateFirstLineRegimen(program,decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfSecondLineRegimenForJan=chaiEmrService.getSecondLineRegimen(program,janStartDate,janEndDate);
		Set<Patient> noOfSecondLineRegimenForFeb=chaiEmrService.getSecondLineRegimen(program,febStartDate,febEndDate);
		Set<Patient> noOfSecondLineRegimenForMarch=chaiEmrService.getSecondLineRegimen(program,marchStartDate,marchEndDate);
		Set<Patient> noOfSecondLineRegimenForApril=chaiEmrService.getSecondLineRegimen(program,aprilStartDate,aprilEndDate);
		Set<Patient> noOfSecondLineRegimenForMay=chaiEmrService.getSecondLineRegimen(program,mayStartDate,mayEndDate);
		Set<Patient> noOfSecondLineRegimenForJune=chaiEmrService.getSecondLineRegimen(program,juneStartDate,juneEndDate);
		Set<Patient> noOfSecondLineRegimenForJuly=chaiEmrService.getSecondLineRegimen(program,julyStartDate,julyEndDate);
		Set<Patient> noOfSecondLineRegimenForAugust=chaiEmrService.getSecondLineRegimen(program,augustStartDate,augustEndDate);
		Set<Patient> noOfSecondLineRegimenForSeptember=chaiEmrService.getSecondLineRegimen(program,septemberStartDate,septemberEndDate);
		Set<Patient> noOfSecondLineRegimenForOctober=chaiEmrService.getSecondLineRegimen(program,octoberStartDate,octoberEndDate);
		Set<Patient> noOfSecondLineRegimenForNovember=chaiEmrService.getSecondLineRegimen(program,novemberStartDate,novemberEndDate);
		Set<Patient> noOfSecondLineRegimenForDecember=chaiEmrService.getSecondLineRegimen(program,decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfArtStoppedCohortForJan=chaiEmrService.getNoOfArtStoppedCohort(program,janStartDate,janEndDate);
		Set<Patient> noOfArtStoppedCohortForFeb=chaiEmrService.getNoOfArtStoppedCohort(program,febStartDate,febEndDate);
		Set<Patient> noOfArtStoppedCohortForMarch=chaiEmrService.getNoOfArtStoppedCohort(program,marchStartDate,marchEndDate);
		Set<Patient> noOfArtStoppedCohortForApril=chaiEmrService.getNoOfArtStoppedCohort(program,aprilStartDate,aprilEndDate);
		Set<Patient> noOfArtStoppedCohortForMay=chaiEmrService.getNoOfArtStoppedCohort(program,mayStartDate,mayEndDate);
		Set<Patient> noOfArtStoppedCohortForJune=chaiEmrService.getNoOfArtStoppedCohort(program,juneStartDate,juneEndDate);
		Set<Patient> noOfArtStoppedCohortForJuly=chaiEmrService.getNoOfArtStoppedCohort(program,julyStartDate,julyEndDate);
		Set<Patient> noOfArtStoppedCohortForAugust=chaiEmrService.getNoOfArtStoppedCohort(program,augustStartDate,augustEndDate);
		Set<Patient> noOfArtStoppedCohortForSeptember=chaiEmrService.getNoOfArtStoppedCohort(program,septemberStartDate,septemberEndDate);
		Set<Patient> noOfArtStoppedCohortForOctober=chaiEmrService.getNoOfArtStoppedCohort(program,octoberStartDate,octoberEndDate);
		Set<Patient> noOfArtStoppedCohortForNovember=chaiEmrService.getNoOfArtStoppedCohort(program,novemberStartDate,novemberEndDate);
		Set<Patient> noOfArtStoppedCohortForDecember=chaiEmrService.getNoOfArtStoppedCohort(program,decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfArtDiedCohortForJan=chaiEmrService.getNoOfArtDiedCohort(program,janStartDate,janEndDate);
		Set<Patient> noOfArtDiedCohortForFeb=chaiEmrService.getNoOfArtDiedCohort(program,febStartDate,febEndDate);
		Set<Patient> noOfArtDiedCohortForMarch=chaiEmrService.getNoOfArtDiedCohort(program,marchStartDate,marchEndDate);
		Set<Patient> noOfArtDiedCohortForApril=chaiEmrService.getNoOfArtDiedCohort(program,aprilStartDate,aprilEndDate);
		Set<Patient> noOfArtDiedCohortForMay=chaiEmrService.getNoOfArtDiedCohort(program,mayStartDate,mayEndDate);
		Set<Patient> noOfArtDiedCohortForJune=chaiEmrService.getNoOfArtDiedCohort(program,juneStartDate,juneEndDate);
		Set<Patient> noOfArtDiedCohortForJuly=chaiEmrService.getNoOfArtDiedCohort(program,julyStartDate,julyEndDate);
		Set<Patient> noOfArtDiedCohortForAugust=chaiEmrService.getNoOfArtDiedCohort(program,augustStartDate,augustEndDate);
		Set<Patient> noOfArtDiedCohortForSeptember=chaiEmrService.getNoOfArtDiedCohort(program,septemberStartDate,septemberEndDate);
		Set<Patient> noOfArtDiedCohortForOctober=chaiEmrService.getNoOfArtDiedCohort(program,octoberStartDate,octoberEndDate);
		Set<Patient> noOfArtDiedCohortForNovember=chaiEmrService.getNoOfArtDiedCohort(program,novemberStartDate,novemberEndDate);
		Set<Patient> noOfArtDiedCohortForDecember=chaiEmrService.getNoOfArtDiedCohort(program,decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfPatientLostToFollowUpForJan=chaiEmrService.getNoOfPatientLostToFollowUp(janStartDate,janEndDate);
		Set<Patient> noOfPatientLostToFollowUpForFeb=chaiEmrService.getNoOfPatientLostToFollowUp(febStartDate,febEndDate);
		Set<Patient> noOfPatientLostToFollowUpForMarch=chaiEmrService.getNoOfPatientLostToFollowUp(marchStartDate,marchEndDate);
		Set<Patient> noOfPatientLostToFollowUpForApril=chaiEmrService.getNoOfPatientLostToFollowUp(aprilStartDate,aprilEndDate);
		Set<Patient> noOfPatientLostToFollowUpForMay=chaiEmrService.getNoOfPatientLostToFollowUp(mayStartDate,mayEndDate);
		Set<Patient> noOfPatientLostToFollowUpForJune=chaiEmrService.getNoOfPatientLostToFollowUp(juneStartDate,juneEndDate);
		Set<Patient> noOfPatientLostToFollowUpForJuly=chaiEmrService.getNoOfPatientLostToFollowUp(julyStartDate,julyEndDate);
		Set<Patient> noOfPatientLostToFollowUpForAugust=chaiEmrService.getNoOfPatientLostToFollowUp(augustStartDate,augustEndDate);
		Set<Patient> noOfPatientLostToFollowUpForSeptember=chaiEmrService.getNoOfPatientLostToFollowUp(septemberStartDate,septemberEndDate);
		Set<Patient> noOfPatientLostToFollowUpForOctober=chaiEmrService.getNoOfPatientLostToFollowUp(octoberStartDate,octoberEndDate);
		Set<Patient> noOfPatientLostToFollowUpForNovember=chaiEmrService.getNoOfPatientLostToFollowUp(novemberStartDate,novemberEndDate);
		Set<Patient> noOfPatientLostToFollowUpForDecember=chaiEmrService.getNoOfPatientLostToFollowUp(decemberStartDate,decemberEndDate);

		List<Obs> noOfPatientWithCD4ForJan=chaiEmrService.getNoOfPatientWithCD4(janStartDate,janEndDate);
		List<Obs> noOfPatientWithCD4ForFeb=chaiEmrService.getNoOfPatientWithCD4(febStartDate,febEndDate);
		List<Obs> noOfPatientWithCD4ForMarch=chaiEmrService.getNoOfPatientWithCD4(marchStartDate,marchEndDate);
		List<Obs> noOfPatientWithCD4ForApril=chaiEmrService.getNoOfPatientWithCD4(aprilStartDate,aprilEndDate);
		List<Obs> noOfPatientWithCD4ForMay=chaiEmrService.getNoOfPatientWithCD4(mayStartDate,mayEndDate);
		List<Obs> noOfPatientWithCD4ForJune=chaiEmrService.getNoOfPatientWithCD4(juneStartDate,juneEndDate);
		List<Obs> noOfPatientWithCD4ForJuly=chaiEmrService.getNoOfPatientWithCD4(julyStartDate,julyEndDate);
		List<Obs> noOfPatientWithCD4ForAugust=chaiEmrService.getNoOfPatientWithCD4(augustStartDate,augustEndDate);
		List<Obs> noOfPatientWithCD4ForSeptember=chaiEmrService.getNoOfPatientWithCD4(septemberStartDate,septemberEndDate);
		List<Obs> noOfPatientWithCD4ForOctober=chaiEmrService.getNoOfPatientWithCD4(octoberStartDate,octoberEndDate);
		List<Obs> noOfPatientWithCD4ForNovember=chaiEmrService.getNoOfPatientWithCD4(novemberStartDate,novemberEndDate);
		List<Obs> noOfPatientWithCD4ForDecember=chaiEmrService.getNoOfPatientWithCD4(decemberStartDate,decemberEndDate);
		
		List<Obs> noOfPatientNormalActivityForJan=chaiEmrService.getNoOfPatientNormalActivity(janStartDate,janEndDate);
		List<Obs> noOfPatientNormalActivityForFeb=chaiEmrService.getNoOfPatientNormalActivity(febStartDate,febEndDate);
		List<Obs> noOfPatientNormalActivityForMarch=chaiEmrService.getNoOfPatientNormalActivity(marchStartDate,marchEndDate);
		List<Obs> noOfPatientNormalActivityForApril=chaiEmrService.getNoOfPatientNormalActivity(aprilStartDate,aprilEndDate);
		List<Obs> noOfPatientNormalActivityForMay=chaiEmrService.getNoOfPatientNormalActivity(mayStartDate,mayEndDate);
		List<Obs> noOfPatientNormalActivityForJune=chaiEmrService.getNoOfPatientNormalActivity(juneStartDate,juneEndDate);
		List<Obs> noOfPatientNormalActivityForJuly=chaiEmrService.getNoOfPatientNormalActivity(julyStartDate,julyEndDate);
		List<Obs> noOfPatientNormalActivityForAugust=chaiEmrService.getNoOfPatientNormalActivity(augustStartDate,augustEndDate);
		List<Obs> noOfPatientNormalActivityForSeptember=chaiEmrService.getNoOfPatientNormalActivity(septemberStartDate,septemberEndDate);
		List<Obs> noOfPatientNormalActivityForOctober=chaiEmrService.getNoOfPatientNormalActivity(octoberStartDate,octoberEndDate);
		List<Obs> noOfPatientNormalActivityForNovember=chaiEmrService.getNoOfPatientNormalActivity(novemberStartDate,novemberEndDate);
		List<Obs> noOfPatientNormalActivityForDecember=chaiEmrService.getNoOfPatientNormalActivity(decemberStartDate,decemberEndDate);
		
		List<Obs> noOfPatientBedriddenLessThanFiftyForJan=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(janStartDate,janEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForFeb=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(febStartDate,febEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForMarch=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(marchStartDate,marchEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForApril=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(aprilStartDate,aprilEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForMay=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(mayStartDate,mayEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForJune=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(juneStartDate,juneEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForJuly=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(julyStartDate,julyEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForAugust=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(augustStartDate,augustEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForSeptember=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(septemberStartDate,septemberEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForOctober=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(octoberStartDate,octoberEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForNovember=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(novemberStartDate,novemberEndDate);
		List<Obs> noOfPatientBedriddenLessThanFiftyForDecember=chaiEmrService.getNoOfPatientBedriddenLessThanFifty(decemberStartDate,decemberEndDate);
		
		List<Obs> noOfPatientBedriddenMoreThanFiftyForJan=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(janStartDate,janEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForFeb=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(febStartDate,febEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForMarch=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(marchStartDate,marchEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForApril=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(aprilStartDate,aprilEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForMay=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(mayStartDate,mayEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForJune=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(juneStartDate,juneEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForJuly=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(julyStartDate,julyEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForAugust=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(augustStartDate,augustEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForSeptember=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(septemberStartDate,septemberEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForOctober=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(octoberStartDate,octoberEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForNovember=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(novemberStartDate,novemberEndDate);
		List<Obs> noOfPatientBedriddenMoreThanFiftyForDecember=chaiEmrService.getNoOfPatientBedriddenMoreThanFifty(decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfPatientPickedUpArvForSixMonthForJan=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(janStartDate,janEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForFeb=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(febStartDate,febEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForMarch=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(marchStartDate,marchEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForApril=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(aprilStartDate,aprilEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForMay=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(mayStartDate,mayEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForJune=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(juneStartDate,juneEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForJuly=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(julyStartDate,julyEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForAugust=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(augustStartDate,augustEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForSeptember=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(septemberStartDate,septemberEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForOctober=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(octoberStartDate,octoberEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForNovember=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(novemberStartDate,novemberEndDate);
		Set<Patient> noOfPatientPickedUpArvForSixMonthForDecember=chaiEmrService.getNoOfPatientPickedUpArvForSixMonth(decemberStartDate,decemberEndDate);
		
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForJan=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(janStartDate,janEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForFeb=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(febStartDate,febEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForMarch=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(marchStartDate,marchEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForApril=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(aprilStartDate,aprilEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForMay=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(mayStartDate,mayEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForJune=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(juneStartDate,juneEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForJuly=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(julyStartDate,julyEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForAugust=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(augustStartDate,augustEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForSeptember=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(septemberStartDate,septemberEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForOctober=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(octoberStartDate,octoberEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForNovember=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(novemberStartDate,novemberEndDate);
		Set<Patient> noOfPatientPickedUpArvForTwelveMonthForDecember=chaiEmrService.getNoOfPatientPickedUpArvForTwelveMonth(decemberStartDate,decemberEndDate);
		
		model.addAttribute("year",year);
		
		model.addAttribute("patientProgramForJan",patientProgramForJan.size());
		model.addAttribute("patientProgramForFeb",patientProgramForFeb.size());
		model.addAttribute("patientProgramForMarch",patientProgramForMarch.size());
		model.addAttribute("patientProgramForApril",patientProgramForApril.size());
		model.addAttribute("patientProgramForMay",patientProgramForMay.size());
		model.addAttribute("patientProgramForJune",patientProgramForJune.size());
		model.addAttribute("patientProgramForJuly",patientProgramForJuly.size());
		model.addAttribute("patientProgramForAugust",patientProgramForAugust.size());
		model.addAttribute("patientProgramForSeptember",patientProgramForSeptember.size());
		model.addAttribute("patientProgramForOctober",patientProgramForOctober.size());
		model.addAttribute("patientProgramForNovember",patientProgramForNovember.size());
		model.addAttribute("patientProgramForDecember",patientProgramForDecember.size());
		
		model.addAttribute("patientTransferInForJan",patientTransferInForJan.size());
		model.addAttribute("patientTransferInForFeb",patientTransferInForFeb.size());
		model.addAttribute("patientTransferInForMarch",patientTransferInForMarch.size());
		model.addAttribute("patientTransferInForApril",patientTransferInForApril.size());
		model.addAttribute("patientTransferInForMay",patientTransferInForMay.size());
		model.addAttribute("patientTransferInForJune",patientTransferInForJune.size());
		model.addAttribute("patientTransferInForJuly",patientTransferInForJuly.size());
		model.addAttribute("patientTransferInForAugust",patientTransferInForAugust.size());
		model.addAttribute("patientTransferInForSeptember",patientTransferInForSeptember.size());
		model.addAttribute("patientTransferInForOctober",patientTransferInForOctober.size());
		model.addAttribute("patientTransferInForNovember",patientTransferInForNovember.size());
		model.addAttribute("patientTransferInForDecember",patientTransferInForDecember.size());
		
		model.addAttribute("patientTransferOutForJan",patientTransferOutForJan.size());
		model.addAttribute("patientTransferOutForFeb",patientTransferOutForFeb.size());
		model.addAttribute("patientTransferOutForMarch",patientTransferOutForMarch.size());
		model.addAttribute("patientTransferOutForApril",patientTransferOutForApril.size());
		model.addAttribute("patientTransferOutForMay",patientTransferOutForMay.size());
		model.addAttribute("patientTransferOutForJune",patientTransferOutForJune.size());
		model.addAttribute("patientTransferOutForJuly",patientTransferOutForJuly.size());
		model.addAttribute("patientTransferOutForAugust",patientTransferOutForAugust.size());
		model.addAttribute("patientTransferOutForSeptember",patientTransferOutForSeptember.size());
		model.addAttribute("patientTransferOutForOctober",patientTransferOutForOctober.size());
		model.addAttribute("patientTransferOutForNovember",patientTransferOutForNovember.size());
		model.addAttribute("patientTransferOutForDecember",patientTransferOutForDecember.size());
		
		model.addAttribute("totalCohortForJan",totalCohortForJan.size());
		model.addAttribute("totalCohortForFeb",totalCohortForFeb.size());
		model.addAttribute("totalCohortForMarch",totalCohortForMarch.size());
		model.addAttribute("totalCohortForApril",totalCohortForApril.size());
		model.addAttribute("totalCohortForMay",totalCohortForMay.size());
		model.addAttribute("totalCohortForJune",totalCohortForJune.size());
		model.addAttribute("totalCohortForJuly",totalCohortForJuly.size());
		model.addAttribute("totalCohortForAugust",totalCohortForAugust.size());
		model.addAttribute("totalCohortForSeptember",totalCohortForSeptember.size());
		model.addAttribute("totalCohortForOctober",totalCohortForOctober.size());
		model.addAttribute("totalCohortForNovember",totalCohortForNovember.size());
		model.addAttribute("totalCohortForDecember",totalCohortForDecember.size());
		
		model.addAttribute("maleCohortForJan",maleCohortForJan.size());
		model.addAttribute("maleCohortForFeb",maleCohortForFeb.size());
		model.addAttribute("maleCohortForMarch",maleCohortForMarch.size());
		model.addAttribute("maleCohortForApril",maleCohortForApril.size());
		model.addAttribute("maleCohortForMay",maleCohortForMay.size());
		model.addAttribute("maleCohortForJune",maleCohortForJune.size());
		model.addAttribute("maleCohortForJuly",maleCohortForJuly.size());
		model.addAttribute("maleCohortForAugust",maleCohortForAugust.size());
		model.addAttribute("maleCohortForSeptember",maleCohortForSeptember.size());
		model.addAttribute("maleCohortForOctober",maleCohortForOctober.size());
		model.addAttribute("maleCohortForNovember",maleCohortForNovember.size());
		model.addAttribute("maleCohortForDecember",maleCohortForDecember.size());
		
		model.addAttribute("femaleCohortForJan",femaleCohortForJan.size());
		model.addAttribute("femaleCohortForFeb",femaleCohortForFeb.size());
		model.addAttribute("femaleCohortForMarch",femaleCohortForMarch.size());
		model.addAttribute("femaleCohortForApril",femaleCohortForApril.size());
		model.addAttribute("femaleCohortForMay",femaleCohortForMay.size());
		model.addAttribute("femaleCohortForJune",femaleCohortForJune.size());
		model.addAttribute("femaleCohortForJuly",femaleCohortForJuly.size());
		model.addAttribute("femaleCohortForAugust",femaleCohortForAugust.size());
		model.addAttribute("femaleCohortForSeptember",femaleCohortForSeptember.size());
		model.addAttribute("femaleCohortForOctober",femaleCohortForOctober.size());
		model.addAttribute("femaleCohortForNovember",femaleCohortForNovember.size());
		model.addAttribute("femaleCohortForDecember",femaleCohortForDecember.size());
		
		model.addAttribute("cohortFor0_14AgeForJan",cohortFor0_14AgeForJan.size());
		model.addAttribute("cohortFor0_14AgeForFeb",cohortFor0_14AgeForFeb.size());
		model.addAttribute("cohortFor0_14AgeForMarch",cohortFor0_14AgeForMarch.size());
		model.addAttribute("cohortFor0_14AgeForApril",cohortFor0_14AgeForApril.size());
		model.addAttribute("cohortFor0_14AgeForMay",cohortFor0_14AgeForMay.size());
		model.addAttribute("cohortFor0_14AgeForJune",cohortFor0_14AgeForJune.size());
		model.addAttribute("cohortFor0_14AgeForJuly",cohortFor0_14AgeForJuly.size());
		model.addAttribute("cohortFor0_14AgeForAugust",cohortFor0_14AgeForAugust.size());
		model.addAttribute("cohortFor0_14AgeForSeptember",cohortFor0_14AgeForSeptember.size());
		model.addAttribute("cohortFor0_14AgeForOctober",cohortFor0_14AgeForOctober.size());
		model.addAttribute("cohortFor0_14AgeForNovember",cohortFor0_14AgeForNovember.size());
		model.addAttribute("cohortFor0_14AgeForDecember",cohortFor0_14AgeForDecember.size());
		
		model.addAttribute("cohortFor15_24AgeForJan",cohortFor15_24AgeForJan.size());
		model.addAttribute("cohortFor15_24AgeForFeb",cohortFor15_24AgeForFeb.size());
		model.addAttribute("cohortFor15_24AgeForMarch",cohortFor15_24AgeForMarch.size());
		model.addAttribute("cohortFor15_24AgeForApril",cohortFor15_24AgeForApril.size());
		model.addAttribute("cohortFor15_24AgeForMay",cohortFor15_24AgeForMay.size());
		model.addAttribute("cohortFor15_24AgeForJune",cohortFor15_24AgeForJune.size());
		model.addAttribute("cohortFor15_24AgeForJuly",cohortFor15_24AgeForJuly.size());
		model.addAttribute("cohortFor15_24AgeForAugust",cohortFor15_24AgeForAugust.size());
		model.addAttribute("cohortFor15_24AgeForSeptember",cohortFor15_24AgeForSeptember.size());
		model.addAttribute("cohortFor15_24AgeForOctober",cohortFor15_24AgeForOctober.size());
		model.addAttribute("cohortFor15_24AgeForNovember",cohortFor15_24AgeForNovember.size());
		model.addAttribute("cohortFor15_24AgeForDecember",cohortFor15_24AgeForDecember.size());
		
		model.addAttribute("cohortFor25_60AgeForJan",cohortFor25_60AgeForJan.size());
		model.addAttribute("cohortFor25_60AgeForFeb",cohortFor25_60AgeForFeb.size());
		model.addAttribute("cohortFor25_60AgeForMarch",cohortFor25_60AgeForMarch.size());
		model.addAttribute("cohortFor25_60AgeForApril",cohortFor25_60AgeForApril.size());
		model.addAttribute("cohortFor25_60AgeForMay",cohortFor25_60AgeForMay.size());
		model.addAttribute("cohortFor25_60AgeForJune",cohortFor25_60AgeForJune.size());
		model.addAttribute("cohortFor25_60AgeForJuly",cohortFor25_60AgeForJuly.size());
		model.addAttribute("cohortFor25_60AgeForAugust",cohortFor25_60AgeForAugust.size());
		model.addAttribute("cohortFor25_60AgeForSeptember",cohortFor25_60AgeForSeptember.size());
		model.addAttribute("cohortFor25_60AgeForOctober",cohortFor25_60AgeForOctober.size());
		model.addAttribute("cohortFor25_60AgeForNovember",cohortFor25_60AgeForNovember.size());
		model.addAttribute("cohortFor25_60AgeForDecember",cohortFor25_60AgeForDecember.size());
		
		model.addAttribute("noOfCohortAliveAndOnArtForJan",noOfCohortAliveAndOnArtForJan.size());
		model.addAttribute("noOfCohortAliveAndOnArtForFeb",noOfCohortAliveAndOnArtForFeb.size());
		model.addAttribute("noOfCohortAliveAndOnArtForMarch",noOfCohortAliveAndOnArtForMarch.size());
		model.addAttribute("noOfCohortAliveAndOnArtForApril",noOfCohortAliveAndOnArtForApril.size());
		model.addAttribute("noOfCohortAliveAndOnArtForMay",noOfCohortAliveAndOnArtForMay.size());
		model.addAttribute("noOfCohortAliveAndOnArtForJune",noOfCohortAliveAndOnArtForJune.size());
		model.addAttribute("noOfCohortAliveAndOnArtForJuly",noOfCohortAliveAndOnArtForJuly.size());
		model.addAttribute("noOfCohortAliveAndOnArtForAugust",noOfCohortAliveAndOnArtForAugust.size());
		model.addAttribute("noOfCohortAliveAndOnArtForSeptember",noOfCohortAliveAndOnArtForSeptember.size());
		model.addAttribute("noOfCohortAliveAndOnArtForOctober",noOfCohortAliveAndOnArtForOctober.size());
		model.addAttribute("noOfCohortAliveAndOnArtNovember",noOfCohortAliveAndOnArtForNovember.size());
		model.addAttribute("noOfCohortAliveAndOnArtForDecember",noOfCohortAliveAndOnArtForDecember.size());
		
		model.addAttribute("noOfOriginalFirstLineRegimenForJan",noOfOriginalFirstLineRegimenForJan.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForFeb",noOfOriginalFirstLineRegimenForFeb.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForMarch",noOfOriginalFirstLineRegimenForMarch.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForApril",noOfOriginalFirstLineRegimenForApril.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForMay",noOfOriginalFirstLineRegimenForMay.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForJune",noOfOriginalFirstLineRegimenForJune.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForJuly",noOfOriginalFirstLineRegimenForJuly.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForAugust",noOfOriginalFirstLineRegimenForAugust.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForSeptember",noOfOriginalFirstLineRegimenForSeptember.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForOctober",noOfOriginalFirstLineRegimenForOctober.size());
		model.addAttribute("noOfOriginalFirstLineRegimenNovember",noOfOriginalFirstLineRegimenForNovember.size());
		model.addAttribute("noOfOriginalFirstLineRegimenForDecember",noOfOriginalFirstLineRegimenForDecember.size());
		
		model.addAttribute("noOfAlternateFirstLineRegimenForJan",noOfAlternateFirstLineRegimenForJan.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForFeb",noOfAlternateFirstLineRegimenForFeb.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForMarch",noOfAlternateFirstLineRegimenForMarch.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForApril",noOfAlternateFirstLineRegimenForApril.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForMay",noOfAlternateFirstLineRegimenForMay.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForJune",noOfAlternateFirstLineRegimenForJune.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForJuly",noOfAlternateFirstLineRegimenForJuly.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForAugust",noOfAlternateFirstLineRegimenForAugust.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForSeptember",noOfAlternateFirstLineRegimenForSeptember.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForOctober",noOfAlternateFirstLineRegimenForOctober.size());
		model.addAttribute("noOfAlternateFirstLineRegimenNovember",noOfAlternateFirstLineRegimenForNovember.size());
		model.addAttribute("noOfAlternateFirstLineRegimenForDecember",noOfAlternateFirstLineRegimenForDecember.size());
		
		model.addAttribute("noOfSecondLineRegimenForJan",noOfSecondLineRegimenForJan.size());
		model.addAttribute("noOfSecondLineRegimenForFeb",noOfSecondLineRegimenForFeb.size());
		model.addAttribute("noOfSecondLineRegimenForMarch",noOfSecondLineRegimenForMarch.size());
		model.addAttribute("noOfSecondLineRegimenForApril",noOfSecondLineRegimenForApril.size());
		model.addAttribute("noOfSecondLineRegimenForMay",noOfSecondLineRegimenForMay.size());
		model.addAttribute("noOfSecondLineRegimenForJune",noOfSecondLineRegimenForJune.size());
		model.addAttribute("noOfSecondLineRegimenForJuly",noOfSecondLineRegimenForJuly.size());
		model.addAttribute("noOfSecondLineRegimenForAugust",noOfSecondLineRegimenForAugust.size());
		model.addAttribute("noOfSecondLineRegimenForSeptember",noOfSecondLineRegimenForSeptember.size());
		model.addAttribute("noOfSecondLineRegimenForOctober",noOfSecondLineRegimenForOctober.size());
		model.addAttribute("noOfSecondLineRegimenNovember",noOfSecondLineRegimenForNovember.size());
		model.addAttribute("noOfSecondLineRegimenForDecember",noOfSecondLineRegimenForDecember.size());
		
		model.addAttribute("noOfArtStoppedCohortForJan",noOfArtStoppedCohortForJan.size());
		model.addAttribute("noOfArtStoppedCohortForFeb",noOfArtStoppedCohortForFeb.size());
		model.addAttribute("noOfArtStoppedCohortForMarch",noOfArtStoppedCohortForMarch.size());
		model.addAttribute("noOfArtStoppedCohortForApril",noOfArtStoppedCohortForApril.size());
		model.addAttribute("noOfArtStoppedCohortForMay",noOfArtStoppedCohortForMay.size());
		model.addAttribute("noOfArtStoppedCohortForJune",noOfArtStoppedCohortForJune.size());
		model.addAttribute("noOfArtStoppedCohortForJuly",noOfArtStoppedCohortForJuly.size());
		model.addAttribute("noOfArtStoppedCohortForAugust",noOfArtStoppedCohortForAugust.size());
		model.addAttribute("noOfArtStoppedCohortForSeptember",noOfArtStoppedCohortForSeptember.size());
		model.addAttribute("noOfArtStoppedCohortForOctober",noOfArtStoppedCohortForOctober.size());
		model.addAttribute("noOfArtStoppedCohortNovember",noOfArtStoppedCohortForNovember.size());
		model.addAttribute("noOfArtStoppedCohortForDecember",noOfArtStoppedCohortForDecember.size());
		
		model.addAttribute("noOfArtDiedCohortForJan",noOfArtDiedCohortForJan.size());
		model.addAttribute("noOfArtDiedCohortForFeb",noOfArtDiedCohortForFeb.size());
		model.addAttribute("noOfArtDiedCohortForMarch",noOfArtDiedCohortForMarch.size());
		model.addAttribute("noOfArtDiedCohortForApril",noOfArtDiedCohortForApril.size());
		model.addAttribute("noOfArtDiedCohortForMay",noOfArtDiedCohortForMay.size());
		model.addAttribute("noOfArtDiedCohortForJune",noOfArtDiedCohortForJune.size());
		model.addAttribute("noOfArtDiedCohortForJuly",noOfArtDiedCohortForJuly.size());
		model.addAttribute("noOfArtDiedCohortForAugust",noOfArtDiedCohortForAugust.size());
		model.addAttribute("noOfArtDiedCohortForSeptember",noOfArtDiedCohortForSeptember.size());
		model.addAttribute("noOfArtDiedCohortForOctober",noOfArtDiedCohortForOctober.size());
		model.addAttribute("noOfArtDiedCohortNovember",noOfArtDiedCohortForNovember.size());
		model.addAttribute("noOfArtDiedCohortForDecember",noOfArtDiedCohortForDecember.size());
		
		model.addAttribute("noOfPatientLostToFollowUpForJan",noOfPatientLostToFollowUpForJan.size());
		model.addAttribute("noOfPatientLostToFollowUpForFeb",noOfPatientLostToFollowUpForFeb.size());
		model.addAttribute("noOfPatientLostToFollowUpForMarch",noOfPatientLostToFollowUpForMarch.size());
		model.addAttribute("noOfPatientLostToFollowUpForApril",noOfPatientLostToFollowUpForApril.size());
		model.addAttribute("noOfPatientLostToFollowUpForMay",noOfPatientLostToFollowUpForMay.size());
		model.addAttribute("noOfPatientLostToFollowUpForJune",noOfPatientLostToFollowUpForJune.size());
		model.addAttribute("noOfPatientLostToFollowUpForJuly",noOfPatientLostToFollowUpForJuly.size());
		model.addAttribute("noOfPatientLostToFollowUpForAugust",noOfPatientLostToFollowUpForAugust.size());
		model.addAttribute("noOfPatientLostToFollowUpForSeptember",noOfPatientLostToFollowUpForSeptember.size());
		model.addAttribute("noOfPatientLostToFollowUpForOctober",noOfPatientLostToFollowUpForOctober.size());
		model.addAttribute("noOfPatientLostToFollowUpNovember",noOfPatientLostToFollowUpForNovember.size());
		model.addAttribute("noOfPatientLostToFollowUpForDecember",noOfPatientLostToFollowUpForDecember.size());
		
		model.addAttribute("noOfPatientWithCD4ForJan",noOfPatientWithCD4ForJan.size());
		model.addAttribute("noOfPatientWithCD4ForFeb",noOfPatientWithCD4ForFeb.size());
		model.addAttribute("noOfPatientWithCD4ForMarch",noOfPatientWithCD4ForMarch.size());
		model.addAttribute("noOfPatientWithCD4ForApril",noOfPatientWithCD4ForApril.size());
		model.addAttribute("noOfPatientWithCD4ForMay",noOfPatientWithCD4ForMay.size());
		model.addAttribute("noOfPatientWithCD4ForJune",noOfPatientWithCD4ForJune.size());
		model.addAttribute("noOfPatientWithCD4ForJuly",noOfPatientWithCD4ForJuly.size());
		model.addAttribute("noOfPatientWithCD4ForAugust",noOfPatientWithCD4ForAugust.size());
		model.addAttribute("noOfPatientWithCD4ForSeptember",noOfPatientWithCD4ForSeptember.size());
		model.addAttribute("noOfPatientWithCD4ForOctober",noOfPatientWithCD4ForOctober.size());
		model.addAttribute("noOfPatientWithCD4November",noOfPatientWithCD4ForNovember.size());
		model.addAttribute("noOfPatientWithCD4ForDecember",noOfPatientWithCD4ForDecember.size());
		
		model.addAttribute("noOfPatientNormalActivityForJan",noOfPatientNormalActivityForJan.size());
		model.addAttribute("noOfPatientNormalActivityForFeb",noOfPatientNormalActivityForFeb.size());
		model.addAttribute("noOfPatientNormalActivityForMarch",noOfPatientNormalActivityForMarch.size());
		model.addAttribute("noOfPatientNormalActivityForApril",noOfPatientNormalActivityForApril.size());
		model.addAttribute("noOfPatientNormalActivityForMay",noOfPatientNormalActivityForMay.size());
		model.addAttribute("noOfPatientNormalActivityForJune",noOfPatientNormalActivityForJune.size());
		model.addAttribute("noOfPatientNormalActivityForJuly",noOfPatientNormalActivityForJuly.size());
		model.addAttribute("noOfPatientNormalActivityForAugust",noOfPatientNormalActivityForAugust.size());
		model.addAttribute("noOfPatientNormalActivityForSeptember",noOfPatientNormalActivityForSeptember.size());
		model.addAttribute("noOfPatientNormalActivityForOctober",noOfPatientNormalActivityForOctober.size());
		model.addAttribute("noOfPatientNormalActivityNovember",noOfPatientNormalActivityForNovember.size());
		model.addAttribute("noOfPatientNormalActivityForDecember",noOfPatientNormalActivityForDecember.size());
		
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForJan",noOfPatientBedriddenLessThanFiftyForJan.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForFeb",noOfPatientBedriddenLessThanFiftyForFeb.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForMarch",noOfPatientBedriddenLessThanFiftyForMarch.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForApril",noOfPatientBedriddenLessThanFiftyForApril.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForMay",noOfPatientBedriddenLessThanFiftyForMay.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForJune",noOfPatientBedriddenLessThanFiftyForJune.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForJuly",noOfPatientBedriddenLessThanFiftyForJuly.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForAugust",noOfPatientBedriddenLessThanFiftyForAugust.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForSeptember",noOfPatientBedriddenLessThanFiftyForSeptember.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForOctober",noOfPatientBedriddenLessThanFiftyForOctober.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyNovember",noOfPatientBedriddenLessThanFiftyForNovember.size());
		model.addAttribute("noOfPatientBedriddenLessThanFiftyForDecember",noOfPatientBedriddenLessThanFiftyForDecember.size());
		
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForJan",noOfPatientBedriddenMoreThanFiftyForJan.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForFeb",noOfPatientBedriddenMoreThanFiftyForFeb.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForMarch",noOfPatientBedriddenMoreThanFiftyForMarch.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForApril",noOfPatientBedriddenMoreThanFiftyForApril.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForMay",noOfPatientBedriddenMoreThanFiftyForMay.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForJune",noOfPatientBedriddenMoreThanFiftyForJune.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForJuly",noOfPatientBedriddenMoreThanFiftyForJuly.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForAugust",noOfPatientBedriddenMoreThanFiftyForAugust.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForSeptember",noOfPatientBedriddenMoreThanFiftyForSeptember.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForOctober",noOfPatientBedriddenMoreThanFiftyForOctober.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyNovember",noOfPatientBedriddenMoreThanFiftyForNovember.size());
		model.addAttribute("noOfPatientBedriddenMoreThanFiftyForDecember",noOfPatientBedriddenMoreThanFiftyForDecember.size());
		
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForJan",noOfPatientPickedUpArvForSixMonthForJan.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForFeb",noOfPatientPickedUpArvForSixMonthForFeb.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForMarch",noOfPatientPickedUpArvForSixMonthForMarch.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForApril",noOfPatientPickedUpArvForSixMonthForApril.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForMay",noOfPatientPickedUpArvForSixMonthForMay.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForJune",noOfPatientPickedUpArvForSixMonthForJune.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForJuly",noOfPatientPickedUpArvForSixMonthForJuly.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForAugust",noOfPatientPickedUpArvForSixMonthForAugust.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForSeptember",noOfPatientPickedUpArvForSixMonthForSeptember.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForOctober",noOfPatientPickedUpArvForSixMonthForOctober.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthNovember",noOfPatientPickedUpArvForSixMonthForNovember.size());
		model.addAttribute("noOfPatientPickedUpArvForSixMonthForDecember",noOfPatientPickedUpArvForSixMonthForDecember.size());
		
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForJan",noOfPatientPickedUpArvForTwelveMonthForJan.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForFeb",noOfPatientPickedUpArvForTwelveMonthForFeb.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForMarch",noOfPatientPickedUpArvForTwelveMonthForMarch.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForApril",noOfPatientPickedUpArvForTwelveMonthForApril.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForMay",noOfPatientPickedUpArvForTwelveMonthForMay.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForJune",noOfPatientPickedUpArvForTwelveMonthForJune.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForJuly",noOfPatientPickedUpArvForTwelveMonthForJuly.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForAugust",noOfPatientPickedUpArvForTwelveMonthForAugust.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForSeptember",noOfPatientPickedUpArvForTwelveMonthForSeptember.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForOctober",noOfPatientPickedUpArvForTwelveMonthForOctober.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthNovember",noOfPatientPickedUpArvForTwelveMonthForNovember.size());
		model.addAttribute("noOfPatientPickedUpArvForTwelveMonthForDecember",noOfPatientPickedUpArvForTwelveMonthForDecember.size());
	}
  }
}