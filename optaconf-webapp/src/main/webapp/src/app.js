'use strict';

var app = angular.module('optaconf', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.utils', 'dashboard', 'rooms', 'schedule', 'talks']);

app.config(["$routeProvider", function($routeProvider) {
    $routeProvider.
        when('/', {
                templateUrl: 'src/dashboard/dashboard.html',
                controller: 'DashboardController as vm'
        }).
        when('/rooms', {
            templateUrl: 'src/rooms/rooms.html',
            controller: 'RoomController as vm'
        }).
        when('/schedule', {
            templateUrl: 'src/schedule/schedule.html',
            controller: 'ScheduleController as vm'
        }).
        when('/talks', {
            templateUrl: 'src/talks/talks.html',
            controller: 'TalkController as vm'
        }).
        otherwise({
            redirectTo: '/'
        });
}]);