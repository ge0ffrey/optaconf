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
            title: 'Optaplanner',
            description: 'Optaplanner webapp'
        };
        vm.title = 'Browse';

        activate();

        function activate() {

        }
    }
})();
