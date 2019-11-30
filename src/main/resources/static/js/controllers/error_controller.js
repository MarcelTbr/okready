'use strict'

app.controller('ErrorController', ['$scope', '$http', '$location', '$interval', '$timeout', '$window', '$rootScope', '$route', '$routeParams', 'toaster',
    function($scope,  $http, $location, $interval, $timeout, $window, $rootScope, $route, $routeParams, toaster){

        function handleErrors(){

            var page =  $window.location.pathname;

            console.log("page: " + page);

            var p = page.substring(0,11);
            var e = parseInt($routeParams.error);
            console.log(p);
            console.log(e);
            console.log($routeParams.error);

            var message = "Something Happened...";
            var title = "Error";
            switch(e){
                case 404: title="Page Not Found"; message="Sorry, we could not find that page... Will redirect you soon.";$timeout(function(){
                    $window.location.href = "/home";        }, 5000);  break;
                case 401: title="Unauthorized"; message = "Sorry, you are not authorized to see this page. Please log in.";break;
                case 500: title="Uups!"; message = "Sorry, something happened... We'll redirect you soon.";$timeout(function(){
                    $window.location.href = "/home";        }, 5000)    ;break;
            }

            $scope.error_message = message;
            $scope.error= title;
        }


        handleErrors();
    }]);

