<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "dispensary/pastDispensingRecordPatientSearchForm", [ defaultWhich: "all" ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "dispensary/pastDispensingPatientSearchResults", [ pageProvider: "chaiemr", page: "dispensary/pastDrugOrderRecord" ]) }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>