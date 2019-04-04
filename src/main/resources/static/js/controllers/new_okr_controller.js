'use strict'

app.controller('NewOkrController', ['$scope', '$http', '$location', '$interval', '$timeout', '$window', '$rootScope',
    function ($scope, $http, $location, $interval, $timeout, $window, $rootScope) {

        function addClass(el, className) {
            if (el.classList)
                el.classList.add(className)
            else if (!hasClass(el, className)) el.className += " " + className;
        }

        $scope.curr_okr = 0;
        $scope.form1 = {};
        $scope.form2 = {};
        $scope.form3 = {};
        var today = new Date();
        $scope.form1.year = today.getFullYear();

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
        //             {title: "something else", wins_ratio: 1, wins: 9},
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
            $("#caroussel").animate({
                scrollTop: "200px"
            }, 2000);
            $timeout(function() {

                if($scope.step1) {
                    $('#objective_title').focus();
                }

            }, 2500);
        };

        $scope.back = function(step){

            switch(step){

                case 0: $scope.step1 = false; $scope.step2 = false; $scope.step3 = false;
                    $("#new_f1, #new_f2").css("backgroundColor", "white");
                    $("#next2").attr("disabled",false);

                    //reset fields
                    $scope.form2.obj_title = "";
                    $scope.form2.obj_wins = "";
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
                    $scope.stopDigest = true;

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

            var cond1 = $("#objective_title").attr("disabled") !== undefined;
            var cond2 = $("#objective_wins").attr("disabled") !== undefined;



            switch(step){
                case 2:
                        //console.log($("#objective_title").attr("disabled"));
                    if(cond1 && cond2){

                        $scope.step2 = true;
                        $("#new_f2").css("backgroundColor", "lightgrey");
                        $("#fs_1").attr("disabled", true);

                        $("#caroussel").animate({
                            scrollTop: "400px"
                        }, 2000);

                        //focus on the new form field

                        $scope.$apply();

                        $timeout(function(){$('input#kr_title').focus();}, 0);

                        step2();




                    } else{
                        alert("please make sure you all fields are full and checked");
                    }

                   break;
                case 3:

                    var ok1Step3 = $("#ok1");
                    var ok2Step3 =$("#ok2");

                    var cond3 = ok1Step3.css("display") == 'none' && ok2Step3.css("display") == 'none';

                    if(cond3) {

                        var kr = {};
                        var kr_array = [];

                        console.log($scope.form3.kr_title);
                        console.log($scope.form3.kr_wins);

                        //to avoid user entering through keyboard causing an extra empty kr_array element
                        if ($scope.form3.kr_title !== undefined && $scope.form3.kr_title !== "") {
                            kr.title = $scope.form3.kr_title;
                            kr.wins_ratio = $scope.form3.kr_wins;
                            kr_array = $scope.okr_array[$scope.curr_okr].results;

                            //be sure to enter 5 key results or less
                            if (kr_array.length < 5) {
                                $scope.okr_array[$scope.curr_okr].results.push(kr);
                                $scope.accOpen($scope.curr_okr);
                                $scope.form3.kr_title = "";
                                $scope.form3.kr_wins = "";
                                $scope.step3 = true;

                                // reset key results title and wins input fields
                                $scope.not("kr_title", 1);
                                $scope.not("kr_wins", 2);

                                //focus on okr title input field
                                $timeout($('#kr_title').focus(), 1000);

                            } else {
                                alert("Sorry it's better to have only a handful of key results."
                                    + "\n You can now save your objective");
                            }
                        }
                    } else {
                        alert("Please fill in your key result fields and check the blue boxes to add an objective.");
                    }

                    break;
                case 4:

                    if($scope.okr_array[$scope.curr_okr ].results.length <= 1 && $scope.okr_array[$scope.curr_okr].results[0].title == undefined){
                        alert ("Sorry you have to defina at least one objective key result");

                        $('#kr_title').focus();

                    }else if($scope.okr_array.length < 5 ) {

                        $scope.step2 = false;
                        $scope.step3 = false;
                        $("#new_f2").css("backgroundColor", "white");
                        $scope.not('objective_title', 11);
                        $scope.not('objective_wins', 12);
                        $("#fs_1").attr("disabled", false);
                        $scope.accToggle($scope.curr_okr);
                        $scope.curr_okr++;
                        $scope.form2.obj_title = "";
                        $scope.form2.obj_wins = "";

                        //focus on objective title
                        $("#objective_title").focus();

                    }else {
                        alert("Sorry you can only have a handful of OKRs");
                    }
                    break;
            }

        }

        $scope.ok = function(id, num){

            var $elem = $('#'+id);

            if($elem.val() !== "") {
                $("#" + id).attr("disabled", true);
                $("#ok" + num).css("display", "none");
                $("#not" + num).css("visibility", "visible");
            } else {
                alert("Please define a value");
            }
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

                console.log(okr_obj);
                okr_obj.results = [];


                $scope.okr_array.push(okr_obj);


                if($scope.okr_array.length > 0){

                    $scope.save = true;
                }

                console.log($scope.okr_array);


        }


        /* ======= save objectives api call ======= */

        $scope.saveOKR = function(){
            alert($scope.form1.year + " " + $scope.form1.semester.value);


            //delete undefined key results if any

            function deleteEmptyKRs(okr_array){


                for(var i = 0; i < okr_array.length; i++){

                    var results = okr_array[i].results;
                    for(var j = 0; j < results.length; j++){

                        var result = results[j];

                        if(result.title === undefined || result.title === ""){
                            results.splice(j,1);
                        }

                    }

                }

                return okr_array;

            }

            $scope.okr_array = deleteEmptyKRs($scope.okr_array);


            var semesterJSON = {
                'year': $scope.form1.year,
                'name' :  $scope.form1.semester.name,
                'value':  $scope.form1.semester.value,
                'okr_array': $scope.okr_array
            };

            console.info('save okr_array', $scope.okr_array);


            $http({
                url: "api/save_semester",
                method: 'POST',
                data: semesterJSON,
                headers: { "Content-Type": "application/json" }
            }).then(function(response){

                console.log(response);


                $rootScope.year = $scope.form1.year;
                $rootScope.semester_index = $scope.form1.semester.value;
                localStorage.setItem("year", $rootScope.year);
                localStorage.setItem("semester-index", $rootScope.semester_index);

                $location.path( "/view_semester/" + $rootScope.year + "/" + $rootScope.semester_index);

            }, function(error){

                console.log(error);
            });
        };


        /* ===== handle key events ===== */


        var $objective_title = $('input#objective_title');
        var $objective_wins = $('input#objective_wins');

        var $kr_title = $('input#kr_title');
        var $kr_wins = $('input#kr_wins');


        $objective_title.on('keydown', function(e) {
            if (e.which === 13) {
                e.preventDefault();
                if($objective_title.val() != "") {
                    $scope.ok('objective_title', 11);
                    $objective_wins.focus();
                } else {
                    alert ("Please define your objective title");
                }
            }
        });

        $objective_wins.on('keydown', function(e) {

            if(e.which === 13) {
                e.preventDefault();
                if ($objective_wins.val() != "") {
                    $scope.ok('objective_wins', 12);
                    $('#next2').focus();
                } else {
                    alert("Please define the number of wins for your objective");
                }

            }
        });

        $('#next2').on('keydown', function(e){
            e.preventDefault();
            if(e.which === 13){
                $scope.next(2);
                console.log("step2");
                console.log($scope.step2);
            }
        });

        $kr_title.on('keydown', function(e) {

            if(e.which === 13) {
                e.preventDefault();
                if ($kr_title.val() !== "") {
                    $scope.ok('kr_title', 1);
                    $kr_wins.focus();
                } else {
                    alert("Please define the title for your Key Result");
                }

            }
        });


        $kr_wins.on('keydown', function(e) {

            if(e.which === 13) {
                e.preventDefault();
                if ($kr_wins.val() != "") {
                    $scope.ok('kr_wins', 2);
                    $('#add_kr_btn').focus();
                } else {
                    alert("Please define the number of wins for your Key Result");
                }

            }
        });




    }]);