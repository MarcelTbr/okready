'use strict'

app.controller('ManageSemestersController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope', 'toaster', '$route',
    function($scope,  $http, $location, $interval, $window, $rootScope, toaster, $route){


        $http.get("api/get_semesters")
            .then(function(response){
                console.log(response);

                $scope.yearsDTO = response.data.yearsDTO;
            }, function(error){

                console.log(error);

                // if (error.status == 404){
                //     $location.replace("show_error/401");
                // }

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


                    actual_wins += result.winsRatio * result.wins;
                }

            }

            return Math.round(actual_wins * 100 / total_wins) + " %";
        };


        $scope.lastSemesterViewed = function(year, semester_index){

            $rootScope.year = year;
            $rootScope.semester_index = semester_index;
            localStorage.setItem("year", year);
            localStorage.setItem("semester-index", semester_index);
        };



        $scope.deleteSemester = function (id, year, semesterName) {


            var warningProm = Promise.resolve ( toaster.warning(
                {
                    title: 'Delete ' + semesterName + ' semester of ' + year + '?',
                    body: 'bind-confirm-deletion',
                    closeButton: false,
                    bodyOutputType: 'directive'

                })
            );

                warningProm.then(function(){
                document.getElementById('doDeleteSem').addEventListener('click', function(){

                    $http.get("api/delete_semester/"+id )
                        .then(function(response){

                            console.log(response);

                            toaster.success("Semester deleted.");

                            $route.reload();

                        }, function(error){

                            console.log(error);

                        });
                });

            });

        };

        $scope.editSemesters = false;

        $scope.allowEditSemesters = function () {

            $scope.editSemesters = true;
        };

        $scope.cancelEditSemesters = function () {

            $scope.editSemesters = false;
        };


    }]);