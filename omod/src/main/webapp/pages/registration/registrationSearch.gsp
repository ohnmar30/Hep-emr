<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	def menuItems = [
			[ label: "Create new patient", extra: "Patient can't be found", iconProvider: "chaiui", icon: "buttons/patient_add.png", href: ui.pageLink("chaiemr", "registration/createPatient") ],
			[ label: "Back to home", iconProvider: "chaiui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("chaiemr", "registration/registrationHome") ]
	]
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiemr", "patient/patientSearchForm", [ defaultWhich: "all" ]) }

	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/patientSearchResults", [ pageProvider: "chaiemr", page: "registration/registrationViewPatient" ]) }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>