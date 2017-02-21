<%
	ui.decorateWith("chaiui", "panel", [ heading: "List Lab Orders" ])
%>
<table style="width:100%; align:center;">
<tr>
<th>No.</th>
<th>Visit Date</th>
<th>Order date</th>
<% /*<th>Update Date</th>  */%>
<th style="align:right;">Action</th>
</tr>
<% listLabOrder.eachWithIndex { labOrder , count -> %>
<tr>
<td>${count+1}</td>
<td>${labOrder.visitDate}</td>
<td>${labOrder.orderDate}</td>
<% /*<td>${labOrder.updateDate}</td>  */%>
<td><button class="ke-compact" type="button" onclick="enterResult('${labOrder.encounterId}')" ><img src="${ ui.resourceLink("chaiui", "images/glyphs/edit.png") }" /></button</td>
</tr>
<% } %>
</table>
<input type="hidden" id="returnUrl" value="${returnUrl}"/>
<script>

function enterResult(encounterId) {
	var url  = jq("#returnUrl").val();
	ui.navigate(ui.pageLink('chaiemr', 'intake/enterLabResultMain', { encounterId :  encounterId , returnUrl : url}) );
}

</script>
