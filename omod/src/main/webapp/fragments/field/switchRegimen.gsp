<%
	def formulations = [ "25", "30", "50", "60", "100", "150", "200" , "300" ,"400" , "600" ]
	
	def units = [ " " , "mg", "g", "ml"]
	
	def blank= [" "]

	def frequencies = [
			bl: " " ,
			OD: "Once daily",
			NOCTE: "Once daily, at bedtime",
			qPM: "Once daily, in the evening",
			qAM: "Once daily, in the morning",
			BD: "Twice daily",
			TDS: "Thrice daily"
	]

	def refDefIndex = 0;

	def groupOptions = {
		it.regimens.collect( { reg -> """<option value="${ refDefIndex++ }">${ reg.name }</option>""" } ).join()
	}

	def drugOptions = drugs.collect( { """<option value="${ it }">${ chaiEmrUi.formatDrug(it, ui) }</option>""" } ).join()
	def formulationOptions = formulations.collect( { """<option value="${ it }">${ it }</option>""" } ).join()
	def blankOptions = blank.collect( { """<option value="${ it }">${ it }</option>""" } ).join()
	def unitsOptions = units.collect( { """<option value="${ it }">${ it }</option>""" } ).join()
	def frequencyOptions = frequencies.collect( { """<option value="${ it.key }">${ it.value }</option>""" } ).join()
%>
<script type="text/javascript">
	// Create variable to hold regimen presets
	if (typeof regimen_presets === 'undefined') {
		regimen_presets = new Array();
	}
	regimen_presets['${ config.id }'] = ${ ui.toJson(regimenDefinitions) };

	jq(function() {
		jq('#${ config.id }-container .standard-regimen-select').change(function () {
			// Get selected regimen definition
			var stdRegIndex = parseInt(jq(this).val());
			var stdReg = regimen_presets['${ config.id }'][stdRegIndex];
			var components = stdReg.components;

			// Get container div and component fields
			var container = jq(this).parent();
			var drugFields = container.find('.regimen-component-drug');
			var doseFields = container.find('.regimen-component-dose');
			var unitsFields = container.find('.regimen-component-units');
			var frequencyFields = container.find('.regimen-component-frequency');
			var durationFields = container.find('.regimen-component-duration');

			// Clear all inputs
			container.find('input, select').val('');

			// Set component controls for each component of selected regimen
			for (var c = 0; c < components.length; c++) {
				var component = components[c];
				jq(drugFields[c]).val(component.drugRef);
				jq(doseFields[c]).val(component.dose);
				jq(unitsFields[c]).val(component.units);
				jq(frequencyFields[c]).val(component.frequency);
				jq(durationFields[c]).val(component.duration);
			}

			chaiemr.updateSwitchRegimenFromDisplay('${ config.id }');
		});

		jq('#${ config.id }-container .regimen-component-drug, #${ config.id }-container .regimen-component-dose, #${ config.id }-container .regimen-component-units, #${ config.id }-container .regimen-component-frequency, #${ config.id }-container .regimen-component-duration').blur(function() {
			chaiemr.updateSwitchRegimenFromDisplay('${ config.id }');
		});
	});
</script>


<div id="${ config.id }-container">
	<input type="hidden" id="${ config.id }" name="${ config.formFieldName }" />
	<i>Use standard:</i> <select class="standard-regimen-select" id="standardRegimenSelectSwitch" name="standardRegimenSelectSwitch" onchange="optionGroupSwitch();">
		<option label="Select..." value="" />
		<% regimenGroups.each { group -> %>
			<optgroup label="${ group.name }">${ groupOptions(group) }</optgroup>
		<% } %>
	</select>
	<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" id="guideSwitch" name="guideSwitch" value="Guide" onClick="guideeSwitch();" />
	<br />
	<br />
	
	<div>
	<input size="5" id="slNoahSwitch" name="slNoahSwitch" value="S.No" disabled>
	<input type="text" id="drugNameahSwitch" name="drugNameahSwitch" size="20" value="Drug Name" disabled>
    <input type="text" id="formulationahSwitch" name="formulationahSwitch" size="20" value="Strength" disabled>
    <input type="text" id="strengthahSwitch" name="strengthahh" size="20" value="Unit" disabled>
    <input type="text" id="frequencyahSwitch" name="frequencyahSwitch" size="20" value="Frequency" disabled>
    <input type="text" id="durationahSwitch" name="durationahSwitch" size="20" value="Duration (in days)" disabled>
    <input type="text" id="actionahSwitch" name="actionahSwitch" size="8" value="Action" disabled>
