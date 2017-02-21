<%
	ui.includeJavascript("chaiemr", "controllers/account.js")

	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])
%>
<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "account/accountSearchForm", [ defaultWhich: "all" ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "account/accountSearchResults") }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>
