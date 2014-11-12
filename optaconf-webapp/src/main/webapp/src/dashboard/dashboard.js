angular.module('dashboard', [])
    .controller('DashboardCtrl', ['Schedule', function(Schedule) {
        var vm = this;
        vm.title = 'Dashboard';

        vm.import = function() {
            Schedule.get({});
        }
    }])
    .factory('Schedule', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];

        return $resource("http://localhost:8080/" + contextPath + "/rest/123/schedule/import/devoxx");
    }]);;