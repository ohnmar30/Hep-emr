<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient, visit: currentVisit ])

	def visit = currentVisit
%>
<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "form/enterHtmlForm", [ patient: currentPatient, encounter: encounter, visit: visit, returnUrl: returnUrl ]) }
</div>