/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

/**
 * Patient service
 */
chaiemrApp.service('PatientService', function ($rootScope) {

	/**
	 * Broadcasts new patient search parameters
	 */
	this.updateSearch = function(query, which) {
		$rootScope.$broadcast('patient-search', { query: query, which: which });
	};
});

/**
 * Patient service2 
 * Support search with date field
 */
chaiemrApp.service('PatientService2', function ($rootScope) {

	/**
	 * Broadcasts new patient search parameters
	 */
	this.updateSearch = function(query, which, date,townShip) {
	$rootScope.$broadcast('patient-search2', { query: query, which: which, date: date,townShip:townShip });
	};
});

/**
 * Patient service3 
 * Support search with date field
 */
chaiemrApp.service('PatientService3', function ($rootScope) {

	/**
	 * Broadcasts new patient search parameters
	 */
	this.updateSearch = function(query, which, date) {
		$rootScope.$broadcast('patient-search3', { query: query, which: which, date: date });
	};
});

/**
 * Patient service4 
 * Support search with date field
 */
chaiemrApp.service('PatientService4', function ($rootScope) {

	/**
	 * Broadcasts new patient search parameters
	 */
	this.updateSearch = function(query, which, date) {
		$rootScope.$broadcast('patient-search4', { query: query, which: which, date: date });
	};
});

/**
 * Controller for patient search form
 */
chaiemrApp.controller('PatientSearchForm', ['$scope', 'PatientService', function($scope, patientService) {

	$scope.query = '';

	$scope.init = function(which) {
		$scope.which = which;
		$scope.$evalAsync($scope.updateSearch); // initiate an initial search
	};

	$scope.updateSearch = function() {
		patientService.updateSearch($scope.query, $scope.which);
	};
}]);

/**
 * Controller for patient search form with Date field
 */
chaiemrApp.controller('PatientSearchForm2', ['$scope', 'PatientService2', function($scope, patientService) {

	$scope.query = '';

	$scope.init = function(which) {
		$scope.which = which;
		$scope.date='';
		$scope.$evalAsync($scope.updateSearch); // initiate an initial search
	};

	$scope.updateSearch = function() {
		var scheduledDate = jQuery("#date").val();
		console.debug(scheduledDate);
		var townShip = jQuery("#township").val();
		patientService.updateSearch($scope.query, $scope.which, scheduledDate,townShip);
	};
	
}]);

/**
 * Controller for patient search form with Dispensing Date field
 */
chaiemrApp.controller('PatientSearchForm3', ['$scope', 'PatientService3', function($scope, patientService) {

	$scope.query = '';

	$scope.init = function(which) {
		$scope.which = which;
		$scope.date='';
		$scope.$evalAsync($scope.updateSearch); // initiate an initial search
	};

	$scope.updateSearch = function() {
		var dispensedDate = jQuery("#dispensedDate").val();
		console.debug(dispensedDate);
		patientService.updateSearch($scope.query, $scope.which, dispensedDate);
	};
	
}]);

/**
 * Controller for patient search form with Past Dispensing Date field
 */
chaiemrApp.controller('PatientSearchForm4', ['$scope', 'PatientService4', function($scope, patientService) {

	$scope.query = '';

	$scope.init = function(which) {
		$scope.which = which;
		$scope.date='';
		$scope.$evalAsync($scope.updateSearch); // initiate an initial search
	};

	$scope.updateSearch = function() {
		var processedDate = jQuery("#processedDate").val();
		console.debug(processedDate);
		patientService.updateSearch($scope.query, $scope.which, processedDate);
	};
	
}]);

/**
 * Controller for patient search results
 */
