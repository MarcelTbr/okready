'use strict';

var app = angular.module('App', [ 'ngRoute', 'ui.router', 'toaster', 'ngAnimate']); //'pascalprecht.translate', 'languageModule'

app.config(['$routeProvider', '$locationProvider', '$stateProvider', '$urlRouterProvider', function($routeProvider, $locationProvider) {

    $routeProvider.when('/',{
        templateUrl:'partials/home.html',
        activetab: 'home'
    }).when('/home',{
        templateUrl:'partials/home.html',
        activetab: 'home'
    }).when('/new_semester', {
        templateUrl: 'partials/new_okr.html',
        controller: 'NewOkrController',
        activetab: 'new'
    }).when('/manage_semesters', {
        templateUrl: 'partials/manage_semesters.html',
        controller: 'ManageSemestersController',
        activetab: 'manage'
    }).when('/view_semester/:year/:semester_value', {
        templateUrl: 'partials/view_semester.html',
        controller: 'ViewSemesterController',
        activetab: "semester"
    }).when('/session_expired', {
        templateUrl: 'partials/session_expired.html'
    });


    $routeProvider.otherwise({redirectTo:'/session_expired'});


    $locationProvider.html5Mode({
        enabled: true,
        requireBase: true
    });

}]);