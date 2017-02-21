<%
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient ])
%>

<div class="ke-page-content">
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td width="30%" valign="top">
					<% if (activeVisit) { %>
						${ ui.includeFragment("chaiemr", "visitAvailableForms", [ visit: activeVisit ]) }
					<% } %></td>
				<td width="40%" valign="top">
					${ ui.includeFragment("chaiemr", "patient/patientSummary", [ patient: currentPatient ]) }
		<%/*	 	${ ui.includeFragment("chaiemr", "patient/patientRelationships", [ patient: currentPatient ]) }  
				${ ui.includeFragment("chaiemr", "program/programHistories", [ patient: currentPatient, showClinicalData: false ]) }*/%> 
				</td>
	 
				<td width="30%" valign="top" style="padding-left: 5px">
				${ ui.includeFragment("chaiemr", "visitMenu", [ patient: currentPatient, visit: activeVisit ]) }
					<% if (activeVisit) { %>
						${ ui.includeFragment("chaiemr", "visitCompletedForms", [ visit: activeVisit ]) }
					<% } %></td>
			</td>
		</tr>
	</table>
</div>
