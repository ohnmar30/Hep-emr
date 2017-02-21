<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient, visit: activeVisit ])

	def defaultEncounterDate = activeVisit ? activeVisitDate : new Date()
%>
<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "form/enterHtmlForm", [
			patient: currentPatient,
			formUuid: formUuid,
			visit: currentVisit,
			defaultEncounterDate: defaultEncounterDate,
			returnUrl: returnUrl
	]) }
</div>