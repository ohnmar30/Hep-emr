<%
	ui.decorateWith("chaiui", "panel", [ heading: "Patient Details", frameOnly: true ])
%>
<div class="ke-panel-content">
	<div class="ke-stack-item">
		<% if (recordedAsDeceased) { %>
		<div class="ke-warning" style="margin-bottom: 5px">
			Patient has been recorded as deceased in a program form. Please update the registration form.
		</div>
		<% } %>

		<button type="button" class="ke-compact" onclick="ui.navigate('${ ui.pageLink("chaiemr", "registration/editPatient", [ patientId: patient.id, returnUrl: ui.thisUrl() ]) }')">
			<img src="${ ui.resourceLink("chaiui", "images/glyphs/edit.png") }" />
		</button>

		
<!--
		<% patient.activeAttributes.each { %>
		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: ui.format(it.attributeType), value: it ]) }
		<% } %>
-->		
		<% if (personWrap.fatherName) { %>
		Father name : <strong>${personWrap.fatherName}</strong>
		<% } %>
		<% if (personWrap.telephoneContact) { %>
			<br/>Patient contact number : <strong> ${personWrap.telephoneContact}</strong>
		<% } %>
		<% if (patientAdd.countyDistrict) { %>
			<% if (patientAdd.countyDistrict!="?") { %>
			<br/>Patient Address : <strong>${patientAdd.countyDistrict}</strong>
		<% }} %>
		<% if (patientWrap.nextOfKinName) { %>
			<br/>Treatment Supporter's Name: <strong>${patientWrap.nextOfKinName}</strong>
		<% } %>
		<% if (patientWrap.nextOfKinContact) { %>
			<br/>Treatment Supporter's contact number: <strong>${patientWrap.nextOfKinContact}</strong>
		<% } %>
		<% if (patientWrap.previousHivTestStatus) { %>
			<br/>HIV confirmation test Status : <strong>${patientWrap.previousHivTestStatus}</strong>
		<% } %>
		<% if (patientWrap.previousHivTestStatus=="Yes") { %>
		<% if (patientWrap.previousHivTestDate) { %>
			<br/>HIV confirmation test date : <strong>${patientWrap.previousHivTestDate}</strong>
		<% } %>
		<% if (patientWrap.previousHivTestPlace) { %>
			<br/>HIV confirmation test place : <strong>${patientWrap.previousHivTestPlace}</strong>
		<% } %>
		<% } %>
		<% if (savedEntryPoint) { %>
		          <% if(entryPoint=='Other') {%>
				   <br/> Entry Point : <strong>${entryPoint}</strong> <strong>(<strong>${otherEntryPoint}</strong>)</strong>
				  <% }  else { %>
				 <br/> Entry Point : <strong>${savedEntryPoint.valueCoded.name}</strong>
				<%} }  %>
		<% if (pregStatusVal) { %>
			<br/>Pregnancy : <strong>${pregStatusVal}</strong>
		<% } %>
		<% if (drugAllergiesVal) { %>
			<br/>Drug Allergy & name : <strong>${drugAllergiesVal} </strong>
		<% } %>
		<% if (drugOtherVal) { %>
			<br/>If other,please specify : <strong>${drugOtherVal} </strong>
		<% } %>
		<% if (methadone!="") { %>
			<br />Methadone Start Date: <strong>${methadone[0]}</strong>
			<br />Methadone End Date: <strong>${methadone[1]}</strong>
		<% } %>
		<% if (comorbidity!="") { %>
			<br/>Co-morbidity : <strong>${comorbidity}</strong>
		<% } %>
		
	</div>
</div>

<% if (forms) { %>
<div class="ke-panel-footer">
	<% forms.each { form -> %>
		${ ui.includeFragment("chaiui", "widget/button", [
				iconProvider: form.iconProvider,
				icon: form.icon,
				label: form.name,
				extra: "Edit form",
				href: ui.pageLink("chaiemr", "editPatientForm", [
					appId: currentApp.id,
					patientId: patient.id,
					formUuid: form.formUuid,
					returnUrl: ui.thisUrl()
				])
		]) }
	<% } %>
</div>

<% } %>
