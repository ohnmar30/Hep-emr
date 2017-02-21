<%
	ui.decorateWith("chaiemr", "standardPage")
%>
<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/editPatient", [ patient: currentPatient, returnUrl: returnUrl ]) }
</div>