</div>

	<span id="${ config.id }-error" class="error" style="display: none"></span>
	<% for (def c = 0; c < maxComponents; ++c) { %>
	<div class="regimen-component">
		<input type="text" id="slNoSwitch${c+1}" name="slNoSwitch${c+1}" size="5" value="${c+1}" readonly="readonly">
		<select class="regimen-component-drug" style='width: 155px;height: 30px;' id="drugSwitch${c+1}"  name="drugSwitch${c+1}"><option value="" />${ drugOptions }</select>
		<select class="regimen-component-dose" style='width: 155px;height: 30px;' id="doseSwitch${c+1}"  name="doseSwitch${c+1}"><option value="" />${ formulationOptions }</select>
		<select class="regimen-component-units" style='width: 158px;height: 30px;' id="unitSwitch${c+1}"  name="unitSwitch${c+1}">${ unitsOptions }</select>
		<select class="regimen-component-frequency" style='width: 158px;height: 30px;' id="frequencySwitch${c+1}"  name="frequencySwitch${c+1}">${ frequencyOptions }</select>
		<input class='regimen-component-duration' type="text" id="durationSwitch${c+1}" name="durationSwitch${c+1}" size="20">
		 
		<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" size="5" id="infoSwitch${c+1}" name="infoSwitch${c+1}" value="Info" onClick="artDrugInfoSwitch('drugSwitch${c+1}');" />
		<% if (c==maxComponents-1) { %>
		<input type="button" class="ui-button ui-widget ui-state-default ui-corner-all" id="addSwitch" name="addSwitch" value="Add" onClick="addDrugOrderForARVTreatmentSwitch();" />
		<% } %>
		<input type="hidden" id="selectedOptionSwitch${c+1}" name="selectedOptionSwitch${c+1}">
	</div>
	<% } %>
	<div id="headerValuea">		
</div>
</div>

<div id="guideDivSwitch" style="visibility:hidden;">

</div>

<div id="drugInfoDivSwitch" style="visibility:hidden;">		
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
var maxSlNoForSwitch=${maxComponents};
var arvtreatment=${maxComponents+1};
var drugRegimen="";

function addDrugOrderForARVTreatmentSwitch() {
   var arvtretment = "arvtreatment"+arvtreatment;;
   var deleteString = 'deleteInputARVTreatment(\"'+arvtretment+'\")';
   var htmlText =  "<input id='slNo"+arvtreatment+"'  name='slNo"+arvtreatment+"' type='text' size='5' value="+arvtreatment+" readonly='readonly'/>&nbsp;"
	       	
	       	 +"<select class=regimen-component-drug' id='drug"+arvtreatment+"'  name='drug"+arvtreatment+"' style='width: 156px;height: 30px;' onchange='drugAddition(this);'>"
	         +'${blankOptions} '
	         +'${ drugOptions }'
	       	 +"</select>&nbsp;"
	       	 +"<select class='regimen-component-dose' id='dose"+arvtreatment+"'  name='dose"+arvtreatment+"' style='width: 156px;height: 30px;'>"
	         +'${blankOptions} '
	         +'${ formulationOptions }'
	       	 +"</select>&nbsp;"
	       	 +"<select class='regimen-component-units' id='unit"+arvtreatment+"'  name='unit"+arvtreatment+"' style='width: 156px;height: 30px;'>"
	         +'${ unitsOptions }'
	       	 +"</select>&nbsp;"
	       	 +"<select class='regimen-component-frequency' id='frequency"+arvtreatment+"'  name='frequency"+arvtreatment+"' style='width: 158px;height: 30px;'>"
	         +'${ frequencyOptions }'
	       	 +"</select>&nbsp;"
	       	 +"<input class='regimen-component-duration' id='duration"+arvtreatment+"'  name='duration"+arvtreatment+"' type='text' size='20'/>&nbsp;"
	       	 +"<input id='info"+arvtreatment+"' class='ui-button ui-widget ui-state-default ui-corner-all' type='button' size='5' name='info"+arvtreatment+"' value='Info' onclick='artDrugInfoSwitch();'/>&nbsp;"
	       	 +"<a style='color:red' href='#' onclick='"+deleteString+"' >[X]</a>&nbsp;"
	       	 +"<input id='drug"+arvtreatment+"List'  name='durgList' type='hidden'/>";
	       	 
   var newElement = document.createElement('div');
   newElement.setAttribute("id", "arvtreatment"+arvtreatment);  
   newElement.setAttribute("class", "regimen-component");    
   newElement.innerHTML = htmlText;
   var fieldsArea = document.getElementById('headerValuea');
   fieldsArea.appendChild(newElement);
   arvtreatment++;
   maxSlNoForSwitch++;
}

