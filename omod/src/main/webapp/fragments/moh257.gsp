<%
	def onFormClick = { form ->
		def opts = [ appId: currentApp.id, patientId: currentPatient.id, formUuid: form.formUuid, returnUrl: ui.thisUrl() ]
		"""ui.navigate('${ ui.pageLink('chaiemr', 'enterForm', opts) }');"""
	}

	def onEncounterClick = { encounter ->
		"""chaiemr.openEncounterDialog('${ currentApp.id }', ${ encounter.id });"""
	}
%>
<div class="ke-panel-frame">
	<div class="ke-panel-heading">Page 1 (Care Summary)</div>
	<div class="ke-panel-content" style="background-color: #F3F9FF">

		<fieldset>
			<legend>New Forms</legend>

			${ ui.includeFragment("chaiui", "widget/formStack", [ forms: page1AvailableForms, onFormClick: onFormClick ]) }
		</fieldset>
		<br />
		<fieldset>
			<legend>Previously Completed Forms</legend>
			${ ui.includeFragment("chaiemr", "widget/encounterStack", [ encounters: page1Encounters, onEncounterClick: onEncounterClick ]) }
		</fieldset>
	</div>
</div>

<div class="ke-panel-frame">
	<div class="ke-panel-heading">Page 2 (Initial and Followup Visits)</div>
	<div class="ke-panel-content" style="background-color: #F3F9FF">
		${ ui.includeFragment("chaiemr", "widget/encounterStack", [ encounters: page2Encounters, onEncounterClick: onEncounterClick ]) }
		<br />
		<% if (inHivProgram) { %>
			<div align="center">
				${ ui.includeFragment("chaiui", "widget/button", [
						label: "Add Visit Summary",
						extra: "From column",
						iconProvider: "chaiui",
						icon: "buttons/visit_retrospective.png",
						href: ui.pageLink("chaiemr", "enterForm", [ appId: currentApp.id, patientId: currentPatient, formUuid: page2Form.uuid, returnUrl: ui.thisUrl() ])
				]) }
			</div>
		<%}%>
		<% if (!(inHivProgram)) { %>
			<div class="ke-warning" align="center">
				You need to enroll the patient into HIV program before completing visit summary and regimen
			</div>
		<%}%>
	</div>
</div>

<div class="ke-panel-frame">
	<div class="ke-panel-heading">ARV Regimen History</div>
	<div class="ke-panel-content" style="background-color: #F3F9FF">
		${ ui.includeFragment("chaiemr", "regimenHistory", [ history: arvHistory ]) }
		<br />
		<% if (inHivProgram) { %>
			<div align="center">
				${ ui.includeFragment("chaiui", "widget/button", [
						label: "Edit History",
						extra: "Go to editor",
						iconProvider: "chaiui",
						icon: "buttons/regimen.png",
						classes: [ "padded" ],
						href: ui.pageLink("chaiemr", "regimenEditor", [ appId: currentApp.id, patientId: currentPatient, category: "ARV", returnUrl: ui.thisUrl() ])
				]) }
			</div>
		<%}%>
	</div>

</div>