package com.marceltbr.okready;


import com.marceltbr.okready.entities.Semester;
import com.marceltbr.okready.entities.Year;
import com.marceltbr.okready.entities.YearSemester;
import com.marceltbr.okready.repositories.SemesterRepository;
import com.marceltbr.okready.repositories.YearRepository;
import com.marceltbr.okready.repositories.YearSemesterRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class AppController {

    @Autowired
    private YearRepository yearRepo;

    @Autowired
    private YearSemesterRepository yearSemesterRepo;

    @Autowired
    private SemesterRepository semesterRepo;



    /**
     * ======== ENDPOINTS ========
     */

    @RequestMapping("get_semester/{year}/{semester}")
    public ResponseEntity<Map<String,Object>> getSemester(Authentication auth, @PathVariable long year, @PathVariable long semester){

        if(auth != null) {


            /** TODO find year, find semester, etc... */



            return new ResponseEntity<>(makeMap("success", "Year: " + year + " Semester: " + semester), HttpStatus.ACCEPTED);


        }else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);
        }

    }



    @RequestMapping(value = "/save_semester", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>>saveSemester(@RequestBody String semesterJson, Authentication auth){


        if(auth != null) {

            JSONObject json = new JSONObject(semesterJson);

            Long yearNum = json.getLong("year");
            Long value = json.getLong("value");
            String name = json.getString("name");

            List<Year> yearList =  yearRepo.findByYear(yearNum);
            Year year = null;
            if(!yearList.isEmpty()){
                year = yearList.get(0);
            } else{
                year = new Year(yearNum); yearRepo.save(year);
            }

            Semester semester = new Semester(value, name); semesterRepo.save(semester);


            YearSemester yearSemester = new YearSemester(year, semester);
            yearSemesterRepo.save(yearSemester);

            return new ResponseEntity<>(makeMap("success", "Semester Saved"), HttpStatus.ACCEPTED);


        }else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping("get_semesters")
    public ResponseEntity<Map<String,Object>> getSemesters(Authentication auth){
        if(auth != null) {

            List<Year> years = yearRepo.findAll();


            Set<Object> yearsDTO = years.stream().map(year->{

                Map<String, Object> yearObj = new HashMap<>();

                List<Map<String, Object>> semesterList = year.getYearSemesters().stream().map(yearSem ->{

                    Map<String, Object> semesterObj = new HashMap<>();
                    Semester semester = yearSem.getSemester();

                    semesterObj.put("name", semester.getName());
                    semesterObj.put("value", semester.getValue());
                    semesterObj.put("okr_array", makeOkrObj(semester));
                    return semesterObj;
                }).collect(Collectors.toList());
                yearObj.put("year", year.getYear());
                yearObj.put("semesters", semesterList);

                return yearObj;
            }).collect(Collectors.toSet());

            return new ResponseEntity<>(makeMap("yearsDTO", yearsDTO), HttpStatus.ACCEPTED);

        }else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);
        }
    }

    private Object makeOkrObj(Semester semester) {


        /* TODO: make entities and repositories for okrs, titles and wins */

        /** okr_array structure **/

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

        /** mockup data **/

        ArrayList<Object> okrArray = new ArrayList<>();
        Map<String, Object> okrObject = makeMap("title", "Do Sport");
        okrObject.put("total_wins", 100);

            ArrayList<Object> krsArray = new ArrayList<>();
                Map<String, Object> krsObject1 = new HashMap<String, Object>(){{

                    put("title", "Go Swimming");
                    put("wins_ratio", 15);
                    put("wins", 4);
                }};
                Map<String, Object> krsObject2 = new HashMap<String, Object>(){{

                    put("title", "Go Jogging");
                    put("wins_ratio", 10);
                    put("wins", 6);
                }};
            krsArray.add(krsObject1);
            krsArray.add(krsObject2);
        okrObject.put("results", krsArray);

        okrArray.add(okrObject);

        return okrArray;
    }

    private Map<String, Object> makeMap(String s, Object object) {

        Map<String, Object> map = new HashMap<String, Object>() {

            {
                put(s, object);

            }

        };
        return map;
    }
}