<%
	config.require("history")
	
	def simpleHistory = chaiEmrUi.simpleRegimenHistory(config.history, ui)
%>
<table id="regimen-history" class="table ">
	<tr>
		<th width="130" >Start</th>
		<th width="130" >End</th>
		<th>Regimen</th>
		<th>Change Reason</th>
	</tr>
	<tbody>
		<% if (!simpleHistory) { %>
			<tr><td colspan="4" style="text-align: center; font-style: italic">None</td></tr>
		<% } %>
		
	  	<% for (def change in simpleHistory) { %>
	  		
	  	<tr <%  if (change.current) { %>style="font-weight: bold"<% }
	  	
	  	 %>>
			<td>${ change.startDate } </td>
			<td>${ change.endDate }</td>
			<td style="text-align: left"><small>${ change.regimen.longDisplay }</small></td>
			<td style="text-align: left">
				<% if (change.changeReasons) { %>
				${ change.changeReasons.join(",") }
				<% } %>
			</td>
	  	</tr>
		<% } %>
	</tbody>
</table>