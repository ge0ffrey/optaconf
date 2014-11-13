angular.module('talks', [])
    .controller('TalkCtrl', ['Talk', '$log','TalkService','$q', function(Talk, $log, TalkService, $q) {
        var vm = this;
        vm.title = 'Talks';
        vm.talks = [];
        vm.schedule = [];
        TalkService.getDays()
            .then(function(result) {
                var days = result.data;
                _.forEach(days, function(d) {
                    var day = {
                        name: d.name
                    };
                    days.push(day);
                    var timeslotsPromises = [];
                    timeslotsPromises.push($q(function(success, reject) {
                        return TalkService.getTimeslotsForDay(d).then(function(result) {
                            day.timeslots = result.data;
                            success(day);
                        }, reject);
                    }));
                    $q.all(timeslotsPromises).then(function(timeslots) {
                        $log.debug(timeslots);
                    });
                });
                vm.days = days;

            });

    }])
    .factory('TalkService', function($http, $window) {
        var contextPath = $window.location.pathname.substr(1).split('/')[0];
        return {
            getDayTalks: function(day) {
                return $http.get('rest/123/day/'+day.id+'/talk');
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