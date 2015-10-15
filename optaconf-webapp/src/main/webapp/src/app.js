'use strict';

var app = angular.module('optaconf', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.utils', 'dashboard', 'rooms', 'schedule', 'talks', 'speakers', 'nav']);

app.config(["$routeProvider", function($routeProvider) {
    $routeProvider.
        when('/', {
                templateUrl: 'src/dashboard/dashboard.html',
                controller: 'DashboardController as vm'
        }).
        when('/rooms/:conferenceId', {
            templateUrl: 'src/rooms/rooms.html',
            controller: 'RoomController as vm'
        }).
        when('/schedule/:conferenceId', {
            templateUrl: 'src/talks/talks.html',
            controller: 'TalkController as vm'
        }).
        when('/speakers/:conferenceId', {
            templateUrl: 'src/speakers/speakers.html',
            controller: 'SpeakerController as vm'
        }).
        otherwise({
            redirectTo: '/'
        });
}]);