<div class="ke-panelbar" style="text-align: right">
	<% if (visit) { %>
	<% if (visit.visitType.name=="Follow up visit" && typeOfUser=="clinician") { %>
	<%= ui.includeFragment("chaiui", "widget/dialogForm", [
			buttonConfig: [ label: "Save & Close Visit", iconProvider: "chaiui", icon: "buttons/visit_end.png" ],
			dialogConfig: [ heading: "Check Out", width: 50, height: 30 ],
			fields: [
						[ hiddenInputName: "visitId", value: visit.visitId ],
						[ hiddenInputName: "appId", value: currentApp.id ],
						[ label: "End Date and Time", formFieldName: "stopDatetime", class: java.util.Date, initialValue:activeVisitDate, showTime: true ]
			],
			fragmentProvider: "chaiemr",
			fragment: "registrationUtil",
			action: "stopVisit",
			onSuccessCallback: "ui.navigate('chaiemr', 'clinician/clinicianHome')",
			submitLabel: ui.message("general.submit"),
			cancelLabel: ui.message("general.cancel")
	]) %>
	<% } else if (!patient.dead && !patient.voided && visit.visitType.name=="NEW PATIENT" && typeOfUser=="registration") { %>
	<%= ui.includeFragment("chaiui", "widget/dialogForm", [
			buttonConfig: [ label: "Check in for visit", iconProvider: "chaiui", icon: "buttons/registration.png" ],
			dialogConfig: [ heading: "Check In", width: 50, height: 30 ],
			fields: [
						[ hiddenInputName: "patientId", value: patient.patientId ],
						[ hiddenInputName: "appId", value: currentApp.id ],
						[ label: "Start Date and Time", formFieldName: "startDatetime", class: java.util.Date, initialValue: new Date(), showTime: true ]
			],
			fragmentProvider: "chaiemr",
			fragment: "registrationUtil",
			action: "startVisit",
			onSuccessCallback: "ui.navigate('chaiemr', 'registration/registrationHome')",
			submitLabel: ui.message("general.submit"),
			cancelLabel: ui.message("general.cancel")
	]) %>
	<% } %>
	<% } else { %>
	<% if (typeOfUser=="registration" && !patient.dead) { %>
	<%= ui.includeFragment("chaiui", "widget/dialogForm", [
			buttonConfig: [ label: "Check in for visit", iconProvider: "chaiui", icon: "buttons/registration.png" ],
			dialogConfig: [ heading: "Check In", width: 50, height: 30 ],
			fields: [
						[ hiddenInputName: "patientId", value: patient.patientId ],
						[ hiddenInputName: "appId", value: currentApp.id ],
						[ label: "Start Date and Time", formFieldName: "startDatetime", class: java.util.Date, initialValue: new Date(), showTime: true ]
			],
			fragmentProvider: "chaiemr",
			fragment: "registrationUtil",
			action: "startVisit",
			onSuccessCallback: "ui.navigate('chaiemr', 'registration/registrationHome')",
			submitLabel: ui.message("general.submit"),
			cancelLabel: ui.message("general.cancel")
	]) %>
	<% } %>
	<% } %>
</div>