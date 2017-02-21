<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient, layout: "sidebar" ])

	def menuItems = [
			[
					label: "Overview",
					href: ui.pageLink("chaiemr", "chart/chartViewPatient", [ patientId: currentPatient.id, section: "overview"]),
					active: (selection == "section-overview"),
					iconProvider: "chaiui",
					icon: "buttons/patient_overview.png"
			]
	];

	def backButton = [
			[
					label: "Back",
					href: ui.pageLink("chaiemr", "clinician/clinicianViewPatient", [ patientId: currentPatient.id]),
					iconProvider: "chaiui",
					icon: "buttons/back.png"
			]
	];
	
%>


<script type="text/javascript">

	function updateSearch(){
			var dateStart = document.getElementById('startDate_date').value;
			var dateEnd = document.getElementById('endDate_date').value;
			ui.navigate('chaiemr', "chart/chartViewPatient",  {patientId: ${currentPatient.id}, section: "overview" , startDate: dateStart, endDate:dateEnd});
	};


</script>


<div class="ke-page-sidebar">
	<div class="ke-panel-frame">
		<% backButton.each { item -> print ui.includeFragment("chaiui", "widget/panelMenuItem", item) } %>
		<% menuItems.each { item -> print ui.includeFragment("chaiui", "widget/panelMenuItem", item) } %>
	</div>	
	
	<div class="ke-panel-frame">
		<div class="ke-panel-heading">Date Filter</div>
	</div>
		
	<span class="ke-field-content">
		Start Date : ${ ui.includeFragment("chaiui", "field/java.util.Date" ,[id:'startDate',  formFieldName:'startDate', showTime: false])}
	</span>
	<span class="ke-field-content">
		End Date &nbsp;&nbsp;: ${ ui.includeFragment("chaiui", "field/java.util.Date" ,[id:'endDate',  formFieldName:'endDate', showTime: false])}
	</span>
	<span class="ke-field-content">
		<input type="button" value="Search" onclick="updateSearch();"/>
	</span>	

	<div class="ke-panel-frame">
		<div class="ke-panel-heading">Visits(${ visitsCount })<br / >${period}</div>
		</div>

		<div class="ke-panel-frame">
		<% if (!visits) {
			print ui.includeFragment("chaiui", "widget/panelMenuItem", [
				label: ui.message("general.none"),
			])
		}
		else {
			visits.each { visit ->
				print ui.includeFragment("chaiui", "widget/panelMenuItem", [
						label: ui.format(visit.visitType),
						href: ui.pageLink("chaiemr", "chart/chartViewPatient", [ patientId: currentPatient.id, visitId: visit.id ]),
						extra: chaiui.formatVisitDates(visit),
						active: (selection == "visit-" + visit.id)
				])
			}
		} %>
	</div>

</div>

<div class="ke-page-content">

	<% if (visit) { %>

		${ ui.includeFragment("chaiemr", "visitSummary", [ visit: visit ]) }
		<% if (!visit.voided) { %>
			${ ui.includeFragment("chaiemr", "visitCompletedForms", [ visit: visit ]) }
			${ ui.includeFragment("chaiemr", "visitAvailableForms", [ visit: visit ]) }
		<% } %>

	<% } else if (form) { %>

		<div class="ke-panel-frame">
			<div class="ke-panel-heading">${ ui.format(form) }</div>
			<div class="ke-panel-content">

				<% if (encounter) { %>
					${ ui.includeFragment("chaiemr", "form/viewHtmlForm", [ encounter: encounter ]) }
				<% } else { %>
					<em>Not filled out</em>
				<% } %>

			</div>
		</div>

	<% } else if (program) { %>

		${ ui.includeFragment("chaiemr", "program/programHistory", [ patient: currentPatient, program: program, showClinicalData: true ]) }

	<% } else if (section == "overview" && filter=="filterVisit") { %>

		${ ui.includeFragment("chaiemr", "program/programCarePanels", [ patient: currentPatient, complete: true, activeOnly: false ,startDate:startDate,endDate:endDate ]) }

	<% } else if (section == "overview" && filter=="") { %>
	
		${ ui.includeFragment("chaiemr", "program/programCarePanels", [ patient: currentPatient, complete: true, activeOnly: false ]) }

	<% } else if (section == "moh257") { %>

		${ ui.includeFragment("chaiemr", "moh257", [ patient: currentPatient ]) }

	<% } %>

</div>