function deleteInputARVTreatment(arvtreatment) {
   var parentDiv = 'headerValuea';
   var child = document.getElementById(arvtreatment);
   var parent = document.getElementById(parentDiv);
   parent.removeChild(child); 
   Array.prototype.remove = function(v) { this.splice(this.indexOf(v) == -1 ? this.length : this.indexOf(v), 1); }
   deleteInputARVTreatment2();
}

function deleteInputARVTreatment2() {
   for (var i = ${maxComponents+1} ; i <= maxSlNoForSwitch; i++){
   var arvtreatment2 = "arvtreatment"+i;
   var parentDiv = 'headerValuea';
   var child = document.getElementById(arvtreatment2);
   var parent = document.getElementById(parentDiv);
   if(child!=null){
   parent.removeChild(child); 
   Array.prototype.remove = function(v) { this.splice(this.indexOf(v) == -1 ? this.length : this.indexOf(v), 1); }
   }
  
   arvtreatment--
  }
  
  maxSlNo2 = maxSlNoForSwitch-1;
  maxSlNoForSwitch = arvtreatment-1;
  for (var i = ${maxComponents+1} ; i <= maxSlNo2; i++){
  addDrugOrderForARVTreatmentSwitch();
  }
  
}

function drugAddition(thisObj){
var id=thisObj.id.toString()+"List";
jQuery('#'+id).val(thisObj.value);
}

function artDrugInfoSwitch(drugParameter){
var drugName=jQuery('#'+drugParameter).find("option:selected").text();
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
newElement.setAttribute("id", "drugDivSwitch"); 
newElement.innerHTML = htmlText;
var fieldsArea = document.getElementById('drugInfoDivSwitch');
jQuery('#guideDivSwitch').empty();
jQuery('#drugInfoDivSwitch').empty();
fieldsArea.appendChild(newElement);
var url = "#TB_inline?height=300&width=750&inlineId=drugDivSwitch";
tb_show("Drug Info",url,false);
 });
}

function guideeSwitch(){
jQuery('#guideDivSwitch').empty();
jQuery('#drugInfoDivSwitch').empty();
var age=${patient.age};
if(age>14){
var htmlText =  "<img src='${ ui.resourceLink('chaiui', 'images/glyphs/flow_chart_adult.JPG') }' />"
var newElement = document.createElement('div');
newElement.setAttribute("id", "guideDivAdultSwitch"); 
newElement.innerHTML = htmlText;
var fieldsArea = document.getElementById('guideDivSwitch');
fieldsArea.appendChild(newElement);
var url = "#TB_inline?height=750&width=1150&inlineId=guideDivAdultSwitch";
tb_show("Guide",url,false);
}
else{
var htmlText =  "<img src='${ ui.resourceLink('chaiui', 'images/glyphs/flow_chart_child.JPG') }' />"
var newElement = document.createElement('div');
newElement.setAttribute("id", "guideDivChildSwitch"); 
newElement.innerHTML = htmlText;
var fieldsArea = document.getElementById('guideDivSwitch');
fieldsArea.appendChild(newElement);
var url = "#TB_inline?height=750&width=1350&inlineId=guideDivChildSwitch";
tb_show("Guide",url,false);
}
}

function optionGroupSwitch(){
var drugRegimenSelected=jQuery('#standardRegimenSelectSwitch option:selected').text(); 
drugRegimen=drugRegimenSelected;
}
</script>