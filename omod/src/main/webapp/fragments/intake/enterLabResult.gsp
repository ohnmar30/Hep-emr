<%
	ui.decorateWith("chaiui", "panel", [ heading: "Enter Lab Results" ])
%>

<% if (listTests == null) { %>
No record found.
<% } else { %>
<form id="enterLabResultForm" action="${ ui.actionLink("chaiemr", "intake/enterLabResult", "submit") }" method="post">
	<style>
		input[type=text] {
			border-bottom:1 solid #777 !important;
			width:100%;
			border-radius:0 !important;
			
		}
		
		.header{
			font-weight:bold;
			text-align:center;
			height:35px;
		}
		
		.description{
			text-align:left;
			padding-left:5px;
		}
		
		.unit {
			text-align:right;
			padding-right:5px;
		}
		
		.right {
			text-align:right;
		}
		th {
			text-align:center;
		}
		table{
			margin:auto;
		}
		input[type=text]{
			background-color:inherit;
		}
	</style>
	<input type="hidden" id="confirm" name="confirmed" value="${confirmed}"/>
	<input type="hidden" name="visitId" value="${visit.visitId}" />
	<% if (resultEncounter != null) { %>
		<input type="hidden" name="encounterId" value="${resultEncounter.encounterId}"/>
	<% } %>
	<table style="text-align:center;" class="${ listTests.empty ? 'hide' : 'table table-striped' }"  >
		<tr class="header">
			<th width="30" >No.</th>
			<th width="350" style="text-align:left;" >Lab Test</th>
			<th >Test Result</th>
			<th width="130" >Unit</th>
			<th width="150" >Reference Range</th>
			<th width="100" >Comment</th>
			<th width="150"></th>
			<th width="150"></th>
		</tr>
	<% listTests.eachWithIndex { test , count -> %>
		<input type="hidden" id="${test.conceptId}_isRadiology" name="${test.conceptId}_isRadiology" value="${test.isRadioloy}"/>	
		<% if (test.isRadioloy) { %>
		<tr name="record" value="${test.conceptId}" >
			<td>${count+1}.</td>
			<td colspan="7"  class="description" >
				${ test.name }
				<input type="hidden" name="conceptIds" value="${test.conceptId}"/>
				<input type="hidden" name="conceptValue" value="${test.json}" /> 
				<input type="hidden" name="${test.conceptId}_value" id="${test.conceptId}_value" value="${test.json}" />
			</td> 
		</tr>
		<tr name="finding" onclick="EnterLabResult.redirectFocus(this)" >
			<td></td>
			<td class="description" >Finding</td>
			<td name="${test.conceptId}" value="finding" colspan="3" ><input type="text" id="${test.conceptId}_valueFinding" name="value" size="15" value="${test.get('finding')}"  onblur="calculateComment(${test.conceptId})"></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr name="impression" onclick="EnterLabResult.redirectFocus(this)" >
			<td></td>
			<td class="description" >Impression</td>
			<td  name="${test.conceptId}" value="impression" colspan="3" ><input type="text" id="${test.conceptId}_valueImpression" name="value" size="15" value="${test.get('impression')}"  onblur="calculateComment(${test.conceptId})"></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<% }else if(test.labSet) { %>	
		<tr name="record" value="${test.conceptId}" >
			<td >${count+1}.</td>
			<td colspan="5" class="description">
				${ test.name }
				<input type="hidden" name="conceptIds" value="${test.conceptId}"/>
				<input type="hidden" id="${test.conceptId}_value" name="${test.conceptId}_value" value="${test.json}" /> 
			</td>
			<td></td>
			<td></td>
		</tr>
		<%  test.answerConcept.eachWithIndex {  answerConcept, testCount -> 
			def unit = test.getUnit(answerConcept.id)
		%>
		<tr onclick="EnterLabResult.redirectFocus(this)" name="${answerConcept.id}">
			<td></td>
			<td class="description">${ answerConcept.name.name }
				<input type="hidden" size="15" />
			</td>
			<td name="${test.conceptId}" value="${answerConcept.conceptId}" >
				<input type="text" name="value" value="${test.get(answerConcept.conceptId)}" onblur="calculateComment(${answerConcept.conceptId})" id="${answerConcept.conceptId}_value" class="right" />
			</td>
			<td class="unit" name="unit" data-info="${test.getUnitInfo(answerConcept)}" >
				<% if(test.isDropdown(unit)) {  %>
					<select onchange="EnterLabResult.convertUnit(this)" onfocus="EnterLabResult.preValue(this)" name="${answerConcept.id}" >
						<% test.getDropDown(unit).eachWithIndex{ dropdownElement, dropdownIndex -> %>
							<option>${dropdownElement}</option>
						<% } %>
					</select>
				<% }else{ %>
					${unit}
				<% } %></td>
			<td name="range" class="unit"  >${test.getReferenceRange(answerConcept.id)} </td>
			<td name="comment" >${test.getComment(answerConcept.id)} </td>
			<td></td>
			<td></td>
		</tr>
		<% } %>
		<% } else if(test.concept.id==159430 || test.concept.id == 111759) { %>
		<tr name="${test.conceptId}" onclick="EnterLabResult.redirectFocus(this)" >
			<td>${count+1}.</td>
			<td class="description">
				${ test.name }
				<input type="hidden" name="conceptIds" value="${test.conceptId}"/>
				<input type="hidden" name="conceptValue" value="${test.json}" /> 
			</td>
			<td class="unit" name="${test.conceptId}" value="${test.conceptId}" colspan="3" style="text-align:left;"  >
				<input class="hide" type="text" id="${test.conceptId}_value" name="${test.conceptId}_value" size="15" value="${test.result}"  onblur="EnterLabResult.initDropDown(${test.conceptId});" class="right" >
				<select onchange="EnterLabResult.updateDropDown(this,${test.conceptId});" >
					<option>Reactive</option>
					<option>Non-reactive</option>
				</select>
			</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<%} else { %> 
		<tr name="${test.conceptId}" onclick="EnterLabResult.redirectFocus(this)" >
			<td>${count+1}.</td>
			<td class="description">
				${ test.name }
				<input type="hidden" name="conceptIds" value="${test.conceptId}"/>
				<input type="hidden" name="conceptValue" value="${test.json}" /> 
			</td>
			<td class="unit" name="${test.conceptId}" value="${test.conceptId}" >
				<input type="text" id="${test.conceptId}_value" name="${test.conceptId}_value" size="15" value="${test.result}"  onblur="calculateComment(${test.conceptId})" class="right" >
			</td>
			<td name="unit" class="unit" data-info="${test.unitInfo}"  >
				<% if(test.isDropdown()) {  %>
					<select onchange="EnterLabResult.convertUnit(this)" onfocus="EnterLabResult.preValue(this)" name="${test.concept.id}" >
						<% test.getDropDown().eachWithIndex{ dropdownElement, dropdownIndex -> %>
							<option>${dropdownElement}</option>
						<% } %>
					</select>
				<% }else{ %>
					${test.units}
				<% } %>
			</td>
			<td name="range" class="unit" >${test.range}</td>
			<td name="comment" ></td>
			<% if(test.conceptId==790) {%>
			<td>Clearance rate :  
				<input disabled id="${test.conceptId}_rate" class="comment hide" size="15" type="text" value=""/>
			</td>
			<td name="other-update"></td>
			<% }else{ %>
				<td></td>
				<td></td>
			<% } %>
		</tr>
		<% } %>
	<% } %>
	</table> 
	</br>
	<% if(cxr) { %>
	<div> CXR </div>
		<div name="record" value="${cxr.conceptId}">
			<input type="hidden" name="conceptIds" value="${cxr.conceptId}" />
			<input type="hidden" name="${cxr.conceptId}_value" id="${cxr.conceptId}_value" value="" />
		</div>
	<table border="1" style="width:90%;" >
		<tr>
			<th></th>
			<th>Finding</th>
			<th>Right</th>
			<th>Left</th>
			<th>Impression</th>
		</tr>
		<tr>
			<td rowspan="3" >Lungs</td>
			<td> Upper zone</td>
			<td name="${cxr.conceptId}" value="lungs-upper-right" ><input type="text" name="value" value="${cxr.get('lungs-upper-right')}" /></td>
			<td name="${cxr.conceptId}" value="lungs-upper-left"><input type="text" name="value" value="${cxr.get('lungs-upper-left')}" /></td>
			<td name="${cxr.conceptId}" value="impression" rowspan="6" ><textarea name="value" rows="6" style="width:100%;resize:false;height:100%;border:none;"  >${cxr.get('impression')}</textarea></td>
		</tr>
		<tr>
			<td>Middle Zone</td>
			<td name="${cxr.conceptId}" value="lungs-middle-right"> <input type="text" name="value" value="${cxr.get('lungs-middle-right')}" /> </td>
			<td name="${cxr.conceptId}" value="lungs-middle-left"> <input type="text" name="value" value="${cxr.get('lungs-middle-left')}" /> </td>
		</tr>
		<tr>
			<td>Lower Zone</td>
			<td name="${cxr.conceptId}" value="lungs-lower-right"> <input type="text" name="value" value="${cxr.get('lungs-lower-right')}" /> </td>
			<td name="${cxr.conceptId}" value="lungs-lower-left"> <input type="text" name="value" value="${cxr.get('lungs-lower-left')}" /> </td>
		</tr>
		<tr>
			<td>Heart</td>
			<td colspan="3" name="${cxr.conceptId}" value="heart" ><input type="text" name="value" value="${cxr.get('heart')}" /></td>
		</tr>
		<tr>
			<td>Bone</td>
			<td colspan="3" name="${cxr.conceptId}" value="bone" ><input type="text" name="value" value="${cxr.get('bone')}"/></td>
		</tr>
		<tr>
			<td>Mediastinum</td>
			<td colspan="3" name="${cxr.conceptId}" value="mediastinum" ><input type="text" name="value" value="${cxr.get('mediastinum')}" /></td>
		</tr>
	</table>
	<br />
	<% } 
	if(sputumSmearMicroscopy) { %>
		<!--  sputumSmearMicroscopy -->
		<div>Sputum Smear Microscopy</div>
		<div name="record" value="${sputumSmearMicroscopy.conceptId}">
			<input type="hidden" name="conceptIds" value="${sputumSmearMicroscopy.conceptId}" />
			<input type="hidden" name="${sputumSmearMicroscopy.conceptId}_value" id="${sputumSmearMicroscopy.conceptId}_value" value="" />
		</div>
		<table border="1" >
			<tr>
				<th>Type of Test</th>
				<th>Result Date</th>
				<th>Lab Nubmer</th>
				<th>Result</th>
			</tr>
			<tr>
				<td>Sputum smear microscopy</td>
				<td name="${sputumSmearMicroscopy.conceptId}" value="resultDate"><input type="text" name="value" value="${sputumSmearMicroscopy.get('resultDate')}" /></td>
				<td name="${sputumSmearMicroscopy.conceptId}" value="labNumber" ><input type="text" name="value" value="${sputumSmearMicroscopy.get('labNumber')}" /></td>
				<td name="${sputumSmearMicroscopy.conceptId}" value="result"  >
					<select name="value">
						<option ${sputumSmearMicroscopy.isSelect('result','afbNotSeen')} value="afbNotSeen" >AFB not seen</option>
						<option ${sputumSmearMicroscopy.isSelect('result','trace')} value="trace" >Trace</option>
						<option ${sputumSmearMicroscopy.isSelect('result','plus')}	value="plus" >+</option>
						<option ${sputumSmearMicroscopy.isSelect('result','doublePlus')} value="doublePlus" >++</option>
						<option ${sputumSmearMicroscopy.isSelect('result','triplePlus')} value="triplePlus" >+++</option>
					</select>
				</td>
			</tr>
		</table>
		<br />
	<% } 
	if(mgit) {%>
			<!--  liquid culture -->
			<div>Liquid Culture MGIT</div>
			<table border="1" >
				<tr>
					<th>Type of Test</th>
					<th>Result Date</th>
					<th>Lab number</th>
					<th>Result</th>
				</tr>
				<tr>
					<td>Liquid Culture(MGIT)</td>
					<td><input type="text" />
					<td><input type="text" />
					<td>
						<select>
							<option>Negative</option>
							<option>Position</option>
							<option>Contaminated</option>
						</select>
					</td>
				</tr>	
			</table>
		<br />
	<% } 
	if(lj) { %>
		<!--  Solid Culture (L J) -->
		<div>Solid Culture(L J)</div>
		<table border="1" >
			<tr>
				<th>Type of Test</th>
				<th>Result Date</th>
				<th>Lab Number</th>
				<th>Result</th>
			</tr>
			<tr>
				<td>Solid Culture (L J)</td>
				<td><input type="text" /></td>
				<td><input type="text" /></td>
				<td>
					<select>
						<option>Negative</option>
						<option>1-10 colonies</option>
						<option>+</option>
						<option>++</option>
						<option>+++</option>
						<option>Contaminated</option>
					</select>
				</td>
			</tr>
		</table>
		<br />
	<% } 
	if(lpa) {%>
	<!--  Line Probe Assay (LPA) -->
		<table border="1" name="main" >
			<tr>
				<th >Type of Test</th>
				<th >Result Date</th>
				<th >Lab number</th>
				<th width="50" >MTBC</th>
				<th width="50" >H</th>
				<th width="50" >R</th>
				<th width="50" >Am</th>
				<th width="50" >Km</th>
				<th width="50" >Cm</th>
				<th width="50" >Ofx</th>
				<th width="50" >Lfx</th>
			</tr>
			<tr>
				<td>LPA</td>
				<td><input type="text" /></td>
				<td><input type="text" /></td>
				<td name="mtbc" >
					<select name="value" >
						<option>Yes</option>
						<option>No</option>
					</select>
				</td>
				<td name="">
					<select name="value" >
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
			</tr>
		</table>
		<br />
	<% } 
	if(dst) { %>
		<table border="1" >
			<tr>
				<th>Drug Sensitivity Test(DST)</th>
				<th>Result Date</th>
				<th>Lab Number</th>
				<th>H</th>
				<th>R</th>
				<th>S</th>
				<th>E</th>
				<th>Am</th>
				<th>Km</th>
				<th>Cm</th>
				<th>Ofx</th>
				<th>Lfx</th>
				<th>Eto</th>
				<th>Cs</th>
				<th>PAS</th>
			</tr>
			<tr>
				<td>DST</td>
				<td><input type="text" /></td>
				<td><input type="text" /></td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
				<td>
					<select>
						<option>R</option>
						<option>S</option>
					</select>
				</td>
			</tr>
		</table>
		<br />
	<% } %>
	<div style="text-align:right;">
	<% if (!confirmed) { %>
	<input type="button" value="Confirm" onclick="EnterLabResult.confirmResult()" class="btn"/>
	<input type="submit" value="Save" class="hide" id="enter-lab-result" class="btn btn-success"/>
	<input type="button" value="Save" onclick="EnterLabResult.validate('enter-lab-result')"  class="btn btn-warn"/>
<% } %>
	<input type="button" value="Cancel" onclick="ui.navigate('${returnUrl}')" class="btn btn-cancel "/>
	</div>
	</form>
<% } %>

<script type="text/javascript">
jq(function() {
	jq('#enterLabResultForm .cancel-button').click(function() {
		ui.navigate('${ returnUrl }');
	});

	chaiui.setupAjaxPost('enterLabResultForm', {
		onSuccess: function(data) {
			ui.navigate('${ returnUrl }');
		}
	});
	
	jq(".comment").each(function () {
		var inputId = jq(this).attr("id");
		var conceptId = inputId.split("_")[0];
		calculateComment(conceptId);
	});
	
	var confirmed = jq("#confirm").val();
	if (confirmed == "true") {
		jq("#enterLabResultForm input[type=text]").each(function(){jq(this).attr("disabled","disabled");});
	}
	
});

function calculateComment(conceptId) {
	if ("true" == jq("#"+conceptId+"_isRadiology").val()) {
		return ;
	}
	EnterLabResult.updateComment('${patientGender}','${patientAge}',conceptId);
}

jq(function(){
	EnterLabResult.initComment();
});

</script>