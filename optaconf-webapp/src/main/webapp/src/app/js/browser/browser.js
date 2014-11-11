(function () {
    'use strict';

    angular
        .module('app.browser')
        .controller('Browser', Browser);

    Browser.$inject = ['$q', 'dataservice', 'logger'];

    /* @ngInject */
    function Browser($q, dataservice, logger) {
        /*jshint validthis: true */
        var vm = this;

        vm.news = {
            title: 'Beobachter',
            description: 'Beobachter webapp'
        };
        vm.topicCount = 0;
        vm.topic = [];
        vm.title = 'Browse';

        activate();

        function activate() {
            var promises = [getTopicCount(), getTopic()];
            return $q.all(promises).then(function(){
                logger.info('Activated Browser View');
            });
        }

        function getTopicCount() {
            return dataservice.getTopicCount().then(function (data) {
                vm.topicCount = data;
                return vm.topicCount;
            });
        }

        function getTopic() {
            return dataservice.getTopic().then(function (data) {
                vm.topic = data;
                return vm.topic;
            });
        }
    }
})();