chaiemrApp.controller('PatientSearchResults', ['$scope', '$http', function($scope, $http) {

	$scope.query = '';
	$scope.results = [];

	/**
	 * Initializes the controller
	 * @param appId the current app id
	 * @param which
	 */
	$scope.init = function(appId, pageProvider, page) {
		$scope.appId = appId;
		$scope.pageProvider = pageProvider;
		$scope.page = page;
	};

	/**
	 * Listens for the 'patient-search' event
	 */
	$scope.$on('patient-search', function(event, data) {
		$scope.query = data.query;
		$scope.which = data.which;
		$scope.refresh();
	});
	
	/**
	 * Listens for the 'patient-search2' event
	 */
	$scope.$on('patient-search2', function(event, data) {
		$scope.query = data.query;
		$scope.which = data.which;
		$scope.date = data.date;
		$scope.townShip = data.townShip;
		$scope.refresh2();
	});
	
	/**
	 * Listens for the 'patient-search3' event
	 */
	$scope.$on('patient-search3', function(event, data) {
		$scope.query = data.query;
		$scope.which = data.which;
		$scope.date = data.date;
		$scope.refresh3();
	});
	
	/**
	 * Listens for the 'patient-search4' event
	 */
	$scope.$on('patient-search4', function(event, data) {
		$scope.query = data.query;
		$scope.which = data.which;
		$scope.date = data.date;
		$scope.refresh4();
	});

	/**
	 * Refreshes the person search
	 */
	$scope.refresh = function() {
		$http.get(ui.fragmentActionLink('chaiemr', 'search', 'patients', { appId: $scope.appId, q: $scope.query, which: $scope.which })).
			success(function(data) {
				$scope.results = data;
			});
	};
	
	/**
	 * Refreshes the person search
	 */
	$scope.refresh2 = function() {
		$http.get(ui.fragmentActionLink('chaiemr', 'search', 'patientsWithDate', { appId: $scope.appId, q: $scope.query, which: $scope.which, date: $scope.date, townShip:$scope.townShip })).
			success(function(data) {
				$scope.results = data;
			});
	};
	
	/**
	 * Refreshes the person search
	 */
	$scope.refresh3 = function() {
		$http.get(ui.fragmentActionLink('chaiemr', 'search', 'patientsWithDispensingDate', { appId: $scope.appId, q: $scope.query, which: $scope.which, date: $scope.date })).
			success(function(data) {
				$scope.results = data;
			});
	};
	
	/**
	 * Refreshes the person search
	 */
	$scope.refresh4 = function() {
		$http.get(ui.fragmentActionLink('chaiemr', 'search', 'patientsWithPastDispensingDate', { appId: $scope.appId, q: $scope.query, which: $scope.which, date: $scope.date })).
			success(function(data) {
				$scope.results = data;
			});
	};

	/**
	 * Result click event handler
	 * @param patient the clicked patient
	 */
	$scope.onResultClick = function(patient) {
		ui.navigate($scope.pageProvider, $scope.page, { patientId: patient.id });
	};
	
	$scope.onResultClickNewPatient = function(patient, appId, returnURL) {
		jQuery.ajax(ui.fragmentActionLink("chaiemr", "patient/checkFormStatus", "checkLastEncounter"), { data: { patient: patient.id}, dataType: 'json'
		}).success(function(data) 
		{
			var encountertype = data.encountertype;
			if (encountertype == 1)
			{
				returnURL = returnURL+ ".page?patientId="+patient.id+"&newVisit=true";
				var screeningresult = ui.pageLink("chaiemr", "registration/screeningResult", {patientId: patient.id, returnUrl: returnURL });
				ui.navigate(screeningresult);
				//ui.navigate('${ ui.pageLink("chaiemr", "registration/screeningResult", [ patientId: patient.id, returnUrl: ui.returnURL ]) }');
				return;
			}
		});	
		
		
		jQuery.ajax(ui.fragmentActionLink("chaiemr", "patient/checkFormStatus", "checkStatus"), { data: { patient: patient.id}, dataType: 'json'
		}).success(function(data) 
		{
			if(appId == "chaiemr.medicalEncounter" && !data.flag)
			{
				returnURL = returnURL+ ".page?patientId="+patient.id+"&newVisit=true";
				var obstetricFormLink = ui.pageLink("chaiemr", "enterForm", {patientId: patient.id, formUuid: '8e4e1abf-7c08-4ba8-b6d8-19a9f1ccb6c9', appId: appId, returnUrl: returnURL });
				var familyFormLink = ui.pageLink("chaiemr", "enterForm", {patientId: patient.id, formUuid: '7efa0ee0-6617-4cd7-8310-9f95dfee7a82', appId: appId, returnUrl: obstetricFormLink });
				var drugFormLink = ui.pageLink("chaiemr", "enterForm", {patientId: patient.id, formUuid: '5286ae88-85bb-46e8-a2f7-6361f463ffd4', appId: appId, returnUrl: familyFormLink });;
				var personalFormLink =  ui.pageLink("chaiemr", "enterForm", {patientId: patient.id, formUuid: "d1db31d0-b415-4788-a233-e4000bf4d108", appId: appId, returnUrl: drugFormLink });
				ui.navigate(personalFormLink); 
			} else {
				ui.navigate($scope.pageProvider, $scope.page, { patientId: patient.id });
			}
		});
	};
	
	$scope.viewDetails = function(patient) {
		jQuery.ajax(ui.fragmentActionLink("chaiemr", "dispensary/pastDispensingDrug", "dispensedDetails"), { data: { patient: patient.id}, dataType: 'json'
		}).success(function(data) {

			$('#pastDispensedDrug').empty();
			var htmlText =   "<table style='width: 100%'>"
				+"<tr>"
                +"<th>"
                +"S.No&nbsp;"
                +"</th>"
                +"<th>"
                +'Drug Name&nbsp;'
                +"</th>"
                 +"<th>"
                +"Strength&nbsp;"
                +"</th>"
                +"<th>"
                +"Unit&nbsp;"
                +"</th>"
                 +"<th>"
                +"Frequency&nbsp;"
                +"</th>"
                +"<th>"
                +'Duration'
                +"</th>"
                +"<th>"
                +'Quantity'
                +"</th>"
                +"</tr>"
			
			$.each(data, function(i, item){
	            $.each(this,function(j) {
	        
	            	htmlText=htmlText
	            	 +"<tr>"
	            	 +"<td>"
	                 +(j+1)
	                 +"</td>"
	            	 +"<td>"
	                 +this.drug
	                 +"</td>"
	                 +"<td>"
	                 +this.strength
	                 +"</td>"
	                 +"<td>"
	                 +this.formulation
	                 +"</td>"
	                 +"<td>"
	                 +this.frequency
	                 +"</td>"
	                 +"<td>"
	                 +this.duration
	                 +"</td>"
	                 +"<td>"
	                 +this.quantity
	                 +"</td>"
	                 +"</tr>"
	            });
	        });
			htmlText=htmlText
			 +"</table>"
			var newElement = document.createElement('div');
			newElement.setAttribute("id", "drugDetailDiv"); 
			newElement.innerHTML = htmlText;
			var fieldsArea = document.getElementById('pastDispensedDrug');
			fieldsArea.appendChild(newElement);

		    var url = "#TB_inline?height=300&width=750&inlineId=drugDetailDiv";
			tb_show("Detail Issue",url,false);
       });
   };

}]);

