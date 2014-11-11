var app = angular.module('optaconf', ['ngResource', 'ngRoute', 'rooms', 'talks']);

app.config(["$routeProvider", function($routeProvider) {
    $routeProvider.
        when('/rooms', {
            templateUrl: 'src/rooms/rooms.html',
            controller: 'RoomCtrl as vm'
        }).
        when('/talks', {
            templateUrl: 'src/talks/talks.html',
            controller: 'TalkCtrl as vm'
        }).
        otherwise({
            redirectTo: '/rooms'
        });
}]);