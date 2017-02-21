<%
def count=0;
%>
<script type="text/javascript">
	
</script>


<div id="${ config.id }-container">
	<input type="hidden" id="${ config.id }" name="${ config.formFieldName }" />
	<div>
		<input size="5" id="slNoah" name="slNoah" value="S.No" disabled>
		<input type="text" id="drugNameah" name="drugNameah" size="20" value="Drug Name" disabled>
    	<input type="text" id="strengthah" name="strengthah" size="20" value="Strength" disabled>
    	<input type="text" id="formulationah" name="formulationah" size="20" value="Unit" disabled>
    	<input type="text" id="frequencyah" name="frequencyah" size="20" value="Frequency" disabled>
    	<input type="text" id="durationah" name="durationah" size="20" value="Duration (in days)" disabled>
	</div>

	<% continueRegimen.each { continueRegim -> %>
	<div>
		<input type="text" id="slNoa${++count}" name="slNoa${count}" size="5" value="${count}" disabled>
	    <input type="text" id="drugName${count}" name="drugName${count}" size="20" value="${continueRegim.concept.name}" disabled>
        <input type="text" id="formulation${count}" name="formulation${count}" size="20" value="${continueRegim.dose}" disabled>
        <input type="text" id="strength${count}" name="strength${count}" size="20" value="${continueRegim.units}" disabled>
        <input type="text" id="frequency${count}" name="frequency${count}" size="20" value="${continueRegim.frequency}" disabled>
        <input type="text" id="duration${continueRegim.concept.name}" name="duration${continueRegim.concept.name}" size="20">
        <input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" size="5" id="info${count}" name="info${count}" value="Info" onClick="artDrugInfoo('drugName${count}');" />
	</div>
	<% } %>
</div>

<div id="guideDiv" style="visibility:hidden;">

</div>

<div id="drugInfoDiv" style="visibility:hidden;">		
</div>

<% if (config.parentFormId) { %>
<script type="text/javascript">
	jq(function() {
		subscribe('${ config.parentFormId }.reset', function() {
			jq('#${ config.id } input, #${ config.id } select').val('');
		});

		jq('#${ config.id }').change(function() {
			publish('${ config.parentFormId }/changed');
		});
	});
</script>
<% } %>

<script type="text/javascript">
	function artDrugInfoo(drugParameter){
		var drugName=jQuery('#'+drugParameter).val();
		jQuery.ajax(ui.fragmentActionLink("chaiemr", "field/drugInfo", "drugDetails"), { data: { drugName: drugName }, dataType: 'json'
						}).done(function(data) {druNam=data.drugName; toxicity=data.toxicity;riskFactor=data.riskFactor;suggestedManagement=data.suggestedManagement;
						drugInteraction=data.drugInteraction;suggestedManagementInteraction=data.suggestedManagementInteraction;
var htmlText =  "<table style='width: 100%'>"
                +"<tr>"
                +"<th>"
                +"Drug Name&nbsp;"
                +"</th>"
                +"<th>"
                +'Toxicity&nbsp;'
                +"</th>"
                 +"<th>"
                +"Risk Factor&nbsp;"
                +"</th>"
                +"<th>"
                +"Suggested Management&nbsp;"
                +"</th>"
                 +"<th>"
                +"Drug Interaction&nbsp;"
                +"</th>"
                +"<th>"
                +'Suggested Management Interaction'
                +"</th>"
                +"</tr>"
                
                +"<tr>"
                +"<td>"
                +druNam
                +"</td>"
                +"<td>"
                +toxicity
                +"</td>"
                 +"<td>"
                +riskFactor
                +"</td>"
                +"<td>"
                +suggestedManagement
                +"</td>"
                 +"<td>"
                +drugInteraction
                +"</td>"
                +"<td>"
                +suggestedManagementInteraction
                +"</td>"
                +"</tr>"
                +"</table>"
var newElement = document.createElement('div');
newElement.setAttribute("id", "drugDiv"); 
newElement.innerHTML = htmlText;
var fieldsArea = document.getElementById('drugInfoDiv');
jQuery('#guideDiv').empty();
jQuery('#drugInfoDiv').empty();
fieldsArea.appendChild(newElement);
var url = "#TB_inline?height=300&width=750&inlineId=drugDiv";
tb_show("Drug Info",url,false);
 });
}
</script>