<%
	ui.includeCss("chaiemr", "chaiemr.css", 50)
	ui.includeCss("chaiemr", "alertify.css")
	ui.includeCss("chaiemr", "alertify.theme.css")
	ui.includeJavascript("chaiemr", "chaiemr.js", 50)
	ui.includeJavascript("chaiemr", "library/alertify.js")
	ui.includeJavascript("chaiemr", "test/test.js");

	if (config.patient) {
		config.context = "patientId=${ config.patient.id }"
	}

	config.beforeContent = ui.includeFragment("chaiemr", "header/pageHeader", config)

	config.beforeContent += ui.includeFragment("chaiemr", "header/systemHeader", config)

	config.beforeContent += ui.includeFragment("chaiemr", "header/headerMenu", config)

	if (config.patient) {
		config.beforeContent += ui.includeFragment("chaiemr", "header/patientHeader", [ patient: config.patient, closeChartUrl: config.closeChartUrl ])
	}
	if (config.visit) {
		config.beforeContent += ui.includeFragment("chaiemr", "header/visitHeader", [ visit: config.visit ])
	}

	config.pageTitle = "MyanmarEMR"
	config.faviconIco = ui.resourceLink("chaiemr", "images/logos/MyanmarFavicon.ico")
	config.faviconPng = ui.resourceLink("chaiemr", "images/logos/myanmarFavicon.png")
	config.angularApp = "chaiemr"
	
	ui.decorateWith("chaiui", "standardPage", config)
%>

<!-- Override content layout from chaiui based on the layout config value -->

<style type="text/css">

<% if (config.layout == "sidebar") { %>
	html {
		background: #FFF url('${ ui.resourceLink("chaiui", "images/background.png") }') repeat-y;
	}
<% } %>

</style>

<%= config.content %>

<!-- Required for the chaiemr.ensureUserAuthenticated(...) method -->
<div id="authdialog" title="Login Required" style="display: none">
	<div class="ke-panel-content">
		<table border="0" align="center">
			<tr>
				<td colspan="2" style="text-align: center; padding-bottom: 12px">
					Your session has expired so please authenticate

					<div class="error" style="display: none;">Invalid username or password. Please try again.</div>
				</td>
			</tr>
			<tr>
				<td align="right"><b>Username:</b></td>
				<td><input type="text" id="authdialog-username"/></td>
			</tr>
			<tr>
				<td align="right"><b>Password:</b></td>
				<td><input type="password" id="authdialog-password"/></td>
			</tr>
		</table>
	</div>
	<div class="ke-panel-controls">
		<button type="button"><img src="${ ui.resourceLink("chaiui", "images/glyphs/login.png") }" /> Login</button>
	</div>
</div>