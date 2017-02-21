<div class="ke-panel-content">
	<table class="ke-table-vertical">
		<thead>
			<tr>
				<th>Name</th>
				<th>Age</th>
				<th>Sex</th>
			</tr>
		</thead>
		<tbody>
			<% patients.each { patient -> %>
			<tr>
				<td>
					<img src="${ ui.resourceLink("chaiui", "images/glyphs/patient_" + patient.gender.toLowerCase() + ".png") }" class="ke-glyph" />
					<a href="${ ui.pageLink("chaiemr", "chart/chartViewPatient", [ patientId: patient.id ]) }">${ patient.name.toString().replaceAll('\\(NULL\\),','') }</a>
				</td>
				<td>${ patient.age }</td>
				<td>${ patient.gender }</td>
			</tr>
			<% } %>
		</tbody>
	</table>
</div>
<div class="ke-panel-footer">
	<button type="button" onclick="chaiui.closeDialog()"><img src="${ ui.resourceLink("chaiui", "images/glyphs/close.png") }" /> Close</button>
</div>