<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	def menuItems = [
			[ label: "Back to home", iconProvider: "chaiui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("chaiemr", "registration/registrationHome") ]
	]
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "patient/patientSearchForm", [ defaultWhich: "all" ]) }

	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/patientSearchResults", [ pageProvider: "chaiemr", page: "chart/chartViewPatient" ]) }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>