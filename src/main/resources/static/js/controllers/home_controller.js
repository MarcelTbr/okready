'use strict'

app.controller('HomeController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope', '$route',
    function($scope,  $http, $location, $interval, $window, $rootScope, $route){

        var vm = this;
        $rootScope.user = false;
        $scope.$route = $route;

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


        function lastViewedSemester() {

            var year = localStorage.getItem("year");
            var semester_index = localStorage.getItem("semester-index");

            if( year !== null && semester_index !== null){

                $rootScope.year = localStorage.getItem("year");
                $rootScope.semester_index = localStorage.getItem("semester-index");

            }

        }

        lastViewedSemester();

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


                //mockdata TODO: change with server fetched data (last saved semester)
                $rootScope.year = 2018;
                $rootScope.semester_index = 1;
                localStorage.setItem("year", 2018);
                localStorage.setItem("semester-index", 1);


                //redirect
                $window.location.href = "/view_semester/"+$rootScope.year + "/" + $rootScope.semester_index;

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
                    localStorage.removeItem("year");
                    localStorage.removeItem("semester-index");
                    $window.location.href = "/home";
                });
             };

    }]);

