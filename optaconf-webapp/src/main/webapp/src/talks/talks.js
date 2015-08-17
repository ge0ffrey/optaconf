'use strict';

angular.module('talks', [])
    .controller('TalkController', ['$log','TalkFactory','$routeParams', function($log, TalkFactory, $routeParams) {
        var vm = this;
        vm.title = 'Talk Schedule';
        
        vm.weekDays = ["monday","tuesday","wednesday","thursday","friday"];
        
        TalkFactory.getRooms($routeParams.conferenceId).then(function(result) {
            vm.rooms = result.data;
            TalkFactory.getMap($routeParams.conferenceId).then(function(map) {
                vm.schedule = map.data;
            });
        });
    }])
    .factory('TalkFactory', function($http, $window, $log) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return {
            getMap: function(conferenceId) {
            	$log.info('Getting schedule for Conference ID: '+conferenceId);
            	var API = 'rest/'+conferenceId+'/talk/map';
                return $http.get(API);
            },
            getRooms: function(conferenceId) {
                return $http.get('rest/'+conferenceId+'/room');
            },
            getDayTalks: function(conferenceId, day) {
                return $http.get('rest/'+conferenceId+'/day/'+day.id+'/talk');
            },
            getDays: function(conferenceId) {
                return $http.get('rest/'+conferenceId+'/day')
            },
            getTimeslotsForDay: function(conferenceId, day) {
                return $http.get('rest/'+conferenceId+'/day/'+day.id+'/timeslot');
            }
        };
    });