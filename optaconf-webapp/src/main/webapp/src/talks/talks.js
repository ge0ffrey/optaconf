angular.module('talks', [])
    .controller('TalkCtrl', ['Talk', function(Talk) {
        var vm = this;
        vm.title = 'Talks';
        vm.talks = [];

        Talk.query(function(talks) {
            vm.talks = talks;
        });
    }])
    .factory('Talk', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];

        return $resource("http://localhost:8080/" + contextPath + "/rest/123/talk");
    }]);