'use strict'

app.controller('MotivatorController', ['$scope', '$http', '$location', '$interval', '$timeout', '$window', '$rootScope', '$route', '$routeParams', 'toaster',
    function($scope,  $http, $location, $interval, $timeout, $window, $rootScope, $route, $routeParams, toaster) {


        function addContentToCategoryControl() {

            var next = document.getElementById("next");
            next.addEventListener("click", selectNext);

            function selectNext() {
                var categories_list = document.querySelector("select#categories");
                var selected_category_value = categories_list.options[categories_list.selectedIndex].value
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
                var selected_category_value = categories_list.options[categories_list.selectedIndex].value

                //console.info("selected_category_value", selected_category_value);

                if (selected_category_value > 1) {
                    categories_list.value = (--selected_category_value);
                }
                //console.log(categories_list.value)

            }


        }

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
                        "To break out of your confort zone use your imagination to feel the painful consequences of not doing something you need to do" ,
                        "Sometimes our mind will serve us, but sometimes it will work against us"
                    ]
            },
            {
                category: "Complaining Excuses",
                items:
                    [
                        "To break out of your confort zone use your imagination to feel the painful consequences of not doing something you need to do" ,
                        "Sometimes our mind will serve us, but sometimes it will work against us"
                    ]
            }
        ]

    }]);