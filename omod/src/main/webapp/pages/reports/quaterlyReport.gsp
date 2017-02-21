<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "report/quaterlyReportForm") }
</div>

<div class="ke-page-content" id="viewReportQuaterly">

</div>

<script type="text/javascript">
	jQuery(function() {
		//jQuery('input[name="query"]').focus();
	});
</script>