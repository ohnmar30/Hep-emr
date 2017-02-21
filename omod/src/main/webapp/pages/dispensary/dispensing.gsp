<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "dispensary/dispensaryPatientSearchForm", [ defaultWhich: "all" ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/patientSearchResults", [ pageProvider: "chaiemr", page: "dispensary/drugOrder" ]) }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>
