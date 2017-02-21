<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "sidebar" ])

%>

${ ui.includeFragment("chaiemr", "report/excelExport",[year:year]) }