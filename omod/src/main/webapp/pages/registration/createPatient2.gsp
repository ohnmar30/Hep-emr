<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

	ui.includeJavascript("chaiemr", "controllers/patient.js")

	def menuItems = [
			[ label: "Back to previous step", iconProvider: "chaiui", icon: "buttons/back.png", href: ui.pageLink("chaiemr", "registration/registrationHome") ]
	]
%>
<div class="ke-page-sidebar">
	${ ui.includeFragment("chaiui", "widget/panelMenu", [ heading: "Create Patient", items: menuItems ]) }

	<% if (!person) { %>
	<div class="ke-panel-frame" id="ng-similarpatients" ng-controller="SimilarPatients" ng-init="init('${ currentApp.id }', 'chaiemr', 'registration/registrationViewPatient')">
		<script type="text/javascript">
			jQuery(function() {
				jQuery('input[name="personName.givenName"]').change(function() {
					var givenName = jQuery('input[name="personName.givenName"]').val();
					chaiui.updateController('ng-similarpatients', function(scope) {
						scope.givenName = givenName;
						scope.refresh();
					});
				});
			});
		</script>
		<div class="ke-panel-heading">Similar Patients</div>
		<div class="ke-panel-content">
			<div class="ke-stack-item ke-navigable" ng-repeat="patient in results" ng-click="onResultClick(patient)">
				${ ui.includeFragment("chaiemr", "patient/result.mini") }
			</div>
			<div ng-if="results.length == 0" style="text-align: center; font-style: italic">None</div>
		</div>
	</div>
	<% } %>
</div>

<div class="ke-page-content">
	${ ui.includeFragment("chaiemr", "patient/screeningRegisterPatient", [ person: person, heading: "Register Patient" ]) }
</div>