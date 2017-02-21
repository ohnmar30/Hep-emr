<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "report/yearlyReportForm") }
</div>

<div class="ke-page-content" id="viewReport">

</div>

<script type="text/javascript">
	jQuery(function() {
		//jQuery('input[name="query"]').focus();
	});
</script>
