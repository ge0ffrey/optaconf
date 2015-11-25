'use strict';

angular.module('talks', [])
    .controller('TalkController', ['$log','TalkFactory','$routeParams','$websocket','$window', function($log, TalkFactory, $routeParams, $websocket, $window) {
        var vm = this;
        vm.title = 'Talk Schedule';
        
        vm.weekDays = ["monday","tuesday","wednesday","thursday","friday"];
        
        var viewSchedule = function(conferenceId) {
        	$log.info('Getting schedule for Conference ID: '+conferenceId);
            var API = $window.location.host+$window.location.pathname+'schedule';
        	
        	var dataStream = $websocket('ws://'+API);
        	
        	dataStream.send(JSON.stringify({ action: 'view',id:conferenceId }));
        	
        	dataStream.onMessage(function(message) {
            	$log.info("onMessage");
            	$log.info(message.data);
                vm.schedule = JSON.parse(message.data);
                
            })
        	
        }
        
        TalkFactory.getRooms($routeParams.conferenceId).then(function(result) {
            vm.rooms = result.data;
            $log.info('-----------------------------------------------');
            $log.info(result);
            viewSchedule($routeParams.conferenceId);
            $log.info('-----------------------------------------------');
            
            
        });
    }])
    .factory('TalkFactory', function($http, $window, $log, $websocket) {
        return {
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