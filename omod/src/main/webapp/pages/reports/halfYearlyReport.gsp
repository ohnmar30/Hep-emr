<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "report/halfYearlyReportForm") }
</div>

<div class="ke-page-content" id="viewHalfYearlyReport">

</div>

<script type="text/javascript">
	jQuery(function() {
		//jQuery('input[name="query"]').focus();
	});
</script>