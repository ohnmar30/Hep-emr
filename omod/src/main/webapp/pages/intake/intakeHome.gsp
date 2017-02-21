<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "patient/patientSearchForm", [ defaultWhich: "checked-in" ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/patientSearchResults", [ pageProvider: "chaiemr", page: "intake/intakeViewPatient" ]) }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>
