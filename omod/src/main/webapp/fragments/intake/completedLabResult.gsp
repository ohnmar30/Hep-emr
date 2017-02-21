<%
	ui.decorateWith("chaiui", "panel", [ heading: "Completed Lab Results" ])

	config.require("visit")
def visitId = currentVisit ? currentVisit.id : activeVisit.id
def onclick = ""
if (encounter != null) {
	def opts = [ appId: currentApp.id, visitId: visitId, patientId: currentPatient.id,  returnUrl: ui.thisUrl() , encounterId : encounter.encounterId]
	onclick = ui.pageLink('chaiemr', 'intake/enterLabResultMain', opts )  
}

%>

<% if (encounter != null) { %>
<b>Date Updated: 
<div class="ke-stack-item ke-navigable" onclick="ui.navigate('${onclick}')">
<b>${encounter.encounterDatetime}</b>
	<div style="clear: both"></div>
</div>
<% } %>