/**
 * Controller for similar patients (on registration form)
 */
chaiemrApp.controller('SimilarPatients', ['$scope', '$http', function($scope, $http) {

	$scope.givenName = '';
	$scope.familyName = '';
	$scope.results = [];

	/**
	 * Initializes the controller
	 * @param appId the current app id
	 * @param which
	 */
	$scope.init = function(appId, pageProvider, page) {
		$scope.appId = appId;
		$scope.pageProvider = pageProvider;
		$scope.page = page;
		$scope.refresh();
	};

	/**
	 * Refreshes the patient search
	 */
	$scope.refresh = function() {
		var query = $scope.givenName + ' ' + $scope.familyName;
		$http.get(ui.fragmentActionLink('chaiemr', 'search', 'patients', { appId: $scope.appId, q: query, which: 'all' })).
			success(function(data) {
				$scope.results = data;
			});
	};

	/**
	 * Result click event handler
	 * @param patient the clicked patient
	 */
	$scope.onResultClick = function(patient) {
		ui.navigate($scope.pageProvider, $scope.page, { patientId: patient.id });
	};

}]);

/**
 * Controller for daily schedule
 */
chaiemrApp.controller('DailySchedule', ['$scope', '$http', function($scope, $http) {

	$scope.date = null;
	$scope.scheduled = [];

	/**
	 * Initializes the controller
	 * @param appId
	 * @param date
	 * @param pageProvider
	 * @param page
	 */
	$scope.init = function(appId, date, pageProvider, page) {
		$scope.appId = appId;
		$scope.date = date;
		$scope.pageProvider = pageProvider;
		$scope.page = page;
		$scope.fetch();
	};

	/**
	 * Refreshes the schedule
	 */
	$scope.fetch = function() {
		$http.get(ui.fragmentActionLink('chaiemr', 'patient/patientUtils', 'getScheduled', { appId: $scope.appId, date: $scope.date })).
			success(function(data) {
				$scope.scheduled = data;
			});
	};

	/**
	 * Result click event handler
	 * @param patient the clicked patient
	 */
	$scope.onResultClick = function(patient) {
		ui.navigate($scope.pageProvider, $scope.page, { patientId: patient.id });
	};
}]);

/**
 * Controller for recently viewed
 */
chaiemrApp.controller('RecentlyViewed', ['$scope', '$http', function($scope, $http) {

	$scope.recent = [];

	/**
	 * Initializes the controller
	 * @param pageProvider
	 * @param page
	 */
	$scope.init = function() {
		$http.get(ui.fragmentActionLink('chaiemr', 'patient/patientUtils', 'recentlyViewed')).
			success(function(data) {
				$scope.recent = data;
			});
	};

	/**
	 * Result click event handler
	 * @param patient the clicked patient
	 */
	$scope.onResultClick = function(patient) {
		ui.navigate('chaiemr', 'chart/chartViewPatient', { patientId: patient.id });
	};
}]);