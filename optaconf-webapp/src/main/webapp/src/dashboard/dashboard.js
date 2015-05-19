'use strict';

angular.module('dashboard', [])
    .controller('DashboardCtrl', ['ScheduleImport', 'ScheduleSolve', '$log','$modal', '$timeout' , function(ScheduleImport, ScheduleSolve, $log, $modal, $timeout) {
        
    	var vm = this;
        vm.title = 'Dashboard';
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
                
                vm.feedback = "solved devoxx schedule";
                
                $timeout(function() {
					vm.feedback = ''
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