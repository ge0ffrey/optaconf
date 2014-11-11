(function () {
    'use strict';

    angular
        .module('blocks.router')
        .provider('statehelperConfig', statehelperConfig)
        .factory('statehelper', statehelper);

    statehelper.$inject = ['$location', '$rootScope', '$state', 'logger', 'statehelperConfig'];

    // Must configure via the statehelperConfigProvider
    function statehelperConfig () {
        /* jshint validthis:true */
        this.config = {
            // These are the properties we need to set
            // $stateProvider: undefined
            // docTitle: ''
            // resolveAlways: {ready: function(){ } }
        };

        this.$get = function () {
            return {
                config: this.config
            };
        };
    }

    function statehelper($location, $rootScope, $state, logger, statehelperConfig) {

        var handlingStateChangeError = false;
        var stateCounts = {
            errors: 0,
            changes: 0
        };
        var states = [];
        var $stateProvider = statehelperConfig.config.$stateProvider;
        var $urlRouterProvider = statehelperConfig.config.$urlRouterProvider;

        var service = {
            configureStates: configureStates,
            getStates: getStates,
            stateCounts: stateCounts
        };

        init();

        return service;




        function configureStates(states){
            states.forEach(function (route) {
//                state.resolve = angular.extend(state.resolve || {}, statehelperConfig.resolveAlways);
                $stateProvider.state(route.name,route);
            });
            $urlRouterProvider.otherwise('/');

        }

        function handleStateErrors() {
            // Route cancellation:
            // On routing error, go to the dashboard.
            // Provide an exit clause if it tries to do it twice.
            $rootScope.$on('$stateChangeError',
                function (event, current, previous, rejection) {
                    if (handlingRouteChangeError) {
                        return;
                    }
                    stateCounts.errors++;
                    handlingStateChangeError = true;
                    var destination = (current && (current.title || current.name || current.loadedTemplateUrl)) ||
                        'unknown target';
                    var msg = 'Error routing to ' + destination + '. ' + (rejection.msg || '');
                    logger.warning(msg, [current]);
                    $location.path('/');
                }
            );

        }

        function init() {
            handleStateErrors();
            updateDocTitle();
        }

        function getStates() {
            console.log($state.toString());
            for (var prop in $state.stateParams) {
                if ($state.stateParams.hasOwnProperty(prop)) {
                    var state = $state.stateParams[prop];
                    var isState = !!state.title;
                    if (isState) {
                        states.push(state);
                    }
                }
            }
            return states;
        }

        function updateDocTitle() {
            $rootScope.$on('$stateChangeSuccess',
                function (event, current, previous) {
                    stateCounts.changes++;
                    handlingStateChangeError = false;
                    var title = statehelperConfig.config.docTitle + ' ' + (current.title || '');
                    $rootScope.title = title; // data bind to <title>
                    var destination = (current && (current.title || current.name || current.loadedTemplateUrl)) ||
                        'unknown target';
                    var msg = 'Routing to ' + destination;
                    logger.warning(msg, [current]);

                }
            );
        }
    }
})();