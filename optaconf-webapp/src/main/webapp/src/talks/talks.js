angular.module('talks', [])
    .controller('TalkCtrl', ['Talk', '$log','TalkService','$q', function(Talk, $log, TalkService, $q) {
        var vm = this;
        vm.title = 'Talks';
        vm.talks = [];

        var promises = [];
        TalkService.getDays()
            .then(function(days) {
                vm.days = days;
                _.forEach(days, function(day) {
                    promises.push($q(function(success, reject) {
                        return TalkService.getDayTalks(day);
                    }));
                    promises.push($q(function(success, reject) {
                        return TalkService.getTimeslotsForDay(day);
                    }));
                });
                $q.all(promises).then(function(results) {
                    $log.debug(results);
                })
            });

        Talk.query(function(talks) {
            vm.talks = talks;
            vm.rooms = [{
                key:'room1',
                value:'Room 1'
            }, {name:'Room 2'}, {name:'Room 3'}, {name:'Room 4'}];
            vm.timeslots = [{name:'12:00 - 12:50'}, {name:'13:00 - 13:50'}, {name:'14:00 - 14:50'}, {name:'15:00 - 15:50'}];
        });
    }])
    .factory('TalkService', function($http, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return {
            getDayTalks: function(day) {
                return $http.get('rest/123/'+day.id+'/talk');
            },
            getDays: function() {
                return $http.get('rest/123/day')
            },
            getTimeslotsForDay: function(day) {
                return $http.get('rest/123/day/'+day.id+'/timeslot');
            }
        };
    })
    .factory('Talk', ['$resource', '$window', function($resource, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return $resource("http://localhost:8080/" + contextPath + "/rest/123/talk");
    }]);