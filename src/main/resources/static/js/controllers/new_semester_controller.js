'use strict'

app.controller('NewSemesterController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope',
    function ($scope, $http, $location, $interval, $window, $rootScope) {


        var year = (new Date()).getFullYear();
        $scope.form1 = { year: year};

        $scope.years = [year, year + 1, year + 2, year + 3, year + 4];
        $scope.form1.year = year;

        $scope.semesters = [
            {'name': '1st', 'value': 1},
            {'name': '2nd', 'value': 2},
            {'name': '3rd', 'value': 3},
            {'name': '4th', 'value': 4}
        ];
        $scope.form1.semester = $scope.semesters[0];

       $scope.step1 = false;
       $scope.step2 = false;
        $scope.message = "";

       $scope.openSemester = function () {

           console.log($scope.form1.year);
            $scope.message = "You selected year: " + $scope.form1.year + " and semester: " + $scope.form1.semester.name + "\n\nHooray! You can now add up to 5 Objectives!";

            $scope.step1 = true;
           $("#new_sect_1:nth-child(2) p").css('visibility', 'visible');
           $("#new_sect_1").css('backgroundColor', 'grey');
        };

       $scope.undoOpenSemester = function () {
          $scope.step1 = false;
          $scope.step2 = false;
          $scope.step3_1 = false;
          $scope.step3_2 = false;
          $scope.message = "";
          $scope.name = "";
           $("#new_sect_1:nth-child(2) p").css('visibility', 'hidden');
           $("#new_sect_1").css('backgroundColor', 'white');
           $("#new_sect_2").css('backgroundColor', 'white');

       };

        var slider = document.getElementById("winsRange");
        var output = document.getElementById("selectedRange");
        output.innerHTML = slider.value; // Display the default slider value
        $scope.wins = 50; //vm.wins

        // Update the current slider value (each time you drag the slider handle)
        slider.oninput = function() {
            output.innerHTML = this.value;
            $scope.wins =  this.value;
        };


        $scope.newProvOKR = function(){

            $("#new_sect_2:nth-child(2)").css('visibility', 'visible');
            $("#new_sect_2").css('backgroundColor', 'grey');
            $scope.step2 = true;
            $scope.result_wins = 1;
        };

       $scope.array = [];
       $scope.step3_1 = false;
       $scope.step3_2 = false;

        $scope.newProvResult = function(){


            $scope.array.push({'title': $scope.key_result, 'wins_ratio': $scope.result_wins});
            console.log($scope.array);

            $scope.key_result = ""; $scope.result_wins = 1;

            if($scope.array.length > 0 ){
                $scope.step3_1 = true;
            }

            if($scope.array.length >= 5){
                $scope.step3_2 = true;
            }
        };


        $scope.saveOKR = function(){

            var okr = {};
            okr.title = $scope.name;
            okr.total_wins = $scope.wins;
            okr.results = $scope.array;

            $scope.okr_array.push(okr);

            $scope.array = [];

            $scope.undoOpenSemester();
        }


        $scope.okr_array = [];



        /*==== accordion logic ========== */


        $scope.accToggle = function(i){

            var acc = document.getElementsByClassName("accordion");
            console.log(this);
            acc[i].classList.toggle("active-acc");
            var panel = acc[i].nextElementSibling;
            if (panel.style.display === "block") {
                panel.style.display = "none";
            } else {
                panel.style.display = "block";
            }

        };

    }]);