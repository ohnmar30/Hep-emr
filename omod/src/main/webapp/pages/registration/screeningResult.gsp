<%
	ui.decorateWith("chaiemr", "standardPage")	

	def menuItems = [
			[ label: "Back to previous step", iconProvider: "chaiui", icon: "buttons/back.png", href: ui.pageLink("chaiemr", "registration/registrationHome") ]
	]
%>
<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Registration Home", items: menuItems ]) }
	</div>
<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/screeningResultPatient", [ patient: currentPatient, returnUrl: returnUrl ]) }
</div>