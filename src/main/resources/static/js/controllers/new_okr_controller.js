'use strict'

app.controller('NewOkrController', ['$scope', '$http', '$location', '$interval', '$window', '$rootScope',
    function ($scope, $http, $location, $interval, $window, $rootScope) {

        function addClass(el, className) {
            if (el.classList)
                el.classList.add(className)
            else if (!hasClass(el, className)) el.className += " " + className
        }

        $scope.curr_okr = 0;
        $scope.form1 = {};
        $scope.form2 = {};
        $scope.form3 = {};
        $scope.form1.year = 2018;

        $scope.semesters = [
            {'name': '1st', 'value': 1},
            {'name': '2nd', 'value': 2},
            {'name': '3rd', 'value': 3},
            {'name': '4th', 'value': 4}
        ];
        $scope.form1.semester = $scope.semesters[0];
        $scope.okr_array = [];
        // $scope.okr_array = [
        //     {   title: "something",
        //         total_wins: 50,
        //         results: [
        //             {title: "something else", wins_ratio: 1},
        //             {title: "something else", wins_ratio: 1}
        //         ]
        //     },
        //     {   title: "something",
        //         total_wins: 50,
        //         results: [
        //             {title: "something else", wins_ratio: 1},
        //             {title: "something else", wins_ratio: 1}
        //         ]
        //     }];


        $scope.step1 = false;
        $scope.save = false;
        $scope.openSemester = function(){

            $("#new_f1").css("backgroundColor", "lightgrey");
            $scope.step1 = true;

        };

        $scope.back = function(step){

            switch(step){

                case 0: $scope.step1 = false; $scope.step2 = false; $scope.step3 = false;
                    $("#new_f1, #new_f2").css("backgroundColor", "white");
                    $("#next2").attr("disabled",false);
                    $scope.okr_array = []; break;
                case 1: $scope.step2 = false; $scope.step3 = false;
                    $("#new_f2").css("backgroundColor", "white");
                    $scope.not('objective_title', 11);
                    $scope.not('objective_wins', 12);
                    $("#fs_1").attr("disabled", false);
                    if($scope.curr_okr > 0){
                        $scope.curr_okr--;
                    }
                    if($scope.okr_array.length >0){
                        $scope.okr_array.pop();
                    }
                    break;
                case 2:

                        $scope.okr_array[$scope.curr_okr].results.pop();
                        if($scope.okr_array[$scope.curr_okr].results.length == 0){
                            $scope.accToggle($scope.curr_okr);
                            $scope.step3 = false;
                        }
                    break;
            }
        };

        $scope.next = function(step){

            var cond1 = $("#objective_title").attr("disabled") != undefined;
            var cond2 = $("#objective_wins").attr("disabled") != undefined;

            switch(step){
                case 2:
                    console.log($("#objective_title").attr("disabled"));
                    if(cond1 && cond2){

                        $scope.step2 = true;
                        $("#new_f2").css("backgroundColor", "lightgrey");
                        $("#fs_1").attr("disabled", true);
                        step2();

                    } else{
                        alert("please make sure you all fields are full and checked");
                    }

                   break;
                case 3:
                    var kr = {};
                    kr.title = $scope.form3.kr_title;
                    kr.wins_ratio = $scope.form3.kr_wins;
                    var kr_array = $scope.okr_array[$scope.curr_okr].results;
                    if(kr_array.length <5) {
                        $scope.okr_array[$scope.curr_okr].results.push(kr);
                        $scope.accOpen($scope.curr_okr);
                        $scope.form3.kr_title = "";
                        $scope.form3.kr_wins = "";
                        $scope.step3 = true;
                    } else{
                        alert("Sorry it's better to have only a handful of key results."
                        + "\n You can now save your objective");
                    }
                    break;
                case 4:
                    if($scope.okr_array.length < 5){

                        $scope.step2 = false; $scope.step3 = false;
                        $("#new_f2").css("backgroundColor", "white");
                        $scope.not('objective_title', 11);
                        $scope.not('objective_wins', 12);
                        $("#fs_1").attr("disabled", false);
                        $scope.accToggle($scope.curr_okr);
                        $scope.curr_okr++;
                        $scope.form2.obj_title = "";
                        $scope.form2.obj_wins = "";

                    }else{
                        alert("Sorry you can only have a handful of OKRs");
                    }
            }

        }

        $scope.ok = function(id, num){


            $("#"+id).attr("disabled", true);
            $("#ok" + num).css("display", "none");
            $("#not" + num).css("visibility", "visible");
        };

        $scope.not = function(id, num) {
            $("#"+id).attr("disabled", false);
            $("#ok" + num).css("display", "block");
            $("#not" + num).css("visibility", "hidden");

        };





        /*==== accordion logic ========== */

        $scope.accOpen = function(i){
            var acc = document.getElementsByClassName("accordion");
            addClass(acc[i], "active-acc");
            var panel = acc[i].nextElementSibling;

            panel.style.display = "block";

        };

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

        /* ====== okr list logic ===== */

        function step2(){

            var okr_obj = {};
            okr_obj.title = $scope.form2.obj_title;
            okr_obj.total_wins = $scope.form2.obj_wins;
            okr_obj.results = [];
            $scope.okr_array.push(okr_obj);
            if($scope.okr_array.length > 0){
                $scope.save = true;
            }
        }


        $scope.saveOKR = function(){
            alert($scope.form1.year + " " + $scope.form1.semester.value);
        };
    }]);