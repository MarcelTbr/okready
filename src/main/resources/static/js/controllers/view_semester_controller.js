'use strict';

app.controller('ViewSemesterController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope', '$routeParams',
    function ($scope, $http, $location, $interval, $window, $rootScope, $routeParams) {


    $http.get("api/get_semester/"+ $routeParams.year + "/" + $routeParams.semester)
        .then(function(response){

            console.log("api/get_semester...");
            console.log(response);

        },function(error){
            alert("There was an error");
            console.log(error);
        });


    }]);
