<%
	ui.decorateWith("chaiui", "panel", [ heading: definition.name ])

	def columns = dataSet.metaData.columns
	def nonIdOrNameColumns = columns.findAll { it.label != "id" && it.label != "Name" }

	def formatData = { result -> (result != null) ? result : "-" }
%>

<fieldset>
	<legend>Summary</legend>
	<table>
		<tr>
			<td style="padding-right: 20px">${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Total", value: summary["total"] ]) }</td>
			<td style="padding-right: 20px">${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Males", value: summary["males"] ]) }</td>
			<td style="padding-right: 20px">${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Females", value: summary["females"] ]) }</td>
			<td>${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Child", value: summary["child"] ]) }</td>
		</tr>
	</table>
</fieldset>

<% if (nonIdOrNameColumns.size() > 0) { %>
	<table class="ke-table-vertical">
		<thead>
		<tr>
			<th>Name</th>
			<th>Age</th>
			<th>Gender</th>
			<th>Contact No.</th>
			<th>Patient ID</th>
			<th>NIC</th>
		</tr>
		</thead>
		<tbody>
		<%
			dataSet.rows.each { row ->
				def patientId = row.getColumnValue("id")
				//remove null values from each row
				def rowName = row.getColumnValue("Name")
				def personName = rowName.toString().replaceAll('\\(NULL\\),','')
				def personGender = row.getColumnValue("Sex").toLowerCase() 
				
				
		%>
			<tr>
				<td>
					<img src="${ ui.resourceLink("chaiui", "images/glyphs/patient_" + personGender + ".png") }" class="ke-glyph" />
					<a href="${ ui.pageLink("chaiemr", "chart/chartViewPatient", [ patientId: patientId ]) }">${ personName }</a>
					
				</td>

			<% nonIdOrNameColumns.each { col -> %>
				<td>${ formatData(row.getColumnValue(col)) }</td>
			<% } %>
			</tr>
		<% } %>
		</tbody>
	</table>
<% } else {%>
<fieldset>
	Insufficient follow-up time to generate this report
</fieldset>
<% } %>