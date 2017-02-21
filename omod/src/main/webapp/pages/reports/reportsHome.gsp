<%
	ui.decorateWith("chaiemr", "standardPage")

	def onReportClick = { report ->
		def opts = [ appId: currentApp.id, reportUuid: report.definitionUuid, returnUrl: ui.thisUrl() ]
		"""ui.navigate('${ ui.pageLink('chaiemr', 'report', opts) }');"""
	}

	def programs = reportsByProgram.keySet()

	def programNameToSlug = {
		it.toLowerCase().replace(" ", "")
	}

	def indicatorReports = { it.findAll({ it.isIndicator }) }
	def cohortReports = { it.findAll({ !it.isIndicator }) }
	
	def iconPath = "reports/" + "patient_list" + ".png"
%>

<div class="ke-page-content">

	<div id="program-tabs" class="ke-tabs">
		<div class="ke-tabmenu">
			<% reportsByProgram.keySet().each { programName -> %>
			<div class="ke-tabmenu-item" data-tabid="${ programNameToSlug(programName) }">${ programName }</div>
			<% } %>
		</div>
		<% reportsByProgram.each { programName, reports -> %>
		<div class="ke-tab" data-tabid="${ programNameToSlug(programName) }">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr>
					<td style="width: 35%; vertical-align: top">
						<div class="ke-panel-frame">
							<div class="ke-panel-heading">Report & Indicator</div>
							<div class="ke-panel-content">
								${ ui.includeFragment("chaiemr", "widget/reportStack", [ reports: indicatorReports(reports), onReportClick: onReportClick ]) }
							</div>
						</div>
					</td>
					<td style="width: 35%; vertical-align: top; padding-left: 5px">
						<div class="ke-panel-frame">
							<div class="ke-panel-heading">Patient Lists</div>
							<div class="ke-panel-content">
								${ ui.includeFragment("chaiemr", "widget/reportStack", [ reports: cohortReports(reports), onReportClick: onReportClick ]) }
							</div>
						</div>
					</td>
					<td style="width: 30%; vertical-align: top; padding-left: 5px">
					<div class="ke-panel-frame">
						<div class="ke-panel-heading">Custom report</div>
						<div class="ke-panel-content">
						
						<div class="ke-stack-item ke-navigable" onclick="yearlyReportView();">
	                    ${ ui.includeFragment("chaiui", "widget/icon", [ iconProvider: "chaiui", icon: iconPath, tooltip: "View report" ]) }
	                    <b>Yearly Report</b>
	                    <div class="ke-extra"></div>
                        </div>
                        
                        <div class="ke-stack-item ke-navigable" onclick="halfYearlyReport();">
	                    ${ ui.includeFragment("chaiui", "widget/icon", [ iconProvider: "chaiui", icon: iconPath, tooltip: "View report" ]) }
	                    <b>Half Yearly Report</b>
	                    <div class="ke-extra"></div>
                        </div>
                        
                        <div class="ke-stack-item ke-navigable" onclick="quaterlyReportView();">
	                    ${ ui.includeFragment("chaiui", "widget/icon", [ iconProvider: "chaiui", icon: iconPath, tooltip: "View report" ]) }
	                    <b>Quaterly Report</b>
	                    <div class="ke-extra"></div>
                        </div>
                        
						</div>
					</div
				</td>
				</tr>
			</table>
		</div>
		<% } %>
	</div>
</div>

<script type="text/javascript">
function yearlyReportView() {
ui.navigate('chaiemr', 'reports/yearlyReport');
}

function halfYearlyReport() {
ui.navigate('chaiemr', 'reports/halfYearlyReport');
}

function quaterlyReportView() {
ui.navigate('chaiemr', 'reports/quaterlyReport');
}
</script>