<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient ])
	
%>

<div class="ke-page-content">
		${ ui.includeFragment("chaiemr", "intake/enterLabResult", [visit : visit, encounter : encounter ]) }
</div>