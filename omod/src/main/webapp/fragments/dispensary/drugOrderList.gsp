<div>
<form id="drug-order-form" action="${ ui.actionLink("chaiemr", "dispensary/drugOrderList", "processDrugOrder") }" method="post">
<table style='width: 100%'>

<tr>
<th>S.No</th>
<th style="text-align:center">Drug Name </th>
<th>Strength </th>
<th>Unit </th>
<th>Frequency </th>
<th>Route </th>
<th>Duration </th>
<th>Issue Qunatity </th>
<th>Drug Regimen </th>
<th>Dispense</th>
<th>Reason for not dispensed</th>
</tr>

<% drugOrderProcesseds.each { drugOrderProcessed -> %>
<% if (drugOrderProcessed!=null) { %>
<tr>
<td>${count}</td>
<td style="text-align:center">${drugOrderProcessed.drugOrder.concept.name} </td>
<td>${drugOrderProcessed.dose}</td>
<td>${drugOrderProcessed.drugOrder.units} </td>
<td>${drugOrderProcessed.drugOrder.frequency}</td>
<td>${drugOrderProcessed.route.name}</td>
<td>${drugOrderProcessed.durationPreProcess}</td>
<td><input type="text" id="${drugOrderProcessed.id}issueQuantity" name="${drugOrderProcessed.id}issueQuantity" size="12"></td>
<td>${drugOrderProcessed.drugRegimen}</td>
<td><input type="checkbox" id="${count}drugOrderProcessedOrNot" name="${count}drugOrderProcessedOrNot" checked="checked" onClick="hideReasonForNotDispensed(${count},'${drugOrderProcessed.id}issueQuantity');"> </td>
<td id="${count++}notDispensedColumn">
<select id="${drugOrderProcessed.id}notDispensedReason"  name="${drugOrderProcessed.id}notDispensedReason" style='width: 400px;'>
<% notDispensedConceptAnswers.each { notDispensedConceptAnswer -> %>
<option value="${notDispensedConceptAnswer.answerConcept.conceptId}">${notDispensedConceptAnswer.answerConcept.name}</option>
<% } %>
</select>
</td>
<td><input type="hidden" id="drugOrderProcessedIds" name="drugOrderProcessedIds" value="${drugOrderProcessed.id}"> </td>
</tr>
<% } %>
<% } %>

<% drugOrderObss.each { drugOrderObs -> %>
<% if (drugOrderObs!=null) { %>
<tr>
<td>${count}</td>
<td>${drugOrderObs.drug} </td>
<td><%if(drugOrderObs.strength) { %> ${drugOrderObs.strength}<%}%></td>
<td><%if(drugOrderObs.formulation) { %>${drugOrderObs.formulation} <%}%></td>
<td><%if(drugOrderObs.frequency) { %>${drugOrderObs.frequency}<%}%></td>
<td><%if(drugOrderObs.route) { %>${drugOrderObs.route.name}<%}%> </td>
<td><%if(drugOrderObs.duration) { %>${drugOrderObs.duration}<%}%></td>
<td><input type="text" id="${drugOrderObs.obsGroupId}obsIssueQuantity" name="${drugOrderObs.obsGroupId}obsIssueQuantity" size="12"></td>
<td> </td>
<td><input type="checkbox" id="${count}drugOrderProcessedOrNot" name="${count}drugOrderProcessedOrNot" checked="checked" onClick="hideReasonForNotDispensed(${count},'${drugOrderObs.obsGroupId}obsIssueQuantity');"> </td>
<td id="${count++}notDispensedColumn">
<select id="${drugOrderObs.obsGroupId}notDispensedReason"  name="${drugOrderObs.obsGroupId}notDispensedReason" style='width: 400px;'>
<% notDispensedConceptAnswers.each { notDispensedConceptAnswer -> %>
<option value="${notDispensedConceptAnswer.answerConcept.conceptId}">${notDispensedConceptAnswer.answerConcept.name}</option>
<% } %>
</select>
</td>
<td><input type="hidden" id="obsGroupIds" name="obsGroupIds" value="${drugOrderObs.obsGroupId}"></td>
</tr>
<% } %>
<% } %>

<tr>
<td>
<input type="hidden" id="patient" name="patient" value="${patient.patientId}">
</td>
</tr>
</table>

<div>
<br/>
</div>

<div>
<button type="submit" onclick="javascript:return drugOrderForm();">
				<img src="${ ui.resourceLink("chaiui", "images/glyphs/ok.png") }" /> ${ "Save" }
			</button>

<button type="button" class="cancel-button" onclick="ke_cancel();"><img src="${ ui.resourceLink("chaiui", "images/glyphs/cancel.png") }" /> Cancel</button>
</div>

</form>
</div>

<script type="text/javascript">
jQuery(document).ready(function(){
var drugOrderSize=${drugOrderSize};
for(var i=1;i<=drugOrderSize;i++){
jQuery('#'+i.toString()+'notDispensedColumn').hide();
}
});

jq(function() {
	chaiui.setupAjaxPost('drug-order-form', {
		onSuccess: function(data) {
			ui.navigate('chaiemr', 'dispensary/dispensing');
		}
	});
});
	
function ke_cancel() {
			ui.navigate('chaiemr', 'dispensary/dispensing');
		}


function drugOrderForm() {
var drugOrderProcessedId = ('${drugOrderProcessedId}');
var drugOrderObsId = ('${drugOrderObsId}');
var drugOrderProcessedIdArr = drugOrderProcessedId.split("/");
var drugOrderObsIdArr = drugOrderObsId.split("/");

for (var i = 0; i < drugOrderProcessedIdArr.length-1; i++){ 
var issueQuantity=jQuery('#'+drugOrderProcessedIdArr[i].toString()+'issueQuantity').val();
var isDisabled = jQuery('#'+drugOrderProcessedIdArr[i].toString()+'issueQuantity').prop('disabled');
if((issueQuantity==null || issueQuantity=="") && isDisabled==false){
alert("Please Enter Issue Quantity");
return false;
}
}

for (var i = 0; i < drugOrderObsIdArr.length-1; i++){ 
var issueQuantity=jQuery('#'+drugOrderObsIdArr[i].toString()+'obsIssueQuantity').val();
var isDisabled = jQuery('#'+drugOrderObsIdArr[i].toString()+'obsIssueQuantity');
if((issueQuantity==null || issueQuantity=="") && isDisabled==false){
alert("Please Enter Issue Quantity");
return false;
}
}
if(confirmDrugOrder()){
return true;
}
else{
return false;
}	
return true;
}

function confirmDrugOrder() {	
if(confirm("Are you sure?")){
return true;
 }
 else{
 return false;
 }
}

function hideReasonForNotDispensed(count,issueQuantity) {
var drugOrderProcessedOrNot=count.toString()+"drugOrderProcessedOrNot";
var notDispensedColumn=count.toString()+"notDispensedColumn";
if(jQuery('#'+drugOrderProcessedOrNot).is(':checked')){
jQuery('#'+notDispensedColumn).hide();
jQuery('#'+issueQuantity).removeAttr('disabled');
}
else{
jQuery('#'+notDispensedColumn).show();
jQuery('#'+issueQuantity).attr('disabled','disabled');
}
}	
</script>