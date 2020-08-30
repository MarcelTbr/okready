'use strict'

app.controller('MotivatorController', ['$scope', '$http', '$location', '$interval', '$timeout', '$window', '$rootScope', '$route', '$routeParams', 'toaster',
    function($scope,  $http, $location, $interval, $timeout, $window, $rootScope, $route, $routeParams, toaster) {

        $scope.motivators_list =
            [
                {
                    id: 4,
                    category: "Comfort Zone",
                    items:
                        [
                            { id: 8, content: "To break out of your confort zone use your imagination to feel the " +
                                "painful consequences of not doing something you need to do" },
                            { id: 9, content: "Sometimes our mind will serve us, but sometimes it will work against us" }
                        ]
                },
                {
                    id: 5,
                    category: "Breaking It Down",
                    items:
                        [
                            {id: 10, content: "Plan your steps in a very specific way, so that you can move faster into achieving your goals" },
                            {id: 11, content: "Better to take small steps in the right direction than to make a great leap forward only to stumble backwards" }
                        ]
                },
                {
                    id: 6,
                    category: "Complaining/Excuses",
                    items:
                        [
                            {id: 12, content: "Invest your complaining into better things, like fixing the problem" },
                            {id: 13, content: "Complaining does not achieve anything other than stop your progress"},
                            {id: 14, content: "Stop complaining and start doing" }
                        ]
                }
            ];

        function makeMotivatorsJSON() {

            var data = $scope.motivators_list.map( function(motivator) { return JSON.stringify(motivator); });


               var motivatorsJSON = { "data": data}   ;


               console.log(motivatorsJSON);


            return motivatorsJSON;

        }


        function motivatorFormControl() {

            $scope.motivatorForm = {};

            $scope.addCategory = function (){

                var new_category_obj = {
                    "id": 0,
                    "category" : $scope.motivatorForm.categoryName,
                    "items": []
                };



                $scope.motivators_list.push(new_category_obj);

                console.info("motivators_list", $scope.motivators_list);

                $scope.motivatorForm.categoryName = "";

                toaster.warning("Remember to save your content once you are done");
            };

            $scope.saveAll = function (){

                //saveCategory();
                saveMotivatorsList();
            };

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

            function saveMotivatorsList(){

               var motivatorsJSON = makeMotivatorsJSON();

                $http({
                    url: "api/save_motivators_list",
                    method: 'POST',
                    data: motivatorsJSON,
                    headers: {
                        "Content-Type": "application/json"
                    }
                }).then(function (response) {

                    console.info("api/save_motivators_list", response.data);

                    toaster.success("List successfully saved!");
                }, function (error) {

                    console.info("api/save_motivators_list", error.data);
                    toaster.error("Sorry could not save list.");

                    $route.reload();
                });
            }

        }


        function addContentToCategoryControl() {

            $scope.selectedCategoryIndex = null;
            $scope.selectedCategoryName = null;


            /**
             * @addContentToCategory = button control
             *
             */

            $scope.addContentToCategory = function () {

                addItemToCategory();
            };


            function addItemToCategory() {

                if($scope.selectedCategoryName !== null && $scope.selectedCategoryIndex !== null){

                    var category_items_list = $scope.motivators_list[$scope.selectedCategoryIndex].items;
                    var new_item = {
                            id: 0,
                            content: $scope.motivatorForm.content
                        };
                        category_items_list.push(new_item);

                    toaster.info("Content added to " + $scope.selectedCategoryName);

                    toaster.warning("Remember to save your content once you are done");

                    $scope.motivatorForm.content = "";

                    console.log(category_items_list);

                } else {
                    toaster.error("Please select the category where you want add your content.");
                }
            }


            /**
             *
             * @next = select next category
             * @previous = select previous category
             *
             * @selectedCategoryName = the string name of the selected category
             * @selectedCategoryIndex = the index of the category in $scope.motivators_list
             *
             */


            var next = document.getElementById("next");
            next.addEventListener("click", selectNext);

            function selectNext() {
                var categories_list = document.querySelector("select#categories");
                var selected_category_value = categories_list.options[categories_list.selectedIndex].value;

                if (selected_category_value < (categories_list.length - 1)) {
                    categories_list.value = (++selected_category_value);

                    let selected_category_text = categories_list.options[categories_list.selectedIndex].innerText;
                    $scope.selectedCategoryName = selected_category_text;
                    $scope.selectedCategoryIndex =  (categories_list.value - 1);
                }

            }

            var previous = document.getElementById("previous");
            previous.addEventListener("click", selectPrevious);

            function selectPrevious() {
                var categories_list = document.querySelector("select#categories");
                console.log(categories_list) ;
                var selected_category_value = categories_list.options[categories_list.selectedIndex].value;

                if (selected_category_value > 1) {
                    categories_list.value = (--selected_category_value);

                    let selected_category_text = categories_list.options[categories_list.selectedIndex].innerText;
                    $scope.selectedCategoryName = selected_category_text;
                    $scope.selectedCategoryIndex = (categories_list.value + 1);
                }

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