'use strict';

angular.module('speakers', [])
    .controller('SpeakerController', ['$log','SpeakerFactory','$routeParams', function($log, SpeakerFactory, $routeParams) {
        var vm = this;
        vm.title = 'Speakers';
        
        function init(){
	        SpeakerFactory.getAll($routeParams.conferenceId).then(function(result) {
	          vm.speakers = result.data;
	        });
        }
        
        init();
        
        vm.toggleRockstar = function(speakerId){
        	$log.info("Toggling rockstar flag for Speaker ID: "+speakerId);
        	SpeakerFactory.toggleRockstar($routeParams.conferenceId, speakerId).then(function(result){
        		var speaker = result.data;
        		init();
        	});
        	
        }
        
    }])
    .factory('SpeakerFactory', function($http, $window, $log) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return {
            getAll: function(conferenceId) {
            	$log.debug('Getting speakers for Conference ID: ' + conferenceId);
            	var API = 'rest/'+conferenceId+'/speaker/';
                return $http.get(API);
            },
            toggleRockstar: function (conferenceId, speakerId){
            	$log.debug('Toggling rockstar flag for Speaker ID: ' + speakerId)
            	var API = 'rest/'+conferenceId+'/speaker/'+speakerId+'/toggleRockstar';
                return $http.put(API);
            }
        };
    });