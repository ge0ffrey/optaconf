'use strict';

angular.module('dashboard', [])
    .controller('DashboardCtrl', ['ScheduleImport', 'ScheduleSolve', '$log','$modal' , function(ScheduleImport, ScheduleSolve, $log, $modal) {
        var vm = this;
        vm.title = 'Dashboard';
        vm.isSolving = false;
        vm.clickedImport = false;
        vm.clickedSolve = false;
        vm.import = function() {
        	//TODO grab importUrl and do input validation
        	$log.info("Importing devoxx schedule...");
            ScheduleImport.import(function() {
                $log.info('imported devoxx schedule!');
                vm.clickedImport = true;
            });
        };

        vm.solve = function() {
            $log.info("Solving schedule...");
         // open modal dialog and save the reference so we can close it
			vm.busyModal = vm.openBusyModal();
            vm.isSolving = true;
            ScheduleSolve.solve(function() {
                $log.info("solved devoxx schedule!");
                vm.busyModal.close();
                vm.clickedSolve = true;
            });
        };
        
        vm.openBusyModal = function() {

			return $modal.open({
				templateUrl : 'src/shared/_busyModal.html',
				backdrop : 'static',
				keyboard : false
			});

		};
    }]);