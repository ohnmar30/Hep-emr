<%
	ui.decorateWith("chaiui", "panel", [ heading: "HIV Care" ])

	def dataPoints = []

	if (config.complete) {
		def initialArtStartDate = calculations.initialArtStartDate ? calculations.initialArtStartDate.value : null
		if (initialArtStartDate) {
			def regimen = calculations.initialArtRegimen ? chaiEmrUi.formatRegimenLong(calculations.initialArtRegimen.value, ui) : null

			
			dataPoints << [ label: "Initial ART regimen", value: regimen ]
		} else {
			
		}
	}
/*
	if (initialArtStartDate) {
		dataPoints << [ label: "ART start date", value: initialHivStartDate, showDateInterval: true ]
	} else {
		dataPoints << [ label: "ART start date", value: "" ]
	}
*/	
	if (calculations.lastWHOStage) {
		dataPoints << [ label: "Last WHO stage", value: ui.format(calculations.lastWHOStage.value.valueCoded), extra: calculations.lastWHOStage.value.obsDatetime ]
	} else {
		dataPoints << [ label: "Last WHO stage", value: "None" ]
	}
	
	if(patient.age.value > 13){
		if (cd4Count) {
			dataPoints << [ label: "Last CD4 count", value: ui.format(cd4Count) + " cells/&micro;L",extra: calculations.lastCD4Count.value.obsDatetime]
		} else {
			dataPoints << [ label: "Last CD4 count", value: "None" ]
		}
	}
	else {
		if (cd4PerCount) {
			dataPoints << [ label: "Last CD4 percentage", value: ui.format(cd4PerCount) + " %" ,extra: calculations.lastCD4Percent.value.obsDatetime]
		}
		else {
			dataPoints << [ label: "Last CD4 percentage", value: "None" ]
		}
	}

	if (viralLoadResult) {
		dataPoints << [ label: "Last Viral Load", value: viralLoadResult + " copies/ml" ,extra:calculations.viralDateResult.value.obsDatetime]
	}
	else{
		dataPoints << [ label: "Last Viral Load", value: "None" ]
	}

	
	
	if (listAllDiag) {
		dataPoints << [ label: "Last Diagnosis", value: listAllDiag  ,extra:calculations.lastDiagnosis.value.obsDatetime]
	}
	else{
		dataPoints << [ label: "Last Diagnosis", value: "None" ]
	}

	if (listAllOI) {
			dataPoints << [ label: "Last OI", value: listAllOI ,extra:calculations.lastOI.value.obsDatetime]
		}
		else{
			dataPoints << [ label: "Last OI", value: "None" ]
		}
if(duration)
{
	if (cpt) {
		dataPoints << [ label: "Currently on CTX", value: cpt ,extra:calculations.onCpt.value.obsDatetime]
	}
	else{
		dataPoints << [ label: "Currently on CTX", value: "None" ]
	}
	}
	else
	{
	dataPoints << [ label: "Currently on CTX", value: "None" ]
	}
%>

<% if (config.complete) { %>
<div class="ke-stack-item">
	<table width="100%" border="0">
		<tr>
			<td width="50%" valign="top">
				${ ui.includeFragment("chaiui", "widget/obsHistoryTable", [ id: "tblhistory", patient: currentPatient, concepts: graphingConcepts ,startDate:startDate,endDate:endDate]) }
			</td>
			<td width="50%" valign="top">
				${ ui.includeFragment("chaiui", "widget/obsHistoryGraph", [ id: "cd4graph", patient: currentPatient, concepts: graphingConcepts, showUnits: true, style: "height: 300px",startDate:startDate,endDate:endDate ]) }
			</td>
		</tr>
	</table>
</div>
<% } %>
<div class="ke-stack-item">
	<% dataPoints.each { print ui.includeFragment("chaiui", "widget/dataPoint", it) } %>
</div>
<div class="ke-stack-item">
	<% if (activeVisit && currentEnrollment) { %>
	<button type="button" class="ke-compact" onclick="ui.navigate('${ ui.pageLink("chaiemr", "regimenEditor", [ patientId: currentPatient.id, category: "ARV", appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
		<img src="${ ui.resourceLink("chaiui", "images/glyphs/edit.png") }" />
	</button>
	<% } %>

	<%
		if (regimenHistory.lastChange) {
			def lastChange = regimenHistory.lastChangeBeforeNow
			def regimen = lastChange.started ? chaiEmrUi.formatRegimenLong(lastChange.started, ui) : ui.message("general.none")
			def dateLabel = lastChange.started ? "ART start date" : "Stopped"
	%>
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Regimen", value: regimen ]) }
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: dateLabel, value: lastChange.date, showDateInterval: true ]) }
	${ ui.includeFragment("chaiemr", "field/HivRegimenChange", [patient: currentPatient ]) }
	<% } else { %>
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Regimen", value: ui.message("chaiemr.neverOnARVs") ]) }
	<% } %>
</div>