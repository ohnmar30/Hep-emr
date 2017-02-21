<%
	ui.includeJavascript("chaiemr", "controllers/patient.js")

	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	def menuItems = [
			[ iconProvider: "chaiui", icon: "buttons/patient_search.png", label: "Find a patient", href: ui.pageLink("chaiemr", "chart/chartSearch") ]
	]
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">

	<div class="ke-panel-frame" ng-controller="RecentlyViewed" ng-init="init()">
		<div class="ke-panel-heading">Recently Viewed</div>
		<div class="ke-panel-content">
			<div class="ke-stack-item ke-navigable" ng-repeat="patient in recent" ng-click="onResultClick(patient)">
				${ ui.includeFragment("chaiemr", "patient/result.full") }
			</div>
			<div ng-if="recent.length == 0" style="text-align: center; font-style: italic">None</div>
		</div>
	</div>
</div>