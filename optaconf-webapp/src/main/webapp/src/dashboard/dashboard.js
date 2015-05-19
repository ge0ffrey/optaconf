'use strict';

angular.module('dashboard', [])
    .controller('DashboardController', ['ScheduleImport', 'ScheduleSolve', '$log','$modal', '$timeout', '$location' , function(ScheduleImport, ScheduleSolve, $log, $modal, $timeout, $location) {
        
    	var vm = this;
        vm.title = 'Import and Optimize Devoxx FR 2015 Schedule';
        vm.feedback = '';
        
        vm.import = function() {
        	$log.info("Importing devoxx schedule...");
        	vm.busyModal = vm.openBusyImport();
            ScheduleImport.import(function() {
            	vm.busyModal.close();
            	
            	$log.info('imported devoxx schedule!');
                
            	vm.feedback = "imported devoxx schedule";
                
                $timeout(function() {
					vm.feedback = ''
				}, 5000);
                
                
            });
        };

        vm.solve = function() {
            $log.info("Solving schedule...");
         
			vm.busyModal = vm.openBusySolve();
			
            ScheduleSolve.solve(function() {
                $log.info("solved devoxx schedule!");
                
                vm.busyModal.close();
                
                vm.feedback = "solved devoxx schedule, redirecting to Talk Schedule in 5 seconds.";
                
                $timeout(function() {
					vm.feedback = '';
					$location.path('/talks');
					
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
    }]);