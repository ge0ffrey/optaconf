'use strict';

angular.module('dashboard', [])
    .controller('DashboardController', ['ScheduleImport', 'ScheduleSolve', '$log','$modal', '$timeout', '$location', 'ConferenceService', function(ScheduleImport, ScheduleSolve, $log, $modal, $timeout, $location, ConferenceService) {
        
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
            $log.info("Solving schedule for conference id " + conferenceId);
         
			vm.busyModal = vm.openBusySolve();
			
            ScheduleSolve.solve(conferenceId).then(function() {
                $log.info("solved devoxx schedule!");
                vm.init();
                vm.busyModal.close();
                
                vm.feedback = "solved devoxx schedule, redirecting to Talk Schedule in 5 seconds.";
                
                $timeout(function() {
					vm.feedback = '';
					$location.path('/schedule/'+conferenceId);
					
				}, 5000);
            });
        };
        
        vm.openBusyImport = function() {

			return $modal.open({
				templateUrl : 'src/dashboard/_importModal.html',
				backdrop : 'static',
				keyboard : false
			});

		};
		
		vm.openBusySolve = function() {

			return $modal.open({
				templateUrl : 'src/dashboard/_solveModal.html',
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