'use strict';

angular.module('schedule', [])
    .controller('ScheduleCtrl', ['Day', 'Room', function(Day, Room) {
        var vm = this;
        vm.title = 'Schedule';
        vm.days = [];
        vm.rooms = [];
        vm.timeslots = [];

        vm.init = function() {
            Day.query(function(days) {
                vm.days = days;
            });

            Room.query(function(rooms) {
                vm.rooms = rooms;
            });
        };

        vm.dayChanged = function(day) {
            Day.getTimeslots({id: day.id}, function(timeslots) {
                vm.timeslots = timeslots;
            });
        };

        vm.init();
    }])
    .factory('Day', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        var url = "/" + contextPath + "/rest/123/day";
        return $resource(url, null, {
            'getTimeslots': {
                url: url + '/:id/timeslot',
                method: 'GET',
                isArray: true
            }
        });
    }])
    .factory('ScheduleImport', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];

        return $resource("/" + contextPath + "/rest/123/schedule/import/devoxx", null, {
            'import': {
                method: 'POST',
                responseType: 'text',
                transformResponse: []
            }
        });
    }])
    .factory('ScheduleSolve', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];

        return $resource("/" + contextPath + "/rest/123/schedule/solve", null, {
            'solve': {
                method: 'PUT',
                responseType: 'text',
                transformResponse: []
            }
        });
    }]);
