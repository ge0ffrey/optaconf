angular.module('rooms', [])
    .controller('RoomCtrl', ['Room', function(Room) {
        var vm = this;
        vm.title = 'Rooms';
        vm.rooms = [];

        Room.query(function(rooms) {
            vm.rooms = rooms;
        });
    }])
    .factory('Room', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];

        return $resource("http://localhost:8080/" + contextPath + "/rest/123/room");
    }]);;