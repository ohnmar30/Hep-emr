<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	def menuItems = [
			[ label: "Create a new account", iconProvider: "chaiui", icon: "buttons/account_add.png", href: ui.pageLink("chaiemr", "admin/createAccount1") ],
			[ label: "Back to home", iconProvider: "chaiui", icon: "buttons/back.png", href: ui.pageLink("chaiemr", "admin/adminHome") ]
	]
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "account/accountSearchForm", [ defaultWhich: "all" ]) }

	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "account/accountSearchResults", [ showUsernames: true, pageProvider: "chaiemr", page: "admin/editAccount" ]) }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>