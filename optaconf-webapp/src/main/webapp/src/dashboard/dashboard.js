'use strict';

angular.module('dashboard', [])
    .controller('DashboardController', ['ScheduleImport', 'ScheduleSolve', '$log','$modal', '$timeout', '$location', 'ConferenceService','$websocket','$window', function(ScheduleImport, ScheduleSolve, $log, $modal, $timeout, $location, ConferenceService, $websocket, $window) {
        
    	var vm = this;
        vm.feedback = '';
        
        
        vm.init = function(){
	        ConferenceService.getAll().then(function(result) {
	            vm.conferences = result.data;
	            $log.info(vm.conferences);
	        });
        }
        
        
        vm.init();
        
        vm.import = function() {
        	$log.info("Importing devoxx schedule...");
        	vm.busyModal = vm.openBusyImport();
            ScheduleImport.import(function() {
            	vm.busyModal.close();
            	vm.init();
            	$log.info('imported devoxx schedule!');
                
            	vm.feedback = "imported devoxx schedule";
                
                $timeout(function() {
					vm.feedback = ''
				}, 5000);
                
                
            });
        };

        
        vm.solve = function(conferenceId) {
        	$log.info('Getting schedule for Conference ID: '+conferenceId);
            var API = $window.location.host+$window.location.pathname+'schedule';
        	
        	var dataStream = $websocket('ws://'+API);
        	
        	dataStream.send(JSON.stringify({ action: 'solve', id: conferenceId }));
			$location.path('/schedule/'+conferenceId);
			
        };
        
        vm.view = function(conferenceId) {
        	$log.info('Getting schedule for Conference ID: '+conferenceId);
            var API = $window.location.host+$window.location.pathname+'schedule';
        	
        	var dataStream = $websocket('ws://'+API);
        	
        	dataStream.send(JSON.stringify({ action: 'view', id: conferenceId }));
        	
        	$location.path('/schedule/'+conferenceId);
        	
        };
        
        vm.openBusyImport = function() {

			return $modal.open({
				templateUrl : 'src/dashboard/_importModal.html',
				backdrop : 'static',
				keyboard : false
			});

		};
		
		
    }]).factory('ConferenceService', function($http, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return {
            getAll: function() {
                return $http.get('rest/conference');
            },
            getOne: function(conferenceId){
            	return $http.get('rest/conference/'+conferenceId);
            }
        };
    });