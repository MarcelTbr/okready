// language=AngularJS
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
        templateUrl: 'partials/new_semester.html',
        controller: 'NewSemesterController',
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
        templateUrl: 'partials/session_expired.html',
        controller: 'HomeController'
    }).when('/show_error/:error', {
        templateUrl: 'partials/error.html',
        controller: 'ErrorController'
    }).when('/motivator', {
        templateUrl: 'partials/motivator.html',
        controller: 'MotivatorController',
        activetab: "motivator"
    })



    $routeProvider.otherwise({redirectTo:'/show_error/404'});


    $locationProvider.html5Mode({
        enabled: true,
        requireBase: true
    });

}]);

app.directive('bindConfirmDeletion', [function () {
    return {
        template:
        "<button type='button'  id='doDeleteSem' class='btn btn-success clear'>Yes</button>" +
        "<button type='button'  id='notDeleteSem' class='btn btn-danger clear'>No</button>"
    };
}]);