<%
	ui.decorateWith("chaiui", "panel", [ heading: "TB Care" ])

	def dataPoints = []


if(calculations.tbPatientStatus){
		dataPoints << [ label: "TB Status", value: calculations.tbPatientStatus ,extra:calculations.tbPatientStatus.obsDatetime ]
}
else{
	dataPoints << [ label: "TB Status", value:  "None" ];
}

if (listAllSite) {
		dataPoints << [ label: "Site", value: listAllSite ,extra:calculations.tbDiseaseSite.obsDatetime]
	}
	else{
		dataPoints << [ label: "Site", value: "None" ]
	}
if(diseaseClassification!=null){
	dataPoints << [label: "Disease Classification", value: diseaseClassification]
}

if(calculations.tbTreatmentDrugStartDate){
		dataPoints << [ label: "TB treatment start date", value: calculations.tbTreatmentDrugStartDate ]
}
if(calculations.tbTreatmentDrugRegimen){		
		dataPoints << [ label: "TB regimen", value: calculations.tbTreatmentDrugRegimen ,extra:calculations.tbTreatmentDrugRegimen.obsDatetime]
}
else{
	dataPoints << [ label: "TB regimen", value: "None" ]
}

if(calculations.tbTreatmentDrugSensitivity){
		dataPoints << [ label: "Drug sensitivity", value: calculations.tbTreatmentDrugSensitivity ,extra:calculations.tbTreatmentDrugSensitivity.obsDatetime ]
}
else{
	dataPoints << [ label: "Drug sensitivity", value: "None" ]
}

if(calculations.tbTreatmentOutcome){

        dataPoints << [ label: "TB treatment outcome", value: calculations.tbTreatmentOutcome]

		

}
else{
    dataPoints << [ label: "TB treatment outcome", value:  "None" ];
}

if(calculations.tbTreatmentOutcomeDate){
        
        dataPoints << [ label: "TB treatment outcome date", value: calculations.tbTreatmentOutcomeDate ]
        
}



if(duration){
	if (ipt) {
		if(!isoniazidStatus){
		dataPoints << [ label: "Currently on IPT", value: ipt ,extra:calculations.onIpt.value.obsDatetime ]
		dataPoints << [label:"IPT duration", value:duration]
		}else{
			dataPoints << [label:"Currently on IPT", value:"Completed on "+isoniazidCompletedDate ]
		}
	}else{
		dataPoints << [ label: "Currently on IPT", value: "None" ]
	}
}else{
	dataPoints << [ label: "Currently on IPT", value: "None" ]
}
	
	
%>

<div class="ke-stack-item">
	<% dataPoints.each { print ui.includeFragment("chaiui", "widget/dataPoint", it) } %>
</div>

<!--
<div class="ke-stack-item">
	<% if (activeVisit) { %>
	<button type="button" class="ke-compact" onclick="ui.navigate('${ ui.pageLink("chaiemr", "regimenEditor", [ patientId: currentPatient.id, category: "TB", appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
		<img src="${ ui.resourceLink("chaiui", "images/glyphs/edit.png") }" />
	</button>
	<% } %>

	<%
		if (regimenHistory.lastChange) {
			def lastChange = regimenHistory.lastChangeBeforeNow
			def regimen = lastChange.started ? chaiEmrUi.formatRegimenLong(lastChange.started, ui) : ui.message("general.none")
			def dateLabel = lastChange.started ? "Started" : "Stopped"
	%>
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Regimen", value: regimen ]) }
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: dateLabel, value: lastChange.date, showDateInterval: true ]) }
	${ ui.includeFragment("chaiemr", "field/HivRegimenChange", [patient: currentPatient ]) }
	<% } else { %>
	${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Regimen", value: ui.message("chaiemr.neverOnTbRegimen") ]) }
	<% } %>
</div>

-->
