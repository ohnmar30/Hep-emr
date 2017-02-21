<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])
%>
<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [
			heading: "Tasks",
			items: [
					[ iconProvider: "chaiui", icon: "buttons/users_manage.png", label: "Manage accounts", href: ui.pageLink("chaiemr", "admin/manageAccounts") ],
					[ iconProvider: "chaiui", icon: "buttons/report_queue.png", label: "Manage report queue", href: ui.pageLink("chaiemr", "admin/manageReportQueue") ],
					[ iconProvider: "chaiui", icon: "buttons/admin_setup.png", label: "Redo first-time setup", href: ui.pageLink("chaiemr", "admin/firstTimeSetup") ],
					[ iconProvider: "chaidq", icon: "buttons/patient_add.png", label: "Import Legacy Data", href: ui.pageLink("chaiemr", "admin/importPatients") ]
					
			]
	]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "system/serverInformation") }
	${ ui.includeFragment("chaiemr", "system/databaseSummary") }
	${ ui.includeFragment("chaiemr", "system/externalRequirements") }
</div>