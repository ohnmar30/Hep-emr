
chaiemrApp.controller('DrugCtrl', ['$scope', function($scope) {
	var counter = 1;
	$scope.choices = [{srNo:'1',srNumber:'srNumber1',id:'choice1',drugKey:'drugKey1',strength:'strength1',noOfTablet:'noOfTablet1',route:'route1',type: 'type1',frequncy: 'frequncy1',duration:'duration1'}];
	$scope.addNewChoice = function() {
		var newItemNo = ++counter//$scope.choices.length+1;
		$scope.choices.push({srNo:newItemNo,srNumber:'srNumber'+newItemNo,id:'choice'+newItemNo,drugKey:'drugKey'+newItemNo,strength:'strength'+newItemNo,noOfTablet:'noOfTablet'+newItemNo,route:'route'+newItemNo,type: 'type'+newItemNo,frequncy:'frequncy'+newItemNo,duration:'duration'+newItemNo});
	}
	
	$scope.removeChoice = function(index) {
	  //$scope.choices.splice(index.srNo-1,1);
		if($scope.choices.length==1){
			alertify.error("You can't delete last row");
			return ;
		}
		if($scope.choices.length==1){
			alertify.error("You can't delete last row");
			return ;
		}
	   	for(var i=0;i<$scope.choices.length;i++){
	   		var element = $scope.choices[i];
	    	if(element.srNo==index.srNo){
	    		console.log("Deleting srno %d",index.srNo);
	    		$scope.choices.splice(i,1);
	    		break;
	    	}
	    }
	    /*
	    var newItemNo = $scope.choices.length;
	    $scope.choices=[];
	    for(var i=1;i<=newItemNo;i++){
	    	$scope.choices.push({srNo:i,srNumber:'srNumber'+i,id:'choice'+i,drugKey:'drugKey'+i,strength:'strength'+i,noOfTablet:'noOfTablet'+i,type: 'type'+i,frequncy:'frequncy'+i,duration:'duration'+i});
	    }*/
	}
	
	$scope.drugSearch = function(drugKey,choice){
	//var drugKey="drugKey"+count.toString();
	//$scope.strength = $scope[drugKey].strength;
	//$scope.strength = $scope.drugKey.strength;
	var srNo=choice.srNo;
	jQuery('#switchRegimenSearch').empty();
	$('#strength'+srNo).val(drugKey.strength);
	$('#noOfTablet'+srNo).val(drugKey.noOfTablet);
	$('#type'+srNo).val(drugKey.type);
	$('#frequncy'+srNo).val(drugKey.frequency);
	$("#route"+srNo).val("160240");
	//$('#duration'+srNo).val(drugKey.duration);
	}
	
	$scope.usageChange = function(drugKey)
	{
		jQuery('#txtTest').val(drugKey);
	}
	
	$scope.drugSearchForSwitch = function(drugKey,choice){
	var srNo=choice.srNo;
	jQuery('#substituteRegimenSearch').empty();
	$('#strength'+srNo).val(drugKey.strength);
	$('#noOfTablet'+srNo).val(drugKey.noOfTablet);
	$('#type'+srNo).val(drugKey.type);
	$('#frequncy'+srNo).val(drugKey.frequency);
	$("#route"+srNo).val("160240");
	//$('#duration'+srNo).val(drugKey.duration);
	}
	
	$scope.artDrugInfoForRegimenSearch=function(drugKey){
		var drugName=drugKey.drugName;
		jQuery('#drugInfoDiv').empty();
		jQuery.ajax(ui.fragmentActionLink("chaiemr", "field/drugInfo", "drugDetails"), { data: { drugNames: drugName }, dataType: 'json'
		}).done(function(data) {
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

        $.each(data, function(i, item){
            $.each(this,function(j) {
        
            	htmlText=htmlText
            	 +"<tr>"
            	 +"<td>"
                 +this.drugName
                 +"</td>"
                 +"<td>"
                 +this.toxicity
                 +"</td>"
                 +"<td>"
                 +this.riskFactor
                 +"</td>"
                 +"<td>"
                 +this.suggestedManagement
                 +"</td>"
                 +"<td>"
                 +this.drugInteraction
                 +"</td>"
                 +"<td>"
                 +this.suggestedManagementInteraction
                 +"</td>"
                 +"</tr>"
            });
        });
		htmlText=htmlText
		 +"</table>"
       var newElement = document.createElement('div');
      newElement.setAttribute("id", "drugDiv"); 
      newElement.innerHTML = htmlText;
      var fieldsArea = document.getElementById('drugInfoDiv');
      fieldsArea.appendChild(newElement);
      var url = "#TB_inline?height=500&width=750&inlineId=drugDiv";
      tb_show("Drug Info",url,false);
      });
	}
	
	$scope.artDrugInfoForContinueRegimenSearch=function(drugName){
		jQuery('#drugInfoDiv').empty();
		jQuery.ajax(ui.fragmentActionLink("chaiemr", "field/drugInfo", "drugDetails"), { data: { drugNames: drugName }, dataType: 'json'
		}).done(function(data) {
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

        $.each(data, function(i, item){
            $.each(this,function(j) {
        
            	htmlText=htmlText
            	 +"<tr>"
            	 +"<td>"
                 +this.drugName
                 +"</td>"
                 +"<td>"
                 +this.toxicity
                 +"</td>"
                 +"<td>"
                 +this.riskFactor
                 +"</td>"
                 +"<td>"
                 +this.suggestedManagement
                 +"</td>"
                 +"<td>"
                 +this.drugInteraction
                 +"</td>"
                 +"<td>"
                 +this.suggestedManagementInteraction
                 +"</td>"
                 +"</tr>"
            });
        });
		htmlText=htmlText
		 +"</table>"
       var newElement = document.createElement('div');
      newElement.setAttribute("id", "drugDiv"); 
      newElement.innerHTML = htmlText;
      var fieldsArea = document.getElementById('drugInfoDiv');
      fieldsArea.appendChild(newElement);
      var url = "#TB_inline?height=500&width=750&inlineId=drugDiv";
      tb_show("Drug Info",url,false);
      });
	}
	
	$scope.init = function(){
		jq.getJSON('/' + OPENMRS_CONTEXT_PATH + '/chaiemr/emrUtils/drugConcept.action',{ patientId: patientId})
	    .done(function(data) {
	    	$scope.$apply(function(){ 
	    		$scope.myDrug = data.drugConceptName;
	    		$scope.myDrug2 = data.substituteConceptName;
	    		$scope.myDrug3 = data.switchConceptName;
			});
	    	
	     });
	 }

}]);
