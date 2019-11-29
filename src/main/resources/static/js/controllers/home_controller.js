'use strict'

app.controller('HomeController', ['$scope', '$http', '$location', '$interval', '$timeout', '$window', '$rootScope', '$route', '$routeParams', 'toaster',
    function($scope,  $http, $location, $interval, $timeout, $window, $rootScope, $route, $routeParams, toaster){

        var vm = this;
        $rootScope.user = false;
        $scope.$route = $route;
        $rootScope.year = 0;
        $rootScope.semester_index = 0;

        function checkSession() {

            var openSes = localStorage.getItem("open-okr-session");
           if(openSes === null || openSes !== "yes"){

               var page =  $window.location.href;
               if(page !== "/home"){
                   page = "/home";
               }
           } else {
               $rootScope.user = true;
           }

        }

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


        function sessionExpired(){

            var page =  $window.location.pathname;

            console.log("page: " + page);

            if(page == "/session_expired"){

                console.log("session_expired");

                logout();

                $timeout(function(){
                $window.location.href = "/home";
                }, 5000)    ;
            }
        }


        sessionExpired();
        handleErrors();
        checkSession();
        //TODO make a session check that goes to back end not the browser's local storage


        //load last viewed semester
        lastViewedSemester();

        function lastViewedSemester() {

            var year = localStorage.getItem("year");
            var semester_index = localStorage.getItem("semester-index");

            if( year !== null && semester_index !== null){

                $rootScope.year = localStorage.getItem("year");
                $rootScope.semester_index = localStorage.getItem("semester-index");

            } else {

                $rootScope.year = 2019;
                $rootScope.semester_index = 1;
                localStorage.setItem("year", 2019);
                localStorage.setItem("semester-index", 1);

            }


        }



        vm.LogmeIn = function() {

            /**
             *  Since angular serializes data to JSON by default we need to use $.param() function for serialization.
             */
            //$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
            $http({
                url: "/app/login",
                method: "POST",
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                data: $.param({'username': vm.password, 'password': vm.password})
            }).then(function(response){

                console.log(response);
                if(response.status == 200) {
                    $rootScope.user = true;
                }
                vm.password = "";
                localStorage.setItem("open-okr-session", "yes");

                //redirect
                $window.location.href = "/view_semester/"+$rootScope.year + "/" + $rootScope.semester_index;



            }, function(error){
                console.log(error);

                toaster.error("Sorry, login failed. Please try again.");

                console.log("Error Status:");
                console.log(error.status);
            });


        };


        function logout(){
            $http({
                url: "/app/logout",
                method: "POST",
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            }).then(function(response) {
                console.log("logged-out!");
                $rootScope.user = false;
                vm.password = "";

                localStorage.removeItem("open-okr-session");
                //localStorage.removeItem("year");
                //localStorage.removeItem("semester-index");
            });
        };



        vm.LogmeOut = function() {

            logout();
            $window.location.href = "/home";

        };
    }]);

