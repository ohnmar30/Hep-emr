<%
	ui.decorateWith("chaiui", "panel", [ heading: "Quaterly Report" ])

%>

<form id="hfgjjbb">

	<label class="ke-field-label">Quaterly Selection:</label>
	<span class="ke-field-content">
		<select style='width: 155px;height: 30px;' type="text" id="quaterly" name="quaterly" >
<% listOfYear.each { year -> %>
<option value="${year}">${year}</option>
<% } %>
</select>
	</span>
	
	<input type="button" value="View" onclick="viewQuaterlyReport();"/>
	<input type="button" value="Export" onclick="exportQuaterlyReportToExcel('exportQuaterlyReport');"/>
	<input type="button" value="Cancel" onclick="ke_cancel_quaterly();"/>
</form>


<script type="text/javascript">
//View Report
function viewQuaterlyReport() {
var quaterly=jQuery('#quaterly').val();
jQuery('#viewReportQuaterly').empty();

jQuery.ajax({
				type : "GET",
				url : getContextPath_quaterly() + "/chaiemr/reports/getQuaterlyReport.page",
				data : ({
					quaterly:quaterly
				}),
				success : function(data) {
				jQuery("#viewReportQuaterly").html(data);	
				}
  });
}

//Excel export
var exportQuaterlyReportToExcel = (function() {
			
var quaterly=jQuery('#quaterly').val();
jQuery('#exportQuaterlyReport').empty();
jQuery.ajax({
				type : "GET",
				url : getContextPath_quaterly() + "/chaiemr/reports/getQuaterlyReport.page",
				data : ({
					quaterly:quaterly
				}),
				success : function(data) {
				jQuery("#exportQuaterlyReport").html(data);	
				}
         });
         
		var uri = 'data:application/vnd.ms-excel;base64,'
		, template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table >{table}</table></body></html>'
		, base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
		, format = function(s, c) { return s.replace(/{(\\w+)}/g, function(m, p) { return c[p]; }) }
		return function(table, name) {
		if (!table.nodeType) table = document.getElementById(table)
		var ctx = {worksheet: name || 'White Card', table: table.innerHTML}
		
		var link = document.createElement("a");
		link.href = uri + base64(format(template, ctx));

		link.style = 'visibility:hidden';
		link.download ='${ currDate } - quaterly report.xls';

		document.body.appendChild(link);
		link.click();
		
		}
	})()

// get context path in order to build controller url
	function getContextPath_quaterly() {
		pn = location.pathname;
		len = pn.indexOf("/", 1);
		cp = pn.substring(0, len);
		return cp;
	}

function ke_cancel_quaterly() {
			ui.navigate('chaiemr', 'reports/reportsHome');
}
</script>

<div class="ke-page-content" id="exportQuaterlyReport" hidden="hidden">

</div>