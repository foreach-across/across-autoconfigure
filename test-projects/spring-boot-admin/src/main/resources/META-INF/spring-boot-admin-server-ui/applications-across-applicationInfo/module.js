(function (sbaModules, angular) {
    'use strict';
    var module = angular.module('sba-applications-across-applicationInfo', ['sba-applications']);
    sbaModules.push(module.name);

    var toArray = function (obj) {
        return Object.getOwnPropertyNames(obj).map(function (key) {
            var value = obj[key] instanceof Object ? toArray(obj[key]) : obj[key];
            return {
                name: key,
                value: value
            };
        });
    };

    module.controller('acrossApplicationInfoCtrl', ['$scope', '$http', 'application', function ($scope, $http, application) {
        $scope.application = application;
        //FIXME when actuator endpoint is up: $http.get('api/applications/' + application.id + '/env').then(function (response) {
        $http.get('debug/api/applicationInfo').then(function (response) {
            $scope.applicationInfo = toArray(response.data);

        }).catch(function (response) {
            $scope.error = response.data;
        });
    }]);

    module.config(['$stateProvider', function ($stateProvider) {
        $stateProvider.state('applications.across-applicationInfo', {
            url: '/acrossApplicationInfo',
            templateUrl: 'applications-across-applicationInfo/views/index.html',
            controller: 'acrossApplicationInfoCtrl'
        });
    }]);

    module.run(['ApplicationViews', '$http', '$sce', function (ApplicationViews, $http, $sce) {
        ApplicationViews.register({
            order: 100,
            title: $sce.trustAsHtml('<i class="fa fa-cube fa-fw"></i>Across Application Info'),
            state: 'applications.across-applicationInfo',
            show: function (application) {
                //FIXME when actuator endpoint is up: return $http.head('api/applications/' + application.id + '/configprops').then(
                return $http.head('debug/api/applicationInfo').then(
                    function () {
                        return true;
                    }).catch(function () {
                    return false;
                });
            }
        });
    }]);
} (sbaModules, angular));