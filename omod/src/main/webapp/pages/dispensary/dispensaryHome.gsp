<%
	ui.decorateWith("chaiemr", "standardPage", [ layout: "" ])
%>

<div class="ke-patientheader">
<div style="float: left; padding-right: 5px">
		<button class="ke-compact" title="Dispensing" onclick="ke_dispensing()"><img src="${ ui.resourceLink("chaiui", "images/apps/registration.png") }"/> Dispensing</button>
</div>

<div style="float: right; padding-right: 5px">
		<button class="ke-compact" title="Past Dispensing Record" onclick="ke_pastDispensingRecord()"><img src="${ ui.resourceLink("chaiui", "images/apps/registration.png") }"/> Past Dispensing Record</button>
</div>

</div>

<script type="text/javascript">

		function ke_dispensing() {
			ui.navigate('chaiemr', 'dispensary/dispensing');
		}
		
		function ke_pastDispensingRecord() {
			ui.navigate('chaiemr', 'dispensary/pastDispensingRecord');
		}

</script>

