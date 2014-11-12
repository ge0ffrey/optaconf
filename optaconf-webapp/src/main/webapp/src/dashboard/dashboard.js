angular.module('dashboard', [])
    .controller('DashboardCtrl', ['Schedule', function(Schedule) {
        var vm = this;
        vm.title = 'Dashboard';
        vm.isSolving = false;

        vm.import = function() {
            console.log("Importing devoxx schedule...");
            Schedule.import(function() {
                console.log('imported devoxx schedule!');
            });
        };

        vm.solve = function() {
            console.log("Solving schedule...");
            vm.isSolving = true;
            Schedule.solve(function() {
                console.log("solved devoxx schedule!");
            });
        };
    }]);