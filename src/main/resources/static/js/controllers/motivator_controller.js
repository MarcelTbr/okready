'use strict'

app.controller('MotivatorController', ['$scope', '$http', '$location', '$interval', '$timeout', '$window', '$rootScope', '$route', '$routeParams', 'toaster',
    function($scope,  $http, $location, $interval, $timeout, $window, $rootScope, $route, $routeParams, toaster) {

        $scope.motivators_list =
            [
                {
                    category: "Comfort Zone",
                    items:
                        [
                            "To break out of your confort zone use your imagination to feel the painful consequences of not doing something you need to do" ,
                            "Sometimes our mind will serve us, but sometimes it will work against us"
                        ]
                },
                {
                    category: "Breaking It Down",
                    items:
                        [
                            "Plan your steps in a very specific way, so that you can move faster into achieving your goals" ,
                            "Better to take small steps in the right direction than to make a great leap forward only to stumble backwards"
                        ]
                },
                {
                    category: "Complaining Excuses",
                    items:
                        [
                            "Invest your complaining into better things, like fixing the problem" ,
                            "Complaining does not achieve anything other than stop your progress",
                            "Stop complaining and start doing"
                        ]
                }
            ];




        function motivatorFormControl() {

            $scope.motivatorForm = {};

            $scope.addCategory = function (){

                var new_category_obj = {
                    "category" : $scope.motivatorForm.categoryName,
                    "items": []
                };



                $scope.motivators_list.push(new_category_obj);

                console.info("motivators_list", $scope.motivators_list);

                $scope.motivatorForm.categoryName = "";

                toaster.info("Remember to save your content once you are done");
            };

            $scope.saveAll = function (){

                saveCategory();
            }

            function saveCategory(){

                var categoryJSON = {
                    'name': $scope.motivatorForm.categoryName
                };


                $http({
                    url: "api/save_category",
                    method: 'POST',
                    data: categoryJSON,
                    headers: {
                        "Content-Type": "application/json"
                    }
                }).then(function (response) {

                    console.info("api/save_category", response.data);

                    toaster.success("Category successfully saved!");
                }, function (error) {

                    console.info("api/save_category", error.data);
                    toaster.error("Sorry could not save category.");

                    $route.reload();
                });
            }


        }


        function addContentToCategoryControl() {

            var next = document.getElementById("next");
            next.addEventListener("click", selectNext);

            function selectNext() {
                var categories_list = document.querySelector("select#categories");
                var selected_category_value = categories_list.options[categories_list.selectedIndex].value;
                //console.info("selected_category_value", selected_category_value);

                if (selected_category_value < (categories_list.length - 1)) {
                    categories_list.value = (++selected_category_value);
                }
                //console.log(categories_list.value)

            }

            var previous = document.getElementById("previous");
            previous.addEventListener("click", selectPrevious);

            function selectPrevious() {
                var categories_list = document.querySelector("select#categories");
                console.log(categories_list)
                var selected_category_value = categories_list.options[categories_list.selectedIndex].value;

                //console.info("selected_category_value", selected_category_value);

                if (selected_category_value > 1) {
                    categories_list.value = (--selected_category_value);
                }
                //console.log(categories_list.value)

            }


        }

        motivatorFormControl();
        addContentToCategoryControl();



        /* ======== accordion ====== */

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



    }]);