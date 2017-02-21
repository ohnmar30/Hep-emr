<%
		
	ui.decorateWith("chaiemr", "standardPage", [ patient: currentPatient ])
%>

<div class="ke-patientheader">

</div>

<div class="ke-page-content">

	${ ui.includeFragment("chaiemr", "dispensary/drugOrderList", [ patient: currentPatient ]) }

</div>

<div id="footer">
<b>Note:</b> By default the boxes for <b>Dispense</b> will be checked for processing the drugs. For drugs that are not dispensed, please uncheck the boxes and select reason for not dispensed.
</div>

<style type="text/css" media="screen">
#footer
{
font-size: 14px;
font-style:  italic;
text-align: center;
position: absolute;
bottom: -300px;
left: 0px;
width: 100%;
height: 20px;
visibility: visible;
display: block
}

th,td {
    text-align: center;
}

</style>


