<%
	ui.decorateWith("chaiui", "panel", [ heading: ui.format(program), frameOnly: true ])
%>
<% if (enrollments) { %>
<div class="ke-panel-content">
	<% enrollments.reverse().each { enrollment -> %>
		<% if (enrollment.dateCompleted) { %>
		<div class="ke-stack-item">
			${ ui.includeFragment("chaiemr", "program/programCompletion", [ patientProgram: enrollment, showClinicalData: config.showClinicalData ]) }
		</div>
		<% } else if (patientForms) { %>
		<div class="ke-stack-item">
			<% patientForms.each { form -> %>
				${ ui.includeFragment("chaiui", "widget/button", [
						iconProvider: form.iconProvider,
						icon: form.icon,
						label: form.name,
						extra: "Edit form",
						href: ui.pageLink("chaiemr", "editProgramForm", [
								appId: currentApp.id,
								patientProgramId: enrollment.id,
								formUuid: form.formUuid,
								returnUrl: ui.thisUrl()
						])
				]) }
			<% } %>
		</div>
		<% } %>
	
		<% if (defaultEnrollmentForm.targetUuid == 'e4b506c1-7379-42b6-a374-284469cba8da') { %>
			<div class="ke-stack-item">	
				${ ui.includeFragment("chaiemr", "program/programEnrollment", [ patientProgram: enrollment, showClinicalData: config.showClinicalData ]) }
			</div>	
		<% } else{ %>			
			<div class="ke-stack-item">
				${ ui.includeFragment("chaiemr", "program/programEnrollment", [ patientProgram: enrollment, showClinicalData: config.showClinicalData ]) }
			</div>
		<% } %>
	<% } %>
</div>
<% } %>

<% if ((currentEnrollment || patientIsEligible)) { %>
<div class="ke-panel-footer">
	<% if (currentEnrollment) { %>
		<% if (defaultEnrollmentForm.targetUuid == 'e4b506c1-7379-42b6-a374-284469cba8da') { %>	
			<button type="button" onclick="ui.navigate('${ ui.pageLink("chaiemr", "enterForm", [ patientId: patient.id, formUuid: defaultCompletionForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/discontinue.png") }" /> End of Follow up
			</button>
		<% } else if (defaultEnrollmentForm.targetUuid == '5f1526f6-64cd-4a90-b4ad-24bb9d2d8709'  && activeVisit) { %>	
			<button type="button" onclick="ui.navigate('${ ui.pageLink("chaiemr", "enterForm", [ patientId: patient.id, formUuid: defaultCompletionForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/discontinue.png") }" /> Stop ART
			</button>	
		<% } else if (activeVisit){ %>
			<button type="button" onclick="ui.navigate('${ ui.pageLink("chaiemr", "enterForm", [ patientId: patient.id, formUuid: defaultCompletionForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/discontinue.png") }" /> Discontinue
			</button>
		<% } %>
				
	<% } else if (patientIsEligible) { %>

		<% if (defaultEnrollmentForm.targetUuid == 'e4b506c1-7379-42b6-a374-284469cba8da') { %>	
			<button type="button" onclick="ui.navigate('${ ui.pageLink("chaiemr", "enterForm", [ patientId: patient.id, formUuid: defaultEnrollmentForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/enroll.png") }" /> Re-Enroll
			</button>
		<% } else if (defaultEnrollmentForm.targetUuid == '5f1526f6-64cd-4a90-b4ad-24bb9d2d8709' && artEncounter=="Stop ART" && activeVisit) { %>	
			<button type="button" onclick="ui.navigate('${ ui.pageLink("chaiemr", "enterForm", [ patientId: patient.id, formUuid: defaultEnrollmentForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/enroll.png") }" /> ReStart ART
			</button>
		<% } else if (defaultEnrollmentForm.targetUuid == '5f1526f6-64cd-4a90-b4ad-24bb9d2d8709' && activeVisit) { %>	
			<button type="button" onclick="ui.navigate('${ ui.pageLink("chaiemr", "enterForm", [ patientId: patient.id, formUuid: defaultEnrollmentForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/enroll.png") }" /> Initiate ART
			</button>
		<% } else if(activeVisit) { %>
			<button type="button" onclick="ui.navigate('${ ui.pageLink("chaiemr", "enterForm", [ patientId: patient.id, formUuid: defaultEnrollmentForm.targetUuid, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/enroll.png") }" /> Enroll
			</button>
		<% } %>
	<% } %>
</div>
<% } %>
