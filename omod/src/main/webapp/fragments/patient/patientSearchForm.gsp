<%
	ui.decorateWith("chaiui", "panel", [ heading: "Search for a Patient" ])

	ui.includeJavascript("chaiemr", "controllers/patient.js")

	def defaultWhich = config.defaultWhich ?: "all"

	def id = config.id ?: ui.generateId();
%>
<form id="${ id }" ng-controller="PatientSearchForm2" ng-init="init('${ defaultWhich }')">
	<label  class="ke-field-label">Which patients</label>
	<span class="ke-field-content">
		<input type="radio" ng-model="which" ng-change="updateSearch()" value="all" /> All
		&nbsp;&nbsp;
		<input type="radio" ng-model="which" ng-change="updateSearch()" value="checked-in" /> Only Checked In
		&nbsp;&nbsp;
		<br><br>
		<input type="radio" ng-model="which" ng-change="updateSearch()" value="screening-register" /> Screening Register
		&nbsp;&nbsp;
		<br><br>
		<input type="radio" ng-model="which" ng-change="updateSearch()" value="scheduled" /> Scheduled Patients
	</span>

	<label class="ke-field-label">Patient ID or Name (3 chars min)</label>
	<span class="ke-field-content">
		<input type="text" name="query" ng-model="query" ng-change="updateSearch()" style="width: 260px" />
	</span>
	
	<label class="ke-field-label">Scheduled Date</label>
	<span class="ke-field-content">
		<input type="text" id="date" name="date" ng-model="date" ng-change="updateSearch()" style="width: 260px" />
		<script type="text/javascript">
                        jQuery(document).ready(function () {
                            jQuery('#date').datepicker({
                                showAnim: 'blind'
                            });
                        });
                    </script>
	</span>
	
	<span class="ke-field-content">
		<input type="hidden" id="township" name="township" ng-model="township" ng-change="updateSearch()" style="width: 260px" />
	</span>
	
	<input type="button" value="Search" ng-click="updateSearch();"/>
</form>