'use strict';

angular.module('nav', ['dashboard'])
    .controller('NavController', ['$log','$location','$routeParams','ConferenceService', function($log, $location, $routeParams, ConferenceService) {
        var vm = this;
        vm.title = '';
        vm.conferenceId = $routeParams.conferenceId;
        vm.sheduleTab = '';
        vm.roomsTab = '';
        vm.speakersTab = '';
        
        function init(){
        	ConferenceService.getOne($routeParams.conferenceId).then(function(result) {
	          vm.conference = result.data;
	          vm.title = vm.conference.name;
	          
	          if($location.url().indexOf('schedule')> -1){
	        	  vm.sheduleTab = 'active';
	          }else if($location.url().indexOf('rooms')> -1){
	        	  vm.roomsTab = 'active';
	          }else if($location.url().indexOf('speakers')> -1){
	        	  vm.speakersTab = 'active';
	          } 
	          
	        });
        }
        
        init();
        
    }]);