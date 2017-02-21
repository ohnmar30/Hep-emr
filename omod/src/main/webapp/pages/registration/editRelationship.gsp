<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient, visit: currentVisit ])
%>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/editRelationship", [ relationship: relationship, patient: currentPatient, returnUrl: returnUrl ]) }
</div>