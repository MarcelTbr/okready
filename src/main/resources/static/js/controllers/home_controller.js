'use strict'

app.controller('HomeController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope', '$route',
    function($scope,  $http, $location, $interval, $window, $rootScope, $route){

        var vm = this;
        $rootScope.user = false;
        $scope.$route = $route;



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
                });
             };

    }]);

