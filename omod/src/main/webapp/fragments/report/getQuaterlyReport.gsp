<table style='width: 100%' class="ke-table-vertical">
<tr>
<th>
Cohort Analysis for the ${year}
</th>

<% if (quaterly=='First Quater') { %>
<th>
Jan Cohort
</th>
<th>
Feb Cohort
</th>
<th>
March Cohort
</th>
<% } %>

<% if (quaterly=='Second Quater') { %>
<th>
April Cohort
</th>
<th>
May Cohort
</th>
<th>
June Cohort
</th>
<% } %>

<% if (quaterly=='Third Quater') { %>
<th>
July Cohort
</th>
<th>
Aug Cohort
</th>
<th>
Sep Cohort
</th>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<th>
Oct Cohort
</th>
<th>
Nov   Cohort
</th>
<th>
Dec Cohort
</th>
<% } %>
</tr>
<tr>
<td><font face="verdana" color="red">Fill up the Year of the Cohort in each cells</font></td>
<td> ${year} </td>
<td> ${year} </td>
<td> ${year} </td>
</tr>

<tr>
<td><font face="verdana" color="green">No. of Patient already on ART in this Clinic</font></td>
<% if (quaterly=='First Quater') { %>
<td>
${patientProgramForJan}
</td>
<td>
${patientProgramForFeb}
</td>
<td>
${patientProgramForMarch}
</td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>
${patientProgramForApril}
</td>
<td>
${patientProgramForMay}
</td>
<td>
${patientProgramForJune}
</td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>
${patientProgramForJuly}
</td>
<td>
${patientProgramForAugust}
</td>
<td>
${patientProgramForSeptember}
</td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>
${patientProgramForOctober}
</td>
<td>
${patientProgramForNovember}
</td>
<td>
${patientProgramForDecember}
</td>
<% } %>
</tr>

<tr>
<td>Transfer In Add+ </td>

<% if (quaterly=='First Quater') { %>
<td>${patientTransferInForJan}</td>
<td>${patientTransferInForFeb}</td>
<td>${patientTransferInForMarch}</td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${patientTransferInForApril}</td>
<td>${patientTransferInForMay}</td>
<td>${patientTransferInForJune}</td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${patientTransferInForJuly}</td>
<td>${patientTransferInForAugust}</td>
<td>${patientTransferInForSeptember}</td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${patientTransferInForOctober}</td>
<td>${patientTransferInForNovember}</td>
<td>${patientTransferInForDecember}</td>
<% } %>
</tr>

<tr>
<td>Transfer Out Subtract- </td>

<% if (quaterly=='First Quater') { %>
<td>${patientTransferOutForJan}</td>
<td>${patientTransferOutForFeb}</td>
<td>${patientTransferOutForMarch}</td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${patientTransferOutForApril}</td>
<td>${patientTransferOutForMay}</td>
<td>${patientTransferOutForJune}</td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${patientTransferOutForJuly}</td>
<td>${patientTransferOutForAugust}</td>
<td>${patientTransferOutForSeptember}</td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${patientTransferOutForOctober}</td>
<td>${patientTransferOutForNovember}</td>
<td>${patientTransferOutForDecember}</td>
<% } %>
</tr>

