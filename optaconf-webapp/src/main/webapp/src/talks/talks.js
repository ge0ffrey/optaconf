angular.module('talks', [])
    .controller('TalkCtrl', ['Talk', '$log','TalkService','$q', function(Talk, $log, TalkService, $q) {
        var vm = this;
        vm.title = 'Talks';

        TalkService.getRooms().then(function(result) {
            var rooms = result.data;
            vm.rooms = rooms;
            TalkService.getMap().then(function(map) {
                var schedule = map.data;
                vm.schedule = map.data;
            });
        });

    }])
    .factory('TalkService', function($http, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return {
            getMap: function() {
                return $http.get('rest/123/talk/map');
            },
            getRooms: function() {
                return $http.get('rest/123/room');
            },
            getDayTalks: function(day) {
                return $http.get('rest/123/day/'+day.id+'/talk');
            },
            getDays: function() {
                return $http.get('rest/123/day')
            },
            getTimeslotsForDay: function(day) {
                return $http.get('rest/123/day/'+day.id+'/timeslot');
            }
        };
    })
    .factory('Talk', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return $resource("http://localhost:8080/" + contextPath + "/rest/123/talk");
    }]);