<%
	ui.decorateWith("chaiui", "panel", [ heading: (config.heading ?: "Edit Patient"), frameOnly: true ])
	
	ui.includeJavascript("chaiemr", "controllers/addresshierarchy.js")

	def patientNameField = [
			[
					[ object: command, property: "personName.givenName", label: "Patient's Name *" ]
			]
	]
	
	def fatherNameField = [
			[
					[ object: command, property: "fatherName", label: "Father's Name" ]
			]
	]
	
	def nationalId = [
			[
					[ object: command, property: "nationalId", label: "National ID Number" ]
			]
	]

	def ingoNameFields = [
		[
				[ object: command, property: "ingoTypeConcept", label:"", config: [ style: "list", answerTo: ingoConcept ] ],
			]	
	]


	def otherDemogFieldRows = [
			[
					[ object: command, property: "dead", label: "Deceased " ],
					[ object: command, property: "deathDate", label: "Date of death" ]
			]
	]
	
	def placeOfBirth = [
			[
					[ object: command, property: "placeOfBirth", label: "Place Of Birth", config: [ style: "search", answerTo: townShipList ]  ]
			]
	]

	def nextOfKinFieldRows = [
			[
					[ object: command, property: "nameOfNextOfKin", label: "Name" ],
					
					[ object: command, property: "nextOfKinContact", label: "Contact Number" ],
					[ object: command, property: "nextOfKinAddress", label: "Permanenet Address", config: [ type: "textarea", size: 20 ] ]
			]
	]
	
	def enrollmentStatus = [
			[
					[ object: command, property: "enrollmentName", label: "Status at enrollment ", config: [ style: "list", answerTo: enrollmentList ] ],
					[ object: command, property: "otherStatus", label: "If other, Please specify" ]
			]		
	]
	
	def patientSource1 = [
			[
					[ object: command, property: "entryPoint", label: "Entry Point ", config: [ style: "list", answerTo: entryPointList ] ],
					[ object: command, property: "otherEntryPoint", label: "If other, Please specify" ]
			]		
	] 
	
	def patientContact = [
		[
				[ object: command, property: "telephoneContact", label: "Contact Number" ]
		]		
	]
	
	def patientSource2 = [
				
	]   

	def patientSource3 = [
				
	]  
	
	def addressFieldRows = [
			[
					[ object: command, property: "personAddress.address1", label: "Permanent Address ", config: [ type: "textarea", size: 20 ] ],
					[ object: command, property: "personAddress.address2", label: "Temporary Address ", config: [ type: "textarea", size: 20 ] ]
			]
	]
%>

