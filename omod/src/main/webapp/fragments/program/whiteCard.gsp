<%
	ui.decorateWith("chaiui", "panel", [ heading: "WHITE CARD" ])
%>

<!--08/02/2016 -->

<style>
td, th, table, tr{
	padding : 2px;
}
</style>

<style>
.tbheader {
    color: white;
} 
</style>

<script>
	var tableToExcel = (function() {
		var uri = 'data:application/vnd.ms-excel;base64,'
		, template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table >{table}</table></body></html>'
		, base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
		, format = function(s, c) { return s.replace(/{(\\w+)}/g, function(m, p) { return c[p]; }) }
		return function(table, name) {
		if (!table.nodeType) table = document.getElementById(table)
		var ctx = {worksheet: name || 'White Card', table: table.innerHTML}
		
		var link = document.createElement("a");
		link.href = uri + base64(format(template, ctx));

		link.style = 'visibility:hidden';
		link.download ='${ patientName } - WhiteCard.xls';

		document.body.appendChild(link);
		link.click();
		
		}
	})()
	
	
</script>
<script>
function print(){
			jQuery('#print').printArea({
				mode : "popup",
				popClose : true
			});
			
		}
</script>

<% if (config.complete) { %>
<div class="ke-stack-item" width="100%">
	<div id="print" hidden="hidden" >
		    <div style="float: left; text-align: right">
					<img src="${ ui.resourceLink("chaiemr", "images/logos/MOH_logo.png") }" width="48" height="48" />
				</div>
			
			<div style="float: centre; text-align: center; font-size: 13px; font-weight: bold; padding: 9px 5px 0 0; color: #7f7b72;">
				<tr><td>${ ui.message("chaiemr.imageprint") }</td></tr>
			</div>
				<div style="float: centre; text-align: center; font-size: 13px; font-weight: bold; padding: 9px 5px 0 0; color: #7f7b72;">
					<tr><td>${ ui.message("chaiemr.whitecardprint") }</td></tr>
					</div>
					<div style="float: centre; text-align: center; font-size: 13px; font-weight: bold; padding: 9px 5px 0 0; color: #7f7b72;">
					<tr><td class="tbheader" bgcolor="#778899">
		         <center><strong>PatientId: ${patientIdd}</strong></center>	
		 <% if (Value==0) { %>
						<center><strong>Pre-Art registration number: ${patientpreart}</strong></center>
						<% } %>
		 <% if (Flag==0) { %>
						<center><strong>NAP ART registration number : ${patientnap}</strong></center>
						<% } %></tr>
		
				</td>
				</div>
				<table border="1" width="100%">
			<tr >
				<td colspan="2" class="tbheader" bgcolor="#778899">
					<center><strong>Patient Identification</strong></center>
				</td>
			</tr>
			<tr>
				<td width="50%">
					<table>
						<tr><td><strong>Name</strong></td><td> : ${ patientName }</td></tr>
						<tr><td><strong>Age</strong></td><td align="left"> : ${ patientAge } </td></tr>
						<tr><td><strong>Date of Birth</strong></td><td align="left"> : ${ birthDate } </td></tr>
						<tr><td><strong>Gender</strong></td><td> : ${ patientGender } </td></tr>
						<% if ( address.address1) { %>
						<tr><td><strong>Address</strong> <small></td><td> : ${ address.address1}.</small> </td></tr>
						<% } %>
						<% if (personWrap.telephoneContact) { %>
							<tr><td><strong>Patient's phone number</strong><small> </td><td align="left"> : ${personWrap.telephoneContact}.</small></td> </tr>
						<% } %>
						<% if ( address.cityVillage !='?') { %>
						<tr><td><strong>Village/City</strong><small></td><td> : ${ address.cityVillage}.</small> </td></tr>
						<% } %>
						<% if (address.countyDistrict != '?') { %>
						<tr><td><strong>Township</strong><small> </td><td> : ${ address.countyDistrict}.</small> </td></tr>
						<% } %>
						<% if (address.stateProvince != '?') { %>
						<tr><td><strong>State or Region</strong><small> </td><td> : ${ address.stateProvince}.</small></br> </td></tr>
						<% } %>
						<% if (patientWrap.previousHivTestStatus) { %>
			            <tr><td><strong>HIV confirmation test Status <small> </td><td> : ${patientWrap.previousHivTestStatus}</small> </td></tr>
		                <% } %>
		                <% if (patientWrap.previousHivTestStatus=="Yes") { %>
						<% if (patientWrap.previousHivTestDate) { %>
						<tr><td><strong>Date HIV+ test</strong><small></td><td> : ${patientWrap.previousHivTestDate}</small> </td></tr>
						<tr><td><strong>Plcae</strong><small></td><td> : ${patientWrap.previousHivTestPlace}</small> </td></tr>
						<% } %>
						<% } %>
						<% if(savedEntryPoint) { %>  <% if(entryPoint=='Other') {%> 
						<tr><td><strong>Entry Point</strong><small></td><td> : ${savedEntryPoint.valueCoded.name} (${otherEntryPoint})</small></br> </td></tr>
						 <% }  else { %>
						<tr><td><strong>Entry Point</strong><small></td><td> : ${savedEntryPoint.valueCoded.name}</small></br> </td></tr>
						<%}} %>
						
						<% if(entryPoint=='Patient transferred in pre ART from another clinic'||entryPoint=='Patient transferred in on ART from another HIV care or ART clinic'){ %> 
						 <% if (patientWrap.previousClinicName) { %>
						<tr><td><strong>Name Previous Clinic</strong><small></td><td> : ${patientWrap.previousClinicName}</small> </td></tr>
						<%} %>
						<% if (savedEntryPointValueDate) { %>	
							<tr><td><strong>Date Transferred in</strong><small></td><td align="left"> : ${savedEntryPointValueDate}</small></br> </td></tr>
						 <%}  } %>
						 <% if (patientWrap.nextOfKinName) { %>
							<tr><td><strong>Treatment Supporter's Name</strong></td> <td><small> : ${patientWrap.nextOfKinName}</small></td></tr>
						<% } %>
						<% if (patientWrap.nextOfKinAddress) { %>
							<tr><td><strong>Treatment Supporter's Address</strong></td> <td><small> : ${patientWrap.nextOfKinAddress}</small></td></tr>
						<% } %>
						<% if (patientWrap.nextOfKinContact) { %>
							<tr><td><strong>Treatment Supporter's phone number</strong></td> <td><small> : ${patientWrap.nextOfKinContact}</small></td></tr>
						<% } %>
					</table>
				</td>			
				<td width="50%" valign="top" >
					<table>
						
					</table>
					<br / ><br / ><br / >
					<table border="0" width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>Personal History</strong></center>
							</td>
						</tr>
						<tr>
							<td>
								<tr><td><strong>Risk Factor for HIV</strong></td> <td> : ${listAllRiskFactor}</td></tr>
								<tr><td><strong>For IDus Substitution therapy</strong></td> <td> : ${iduStatusValue} (${iduNameValue})</td></tr>
								<tr><td><strong>Literate</strong></td> <td> : ${literate}</td></tr>
								<tr><td><strong>Employed</strong></td> <td> : ${employed}</td></tr>
								<tr><td><strong>Alcoholic</strong></td> <td> : ${alcoholic} (${alcoholicType})</td></tr>
								<tr><td><strong>Income</strong></td> <td> : ${income} Kyats</td></tr>
								<tr><td><strong>Co-morbidity</strong></td> <td> : ${comorbidity} </td></tr>
								
							</td>
						</tr>
					</table>
							
				
					<table width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>Antiretroviral Treament History</strong></center>
							</td>
						</tr>
						<tr>
							<td>
								<tr>
									<td><strong>ART received</strong></td><td> : ${artReceivedVal}</td>
								</tr>
								<tr>
									<td><strong>If Yes</strong></td><td> : ${artReceivedTypeValue}</td>
								</tr>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table border="1" width="100%">
									<tr>
										<td><strong>Drug Regimen</strong></td>
										<td><strong>Duration (months)</strong></td>
										<td><strong>Place</strong></td>
									</tr>
									<% for ( e in drugMembers ) {  %>
										<% def values = e.value.split (",") %>
										<% if(values[0]) { %>	
										<tr>
											
											<td><% println  values[0] %> </td>
											<td><% println  values[1] %> </td>
											<td><% println  values[2] %><br/> </td>
										</tr>
										
									<%} } %>						
								</table>
							</td>
						</tr>
					</table>
				<table width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>End of Follow-up </strong></center>
							</td>
						</tr>
						<tr>
							<td >
								<tr><td><b>Reason : </b></td> <td>${programDiscontinuationReasonVal}</td></tr>
								<tr><td><b>Date : </b></td> <td>${dataPlaceVal}</td></tr>
							</td>
						</tr>
					</table>
		
				</td>
			</tr>
		</table>
		
		<!--HISTORY ==========================================================================================================================================-->

		<td width="50%" valign="top">
					<table border="1" width="100%">
						<tr >
							<td class="tbheader" bgcolor="#778899">
								<center><strong>Family History</strong></center>
							</td>
						</tr>
						<tr>
							<td><strong>Marital Status: <strong> ${civilStatusVal}</td>
						</tr>
						
						<tr>
							<td>
								<table border="1" width="100%">
									<tr>
										<td><strong>Name of Spouse/Children</strong></td>
										<td><strong>Age</strong></td>
										<td><strong></strong></td>
										<td><strong>Sex</strong></td>
										<td><strong>HIV +/-/ Unknown</strong></td>
										<td><strong>ART Y/N</strong></br></td>
										
									</tr>
									<% for ( e in familyMembers ) {  %>
										<% def values = e.value.split (",") %>	
										<tr>
											<td><% println  values[0] %> </td>
											<td><% println  values[1] %> </td>
											<td><% println  values[2] %> </td>
											<td><% println  values[3] %> </td>
											<td><% println  values[4] %> </td>
											<td><% println  values[5] %> </br></td>
										</tr>
									<% } %>						
								</table>
								<table width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>Tuberculosis Treatment during HIV Care</strong></center>
							</td>
						</tr>
						<tr>
							<td >
								<tr><td><b>Disease Classification : </b></td> <td>${tbTreatmentVal}</td></tr>
								<tr><td><b>TB Site : </b></td> <td>${tbSiteVal}</td></tr>
								<tr><td><b>TB Registration No. : </b></td> <td>${tbRegVal}</td></tr>
								<tr><td><b>Township : </b></td> <td>${tbTownVal}</td></tr>
								<tr><td><b>TB Clinic : </b></td> <td>${tbClinicVal}</td></tr>
								<tr><td><b>TB Regimen : </b></td> <td>${tbRegimenVal}</td></tr>
							</td>
						</tr>
					</table>
				</td>	
      <td width="50%" valign="top" >
						<table border="1" width="100%">
						<tr >
							<td class="tbheader" bgcolor="#778899">
								<center><strong>Antiretroviral treatment</strong></center>
							</td>
						</tr>
						<tr>
							<td>
								<table border="1" width="100%">
									<tr>
										<td><strong>Date</strong></td>
										<td><strong>Substitue/Switch/Stop & Reason</strong></td>
										<td><strong>Date restart</strong></td>
										<td><strong>New Regimen</strong></br></td>
									</tr>
									<% for ( rList in regimenList ) {  %>
										<% def values = rList.value.split (",") %>	
										<tr>
											<td><% println  values[0] %> </td>
											<td><% println  values[1] %> </td>
											<td><% println  values[2] %> </td>
											<td><% println  values[3] %></br> </td>
											
										</tr>
									<% } %>						
								</table>
							</td>
						</tr>
					</table>
					
					
				</td>
		</table>
		
		<table border="1" width="100%">
			<!--Exposed-infant follow-up ========================================================================================================================================== -->
			<tr >
				<td colspan="2" class="tbheader" bgcolor="#778899">
					<center><strong>Exposed Infant Follow Up</strong></center>
				</td>
			</tr> 
			<tr>
				<td colspan="2">
					<table border="1" width="100%">
						<tr >
							
							<th><strong>Exposed-Infant Name/No</strong></th>
							<th><strong>DOB</strong></th>
							<th><strong>Infant feeding practice</strong></th>
							<th><strong>CPT started date</strong></th>
							<th><strong>HIV Test type</strong></th>
							<th><strong>Result</strong></th>
							<th><strong>Result date</strong></th>
							<th><strong>Final status</strong></th>
							<th><strong>Unique Id (if confirmed)</strong></br></th>
							
						</tr>
						<% for ( d in infantList ) { %>
						<% def values = d.value.split(",")	%>
							<tr>
								<td><% println  values[0] %> </td>
								<td><% println  values[1] %> </td>
								<td><% println  values[2] %> </td>
								<td><% println  values[3] %> </td>
								<td><% println  values[4] %> </td>
								<td><% println  values[5] %> </td>
								<td><% println  values[6] %> </td>
								<td><% println  values[7] %> </td>
								<td><% println  values[8] %> </br></td>
							</tr>
						<% } %>
					</table>
				</td>
			</tr>	
			
			<!--Patient HIV care & Antiretroviral treatment Follow-up============================================================================================================= -->
			<tr>
				<td colspan="2" class="tbheader" bgcolor="#778899">
					<center><strong>Patient HIV Care & Antiretroviral Treatment Follow Up</strong></center>
				</td>
			</tr>
			<tr>
				<td valign="top" class="table" colspan="2">
					${ ui.includeFragment("chaiui", "widget/obsHistoryTable", [ id: "tblhistory", patient: currentPatient, concepts: graphingConcepts ]) }
				</td>
				
			</tr>
		</table>
		
				</div>
	<div class="widget-content" id="printArea" width="100%">
		
				
		<!--PATIENT DETAILS ==========================================================================================================================================-->
		<table border="1" width="100%">
			<tr >
				<td colspan="2" class="tbheader" bgcolor="#778899">
					<center><strong>Patient Identification</strong></center>
				</td>
			</tr>
			<tr>
				<td width="50%">
					<table>
						<tr><td><strong>Name</strong></td><td> : ${ patientName }</td></tr>
						<tr><td><strong>Age</strong></td><td align="left"> : ${ patientAge } </td></tr>
						<tr><td><strong>Date of Birth</strong></td><td align="left"> : ${ birthDate } </td></tr>
						<tr><td><strong>Gender</strong></td><td> : ${ patientGender } </td></tr>
						<% if ( address.address1) { %>
						<tr><td><strong>Address</strong> <small></td><td> : ${ address.address1}.</small> </td></tr>
						<% } %>
						<% if (personWrap.telephoneContact) { %>
							<tr><td><strong>Patient's phone number</strong><small> </td><td align="left"> : ${personWrap.telephoneContact}.</small></td> </tr>
						<% } %>
						<% if ( address.cityVillage !='?') { %>
						<tr><td><strong>Village/City</strong><small></td><td> : ${ address.cityVillage}.</small> </td></tr>
						<% } %>
						<% if (address.countyDistrict != '?') { %>
						<tr><td><strong>Township</strong><small> </td><td> : ${ address.countyDistrict}.</small> </td></tr>
						<% } %>
						<% if (address.stateProvince != '?') { %>
						<tr><td><strong>State or Region</strong><small> </td><td> : ${ address.stateProvince}.</small></br> </td></tr>
						<% } %>
						<% if (patientWrap.previousHivTestStatus) { %>
			            <tr><td><strong>HIV confirmation test Status <small> </td><td> : ${patientWrap.previousHivTestStatus}</small> </td></tr>
		                <% } %>
		                <% if (patientWrap.previousHivTestStatus=="Yes") { %>
						<% if (patientWrap.previousHivTestDate) { %>
						<tr><td><strong>Date HIV+ test</strong><small></td><td> : ${patientWrap.previousHivTestDate}</small> </td></tr>
						<tr><td><strong>Plcae</strong><small></td><td> : ${patientWrap.previousHivTestPlace}</small> </td></tr>
						<% } %>
						<% } %>
						
						<% if(savedEntryPoint) { %>  <% if(entryPoint=='Other') {%> 
						<tr><td><strong>Entry Point</strong><small></td><td> : ${savedEntryPoint.valueCoded.name} (${otherEntryPoint})</small></br> </td></tr>
						 <% }  else { %>
						<tr><td><strong>Entry Point</strong><small></td><td> : ${savedEntryPoint.valueCoded.name}</small></br> </td></tr>
						<%}} %>
						<% if(entryPoint=='Patient transferred in pre ART from another clinic'||entryPoint=='Patient transferred in on ART from another HIV care or ART clinic'){ %> 
						 <% if (patientWrap.previousClinicName) { %>
						<tr><td><strong>Name Previous Clinic</strong><small></td><td> : ${patientWrap.previousClinicName}</small> </td></tr>
						<%} %>
						<% if (savedEntryPointValueDate) { %>	
							<tr><td><strong>Date Transferred in</strong><small></td><td align="left"> : ${savedEntryPointValueDate}</small></br> </td></tr>
						 <%}  } %>
						 <% if (patientWrap.nextOfKinName) { %>
							<tr><td><strong>Treatment Supporter's Name</strong></td> <td><small> : ${patientWrap.nextOfKinName}</small></td></tr>
						<% } %>
						<% if (patientWrap.nextOfKinAddress) { %>
							<tr><td><strong>Treatment Supporter's Address</strong></td> <td><small> : ${patientWrap.nextOfKinAddress}</small></td></tr>
						<% } %>
						<% if (patientWrap.nextOfKinContact) { %>
							<tr><td><strong>Treatment Supporter's phone number</strong></td> <td><small> : ${patientWrap.nextOfKinContact}</small></td></tr>
						<% } %>
					</table>
				</td>			
				<td width="50%" valign="top" >
					<table>
						
					</table>
					<br / ><br / ><br / >
					<table border="0" width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>Personal History</strong></center>
							</td>
						</tr>
						<tr>
							<td>
								<tr><td><strong>Risk Factor for HIV</strong></td> <td> : ${listAllRiskFactor}</td></tr>
								<tr><td><strong>For IDus Substitution therapy</strong></td> <td> : ${iduStatusValue} (${iduNameValue})</td></tr>
								<tr><td><strong>Literate</strong></td> <td> : ${literate}</td></tr>
								<tr><td><strong>Employed</strong></td> <td> : ${employed}</td></tr>
								<tr><td><strong>Alcoholic</strong></td> <td> : ${alcoholic} (${alcoholicType})</td></tr>
								<tr><td><strong>Income</strong></td> <td> : ${income} Kyats</td></tr>
								<tr><td><strong>Co-morbidity</strong></td> <td> : ${comorbidity} </td></tr>
								
							</td>
						</tr>
					</table>
							
				
					<table width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>Antiretroviral Treament History</strong></center>
							</td>
						</tr>
						<tr>
							<td>
								<tr>
									<td><strong>ART received</strong></td><td> : ${artReceivedVal}</td>
								</tr>
								<tr>
									<td><strong>If Yes</strong></td><td> : ${artReceivedTypeValue}</td>
								</tr>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table border="1" width="100%">
									<tr>
										<td><strong>Drug Regimen</strong></td>
										<td><strong>Duration (months)</strong></td>
										<td><strong>Place</strong></td>
									</tr>
									<% for ( e in drugMembers ) {  %>
										<% def values = e.value.split (",") %>
										<% if(values[0]) { %>	
										<tr>
											
											<td><% println  values[0] %> </td>
											<td><% println  values[1] %> </td>
											<td><% println  values[2] %><br/> </td>
										</tr>
										
									<%} } %>						
								</table>
							</td>
						</tr>
					</table>
				<table width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>End of Follow-up </strong></center>
							</td>
						</tr>
						<tr>
							<td >
								<tr><td><b>Reason : </b></td> <td>${programDiscontinuationReasonVal}</td></tr>
								<tr><td><b>Date : </b></td> <td>${dataPlaceVal}</td></tr>
							</td>
						</tr>
					</table>
		
				</td>
			</tr>
		</table>
		
		<!--HISTORY ==========================================================================================================================================-->
		
		<td width="50%" valign="top">
					<table border="1" width="100%">
						<tr >
							<td class="tbheader" bgcolor="#778899">
								<center><strong>Family History</strong></center>
							</td>
						</tr>
						<tr>
							<td><strong>Marital Status: <strong> ${civilStatusVal}</td>
						</tr>
						
						<tr>
							<td>
								<table border="1" width="100%">
									<tr>
										<td><strong>Name of Spouse/Children</strong></td>
										<td><strong>Age</strong></td>
										<td><strong></strong></td>
										<td><strong>Sex</strong></td>
										<td><strong>HIV +/-/ Unknown</strong></td>
										<td><strong>ART Y/N</strong></br></td>
										
									</tr>
									<% for ( e in familyMembers ) {  %>
										<% def values = e.value.split (",") %>	
										<tr>
											<td><% println  values[0] %> </td>
											<td><% println  values[1] %> </td>
											<td><% println  values[2] %> </td>
											<td><% println  values[3] %> </td>
											<td><% println  values[4] %> </td>
											<td><% println  values[5] %> </br></td>
										</tr>
									<% } %>						
								</table>
								<table width="100%">
						<tr>
							<td colspan="2" class="tbheader" bgcolor="#778899">
								<center><strong>Tuberculosis Treatment during HIV Care</strong></center>
							</td>
						</tr>
						<tr>
							<td >
								<tr><td><b>Disease Classification : </b></td> <td>${tbTreatmentVal}</td></tr>
								<tr><td><b>TB Site : </b></td> <td>${tbSiteVal}</td></tr>
								<tr><td><b>TB Registration No. : </b></td> <td>${tbRegVal}</td></tr>
								<tr><td><b>Township : </b></td> <td>${tbTownVal}</td></tr>
								<tr><td><b>TB Clinic : </b></td> <td>${tbClinicVal}</td></tr>
								<tr><td><b>TB Regimen : </b></td> <td>${tbRegimenVal}</td></tr>
							</td>
						</tr>
					</table>
				</td>	
      <td width="50%" valign="top" >
						<table border="1" width="100%">
						<tr >
							<td class="tbheader" bgcolor="#778899">
								<center><strong>Antiretroviral treatment</strong></center>
							</td>
						</tr>
						<tr>
							<td>
								<table border="1" width="100%">
									<tr>
										<td><strong>Date</strong></td>
										<td><strong>Substitue/Switch/Stop & Reason</strong></td>
										<td><strong>Date restart</strong></td>
										<td><strong>New Regimen</strong></br></td>
									</tr>
									<% for ( rList in regimenList ) {  %>
										<% def values = rList.value.split (",") %>	
										<tr>
											<td><% println  values[0] %> </td>
											<td><% println  values[1] %> </td>
											<td><% println  values[2] %> </td>
											<td><% println  values[3] %></br> </td>
											
										</tr>
									<% } %>						
								</table>
							</td>
						</tr>
					</table>
					
					
				</td>
		</table>
		<table border="1" width="100%">
			<!--Exposed-infant follow-up ========================================================================================================================================== -->
			<tr >
				<td colspan="2" class="tbheader" bgcolor="#778899">
					<center><strong>Exposed Infant Follow Up</strong></center>
				</td>
			</tr> 
			<tr>
				<td colspan="2">
					<table border="1" width="100%">
						<tr >
							
							<th><strong>Exposed-Infant Name/No</strong></th>
							<th><strong>DOB</strong></th>
							<th><strong>Infant feeding practice</strong></th>
							<th><strong>CPT started date</strong></th>
							<th><strong>HIV Test type</strong></th>
							<th><strong>Result</strong></th>
							<th><strong>Result date</strong></th>
							<th><strong>Final status</strong></th>
							<th><strong>Unique Id (if confirmed)</strong></br></th>
							
						</tr>
						<% for ( d in infantList ) { %>
						<% def values = d.value.split(",")	%>
							<tr>
								<td><% println  values[0] %> </td>
								<td><% println  values[1] %> </td>
								<td><% println  values[2] %> </td>
								<td><% println  values[3] %> </td>
								<td><% println  values[4] %> </td>
								<td><% println  values[5] %> </td>
								<td><% println  values[6] %> </td>
								<td><% println  values[7] %> </td>
								<td><% println  values[8] %> </br></td>
							</tr>
						<% } %>
					</table>
				</td>
			</tr>	
			
			<!--Patient HIV care & Antiretroviral treatment Follow-up============================================================================================================= -->
			<tr>
				<td colspan="2" class="tbheader" bgcolor="#778899">
					<center><strong>Patient HIV Care & Antiretroviral Treatment Follow Up</strong></center>
				</td>
			</tr>
			<tr>
				<td valign="top" class="table" colspan="2">
					${ ui.includeFragment("chaiui", "widget/obsHistoryTable", [ id: "tblhistory", patient: currentPatient, concepts: graphingConcepts ]) }
				</td>
				
			</tr>
		</table>
		
	</div>
	
	</br></br>
	<div>
	<input type="button" id="toexcel" value="Export as Excel" onclick="tableToExcel('printArea')"/>
	<input type="button" id="toprint" value="Print" onclick="print();"/>
	<button onclick="ui.navigate('${returnUrl}')"><b>Back</b></button>
	</div>
</div>
<% } %>