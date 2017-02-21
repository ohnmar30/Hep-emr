<div class="ke-panel-content">
	<script type="text/javascript">
		function onEncounterEdit() {
			ui.navigate('chaiemr', 'editForm', { appId: '${ currentApp.id }', encounterId: ${ encounter.id }, returnUrl: '${ currentUrl }' });
		}
		function onEncounterDelete(encounterId) {
			if (confirm('Are you sure you want to delete this encounter?')) {
				ui.getFragmentActionAsJson('chaiemr', 'form/formUtils', 'deleteEncounter', { appId: '${ currentApp.id }', encounterId: encounterId }, function() {
					ui.reloadPage();
				});
			}
		}
	</script>

	${ ui.includeFragment("chaiemr", "form/viewHtmlForm", [ encounter: encounter ]) }
</div>
<div class="ke-panel-controls">
	<button type="button" onclick="onEncounterEdit(${ encounter.id })"><img src="${ ui.resourceLink("chaiui", "images/glyphs/edit.png") }" /> Edit</button>
<!-- 	<button type="button" onclick="onEncounterDelete(${ encounter.id })"><img src="${ ui.resourceLink("chaiui", "images/glyphs/trash.png") }" /> Delete</button> -->
	<button type="button" onclick="chaiui.closeDialog()"><img src="${ ui.resourceLink("chaiui", "images/glyphs/close.png") }" /> Close</button>
</div>