<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	def menuItems = [
			[ label: "Import Legacy Data", iconProvider: "chaiemr", icon: "buttons/patient_merge.png", href: ui.pageLink("chaiemr", "admin/importPatients", [ returnUrl: ui.thisUrl() ]) ],
			[ label: "Back to home", iconProvider: "chaiui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("chaiemr", "admin/adminHome") ]
	]
%>
<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>


<div class="ke-page-content">
		${ ui.includeFragment("chaiemr", "patient/importPatientsList") }
</div>