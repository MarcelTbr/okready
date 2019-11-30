'use strict';

app.controller('ViewSemesterController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope', '$routeParams', 'toaster', '$timeout',
    function ($scope, $http, $location, $interval, $window, $rootScope, $routeParams, toaster, $timeout) {


    $http.get("api/get_semester/"+ $routeParams.year + "/" + $routeParams.semester_value)
        .then(function(response){

            console.log("api/get_semester...");
            console.log(response.data);

            $scope.semester = response.data.semesterDTO;

        },function(error){

            console.log(error);

            if(error.status === 409){
                toaster.info("Last viewed semester could not be found. Please visit Manage Semesters to select which semester you want to view.");
            }

            // else if (error.status == 404){
            //     $location.replace("show_error/401");
            // }

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

    $scope.accOpenAll = function (okrs) {

        var acc = document.getElementsByClassName("accordion");

        for (var i = 0; i < okrs.length; i++){

            var activeAcc = acc[i].classList.contains("active-acc");

            // console.log(acc[i].classList);
            //
            // console.log(acc[i].classList.value.indexOf("active-acc"));
            //
            // console.log(activeAcc);

            if(!activeAcc) acc[i].classList.add("active-acc");

            var panel = acc[i].nextElementSibling;
            panel.style.display = "block";

        }

    };

    $scope.accCloseAll = function (okrs) {

        var acc = document.getElementsByClassName("accordion");

        for (var i = 0; i < okrs.length; i++){

            var activeAcc = acc[i].classList.value.indexOf("active-acc") !== -1;

            // console.log(acc[i].classList);
            //
            // console.log(acc[i].classList.value.indexOf("active-acc"));
            //
            // console.log(activeAcc);

            if (activeAcc) acc[i].classList.remove("active-acc");

            console.log(acc[i].classList);

            var panel = acc[i].nextElementSibling;
            panel.style.display = "none";

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

            return Math.round(actual_wins * 100 / total_wins) + "%";
        };


        $scope.edit = false;

        $scope.editSemester = function() {

            $("span.toggle-kr-edit").css("display", "block");
            $(".kr-wins-detail button").css("display", "inline-block");

            $(".kr-wins-input").prop("disabled", false).css({color: "dodgerblue", backgroundColor: "white"});

            $scope.edit = true;
        };

        $scope.cancelEditSemester = function() {

            $("span.toggle-kr-edit").css("display", "none");
            $(".kr-wins-detail button").css("display", "none");

            $(".kr-wins-input").prop("disabled", true).css({color: "black", backgroundColor: "whitesmoke"});

            $scope.edit = false;

            $http.get("api/get_semester/"+ $routeParams.year + "/" + $routeParams.semester_value)
                .then(function(response){

                    console.log("api/get_semester...");
                    console.log(response.data);

                    $scope.semester = response.data.semesterDTO;

                    $timeout(function(){
                        $scope.accCloseAll($scope.semester.okr_array);
                    }, 50);

                },function(error){
                    alert("There was an error");
                    console.log(error);

                    $timeout(function(){
                        $scope.accCloseAll($scope.semester.okr_array);
                    }, 50);
                });

        };

        $scope.minus1 = function($i_out, $i_in) {

            $scope.semester.okr_array[$i_out].results[$i_in].wins -= 1;

        };

        $scope.plus1 = function($i_out, $i_in) {

            $scope.semester.okr_array[$i_out].results[$i_in].wins += 1;

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

               var cancelPromise = Promise.resolve( $scope.cancelEditSemester() );

                cancelPromise.then( $timeout(function(){
                        $scope.accOpenAll($scope.semester.okr_array);
                    }, 100)
                );

                toaster.success("Semester progress successfully saved!");



            }, function (error){

                console.log(error);

                var cancelPromise = Promise.resolve( $scope.cancelEditSemester() );

                cancelPromise.then( $timeout(function(){
                        $scope.accOpenAll($scope.semester.okr_array);
                    }, 100)
                );

                    toaster.warning("Sorry, your progress could not be saved.");
            });

        };




}]);
