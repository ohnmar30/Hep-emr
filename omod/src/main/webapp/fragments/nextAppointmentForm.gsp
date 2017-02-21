<%
	ui.decorateWith("chaiui", "panel", [ heading: "Appointment" ])
%>
<script type="text/javascript">jQuery(document).ready(function(){ 
jq('#afterdates').on('change', function() 
            {  
			getSelectOption(jq(this).val());
			
		}); });
 	jq(function() {
		jq('#startDate_date').change(function() {document.getElementById('afterdates').value="";
			var dateStart = document.getElementById('startDate_date').value;
			var patientId =${patient.id};
		
			jQuery.ajax(ui.fragmentActionLink("chaiemr" , "nextAppointmentCount",  "getTotalPatient"),{ data: { patient:patientId ,date:dateStart}, dataType: 'json'
			}).done(function(data) {
		            alert("There are "+data.count+" patients appointed on "+dateStart);
		 });

		});
	});
	
	    function getSelectOption(elem)
    {  
     var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
		var date = new Date();
        
        if(elem== 1805){ 
            date.setDate(date.getDate() + 15);
        }
        if(elem== 1750){ 
            date.setDate(date.getDate() + 30);  
        }
        
        if(elem== 1802){ 
            date.setDate(date.getDate() + 60); 
        }
        if(elem== 1867){ 
            date.setDate(date.getDate() + 90);  
        }
        var someFormattedDate = date.getDate() + "-" + months[date.getMonth()] + "-" + date.getFullYear();
        if(someFormattedDate!="Invalid Date")
        {
			   document.getElementById('startDate_date').value =  date.getDate() + "-" + months[date.getMonth()] + "-" + date.getFullYear();
					var dateStart= document.getElementById('startDate_date').value;
					var patientId =${patient.id};
		            var value=document.getElementById('afterdates').value;
		            
			jQuery.ajax(ui.fragmentActionLink("chaiemr" , "nextAppointmentCountAfterDay",  "getTotalPatient"),{ data: { patient:patientId ,date:dateStart,answer:value}, dataType: 'json'
			}).done(function(data) {
		            alert("There are "+data.count+" patients appointed on "+dateStart);
		 });
					
		}
		
    };
 	
				
				
		    	
		
function saveAppointment(){
		var dateStart = document.getElementById('startDate_date').value;
		
		var patientId =${patient.id} ;
      var value=document.getElementById('afterdates').value;
     
      if(document.getElementById('afterdates').value =="")
      { 
      jQuery.ajax(ui.fragmentActionLink("chaiemr" , "nextAppointmentCount",  "saveAppointment"),{ data: { patient:patientId ,date:dateStart}, dataType: 'json'
		}).done(function(data) {
	            alert("Saved");	            
                ui.reloadPage();
	 });
		
	 }
	 else
	 {
	 jQuery.ajax(ui.fragmentActionLink("chaiemr" , "nextAppointmentCountAfterDay",  "saveAppointment"),{ data: { patient:patientId ,date:dateStart,answer:value}, dataType: 'json'
		}).done(function(data) {
	            alert("Saved");
                ui.reloadPage();
	 });
	 }
	 
};
</script>
<div>
	<span class="ke-field-content">
	<%if(appoint){ %>
		Next Appointment Date : ${ ui.includeFragment("chaiui", "field/java.util.Date" ,[id:'startDate', initialValue:appointmentDate, formFieldName:'startDate', showTime: false])}
	    After:  
	    <select id="afterdates" style='width: 100px;height: 25px;'><option value=""></option>
               <% afterDate.each { after -> %> 
               <option value="${after.answerConcept.conceptId}">${after.answerConcept.name}</option>
               <% } %>
               </select>
	    <% } else  { %>
	     Next Appointment Date : ${ ui.includeFragment("chaiui", "field/java.util.Date" ,[id:'startDate', initialValue:appointmentafterDate, formFieldName:'startDate', showTime: false])}
	   After:  
	    <select id="afterdates" style='width: 100px;height: 25px;'><option value=""></option>
               <% afterDate.each { after -> %> 
               <option value="${after.answerConcept.conceptId}" <%if(after.answerConcept.conceptId == appointmentafterDay){ %> selected="true"
                <% } %>>${after.answerConcept.name}</option>
               <% } %>
               </select>
	   <% } %>
	  

	</span>
	<span class="ke-field-content">
		<input type="button" value="Save" onclick="saveAppointment();"/>
	</span>
</div>		
