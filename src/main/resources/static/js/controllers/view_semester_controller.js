'use strict';

app.controller('ViewSemesterController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope', '$routeParams',
    function ($scope, $http, $location, $interval, $window, $rootScope, $routeParams) {


        //temporary mockup code


        //$scope.semester = {
        //            name: "1st",
        //            okr_array: [],
        //            value: 1


        // $scope.okr_array = [
        //     {   title: "something",
        //         total_wins: 50,
        //         results: [
        //             {title: "something else", wins_ratio: 1, wins: 8},
        //             {title: "something else", wins_ratio: 1, wins: 8}
        //         ]
        //     },
        //     {   title: "something",
        //         total_wins: 50,
        //         results: [
        //             {title: "something else", wins_ratio: 1, wins: 8},
        //             {title: "something else", wins_ratio: 1, wins: 8}
        //         ]
        //     }];


     //get semesters, temporary mockup data
    $http.get("api/get_semester/"+ $routeParams.year + "/" + $routeParams.semester_value)
        .then(function(response){

            console.log("api/get_semester...");
            console.log(response.data);

            $scope.semester = response.data.semesterDTO;

        },function(error){
            alert("There was an error");
            console.log(error);
        });

    // manage accordion

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

    // toggle kr edit

    $scope.edit_kr = function(i_out, i_in) {

            console.log("toggle " + i_out + "" + i_in);

        $("#tgl-krt-"+ i_out + "" + i_in).css("top", "-30px");

        $("#kr-wins-"+ i_out + "" + i_in).prop("disabled", false).css({color: "dodgerblue", backgroundColor: "white"});

    };

    $scope.ok_kr = function(i_out, i_in) {

        console.log("toggle " + i_out + "" + i_in);

        $("#tgl-krt-"+ i_out + "" + i_in).css("top", "0px");

        $("#kr-wins-"+ i_out + "" + i_in).prop("disabled", true).css({color: "black", backgroundColor: "whitesmoke"});

    };


    // okr completion

        $scope.calculateCompletion = function(objective) {

            var total_wins = 0;
            var actual_wins = 0;


                total_wins += objective.total_wins;

                var results = objective.results;
                for (var j = 0; j < results.length; j++){

                    var result = results[j];


                    actual_wins += result.winsRatio * result.wins;
                }



            return Math.round(actual_wins * 100 / total_wins) + " %";
        };


        $scope.edit = false;

        $scope.editSemester = function() {

            $("span.toggle-kr-edit").css("display", "block");
            $scope.edit = true;
        };
        $scope.cancelEditSemester = function() {

            $("span.toggle-kr-edit").css("display", "none");
            $scope.edit = false;
        };

        $scope.saveSemesterEdit = function(){

            console.log($scope.semester);

            var semesterJSON = {
                'year': $scope.year, //$routeParams.year
                'name' :  $scope.semester.name,
                'value':  $scope.semester.value,
                'okr_array': $scope.semester.okr_array
            };


            $http({
                url: "api/save_semester_edit",
                method: 'POST',
                data: semesterJSON,
                headers: {"Content-Type": "application/json"
            }
            }).then(function(response){

                    console.log(response);
            }, function (error){
                    console.log(error);
            });

        };




        }]);
