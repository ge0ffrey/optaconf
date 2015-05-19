'use strict';

var app = angular.module('optaconf', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.utils', 'dashboard', 'rooms', 'schedule', 'talks']);

app.config(["$routeProvider", function($routeProvider) {
    $routeProvider.
        when('/', {
                templateUrl: 'src/dashboard/dashboard.html',
                controller: 'DashboardCtrl as vm'
        }).
        when('/rooms', {
            templateUrl: 'src/rooms/rooms.html',
            controller: 'RoomCtrl as vm'
        }).
        when('/schedule', {
            templateUrl: 'src/schedule/schedule.html',
            controller: 'ScheduleCtrl as vm'
        }).
        when('/talks', {
            templateUrl: 'src/talks/talks.html',
            controller: 'TalkCtrl as vm'
        }).
        otherwise({
            redirectTo: '/'
        });
}]);