(function () {
    'use strict';

    var core = angular.module('app.core');

    core.config(toastrConfig);

    /* @ngInject */
    function toastrConfig(toastr){
        toastr.options.timeOut = 4000;
        toastr.options.positionClass = 'toast-bottom-right';
    }

    var config = {
        appErrorPrefix: '[Error] ', //Configure the exceptionHandler decorator
        appTitle: 'Optaplanner',
        version: '0.0.1'
    };

    core.value('config', config);

    core.config(configure);

    /* @ngInject */
    function configure ($logProvider, $stateProvider, $urlRouterProvider, statehelperConfigProvider, exceptionConfigProvider) {
        // turn debugging off/on (no info or warn)
        if ($logProvider.debugEnabled) {
            $logProvider.debugEnabled(true);
        }


        // Configure the common route provider
        statehelperConfigProvider.config.$stateProvider = $stateProvider;
        statehelperConfigProvider.config.$urlRouterProvider = $urlRouterProvider;
        statehelperConfigProvider.config.docTitle = 'Optaplanner: ';

        // Configure the common exception handler
        exceptionConfigProvider.config.appErrorPrefix = config.appErrorPrefix;
    }

})();