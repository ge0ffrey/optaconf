angular.module('dashboard', [])
    .controller('DashboardCtrl', ['ScheduleImport', 'ScheduleSolve', '$log' , function(ScheduleImport, ScheduleSolve, $log) {
        var vm = this;
        vm.title = 'Dashboard';
        vm.isSolving = false;
        vm.importUrl = "";

        vm.import = function() {
        	//TODO grab importUrl and do input validation
        	$log.info("Importing devoxx schedule...");
            ScheduleImport.import(function() {
                $log.info('imported devoxx schedule!');
            });
        };

        vm.solve = function() {
            $log.info("Solving schedule...");
            vm.isSolving = true;
            ScheduleSolve.solve(function() {
                $log.info("solved devoxx schedule!");
            });
        };
    }]);