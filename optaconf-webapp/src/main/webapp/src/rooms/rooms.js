angular.module('rooms', [])
    .controller('RoomController', ['RoomFactory', '$routeParams', '$log', function(RoomFactory, $routeParams, $log) {
        var vm = this;
        vm.title = 'Rooms';
        vm.rooms = [];

        RoomFactory.get($routeParams.conferenceId).then(function(reponse) {
            vm.rooms = reponse.data;
        });
    }])
    .factory('RoomFactory', ['$resource', '$window','$log', '$http', function($resource, $window, $log, $http) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        
        return {
			get : function(conferenceId) {
				$log.info(conferenceId);
				
				var API = "/" + contextPath + "/rest/"+conferenceId+"/room";
				var headers = {
					'Content-Type' : 'application/json'
				};
				$log.info('Getting room list for schedule ID: '+conferenceId);
				$log.info(API);
				return $http.get(API, headers);

			}		
        };
    }]);;