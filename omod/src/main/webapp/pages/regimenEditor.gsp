<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient ])

	def allowNew = !history.changes
	def allowChange = history.changes && history.changes.last().started
	def allowRestart = history.changes && !history.changes.last().started
	def allowUndo = history.changes && history.changes.size() > 0

	def changeDateField = { label ->
		[ label: label, formFieldName: "changeDate", class: java.util.Date, showTime: true, initialValue: initialDate ]
	}

	def regimenField = {
		[ label: "Regimen", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/Regimen", category: category,patient: currentPatient ]
	}
	
	def regimenSearchField = {
		[ label: "Info Buttons", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/RegimenSearch", category: category,patient: currentPatient ]
	}
	
	def substituteRegimenField = {
		[ label: "Regimen", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/Regimen", category: category,patient: currentPatient ]
	}
	
	def substituteRegimenSearchField = {
		[ label: "Regimen", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/substituteRegimenSearch", category: category,patient: currentPatient ]
	}
	
	def switchRegimenField = {
		[ label: "Regimen", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/switchRegimen", category: category,patient: currentPatient ]
	}
	
	def switchRegimenSearchField = {
		[ label: "Regimen", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/switchRegimenSearch", category: category,patient: currentPatient ]
	}
	
	def continueRegimenField = {
		[ label: "Regimen", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/continueRegimen", category: category,patient: currentPatient ]
	}
	
	def continueRegimenSearchField = {
		[ label: "Regimen", formFieldName: "regimen", class: "org.openmrs.module.chaiemr.regimen.Regimen", fieldFragment: "field/continueRegimenSearch", category: category,patient: currentPatient ]
	}

	def reasonFields = { reasonType ->
		ui.includeFragment("chaiui", "widget/rowOfFields", [
			fields: [
				[ label: "Reason", formFieldName: "changeReason", class: "org.openmrs.Concept", fieldFragment: "field/RegimenChangeReason", category: category, reasonType: reasonType ],
				[ label: "Reason (Other)", formFieldName: "changeReasonNonCoded", class: java.lang.String ]
			]
		]);
	}
%>

<script type="text/javascript">

	function choseAction(formId) {
		// Hide the regimen action buttons
		jq('#regimen-action-buttons').hide();

		ui.confirmBeforeNavigating('#' + formId);

		// Show the relevant regimen action form
		jq('#' + formId).show();
	}

	function cancelAction() {
		ui.cancelConfirmBeforeNavigating('.regimen-action-form');

		// Hide and clear all regimen action forms
		jq('.regimen-action-form').hide();
		jq('.regimen-action-form form').get(0).reset();

		// Redisplay the regimen action buttons
		jq('#regimen-action-buttons').show();
	}

	function undoLastChange() {
		if (confirm('Undo the last regimen change?')) {
			ui.getFragmentActionAsJson('chaiemr', 'regimenUtil', 'undoLastChange', { patient: ${ currentPatient.patientId }, category: '${ category }' }, function (data) {
				ui.reloadPage();
			});
		}
	}
</script>

<!--  div class="ke-page-sidebar">
	<div class="ke-panel-frame">
		${ ui.includeFragment("chaiui", "widget/panelMenuItem", [ iconProvider: "chaiui", icon: "buttons/back.png", label: "Back", href: returnUrl ]) }
	</div>
</div -->

<div class="ke-page-content">
<div >
	<div class="ke-panel-frame">
		<div class="ke-panel-heading">${ category } Regimen</div>
		<div class="ke-panel-content" style="max-width:100%;overflow:auto">

			${ ui.includeFragment("chaiemr", "regimenHistory", [ history: history ]) }

			<br/>

			<div id="regimen-action-buttons" style="text-align: center">
				<% if (allowNew) { %>
				${ ui.includeFragment("chaiui", "widget/button", [ iconProvider: "chaiui", icon: "buttons/regimen_start.png", label: "Start", extra: "a new regimen", onClick: "choseAction('start-new-regimen')" ]) }
				<% } %>

				<% if (allowChange) { %>
				${ ui.includeFragment("chaiui", "widget/button", [ iconProvider: "chaiui", icon: "buttons/regimen_change.png", label: "Substitute", extra: "the current regimen", onClick: "choseAction('substitute-regimen')" ]) }

				${ ui.includeFragment("chaiui", "widget/button", [ iconProvider: "chaiui", icon: "buttons/regimen_change.png", label: "Switch", extra: "the current regimen", onClick: "choseAction('switch-regimen')" ]) }
			
				${ ui.includeFragment("chaiui", "widget/button", [ iconProvider: "chaiui", icon: "buttons/regimen_stop.png", label: "Continue", extra: "the current regimen", onClick: "choseAction('continue-regimen')" ]) }
				<% } %>

				<% if (allowRestart) { %>
				${ ui.includeFragment("chaiui", "widget/button", [ iconProvider: "chaiui", icon: "buttons/regimen_restart.png", label: "Restart", extra: "a new regimen", onClick: "choseAction('restart-regimen')" ]) }
				<% } %>

				<% if (allowUndo) { %>
				${ ui.includeFragment("chaiui", "widget/button", [ iconProvider: "chaiui", icon: "buttons/undo.png", label: "Undo", extra: "the last change", onClick: "undoLastChange()" ]) }
				<% } %>
			</div>

			<% if (allowNew) { %>
			<div>New</div>
			<fieldset id="start-new-regim" class="regimen-action-form" style="display: none">
				<legend>Start New Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Start" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Start date"),
							regimenField()
					],
					submitLabel: "Save",
					successCallbacks: [ "ui.reloadPage();" ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			
			<fieldset id="start-new-regimen" class="regimen-action-form" style="display: none">
				<legend>Start New Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Start" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Start date"),
							regimenSearchField()
					],
					submitLabel: "Save",
					successCallbacks: [ back ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			<% } %>

			<% if (allowChange) { %>
			<fieldset id="substitute-regim" class="regimen-action-form" style="display: none">
				<legend>Substitute Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Substitute" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Change date"),
							substituteRegimenField(),
							[ value: reasonFields("substitute") ]
					],
					submitLabel: "Save",
					successCallbacks: [ "ui.reloadPage();" ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			
			<fieldset id="substitute-regimen" class="regimen-action-form" style="display: none">
				<legend>Substitute Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Substitute" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Change date"),
							substituteRegimenSearchField(),
							[ value: reasonFields("substitute") ]
					],
					submitLabel: "Save",
					successCallbacks: [ back ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			
			<fieldset id="switch-regim" class="regimen-action-form" style="display: none">
				<legend>Switch Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Switch" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Change date"),
							switchRegimenField(),
							[ value: reasonFields("switch") ]
					],
					submitLabel: "Save",
					successCallbacks: [ "ui.reloadPage();" ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			
			<fieldset id="switch-regimen" class="regimen-action-form" style="display: none">
				<legend>Switch Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Switch" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Change date"),
							switchRegimenSearchField(),
							[ value: reasonFields("switch") ]
					],
					submitLabel: "Save",
					successCallbacks: [ back ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>

			<fieldset id="continue-regim" class="regimen-action-form" style="display: none">
				<legend>Continue Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Continue" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Continue date"),
							continueRegimenField()
					],
					submitLabel: "Save",
					successCallbacks: [ "ui.reloadPage();" ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			
			<fieldset id="continue-regimen" class="regimen-action-form" style="display: none">
				<legend>Continue Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Continue" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Continue date"),
							continueRegimenSearchField()
					],
					submitLabel: "Save",
					successCallbacks: [ back ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			<% } %>

			<% if (allowRestart) { %>
			<fieldset id="restart-regim" class="regimen-action-form" style="display: none">
				<legend>Restart Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Restart" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Restart date"),
							regimenField()
					],
					submitLabel: "Save",
					successCallbacks: [ "ui.reloadPage();" ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			
			<fieldset id="restart-regimen" class="regimen-action-form" style="display: none">
				<legend>Restart Regimen</legend>

				${ ui.includeFragment("chaiui", "widget/form", [
					fragmentProvider: "chaiemr",
					fragment: "regimenUtil",
					action: "changeRegimen",
					fields: [
							[ hiddenInputName: "patient", value: currentPatient.id ],
							[ hiddenInputName: "changeType", value: "Restart" ],
							[ hiddenInputName: "category", value: category ],
							changeDateField("Restart date"),
							regimenSearchField()
					],
					submitLabel: "Save",
					successCallbacks: [ back ],
					cancelLabel: "Cancel",
					cancelFunction: "cancelAction"
				]) }
			</fieldset>
			<% } %>
		</div>
	</div>
</div>