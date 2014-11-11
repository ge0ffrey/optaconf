(function () {
    'use strict';

    angular
        .module('app.layout')
        .controller('Sidebar', Sidebar);

    Sidebar.$inject = ['$state', 'statehelper'];

    function Sidebar($state, statehelper) {
        /*jshint validthis: true */
        var vm = this;
        var states = statehelper.getStates();

        vm.isCurrent = isCurrent;

        activate();

        function activate() {
            getNavRoutes();
        }

        function getNavRoutes() {

            vm.navRoutes = states.filter(function (r) {
                return r.settings && r.settings.nav;
            }).sort(function (r1, r2) {
                return r1.settings.nav - r2.settings.nav;
            });
        }

        function isCurrent(state) {
            if (!state.title || !state.current || !state.current.title) {
                return '';
            }
            var menuName = state.title;
            return $state.current.title.substr(0, menuName.length) === menuName ? 'current' : '';
        }
    }
})();