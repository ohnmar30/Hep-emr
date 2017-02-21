<div class="ke-panel-frame">
	<div class="ke-panel-heading">Personal Details</div>
	<div class="ke-panel-content">
		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Real name", value: chaiui.formatPersonName(person) ]) }
		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Gender", value: chaiui.formatPersonGender(person) ]) }
		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Telephone", value: ui.format(form.telephoneContact) ]) }
		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Email", value: ui.format(form.emailAddress) ]) }
	</div>
	<div class="ke-panel-footer">
		${ ui.includeFragment("chaiui", "widget/dialogForm", [
				id: "person-details-form",
				buttonConfig: [
						label: "Edit",
						iconProvider: "chaiui",
						icon: "glyphs/edit.png"
				],
				dialogConfig: [ heading: "Edit personal details" ],
				fragment: "account/personDetails",
				fragmentProvider: "chaiemr",
				action: "submit",
				prefix: "person",
				commandObject: form,
				hiddenProperties: [ "personId" ],
				properties: [ "personName.givenName", "personName.familyName", "gender", "telephoneContact", "emailAddress" ],
				propConfig: [
						"gender": [
								options: [
										[ label: "Female", value: "F" ],
										[ label: "Male", value: "M" ]
								]
						]
				],
				submitLabel: "Save Changes",
				cancelLabel: "Cancel",
				onSuccessCallback: "ui.reloadPage();"
		]) }
	</div>
</div>