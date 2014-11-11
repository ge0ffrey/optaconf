(function () {
    'use strict';

    angular
        .module('app.browser')
        .run(appRun);

    appRun.$inject = ['statehelper'];

    /* @ngInject */
    function appRun(statehelper) {
        statehelper.configureStates(getStates());
    }

    function getStates() {
        return [
            {
                name: 'Browse',
                url: '/',
                templateUrl: 'js/browser/browser.html',
                controller: 'Browser',
                controllerAs: 'vm',
                data: {
                    nav: 2,
                    content: 'Browse'
                }
            }
        ];
    }
})();