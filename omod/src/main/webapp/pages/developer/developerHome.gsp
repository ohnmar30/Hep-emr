<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	def menuItems = [
			[ label: "Overview", iconProvider: "chaiui", icon: "buttons/developer_overview.png", active: (section == "overview"), href: ui.pageLink("chaiemr", "developer/developerHome") ],
			[ label: "Content", iconProvider: "chaiui", icon: "buttons/admin_content.png", active: (section == "content"), href: ui.pageLink("chaiemr", "developer/developerHome", [ section: "content" ]) ],
			[ label: "Modules", iconProvider: "chaiui", icon: "buttons/admin_modules.png", active: (section == "modules"), href: ui.pageLink("chaiemr", "developer/developerHome", [ section: "modules" ]) ],
			[ label: "Profiling", iconProvider: "chaiui", icon: "buttons/profiling.png", active: (section == "profiling"), href: ui.pageLink("chaiemr", "developer/developerHome", [ section: "profiling" ]) ],
			[ label: "Validation", iconProvider: "chaiui", icon: "buttons/validation.png", active: (section == "validation"), href: ui.pageLink("chaiemr", "developer/developerHome", [ section: "validation" ]) ],
			[ label: "Groovy console", iconProvider: "chaiui", icon: "buttons/groovy.png", active: (section == "groovy"), href: ui.pageLink("chaiemr", "developer/developerHome", [ section: "groovy" ]) ]
	]
%>
<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Developer", items: menuItems ]) }
</div>

<div class="ke-page-content">
	<% if (section == "content") { %>
	<div id="content-tabs" class="ke-tabs">
		<div class="ke-tabmenu">
			<div class="ke-tabmenu-item" data-tabid="apps">Apps</div>
			<div class="ke-tabmenu-item" data-tabid="programs">Programs</div>
			<div class="ke-tabmenu-item" data-tabid="forms">Forms</div>
			<div class="ke-tabmenu-item" data-tabid="identifiers">Identifiers</div>
			<div class="ke-tabmenu-item" data-tabid="flags">Flags</div>
			<div class="ke-tabmenu-item" data-tabid="reports">Reports</div>
			<div class="ke-tabmenu-item" data-tabid="roles">Roles</div>
		</div>
		<div class="ke-tab" data-tabid="apps">${ ui.includeFragment("chaiemr", "system/appsContent") }</div>
		<div class="ke-tab" data-tabid="programs">${ ui.includeFragment("chaiemr", "system/programsContent") }</div>
		<div class="ke-tab" data-tabid="forms">${ ui.includeFragment("chaiemr", "system/formsContent") }</div>
		<div class="ke-tab" data-tabid="identifiers">${ ui.includeFragment("chaiemr", "system/identifiersContent") }</div>
		<div class="ke-tab" data-tabid="flags">${ ui.includeFragment("chaiemr", "system/flagsContent") }</div>
		<div class="ke-tab" data-tabid="reports">${ ui.includeFragment("chaiemr", "system/reportsContent") }</div>
		<div class="ke-tab" data-tabid="roles">${ ui.includeFragment("chaiemr", "system/rolesContent") }</div>
	</div>
	<% } else if (section == "modules") { %>
	${ ui.includeFragment("chaiemr", "developer/loadedModules") }
	<% } else if (section == "profiling") { %>
	${ ui.includeFragment("chaiemr", "developer/profiling") }
	<% } else if (section == "validation") { %>
	${ ui.includeFragment("chaiemr", "developer/validation") }
	<% } else if (section == "groovy") { %>
	${ ui.includeFragment("chaiemr", "developer/groovyConsole") }
	<% } else { %>

	<div class="ke-panel-frame">
		<div class="ke-panel-heading">Overview</div>
		<div class="ke-panel-content">
			<div class="ke-warning">
			You are currently logged in as a developer. Misuse of this account to perform unauthorised activities is a
			disciplinary offence.
			</div>

			<div style="text-align: center; padding-top: 20px">
				<button onclick="ui.navigate('/' + OPENMRS_CONTEXT_PATH + '/admin')">
					<img src="${ ui.resourceLink("chaiui", "images/buttons/legacy.png") }" /> Legacy admin UI
				</button>
			</div>
		</div>
	</div>

	<% } %>

</div>