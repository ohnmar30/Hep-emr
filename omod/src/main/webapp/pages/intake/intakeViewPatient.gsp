<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient ])
%>

<div class="ke-page-content">
		${ ui.includeFragment("chaiemr", "intake/listLabOrders", [patientId : currentPatient.patientId, returnUrl : ui.thisUrl() ]) }
</div>