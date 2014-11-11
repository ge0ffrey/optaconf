angular.module('dashboard', [])
    .controller('DashboardCtrl', ['$resource', function($resource) {
        var vm = this;
        vm.title = 'Dashboard';

        var scheduleResource = $resource("http://localhost:8080/optaconf-webapp/rest/123/schedule/import/devoxx");
        vm.import = function() {
            scheduleResource.get({});
        }
    }]);