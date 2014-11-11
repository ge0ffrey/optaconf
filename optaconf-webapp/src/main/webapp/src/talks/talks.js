angular.module('talks', [])
    .controller('TalkCtrl', ['$resource', function($resource) {
        var vm = this;
        vm.title = 'Talks';
        vm.talks = [];

        var talkResource = $resource("http://localhost:8080/optaconf-webapp/rest/123/talk");
        talkResource.query(function(talks) {
            vm.talks = talks;
        });
    }]);