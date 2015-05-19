angular.module('rooms', [])
    .controller('RoomController', ['Room', function(Room) {
        var vm = this;
        vm.title = 'Rooms';
        vm.rooms = [];

        Room.query(function(rooms) {
            vm.rooms = rooms;
        });
    }])
    .factory('Room', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];

        return $resource("/" + contextPath + "/rest/123/room");
    }]);;