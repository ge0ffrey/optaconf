angular.module('rooms', [])
    .controller('RoomCtrl', ['$resource', function($resource) {
        var vm = this;
        vm.title = 'Rooms';
        vm.rooms = [];

        var roomResource = $resource("http://localhost:8080/optaconf-webapp/rest/123/room");
        roomResource.query(function(rooms) {
            vm.rooms = rooms;
        });
    }]);