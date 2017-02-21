<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient ])
%>

<div class="ke-page-content">

	${ /*ui.includeFragment("chaiui", "widget/tabMenu", [ items: [
			[ label: "Overview", tabid: "overview" ],
			[ label: "Lab Tests", tabid: "labtests" ],
			[ label: "Prescriptions", tabid: "prescriptions" ]
	] ])*/ "" }

	<!--<div class="ke-tab" data-tabid="overview">-->
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr>
				<td width="30%" valign="top" style="padding-left: 5px">
					<% if (activeVisit) { %>
					${ ui.includeFragment("chaiemr", "visitAvailableForms", [ visit: activeVisit ]) }
					${ ui.includeFragment("chaiemr", "nextAppointmentForm", [ patient: currentPatient, visit: activeVisit ]) }

					<% } %>
					${ ui.includeFragment("chaiemr", "program/programHistories", [ patient: currentPatient, showClinicalData: true ]) }
				</td>
				<td width="40%" valign="top">
					${ ui.includeFragment("chaiemr", "patient/patientSummary", [ patient: currentPatient ]) }
			<!-- 	${ ui.includeFragment("chaiemr", "patient/patientRelationships", [ patient: currentPatient ]) } -->
					${ ui.includeFragment("chaiemr", "program/programCarePanels", [ patient: currentPatient, complete: false, activeOnly: true ]) }

					${ ui.includeFragment("chaiemr", "program/tb/tbCarePanel", [ patient: currentPatient, complete: false, activeOnly: false ]) }	
				</td>
				<td width="30%" valign="top" style="padding-left: 5px">
					
					${ ui.includeFragment("chaiemr", "visitMenu", [ patient: currentPatient, visit: activeVisit ]) }
					
					${ ui.includeFragment("chaiemr", "patient/patientWhiteCard", [ patient: currentPatient ]) }
					${ ui.includeFragment("chaiemr", "patient/patientChart", [ patient: currentPatient ]) }
					<% if (activeVisit) { %>
					
					${ ui.includeFragment("chaiemr", "visitCompletedForms", [ visit: activeVisit ]) }
					<% } %>
					
				</td>
			</tr>
		</table>
	<!--</div>-->

</div>