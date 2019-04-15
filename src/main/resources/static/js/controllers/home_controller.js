'use strict'

app.controller('HomeController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope', '$route', 'toaster',
    function($scope,  $http, $location, $interval, $window, $rootScope, $route, toaster){

        var vm = this;
        $rootScope.user = false;
        $scope.$route = $route;
        $rootScope.year = 0;
        $rootScope.semester_index = 0;

        function checkSession() {

            var pass = localStorage.getItem("open-okr-session");
           if(pass === null){

               var page =  $window.location.href;
               if(page != "/home"){
                   page = "/home";
               }
           } else {
               $rootScope.user = true;
           }

        }

        checkSession();

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
            });


        };


        vm.LogmeOut = function(){
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
                    $window.location.href = "/home";
                });
             };





    }]);

