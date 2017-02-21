<%
	ui.decorateWith("chaiui", "panel", [ heading: "Server Information" ])

	ui.includeJavascript("chaiemr", "controllers/system.js")
%>
<div ng-controller="ServerInformation" ng-init="init()">
	<table class="ke-table-vertical">
		<tbody>
			<tr ng-repeat="info in infos">
				<td>{{ info.label }}</td>
				<td>{{ info.value }}</td>
			</tr>
		</tbody>
	</table>
</div>