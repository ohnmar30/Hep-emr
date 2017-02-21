
<div class="ke-page-content">
	<form id="importPatientForm" name="importPatientForm" action="${ ui.actionLink("chaiemr", "patient/importPatientsList", "submit") }" method="post" enctype="multipart/form-data">
	  <table>
	    <tr>
	      <td class="NormalB">Upload/Import (XLS) File :</td>
	      <td>&nbsp;</td>
	
	      <td class="NormalB">
	        <input type="file" id="upload" name="upload" size="31" class="{validate:{required:true}}" >
	      </td>
	    </tr>
	
	    <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	    <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	
	    <tr>
	      <td class="NormalB">
	        <input type="submit" name="importReport" value='Import Data' style="width:150px">
	      </td>
	    </tr>
	  </table>
	</form>
</div>

<script type="text/javascript">
function savePatient(){
var filename =  document.getElementById("upload").value;

	alert(filename);
		jQuery.ajax(ui.fragmentActionLink("chaiemr" , "patient/importPatientsList",  "submit"),{ data: { fileName:filename }, dataType: 'json'
		}).done(function(data) {
			ui.navigate('${ returnUrl }');
	 });
		
};


</script>

