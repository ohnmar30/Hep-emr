<%
	ui.decorateWith("chaiui", "panel", [ heading: "Search for a Patient" ])

	ui.includeJavascript("chaiemr", "controllers/patient.js")

	def defaultWhich = config.defaultWhich ?: "all"

	def id = config.id ?: ui.generateId();
%>
<form id="${ id }" ng-controller="PatientSearchForm4" ng-init="init('${ defaultWhich }')">
	<label  class="ke-field-label">Past Dispensing Record</label>

	<label class="ke-field-label">Patient ID or Name (3 chars min)</label>
	<span class="ke-field-content">
		<input type="text" name="query" ng-model="query" ng-change="updateSearch()" style="width: 260px" />
	</span>
	
	<label class="ke-field-label">Processed Date</label>
	<span class="ke-field-content">
	${ ui.includeFragment("chaiui", "field/java.util.Date" ,[id:'processedDate',  formFieldName:'date', showTime: false])}
	
	</span>
	<input type="button" value="Search" ng-click="updateSearch();"/>
</form>