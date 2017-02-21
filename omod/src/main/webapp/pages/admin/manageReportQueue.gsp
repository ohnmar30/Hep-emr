<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	ui.includeJavascript("chaiemr", "controllers/report.js")
%>
<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [
			heading: "Reports",
			items: [
					[ iconProvider: "chaiui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("chaiemr", "admin/adminHome") ]
			]
	]) }
</div>

<div class="ke-page-content" ng-controller="ReportController" ng-init="init('${ currentApp.id }', null)">
	${ ui.includeFragment("chaiemr", "report/reportQueue", [ allowCancel: true ]) }
</div>