

chaiemrApp.controller('AddresshierarchyCtrl', ['$scope', function($scope) {

	
	$scope.stateSelection = function(myState) {
		jq.getJSON('/' + OPENMRS_CONTEXT_PATH + '/chaiemr/emrUtils/addressHierarchy.action',{ state: myState})
		.done(function(data) {
			
			$scope.$apply(function(){ 
				$scope.townships = data.township;
				//console.debug("$scope.townships = data.township;");
				$scope.villages = "";
			});
			
	    });
	};
	
	$scope.townshipSelection = function(myState,myTownship) {
		jq.getJSON('/' + OPENMRS_CONTEXT_PATH + '/chaiemr/emrUtils/addressHierarchy.action',{ state: myState,township: myTownship})
		.done(function(data) {
			$scope.$apply(function(){ 
				$scope.villages = data.village;
				//console.debug("$scope.villages = data.village;");
			});
	    });
	};
	
	$scope.init = function(){
		jq.getJSON('/' + OPENMRS_CONTEXT_PATH + '/chaiemr/emrUtils/addressHierarchy.action',{ patientId: patientId})
	    .done(function(data) {
	    	$scope.$apply(function(){ 
	    		$scope.states = data.state;
		    	console.debug("$scope.states = data.state;");
		    	$scope.myState = data.selectedState;
		    	$scope.townships = data.townshipListForSelectedState;
		    	$scope.myTownship =data.selectedtownship;
		    	$scope.villages = data.villageListForSelectedTownship;
		    	$scope.myVillage =data.selectedvillage;
			});
	    	
	    });
	}
}]);