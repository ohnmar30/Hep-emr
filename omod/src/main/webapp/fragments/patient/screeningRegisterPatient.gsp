<%
	ui.decorateWith("chaiui", "panel", [ heading: (config.heading ?: "Screening Registration"), frameOnly: true ])
	
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
	
	
%>

<form id="screening-registration-form" method="post" action="${ ui.actionLink("chaiemr", "patient/screeningRegisterPatient", "savePatient") }">
	<% if (command.original) { %>
		<input type="hidden" name="personId" value="${ command.original.id }"/>
	<% } %>

	<div class="ke-panel-content">

		<div class="ke-form-globalerrors" style="display: none"></div>

		<div class="ke-form-instructions">
			<strong>*</strong> indicates a required field
		</div>
		
		<fieldset>
			<legend>Demographics</legend>
			
			<table>		
				
				<tr>
					<td class="ke-field-label"><b>OPD Number*</b></td>
					<td>${ ui.includeFragment("chaiui", "widget/field", [ object: command, property: "opdNumber" ]) }</td>
				</tr>
			</table>

			<br />
			<% patientNameField.each { %>
			${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
			<% } %>
			
			<% fatherNameField.each { %>
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
		</fieldset>

		<fieldset>
			<legend>Screening Test</legend>
			<table>			
				<tr>	
					<td>HBsAg&nbsp;&nbsp;&nbsp;</td>
					<td><input type="checkbox" id="hBsAg" name="hBsAg" checked="checked"></td>
					
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>Anti-HCV Ag&nbsp;&nbsp;&nbsp;</td>
					<td><input type="checkbox" id="antiHCV" name="antiHCV" checked="checked"></td>
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

jQuery(document).ready(function(){  
  
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
	
	});
	

	jQuery(function() {
		jQuery('#from-age-button').appendTo(jQuery('#from-age-button-placeholder'));
		
		jQuery('#edit-patient-form .cancel-button').click(function() {
			ui.navigate('${ config.returnUrl }');
		});

		chaiui.setupAjaxPost('screening-registration-form', {
			onSuccess: function(data) {
				if (data.id) {
					ui.navigate('chaiemr', 'intake/intakeHome');
										
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