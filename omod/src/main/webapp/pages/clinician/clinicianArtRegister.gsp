<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient, layout: "sidebar" ])
	
%>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "program/artRegister", [ patient: currentPatient, complete: true, activeOnly: false ]) }
</dit>