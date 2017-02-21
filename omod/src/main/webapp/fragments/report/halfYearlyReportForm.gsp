<%
	ui.decorateWith("chaiui", "panel", [ heading: "Half Yearly Report" ])

%>

<form id="halfYearlyReport">

	<label class="ke-field-label">Half Yearly Selection:</label>
	<span class="ke-field-content">
		<select style='width: 155px;height: 30px;' type="text" id="halfYearly" name="halfYearly" >
<% listOfYear.each { year -> %>
<option value="${year}">${year}</option>
<% } %>
</select>
	</span>
	
	<input type="button" value="View" onclick="viewHalfYearlyReport();"/>
	<input type="button" value="Export" onclick="exportHalfYearlyReportToExcel('exportHalfYearlyReport');"/>
	<input type="button" value="Cancel" onclick="ke_cancel_halfyearly();"/>
</form>


<script type="text/javascript">
//View Report
function viewHalfYearlyReport() {
var halfYearly=jQuery('#halfYearly').val();
jQuery('#viewHalfYearlyReport').empty();

jQuery.ajax({
				type : "GET",
				url : getContextPath_halfyearly() + "/chaiemr/reports/getHalfYearlyReport.page",
				data : ({
					halfYearly:halfYearly
				}),
				success : function(data) {
				jQuery("#viewHalfYearlyReport").html(data);	
				}
  });
}

//Excel export
var exportHalfYearlyReportToExcel = (function() {
			
var halfYearly=jQuery('#halfYearly').val();
jQuery('#exportHalfYearlyReport').empty();
jQuery.ajax({
				type : "GET",
				url : getContextPath_halfyearly() + "/chaiemr/reports/getHalfYearlyReport.page",
				data : ({
					halfYearly:halfYearly
				}),
				success : function(data) {
				jQuery("#exportHalfYearlyReport").html(data);	
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
		link.download ='${ currDate } - half yearly report.xls';

		document.body.appendChild(link);
		link.click();
		
		}
	})()

// get context path in order to build controller url
	function getContextPath_halfyearly() {
		pn = location.pathname;
		len = pn.indexOf("/", 1);
		cp = pn.substring(0, len);
		return cp;
	}

function ke_cancel_halfyearly() {
			ui.navigate('chaiemr', 'reports/reportsHome');
}
</script>

<div class="ke-page-content" id="exportHalfYearlyReport" hidden="hidden">

</div>