<form id="edit-patient-form" method="post" action="${ ui.actionLink("chaiemr", "patient/editPatient", "savePatient") }">
	<% if (command.original) { %>
		<input type="hidden" name="personId" value="${ command.original.id }"/>
	<% } %>

	<div class="ke-panel-content">

		<div class="ke-form-globalerrors" style="display: none"></div>

		<div class="ke-form-instructions">
			<strong>*</strong> indicates a required field
		</div>

		<fieldset>
			<legend>ID Numbers</legend>

			<table>
				<tr>
					<td class="ke-field-label">Master Patient Index</td>
					<td>${ ui.includeFragment("chaiui", "widget/field", [ object: command, property: "masterPatientIndex" ]) }</td>
					<td class="ke-field-instructions"><% if (!command.masterPatientIndex) { %>(if available)<% } %></td>
				</tr>
				<tr>
					<td class="ke-field-label">Registration Number</td>
					<td>${ ui.includeFragment("chaiui", "widget/field", [ object: command, property: "registrationNumber" ]) }</td>
					<td class="ke-field-instructions"><% if (!command.registrationNumber) { %>(if available)<% } %></td>
				</tr>
				<tr>
					<td class="ke-field-label">INGO HEP Registration Number</td>
					<td>${ ui.includeFragment("chaiui", "widget/field", [ object: command, property: "hepCRegistrationNumber" ]) }</td>
					<td class="ke-field-instructions"><% if (!command.hepCRegistrationNumber) { %>(if available)<% } %></td>
					
					<td class="ke-field-label"> INGO NAME </td>
					<td>	<% ingoNameFields.each { %>
							${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }<% } 
						%> </td> 
				</tr>
				<tr>
					<td class="ke-field-label"><b>Patient ID*</b></td>
					<td><input name="systemPatientId" style="width: 260px" value=${ patientIdentifier} readonly autocomplete="off" ></td>
				</tr>
			</table>

		</fieldset>

		<fieldset>
			<legend>Demographics</legend>

			<% patientNameField.each { %>
			${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
			<% } %>
			
			<% fatherNameField.each { %>
			${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
			<% } %>
			
			<% nationalId.each { %>
			${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
			<% } %>

			<table>
				<tr>
					<td valign="top">
						<label class="ke-field-label">Birthdate *</label>
						<span class="ke-field-content">
							${ ui.includeFragment("chaiui", "widget/field", [ id: "patient-birthdate", object: command, property: "birthdate" ]) }

							
							&nbsp;&nbsp;&nbsp;

							<span id="from-age-button-placeholder"></span>
						</span>
					</td>
				</tr>
				<tr>
					<td>
						<% if (recordedAsDeceased) { %>
							<div class="ke-warning" style="margin-bottom: 5px">
								<% otherDemogFieldRows.each { %>
									${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
								<% } %>
							</div>
						<% } %>
					</td>			
				</tr>
				<tr>
				<td valign="top">
						<label class="ke-field-label">Gender *</label>
						<span class="ke-field-content">
							<input type="radio" name="gender" value="F" id="gender-F" ${ command.gender == 'F' ? 'checked="checked"' : '' }/> Female
							<input type="radio" name="gender" value="M" id="gender-M" ${ command.gender == 'M' ? 'checked="checked"' : '' }/> Male
							<span id="gender-F-error" class="error" style="display: none"></span>
							<span id="gender-M-error" class="error" style="display: none"></span>
						</span>
					</td>
				</tr>
			</table>

			<% placeOfBirth.each { %>
			${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
			<% } %>
		</fieldset>

		<fieldset>
			<legend>Address</legend>

			<% addressFieldRows.each { %>
				${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
			<% } %>

		<div ng-controller="AddresshierarchyCtrl" data-ng-init="init()">
        <table>
        <tr>
        
        <td valign="top">
        <label class="ke-field-label">State / Region</label>
        <span class="ke-field-content">
        <select style="width: 180px;" name="personAddress.stateProvince" ng-model="myState" ng-options="state for state in states track by state" ng-change="stateSelection(myState)"></select>
        </span>
        </td>
        
        <td valign="top">
        <label class="ke-field-label">Township</label>
        <span class="ke-field-content">
        <select style="width: 180px;" name="personAddress.countyDistrict" ng-model="myTownship" ng-options="township for township in townships track by township" ng-change="townshipSelection(myState,myTownship)"></select>
        </span>
        </td>
        
        <td valign="top">
        <label class="ke-field-label">Town / Village</label>
        <span class="ke-field-content">
        <select style="width: 180px;"name="personAddress.cityVillage" ng-model="myVillage" ng-options="village for village in villages track by village"></select>
        </span>
        </td>
     
        </tr>
        </table>
        </div>
        
         <% patientContact.each { %>
			   ${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
		<% } %>
        
		</fieldset>

		<fieldset>
			<legend>Treatment Supporter's</legend>

			 <% nextOfKinFieldRows.each { %>
			   ${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
			 <% } %>

		</fieldset>

		<fieldset>
			<legend>Enrollment Status</legend>

			<table>
				<tr>
				<td> 
				     
					<center><label class="ke-field-label">Status at enrollment</label></center>
					
					<span class="ke-field-content" >
						
							${ ui.includeFragment("chaiui", "widget/field",[ id:"enrollmentName", object: command, property: "enrollmentName", config: [ style: "list", answerTo: enrollmentList ] ]) }
		
					
					</span>
				</td>
				 <td  id="otherStatus">
						<center><label class="ke-field-label">If other, Please specify</label></center>
						<span class="ke-field-content">
							${ ui.includeFragment("chaiui", "widget/field",[ id:"otherStatus",object: command, property: "otherStatus" ]) }
						</span>	
					</td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset>
			

			<table>
				<tr>
				<td> 
				    
					<center><label class="ke-field-label">Entry Point</label></center>
					
				<span class="ke-field-content" >
					${ ui.includeFragment("chaiui", "widget/field",[ id:"entryPoint", object: command, property: "entryPoint", config: [ style: "list", answerTo: entryPointList ] ]) }
		
					
					</span>
				</td>
				 <td  id="otherEntryPoint">
						<center><label class="ke-field-label">If other, Please specify</label></center>
						<span  class="ke-field-content">
							${ ui.includeFragment("chaiui", "widget/field",[ id:"otherEntryPoint",object: command, property: "otherEntryPoint" ]) }
						</span>	
					</td>
				</tr>	
				
				</tr>
			</table>
			  <%if(command.original) { %>			 
			 <table>
				<tr> 
			                  <td  id="previousClinicName">
			                     <center><label class="ke-field-label">Previous Clinic Name</label></center>
			             <span  class="ke-field-content">
				 ${ ui.includeFragment("chaiui", "widget/field", [id:"previousClinicName", object: command, property: "previousClinicName" , config: [ width: 350 ] ]) }
				 </span>	
					</td>
					<td  id="transferredInDateTd">
						 <center><label class="ke-field-label">Transferred In Date</label></center>
						<span  class="ke-field-content">
							${ ui.includeFragment("chaiui", "widget/field",[ id:"transferredInDate",object: command, property: "transferredInDate", config: [ width: 300 ] ]) }
						</span>	
					</td>
					</tr>
					</table>
			 <% }  else {%>
				  
				  		<table>
				<tr> 
				   <td  id="previousClinicName">
				   <center><label class="ke-field-label">Previous Clinic Name</label></center>
			       <span  class="ke-field-content">
				   ${ ui.includeFragment("chaiui", "widget/field", [id:"previousClinicName", object: command, property: "previousClinicName" ,label: "Previous Clinic Name ",  config: [ width: 350 ] ]) }
				    </span>	
					</td>
				    <td  id="transferredInDateId">
				     <center><label class="ke-field-label">Transferred In Date</label></center>
				    <span  class="ke-field-content">
					${ ui.includeFragment("chaiui", "widget/field",[id:"transferredInDate",object: command, property: "transferredInDate", label: "Transferred In Date " , class: java.util.Date, initialValue: new Date(), config: [ width: 300 ] ]) }
					</span>	
					</td>
				</tr>
			</table>
			 <% } %>
			

		</fieldset>

			<fieldset>
			<legend>HIV Testing</legend>

			<table>
				<tr>
					<td valign="top">
						<label class="ke-field-label">Have you performed HIV confirmatory test previously? *</label>
						<span class="ke-field-content" >
							<input type="radio" name="hivTestPerformed" value="Yes" id="hivTestPerformed-Yes" onClick="getDateAndPlace();" ${ command.hivTestPerformed == 'Yes' ? 'checked="checked"' : '' }/> Yes
							<input type="radio" name="hivTestPerformed" value="No" id="hivTestPerformed-No" onClick="getDateAndPlace();" ${ command.hivTestPerformed == 'No' ? 'checked="checked"' : '' }/> No
							<span id="hivTestPerformed-Yes-error" class="error" style="display: none"></span>
							<span id="hivTestPerformed-No-error" class="error" style="display: none"></span>
						</span>
					</td>
					<td valign="top"></td>
					<td valign="top"  id="hivTestPerformedPlace">
						<center><label class="ke-field-label">Place*</label></center>
						<span class="ke-field-content">
							${ ui.includeFragment("chaiui", "widget/field", [ object: command, property: "hivTestPerformedPlace" ]) }
						</span>	
					</td>
					<td valign="top"></td>
					<td valign="top" id="hivTestPerformedDate">
						<label class="ke-field-label">Date of Test*</label>
						<span class="ke-field-content">
							${ ui.includeFragment("chaiui", "widget/field", [id: "patient-hivTestPerformedDate", object: command, property: "hivTestPerformedDate" ]) }
						</span>
					</td>
					<td valign="top" id="checkInField">
						<span class="ke-field-content">
							<input name="checkInType" id="checkInType" ${ command.checkInType == '1' ? '1' : '0' }/> 
						</span>		
					</td>
				</tr>
			</table>

			

		</fieldset>

	</div>
	
	<div class="ke-panel-footer">
		<% if (command.original) { %>
			<button onClick="checkIn(1)" type="submit">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/ok.png") }" /> ${"Save Changes" }
			</button>		
		<% } else {%>
			<!--<button onClick="checkIn(1)" type="submit">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/ok.png") }" /> ${ "Create Patient and Check In" }
			</button>-->
			<button onClick="return validateDateOfRegistration();checkIn(0)" type="submit">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/ok.png") }" /> ${ "Create Patient" }
			</button>
			<input type="text" id="dateOfRegistration" name="dateOfRegistration" placeholder="Date of registration">
			<script type="text/javascript">
                        jq(document).ready(function () {
                            jq('#dateOfRegistration').datepicker({
                                dateFormat: "dd-M-yy", 
                                showAnim: 'blind'
                            });
                        });
                    </script>
		<% } %>
		<% if (config.returnUrl) { %>
			<button type="button" class="cancel-button"><img src="${ ui.resourceLink("chaiui", "images/glyphs/cancel.png") }" /> Cancel</button>
		<% } %>
	</div>
	
</form>

<!-- You can't nest forms in HTML, so keep the dialog box form down here -->
${ ui.includeFragment("chaiui", "widget/dialogForm", [
		buttonConfig: [ id: "from-age-button", label: "from age", iconProvider: "chaiui", icon: "glyphs/calculate.png" ],
		dialogConfig: [ heading: "Calculate Birthdate", width: 40, height: 40 ],
		fields: [
				[ label: "Age in y/m/w/d", formFieldName: "age", class: java.lang.String ],
				[ label: "On date", formFieldName: "now", class: java.util.Date, initialValue: new Date() ]
		],
		fragmentProvider: "chaiemr",
		fragment: "emrUtils",
		action: "birthdateFromAge",
		onSuccessCallback: "updateBirthdate(data);",
		onOpenCallback: """jQuery('input[name="age"]').focus()""",
		submitLabel: ui.message("general.submit"),
		cancelLabel: ui.message("general.cancel")
]) }

<script type="text/javascript">
var patientId=${patientId};

jQuery(document).ready(function(){  if(patientId==null)
                                     { jq("#otherStatus").hide();
                                       jq("#otherEntryPoint").hide(); 
                                       jq("#transferredInDateId").hide(); 
                                       jq("#previousClinicName").hide();
                                     }
                                    else
                                    {
                                    var stats=${statusother};
                                    if(stats==5622)
                                    { jq("#otherStatus").show();
                                          
                                    }
                                     else
                                    {
                                    jq("#otherStatus").hide();
                                    }
                                    var entry=${pointentry};
                                      if(entry==5622)
                                     {
                                     jq("#otherEntryPoint").show();
                                     jq("#previousClinicName").hide();
                                     jq("#transferredInDateTd").hide();
                                    
                                     } 
                                     else if(entry==162870||entry==162871)
                                     {
                                     jq("#previousClinicName").show();
                                     jq("#transferredInDateTd").show();
                                    
                                     jq("#otherEntryPoint").hide();
                                     } 
                                      else
                                     {
                                      jq("#otherEntryPoint").hide();
                                      jq("#previousClinicName").hide();
                                     jq("#transferredInDateTd").hide();
                                     
                                     }
                                     
                                     }
                                   
                                     
  
 document.getElementById('checkInField').style.display='none';
	var hivTestPerformedYes = document.getElementById('hivTestPerformed-Yes').checked; 
			if(hivTestPerformedYes==true)
			{
			  document.getElementById('hivTestPerformedDate').style.display='';
			  document.getElementById('hivTestPerformedPlace').style.display=''; 
			}
			else
			{
			  document.getElementById('hivTestPerformedDate').style.display='none';
  			  document.getElementById('hivTestPerformedPlace').style.display='none'; 
			}
			var m_names = new Array("Jan", "Feb", "Mar", 
			"Apr", "May", "Jun", "Jul", "Aug", "Sep", 
			"Oct", "Nov", "Dec");

			var d = new Date();
			var curr_date = d.getDate();
			var curr_month = d.getMonth();
			var curr_year = d.getFullYear();
			var newDate = curr_date + "-" + m_names[curr_month]	+ "-" + curr_year;
			if(document.getElementById('dateOfRegistration')!=null){
				document.getElementById('dateOfRegistration').value=newDate;
			}
			
		jq('#enrollmentName').on('change', function() 
            {  
			getSelectOption(jq(this).val());
			
		});
		jq('#entryPoint').on('change', function() 
          {
			getSelectEntryPoint(jq(this).val());
		});
	});
	

	jQuery(function() {
		jQuery('#from-age-button').appendTo(jQuery('#from-age-button-placeholder'));
		
		jQuery('#edit-patient-form .cancel-button').click(function() {
			ui.navigate('${ config.returnUrl }');
		});

		chaiui.setupAjaxPost('edit-patient-form', {
			onSuccess: function(data) {
				if (data.id) {
					<% if (config.returnUrl) { %>
					ui.navigate('${ config.returnUrl }');
					<% } else { %>
					
					 if (document.getElementById('checkInType').value==1) { 
						ui.navigate('chaiemr', 'registration/registrationHome');
						} else { 
						ui.navigate('chaiemr', 'registration/registrationHome');
					 } 
					<% } %>
				} else {
					chaiui.notifyError('Saving patient was successful, but unexpected response');
				}
			}
		});
	});
	
	function checkIn(check){
		document.getElementById('checkInType').value = check;
	}

	function getDateAndPlace(){
			var hivTestPerformedYes = document.getElementById('hivTestPerformed-Yes').checked; 
			if(hivTestPerformedYes==true)
			{
			  document.getElementById('hivTestPerformedDate').style.display='';
			  document.getElementById('hivTestPerformedPlace').style.display=''; 
			}
			else
			{
			  document.getElementById('hivTestPerformedDate').style.display='none';
  			  document.getElementById('hivTestPerformedPlace').style.display='none'; 
			}
	};
    function getSelectOption(elem)
    {   
        
        if(elem== 5622){ 
            document.getElementById("otherStatus").style.display = "";
        }
        
        else
        {
          document.getElementById("otherStatus").style.display = 'none';
        }
    };
    function getSelectEntryPoint(elem)
    { 
       
     if(patientId==null) 
             {  if(elem== 5622){
            document.getElementById("otherEntryPoint").style.display = "";
            document.getElementById("previousClinicName").style.display ='none';
            document.getElementById("transferredInDateId").style.display = 'none';
           
    }
             
           else  if(elem== 162870||elem== 162871)
               { 
            document.getElementById("otherEntryPoint").style.display = 'none';
            document.getElementById("previousClinicName").style.display = "";
            document.getElementById("transferredInDateId").style.display = "";
           
           
        }
         else
        {
          document.getElementById("otherEntryPoint").style.display = 'none';
          document.getElementById("previousClinicName").style.display ='none';
          document.getElementById("transferredInDateId").style.display = 'none';
            
        }
        }
        else
        {  if(elem== 5622){
            document.getElementById("otherEntryPoint").style.display = "";
            document.getElementById("previousClinicName").style.display ='none';
            document.getElementById("transferredInDateTd").style.display = 'none';
           
    }
       else if(elem== 162870||elem== 162871)
        { 
            document.getElementById("otherEntryPoint").style.display = 'none';
            document.getElementById("previousClinicName").style.display = "";
            document.getElementById("transferredInDateTd").style.display = "";
           
           
        
        }
         else
        { 
          document.getElementById("otherEntryPoint").style.display = 'none';
          document.getElementById("previousClinicName").style.display ='none';
            document.getElementById("transferredInDateTd").style.display = 'none';
            
        }
        }
        
       
   };
	function updateBirthdate(data) {
		var birthdate = new Date(data.birthdate);

		chaiui.setDateField('patient-birthdate', birthdate);
		chaiui.setRadioField('patient-birthdate-estimated', 'true');
	}
	
	function validateDateOfRegistration() {
	var dateOfRegistration = jQuery("#dateOfRegistration").val();
	if(dateOfRegistration==""){
	alert("Please Enter Date Of Registration");
	return false;
	} 
	}
</script>

<style>

</style>