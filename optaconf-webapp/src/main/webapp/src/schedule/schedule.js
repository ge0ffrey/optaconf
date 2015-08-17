'use strict';

angular.module('schedule', [])
    .controller('ScheduleController', ['Day', 'Room', function(Day, Room) {
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
        var url = "/" + contextPath + "/rest/2/day";
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

        return $resource("/" + contextPath + "/rest/conference/import/devoxx", null, {
            'import': {
                method: 'POST',
                responseType: 'text',
                transformResponse: []
            }
        });
    }])
    .factory('ScheduleSolve', ['$http', '$log','$window', function($http, $log, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        
        return {
			solve : function(conferenceId) {
				$log.info(conferenceId);
				
				var solveAPI = "/" + contextPath + "/rest/conference/"+conferenceId+"/solve";
				var headers = {
					'Content-Type' : 'application/json'
				};
				$log.info('Solving schedule ID: '+conferenceId);
				$log.info(solveAPI);
				return $http.put(solveAPI, null, headers);

			}		
        };
        
        
    }]);
