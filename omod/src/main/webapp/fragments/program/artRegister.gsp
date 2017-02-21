
<%
	ui.decorateWith("chaiui", "panel", [ heading: "ART REGISTER" ])
%>


<% if (config.complete) { %>
<div class="ke-stack-item">
	<div class="widget-content">
		
			
		<table id="table1"  width="100%" border="1" bordercolor="#000000" style="vertical-align:top" >
        <tr>
        <td width="100%" colspan="7">
        <table border="1" width="100%" style="vertical-align:top">
		<tr bgcolor="#778899">
				<td colspan="7">
					<h4><strong><center>Patient Identification</center> </strong></h4>
				</td>
			</tr>
			<tr>
				<td colspan="3" style="text-align: left; vertical-align: top;  padding-left:1%">
				
						<br/><strong>Date  of start of ART : </strong> 
						<br/> <strong>Registration Number :</strong> 
						<br/><strong>Name : </strong>
						<br/><strong>Address : </strong>
						<% if (personWrap.telephoneContact) { %>
						<br/><strong>Patient's contact number : </strong>
						<% } %>
						<br/><strong>Age : </strong>
						<br/><strong>Date of Birth :</strong>
						<br/><strong>Gender :  </strong>
						<br/>
						<% if (patientWrap.nextOfKinName) { %>
						<strong>Treatment Supporter's Name: </strong>
						<% } %>
						<% if (patientWrap.nextOfKinContact) { %>
						<br/><strong>Treatment Supporter's phone number: </strong>
						<% } %>
						
						<br/>
						<br/>
						 
				</td>
                <td colspan="4" style="text-align: left; vertical-align: top;  padding-left:1%">
				
						<br/> <% if(artInitiationDate) { %>  ${artInitiationDate} <% } %>
						<br/> <% if(patientWrap.napArtRegistrationNumber) { %>  ${patientWrap.napArtRegistrationNumber  } <% } %>
						<br/>${ patientName }
						<br/><% if(address.address1) { %> ${ address.address1},  <%} %> <% if(address.cityVillage !='?') { %> ${address.cityVillage}, <%} %>
							<% if(address.countyDistrict !='?') { %> ${ address.countyDistrict},  <%} %> <% if(address.stateProvince !='?') { %> ${address.stateProvince} <%} %>
						<% if (personWrap.telephoneContact) { %>
						<br/><small> ${personWrap.telephoneContact}.</small>
						<% } %>
						<br/> ${ patientAge }
						<br/>${ birthDate }
						<br/> ${ patientGender } 
						<br/>
						<% if (patientWrap.nextOfKinName) { %>
						<small>${patientWrap.nextOfKinName}</small>
						<% } %>
						<% if (patientWrap.nextOfKinContact) { %>
						<br/><small>${patientWrap.nextOfKinContact}</small>
						<% } %>
						
						<br/>
						<br/>
					
				</td>
			</tr>	
            </table>
            </td>
            </tr>
				
								<!--DRUGS ========================================================================================================================================== -->
			<tr>
				<td  valign="top" colspan="3">
					<table width="100%" border="1">
						<tr>
							<td colspan="3" class="tbheader" bgcolor="#778899">
								<center><strong>Drug History</strong></center>
							</td>
						</tr>
						<tr>
							<td colspan="1">
								<% if(artReceivedVal) { %>
								<strong>ART received</strong></td><td colspan="2"> : ${artReceivedVal}</td>
								</tr>
                                <tr>
									<td colspan="1">
								<% } %>
								<%if(artReceivedTypeValue) {%>  
								<strong>If Yes</strong></td><td colspan="2"> : ${artReceivedTypeValue}</td>
								</tr>
                                <tr>
									<td colspan="1">
								<% } %>
								<% if(artReceivedPlaceValue) {  %>
								<strong>Place</strong></td><td colspan="2"> : ${artReceivedPlaceValue}</td>
								</tr>
                                <tr>
									<td colspan="1">
								<% } %>
								<% if(drugStartDateVal) {  %>								
								<strong>Start Date</strong></td><td colspan="2"> : ${drugStartDateVal}</br></td>
								</tr>
                                <tr>
									<td colspan="1">
								<% } %>
								<% if(drugDurationVal) {  %>
								<strong>Duration</strong></td><td colspan="2"> : ${drugDurationVal} Months</br></td>
								</tr>
                                <tr>
									<td colspan="1">
								<% } %>
								<%  if(drugNameVal) {  %>								
								<strong>Drug Regimen</strong></td><td colspan="2"> : ${drugNameVal}</td>
								
								<% }  %>	</tr>	</table>						
							</td>
						<td valign="top" colspan="4">
						
					<table border="1" width="100%">
						<tr >
							<td colspan="4"  bgcolor="#778899">
								<center><strong>Antiretroviral treatment</strong></center>
							</td>
						</tr>
						<tr>
							
										<td colspan="1" style="padding-left:1%"><strong>Date</strong></td>
										<td colspan="1" style="padding-left:1%"><strong>Reason</strong></td>
										<td colspan="1" style="padding-left:1%"><strong>Date restart</strong></td>
										<td colspan="1" style="padding-left:1%"><strong>New Regimen</strong></br></td>
									</tr>
									<% for ( rList in regimenList ) {  %>
										<% def values = rList.value.split (",") %>	
										<tr>
											<td colspan="1" style="padding-left:1%"><% println  values[0] %> </td>
											<td colspan="1" style="padding-left:1%"><% println  values[1] %> </td>
											<td colspan="1" style="padding-left:1%"><% println  values[2] %> </td>
											<td colspan="1" style="padding-left:1%"><% println  values[3] %></br> </td>
											
										
									<% } %>	
                                    </tr>					
								</table>
							</td>
						</tr>
					
		
		<tr >
        <td width="100%" colspan="7">
        <table width="100%" border="1">
             <tr bgcolor="#778899">
				<td colspan="7" valign="top">
					<h4><strong><center>ART details</center></strong></h4>
				</td>
			</tr>
			<tr>
				<td colspan="3" style="text-align: left; vertical-align: top;  width: 30%; padding-left:1%">
												
						
						
						<br/><strong>End of follow-up Cause : </strong>
						<br/><strong>End of follow-up Date : </strong>
						<br/>
						
				</td>
                <td colspan="4" style="text-align: left; vertical-align: top; width: 70%; padding-left:1%">
						
						
						<br/>${dicontinuationReasonVal}
						<br/>${dicontinuationDateVal}
						<br/>
						<br/>
						
				</td>
			</tr>
            </table>
            </td>
            </tr>
			
			<tr>
				<td colspan="7" bordercolor="#000000" width="50%" valign="top">
					${ ui.includeFragment("chaiui", "widget/obsHistoryTable", [ id: "tblhistory", patient: currentPatient, concepts: graphingConcepts ]) }
				</td>
				
			</tr>
		</table>
		
	</div>
	<a id="dlink"  style="display:none;"></a> 
	<div>
	<input type="button" onClick="tableToExcel('table1','ART Register','${patientWrap.napArtRegistrationNumber}-ART Register.xls');"  value="Export as Excel" />
	<button onclick="ui.navigate('${returnUrl}')"><b>Back</b></button>
	</div
</div>
<% } %>




<script type="text/javascript">
var tableToExcel = (function() {
  var uri = 'data:application/vnd.ms-excel;base64,'
    , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table >{table}</table></body></html>'
    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
    , format = function(s, c) { return s.replace(/{(\\w+)}/g, function(m, p) { return c[p]; }) }
  return function(table, name, filename) {
    if (!table.nodeType) table = document.getElementById(table)
    var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
    
            document.getElementById("dlink").href = uri + base64(format(template, ctx));
            document.getElementById("dlink").download = filename;
            document.getElementById("dlink").click();
    
  }

})()
</script>
  