<tr>
<td><font face="verdana" color="green">Total  Cohort for Respective month (s)</font></td>
<% if (quaterly=='First Quater') { %>
<<td>${totalCohortForJan} </td>
<td>${totalCohortForFeb} </td>
<td>${totalCohortForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${totalCohortForApril} </td>
<td>${totalCohortForMay} </td>
<td>${totalCohortForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${totalCohortForJuly} </td>
<td>${totalCohortForAugust} </td>
<td>${totalCohortForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${totalCohortForOctober} </td>
<td>${totalCohortForNovember} </td>
<td>${totalCohortForDecember} </td>
<% } %>

</tr>

<tr>
<td>Male </td>
<% if (quaterly=='First Quater') { %>
<td>${maleCohortForJan} </td>
<td>${maleCohortForFeb} </td>
<td>${maleCohortForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${maleCohortForApril} </td>
<td>${maleCohortForMay} </td>
<td>${maleCohortForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${maleCohortForJuly} </td>
<td>${maleCohortForAugust} </td>
<td>${maleCohortForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${maleCohortForOctober} </td>
<td>${maleCohortForNovember} </td>
<td>${maleCohortForDecember} </td>
<% } %>

</tr>

<tr>
<td>Female </td>
<% if (quaterly=='First Quater') { %>
<td>${femaleCohortForJan} </td>
<td>${femaleCohortForFeb} </td>
<td>${femaleCohortForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${femaleCohortForApril} </td>
<td>${femaleCohortForMay} </td>
<td>${femaleCohortForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${femaleCohortForJuly} </td>
<td>${femaleCohortForAugust} </td>
<td>${femaleCohortForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${femaleCohortForOctober} </td>
<td>${femaleCohortForNovember} </td>
<td>${femaleCohortForDecember} </td>
<% } %>

</tr>

<tr>
<td>Age 0-14yr </td>
<% if (quaterly=='First Quater') { %>
<<td>${cohortFor0_14AgeForJan} </td>
<td>${cohortFor0_14AgeForFeb} </td>
<td>${cohortFor0_14AgeForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${cohortFor0_14AgeForApril} </td>
<td>${cohortFor0_14AgeForMay} </td>
<td>${cohortFor0_14AgeForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${cohortFor0_14AgeForJuly} </td>
<td>${cohortFor0_14AgeForAugust} </td>
<td>${cohortFor0_14AgeForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${cohortFor0_14AgeForOctober} </td>
<td>${cohortFor0_14AgeForNovember} </td>
<td>${cohortFor0_14AgeForDecember} </td>
<% } %>

</tr>

<tr>
<td>Age 15-24yr </td>
<% if (quaterly=='First Quater') { %>
<td>${cohortFor15_24AgeForJan} </td>
<td>${cohortFor15_24AgeForFeb} </td>
<td>${cohortFor15_24AgeForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${cohortFor15_24AgeForApril} </td>
<td>${cohortFor15_24AgeForMay} </td>
<td>${cohortFor15_24AgeForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${cohortFor15_24AgeForJuly} </td>
<td>${cohortFor15_24AgeForAugust} </td>
<td>${cohortFor15_24AgeForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${cohortFor15_24AgeForOctober} </td>
<td>${cohortFor15_24AgeForNovember} </td>
<td>${cohortFor15_24AgeForDecember} </td>
<% } %>

</tr>

<tr>
<td>Age 25-60yr</td>
<% if (quaterly=='First Quater') { %>
<td>${cohortFor25_60AgeForJan} </td>
<td>${cohortFor25_60AgeForFeb} </td>
<td>${cohortFor25_60AgeForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${cohortFor25_60AgeForApril} </td>
<td>${cohortFor25_60AgeForMay} </td>
<td>${cohortFor25_60AgeForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${cohortFor25_60AgeForJuly} </td>
<td>${cohortFor25_60AgeForAugust} </td>
<td>${cohortFor25_60AgeForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${cohortFor25_60AgeForOctober} </td>
<td>${cohortFor25_60AgeForNovember} </td>
<td>${cohortFor25_60AgeForDecember} </td>
<% } %>

</tr>

<tr>
<td><font face="verdana" color="green">No. of Cohort Alive & on ART in that month</font></td>
<% if (quaterly=='First Quater') { %>
<td>${noOfCohortAliveAndOnArtForJan} </td>
<td>${noOfCohortAliveAndOnArtForFeb} </td>
<td>${noOfCohortAliveAndOnArtForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfCohortAliveAndOnArtForApril} </td>
<td>${noOfCohortAliveAndOnArtForMay} </td>
<td>${noOfCohortAliveAndOnArtForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfCohortAliveAndOnArtForJuly} </td>
<td>${noOfCohortAliveAndOnArtForAugust} </td>
<td>${noOfCohortAliveAndOnArtForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfCohortAliveAndOnArtForOctober} </td>
<td>${noOfCohortAliveAndOnArtNovember} </td>
<td>${noOfCohortAliveAndOnArtForDecember} </td>
<% } %>

</tr>

<tr>
<td>On Original First Line Regimen </td>
<% if (quaterly=='First Quater') { %>
<td>${noOfOriginalFirstLineRegimenForJan} </td>
<td>${noOfOriginalFirstLineRegimenForFeb} </td>
<td>${noOfOriginalFirstLineRegimenForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfOriginalFirstLineRegimenForApril} </td>
<td>${noOfOriginalFirstLineRegimenForMay} </td>
<td>${noOfOriginalFirstLineRegimenForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfOriginalFirstLineRegimenForJuly} </td>
<td>${noOfOriginalFirstLineRegimenForAugust} </td>
<td>${noOfOriginalFirstLineRegimenForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfOriginalFirstLineRegimenForOctober} </td>
<td>${noOfOriginalFirstLineRegimenNovember} </td>
<td>${noOfOriginalFirstLineRegimenForDecember} </td>
<% } %>

</tr>

<tr>
<td>On Alternate First Line Regimen( Substituted) </td>
<% if (quaterly=='First Quater') { %>
<td>${noOfAlternateFirstLineRegimenForJan} </td>
<td>${noOfAlternateFirstLineRegimenForFeb} </td>
<td>${noOfAlternateFirstLineRegimenForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<<td>${noOfAlternateFirstLineRegimenForApril} </td>
<td>${noOfAlternateFirstLineRegimenForMay} </td>
<td>${noOfAlternateFirstLineRegimenForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfAlternateFirstLineRegimenForJuly} </td>
<td>${noOfAlternateFirstLineRegimenForAugust} </td>
<td>${noOfAlternateFirstLineRegimenForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfAlternateFirstLineRegimenForOctober} </td>
<td>${noOfAlternateFirstLineRegimenNovember} </td>
<td>${noOfAlternateFirstLineRegimenForDecember} </td>
<% } %>

</tr>

<tr>
<td>On Second Line Regimen (Switched) </td>
<% if (quaterly=='First Quater') { %>
<td>${noOfSecondLineRegimenForJan} </td>
<td>${noOfSecondLineRegimenForFeb} </td>
<td>${noOfSecondLineRegimenForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfSecondLineRegimenForApril} </td>
<td>${noOfSecondLineRegimenForMay} </td>
<td>${noOfSecondLineRegimenForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfSecondLineRegimenForJuly} </td>
<td>${noOfSecondLineRegimenForAugust} </td>
<td>${noOfSecondLineRegimenForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfSecondLineRegimenForOctober} </td>
<td>${noOfSecondLineRegimenNovember} </td>
<td>${noOfSecondLineRegimenForDecember} </td>
<% } %>

</tr>

<tr>
<td>Stopped for any reason (medical or self) </td>
<% if (quaterly=='First Quater') { %>
<td>${noOfArtStoppedCohortForJan} </td>
<td>${noOfArtStoppedCohortForFeb} </td>
<td>${noOfArtStoppedCohortForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfArtStoppedCohortForApril} </td>
<td>${noOfArtStoppedCohortForMay} </td>
<td>${noOfArtStoppedCohortForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfArtStoppedCohortForJuly} </td>
<td>${noOfArtStoppedCohortForAugust} </td>
<td>${noOfArtStoppedCohortForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfArtStoppedCohortForOctober} </td>
<td>${noOfArtStoppedCohortNovember} </td>
<td>${noOfArtStoppedCohortForDecember} </td>
<% } %>

</tr>

<tr>
<td>Died </td>
<% if (quaterly=='First Quater') { %>
<td>${noOfArtDiedCohortForJan} </td>
<td>${noOfArtDiedCohortForFeb} </td>
<td>${noOfArtDiedCohortForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfArtDiedCohortForApril} </td>
<td>${noOfArtDiedCohortForMay} </td>
<td>${noOfArtDiedCohortForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfArtDiedCohortForJuly} </td>
<td>${noOfArtDiedCohortForAugust} </td>
<td>${noOfArtDiedCohortForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfArtDiedCohortForOctober} </td>
<td>${noOfArtDiedCohortNovember} </td>
<td>${noOfArtDiedCohortForDecember} </td>
<% } %>

</tr>

<tr>
<td>Lost to Follow up (DROP) </td>
<% if (quaterly=='First Quater') { %>
<td>${noOfPatientLostToFollowUpForJan} </td>
<td>${noOfPatientLostToFollowUpForFeb} </td>
<td>${noOfPatientLostToFollowUpForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfPatientLostToFollowUpForApril} </td>
<td>${noOfPatientLostToFollowUpForMay} </td>
<td>${noOfPatientLostToFollowUpForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfPatientLostToFollowUpForJuly} </td>
<td>${noOfPatientLostToFollowUpForAugust} </td>
<td>${noOfPatientLostToFollowUpForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfPatientLostToFollowUpForOctober} </td>
<td>${noOfPatientLostToFollowUpNovember} </td>
<td>${noOfPatientLostToFollowUpForDecember} </td>
<% } %>

</tr>

<tr>
<td><font face="verdana" color="green">Percent of Cohort Alive and on ART</font></td>
<% if (quaterly=='First Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

</tr>

<tr>
<td>Number of Patient with CD4 > or equal to 200  </td>
<% if (quaterly=='First Quater') { %>
td>${noOfPatientWithCD4ForJan} </td>
<td>${noOfPatientWithCD4ForFeb} </td>
<td>${noOfPatientWithCD4ForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfPatientWithCD4ForApril} </td>
<td>${noOfPatientWithCD4ForMay} </td>
<td>${noOfPatientWithCD4ForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfPatientWithCD4ForJuly} </td>
<td>${noOfPatientWithCD4ForAugust} </td>
<td>${noOfPatientWithCD4ForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfPatientWithCD4ForOctober} </td>
<td>${noOfPatientWithCD4November} </td>
<td>${noOfPatientWithCD4ForDecember} </td>
<% } %>

</tr>

<tr>
<td>Performance Scale </td>
<% if (quaterly=='First Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td></td>
<td></td>
<td></td>
<% } %>

</tr>

<tr>
<td>A-Normal activity</td>
<% if (quaterly=='First Quater') { %>
<td>${noOfPatientNormalActivityForJan} </td>
<td>${noOfPatientNormalActivityForFeb} </td>
<td>${noOfPatientNormalActivityForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfPatientNormalActivityForApril} </td>
<td>${noOfPatientNormalActivityForMay} </td>
<td>${noOfPatientNormalActivityForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfPatientNormalActivityForJuly} </td>
<td>${noOfPatientNormalActivityForAugust} </td>
<td>${noOfPatientNormalActivityForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfPatientNormalActivityForOctober} </td>
<td>${noOfPatientNormalActivityNovember} </td>
<td>${noOfPatientNormalActivityForDecember} </td>
<% } %>

</tr>

<tr>
<td>B-Bedridden less than 50%</td>
<% if (quaterly=='First Quater') { %>
<td>${noOfPatientBedriddenLessThanFiftyForJan} </td>
<td>${noOfPatientBedriddenLessThanFiftyForFeb} </td>
<td>${noOfPatientBedriddenLessThanFiftyForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfPatientBedriddenLessThanFiftyForApril} </td>
<td>${noOfPatientBedriddenLessThanFiftyForMay} </td>
<td>${noOfPatientBedriddenLessThanFiftyForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfPatientBedriddenLessThanFiftyForJuly} </td>
<td>${noOfPatientBedriddenLessThanFiftyForAugust} </td>
<td>${noOfPatientBedriddenLessThanFiftyForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfPatientBedriddenLessThanFiftyForOctober} </td>
<td>${noOfPatientBedriddenLessThanFiftyNovember} </td>
<td>${noOfPatientBedriddenLessThanFiftyForDecember} </td>
<% } %>

</tr>

<tr>
<td>C-Bedridden for more than 50%</td>
<% if (quaterly=='First Quater') { %>
<<td>${noOfPatientBedriddenMoreThanFiftyForJan} </td>
<td>${noOfPatientBedriddenMoreThanFiftyForFeb} </td>
<td>${noOfPatientBedriddenMoreThanFiftyForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfPatientBedriddenMoreThanFiftyForApril} </td>
<td>${noOfPatientBedriddenMoreThanFiftyForMay} </td>
<td>${noOfPatientBedriddenMoreThanFiftyForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfPatientBedriddenMoreThanFiftyForJuly} </td>
<td>${noOfPatientBedriddenMoreThanFiftyForAugust} </td>
<td>${noOfPatientBedriddenMoreThanFiftyForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfPatientBedriddenMoreThanFiftyForOctober} </td>
<td>${noOfPatientBedriddenMoreThanFiftyNovember} </td>
<td>${noOfPatientBedriddenMoreThanFiftyForDecember} </td>
<% } %>

</tr>

<tr>
<td>No. of Persons who picked up ARVs consistently in every month for 6 mths</td>
<% if (quaterly=='First Quater') { %>
<td>${noOfPatientPickedUpArvForSixMonthForJan} </td>
<td>${noOfPatientPickedUpArvForSixMonthForFeb} </td>
<td>${noOfPatientPickedUpArvForSixMonthForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfPatientPickedUpArvForSixMonthForApril} </td>
<td>${noOfPatientPickedUpArvForSixMonthForMay} </td>
<td>${noOfPatientPickedUpArvForSixMonthForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfPatientPickedUpArvForSixMonthForJuly} </td>
<td>${noOfPatientPickedUpArvForSixMonthForAugust} </td>
<td>${noOfPatientPickedUpArvForSixMonthForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfPatientPickedUpArvForSixMonthForOctober} </td>
<td>${noOfPatientPickedUpArvForSixMonthNovember} </td>
<td>${noOfPatientPickedUpArvForSixMonthForDecember} </td>
<% } %>

</tr>

<tr>
<td>No. of Persons who picked up ARVs consistently in every month for 12 mths</td>
<% if (quaterly=='First Quater') { %>
<td>${noOfPatientPickedUpArvForTwelveMonthForJan} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthForFeb} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthForMarch} </td>
<% } %>

<% if (quaterly=='Second Quater') { %>
<td>${noOfPatientPickedUpArvForTwelveMonthForApril} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthForMay} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthForJune} </td>
<% } %>

<% if (quaterly=='Third Quater') { %>
<td>${noOfPatientPickedUpArvForTwelveMonthForJuly} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthForAugust} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthForSeptember} </td>
<% } %>

<% if (quaterly=='Fourth Quater') { %>
<td>${noOfPatientPickedUpArvForTwelveMonthForOctober} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthNovember} </td>
<td>${noOfPatientPickedUpArvForTwelveMonthForDecember} </td>
<% } %>

</tr>
</table>