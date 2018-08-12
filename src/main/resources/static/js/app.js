'use strict';

var app = angular.module('App', [ 'ngRoute', 'ui.router']); //'pascalprecht.translate', 'languageModule'

app.config(['$routeProvider', '$locationProvider', '$stateProvider', '$urlRouterProvider', function($routeProvider, $locationProvider){

    $routeProvider.when('/home',{
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
    });



    $routeProvider.otherwise({redirectTo:'/home'});


    $locationProvider.html5Mode({
        enabled: true,
        requireBase: true
    });

}]);