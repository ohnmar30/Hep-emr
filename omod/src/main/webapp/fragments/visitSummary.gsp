<%
	ui.decorateWith("chaiui", "panel", [ heading: "Visit Summary", frameOnly: true ])

	def nonVoidedEncounters = visit.encounters.findAll { !it.voided }
	
	def dataPoints = []
	
	if (appointmentDate) {
		dataPoints << [ label: "Next Appointment Date ", value: appointmentDate, showDateInterval: true ]
	} 
	
%>
<script type="text/javascript">
	function ke_deleteVisit(visitId) {
		chaiui.openConfirmDialog({
			heading: 'Visit',
			message: '${ ui.message("chaiemr.confirmVoidVisit") }',
			okCallback: function() {
				ui.getFragmentActionAsJson('chaiemr', 'emrUtils', 'voidVisit', { visitId: visitId, reason: 'Data entry error' }, function() {
					ui.reloadPage();
				});
			}
		});
	}
</script>

<div class="ke-panel-content">
	<% if (visit.voided) { %>
	<div class="ke-warning" style="margin-bottom: 5px">This visit has been voided</div>
	<% } %>

	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Type", value: visit.visitType ]) }
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Location", value: visit.location ]) }
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "When", value: chaiui.formatVisitDates(visit) ]) }
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Entry", value: sourceForm ?: "Registration" ]) }
	<% dataPoints.each { print ui.includeFragment("chaiui", "widget/dataPoint", it) } %>
</div>

<% if (allowVoid && !visit.voided) { %>
<div class="ke-panel-controls" style="text-align: center">
	<% if (!nonVoidedEncounters) { %>
	<button type="button" onclick="ke_deleteVisit(${ visit.id })">
		<img src="${ ui.resourceLink("chaiui", "images/glyphs/void.png") }" /> Delete visit
	</button>
	<% } else { %>
	<em>To delete this visit, please delete all encounters first.</em>
	<% } %>
</div>
<% } %>