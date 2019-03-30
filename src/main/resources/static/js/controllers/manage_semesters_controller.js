'use strict'

app.controller('ManageSemestersController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope',
    function($scope,  $http, $location, $interval, $window, $rootScope){


        $http.get("api/get_semesters")
            .then(function(response){
                console.log(response);

                $scope.yearsDTO = response.data.yearsDTO;
            });


        $scope.accToggle = function(i){

            var acc = document.getElementsByClassName("accordion");
            acc[i].classList.toggle("active-acc");
            var panel = acc[i].nextElementSibling;
            if (panel.style.display === "block") {
                panel.style.display = "none";
            } else {
                panel.style.display = "block";
            }

        };



        $scope.calculateCompletion = function(semester) {
            var okr_array = semester.okr_array;
            var total_wins = 0;
            var actual_wins = 0;
            for(var i = 0; i < okr_array.length; i++){
                var objective = okr_array[i];

                total_wins += objective.total_wins;

                var results = objective.results;
                for (var j = 0; j < results.length; j++){

                    var result = results[j];


                    actual_wins += result.wins_ratio * result.wins;
                }

            }

            return (actual_wins * 100 / total_wins) + " %";
        };


    }]);