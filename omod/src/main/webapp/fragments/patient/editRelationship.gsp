<%
	ui.decorateWith("chaiui", "panel", [ heading: (command.existing ? "Edit" : "Add") + " Relationship", frameOnly: true ])

	def rows = [
		[
			[ object: command, property: "person", label: "Person" ]
		],
		[
			[ object: command, property: "isToPatient", label: "Relationship to patient", config: [ options: typeOptions ] ]
		],
		[
			[ object: command, property: "startDate", label: "Start date" ],
			[ object: command, property: "endDate", label: "End date" ]
		]
	]
%>

<form id="edit-relationship-form" method="post" action="${ ui.actionLink("chaiemr", "patient/editRelationship", "saveRelationship") }">
	<% if (command.existing) { %>
		<input type="hidden" name="existingId" value="${ command.existing.id }"/>
	<% } %>
	<input type="hidden" name="patientId" value="${ command.patient.id }"/>

	<div class="ke-panel-content">

		<div class="ke-form-globalerrors" style="display: none"></div>

		<% rows.each { %>
		${ ui.includeFragment("chaiui", "widget/rowOfFields", [ fields: it ]) }
		<% } %>

	</div>
	<div class="ke-panel-footer">
		<button type="submit"><img src="${ ui.resourceLink("chaiui", "images/glyphs/ok.png") }" /> Save</button>
		<button type="button" class="cancel-button"><img src="${ ui.resourceLink("chaiui", "images/glyphs/cancel.png") }" /> Cancel</button>
	</div>
	
</form>

<script type="text/javascript">
jq(function() {
	jq('#edit-relationship-form .cancel-button').click(function() {
		ui.navigate('${ returnUrl }');
	});

	chaiui.setupAjaxPost('edit-relationship-form', {
		onSuccess: function(data) {
			ui.navigate('${ returnUrl }');
		}
	});
});
</script>