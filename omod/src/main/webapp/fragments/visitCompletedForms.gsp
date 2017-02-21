<%
	ui.decorateWith("chaiui", "panel", [ heading: "Completed Visit Forms" ])

	def onEncounterClick = { encounter ->
		"""chaiemr.openEncounterDialog('${ currentApp.id }', ${ encounter.id });"""
	}
%>

${ ui.includeFragment("chaiemr", "widget/encounterStack", [ encounters: encounters, onEncounterClick: onEncounterClick ]) }