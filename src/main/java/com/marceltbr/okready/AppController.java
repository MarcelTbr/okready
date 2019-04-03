package com.marceltbr.okready;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.marceltbr.okready.entities.*;
import com.marceltbr.okready.repositories.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
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

    @Autowired
    private ObjectiveRepository objectiveRepo;

    @Autowired
    private SemesterObjectiveRepository semesterObjectiveRepo;

    @Autowired
    private ResultRepository resultRepo;

    @Autowired
    private ObjectiveResultRepository objectiveResultRepo;

    /**
     * ======== ENDPOINTS ========
     */

    @RequestMapping("get_semester/{year}/{semesterValue}")
    public ResponseEntity<Map<String,Object>> getSemester(Authentication auth, @PathVariable long year, @PathVariable long semesterValue){

        if(auth != null) {

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

            List<Semester> semesterList = semesterRepo.findByValue(semesterValue);

            List<Semester> semesterFoundList = semesterList.stream().filter(semester -> Objects.equals(semester.getYearSemester().getYear().getYear(), year))
                    .collect(Collectors.toList());


            if(semesterFoundList.size() > 0 ){


                ArrayList<Map<String,Object>> objectiveArrayList = new ArrayList<>();
                semesterFoundList.stream().forEach( semester -> {

                    semester.getSemesterObjectives().stream().map(semObj -> semObj.getObjective()).forEach( objective -> {


                        List<Result> results = objective.getObjectiveResults().stream().map(objRes -> {

                            return objRes.getResult();

                        }).collect(Collectors.toList());

                        HashMap<String, Object> objectiveMap = new HashMap<String, Object>(){{
                            put("title", objective.getTitle());
                            put("total_wins", objective.getTotal_wins());
                            put("results", results);
                        }};

                        objectiveArrayList.add(objectiveMap);

                    });


                });

                HashMap<String, Object> semesterDTO = new HashMap<String, Object>(){{

                    Semester semester = semesterFoundList.get(0);

                    put("name", semester.getName());
                    put("value", semester.getValue());
                    put("okr_array", objectiveArrayList);


                }};


                return new ResponseEntity<>(makeMap("semesterDTO", semesterDTO), HttpStatus.ACCEPTED);

            } else {

                return new ResponseEntity<>(makeMap("error", "Semester does not exist"), HttpStatus.CONFLICT);
            }


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



            List<Semester> semesterList = semesterRepo.findByValue(value);


            Semester semester = null;

            if(semesterList != null){

                //semester exists

                // get the semester of the given year from the list
                List<Semester> foundSemesterList = semesterList.stream().filter(s -> Objects.equals(s.getYearSemester().getYear().getYear(), yearNum))
                        .collect(Collectors.toList());


                if(foundSemesterList.size() > 0){

                    //find out wether year is already defined for that semester
                    Optional<Year> yearOptional = yearRepo.findByYearSemestersLike(foundSemesterList.get(0).getYearSemester());

                    long yearFound = yearOptional.get().getYear();

                    System.out.println("year found: " + yearFound);
                    System.out.println("year saving: " + yearNum);

                    if(Objects.equals(yearFound, yearNum)){

                        //year and semester already there
                        return new ResponseEntity<>(makeMap("error", "Semester already there"), HttpStatus.CONFLICT);


                    } else {
                        //semester exists but not from the same year

                        saveYearAndSemester(yearNum, value, name, json);

                        return new ResponseEntity<>(makeMap("success", "Semester Saved"), HttpStatus.ACCEPTED);
                    }

                } else {

                    //year is not already there

                    saveYearAndSemester(yearNum, value, name, json);

                    return new ResponseEntity<>(makeMap("success", "Semester Saved"), HttpStatus.ACCEPTED);
                }

            } else {

                //semester doesn't exist

                saveYearAndSemester(yearNum, value, name, json);

                return new ResponseEntity<>(makeMap("success", "Semester Saved"), HttpStatus.ACCEPTED);
            }


        }else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);
        }

    }

    private ArrayList<Map<String, Object>> makeOKRArrayList(JSONObject json) {
        JSONArray okr_array = json.getJSONArray("okr_array");

        Iterator okrArrayIterator = okr_array.iterator();


        ArrayList<Map<String,Object>> okrArrayList = new ArrayList<>();

        while(okrArrayIterator.hasNext()){

            Object objective = okrArrayIterator.next();

                //System.out.println(objective.toString());

            Gson gson = new Gson();
            //make objective map
            Map<String, Object> objectiveMap = gson.fromJson(objective.toString(), new TypeToken<Map<String, Object>>(){}.getType());

            //make results array and put 0 wins as default (when saving new semester)

            JsonArray newJsonArray = gson.toJsonTree(objectiveMap.get("results")).getAsJsonArray();

            Iterator resultsIterator = newJsonArray.iterator();

            ArrayList<Map<String, Object>> resultsArrayList = new ArrayList<>();
            while(resultsIterator.hasNext()){

                Object result = resultsIterator.next();
                Map<String, Object> resultMap = gson.fromJson(result.toString(), new TypeToken<Map<String, Object>>(){}.getType());
                resultMap.put("wins", 0L);

                resultsArrayList.add(resultMap);
            }

                //resultsArrayList.forEach(x -> System.out.println(x));

            //put results ArrayList into it's objective Map
            objectiveMap.put("results", resultsArrayList);

                //objectiveMap.forEach((x,y)-> System.out.println("key : " + x + " , value : " + y));

            // add eat objective Map to the okr Array List
            okrArrayList.add(objectiveMap);


        }

        return okrArrayList;
    }

    private void saveYearAndSemester(Long yearNum, Long value, String name, JSONObject json ) {

        // turn a javascript object into an ArrayList
        ArrayList<Map<String,Object>> okrArrayList = makeOKRArrayList(json);

                //print out again to double-check
                okrArrayList.stream().forEach(objective -> {

                            System.out.print(objective.get("title") + " ");
                            System.out.print("Total Wins: " + objective.get("total_wins"));
                            System.out.println(objective.get("results").toString());
                        }
                );

        //create and save semester
        Semester semester;
        semester =  new Semester(value, name);
        semesterRepo.save(semester);

        //loop objectives and save to semester
        okrArrayList.stream().forEachOrdered(objv -> {

            Objective objective = new Objective();
            objective.setTitle(objv.get("title").toString());
            objective.setTotal_wins(new Double((Double)objv.get("total_wins")).longValue());
            objectiveRepo.save(objective);

            ArrayList resultsArrayList = (ArrayList) objv.get("results");

            resultsArrayList.stream().forEachOrdered(res ->{

                Map<String,Object> resultMap = (Map<String,Object>) res;

                String title = resultMap.get("title").toString();
                long winsRatio = new Double((Double)resultMap.get("wins_ratio")).longValue();
                long wins = (Long) resultMap.get("wins");
                Result result = new Result(title, winsRatio, wins);
                resultRepo.save(result);

                ObjectiveResult objectiveResult = new ObjectiveResult(objective, result);
                objectiveResultRepo.save(objectiveResult);
                    });

            SemesterObjective semesterObjective = new SemesterObjective(semester, objective);
            semesterObjectiveRepo.save(semesterObjective);
        });


        List<Year> yearList =  yearRepo.findByYear(yearNum);
        Year year = null;
        if(!yearList.isEmpty()){
            //if year exists, get it
            year = yearList.get(0);
        } else{
            //otherwise create and save it
            year = new Year(yearNum); yearRepo.save(year);
        }

        YearSemester yearSemester = new YearSemester(year, semester);
        yearSemesterRepo.save(yearSemester);
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

        /** okr_array structure **/

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


        List<Object> okrDTO = semester.getSemesterObjectives().stream().map( semObj -> {

            Objective objective = semObj.getObjective();
            HashMap<String,Object> objectiveMap = new HashMap<String,Object>(){{
                put("title", objective.getTitle());
                put("total_wins", objective.getTotal_wins());
            }};

            List<Result> results = objective.getObjectiveResults().stream().map(objRes -> objRes.getResult()).collect(Collectors.toList());

            objectiveMap.put("results", results);

            return objectiveMap;


        }).collect(Collectors.toList());

        return okrDTO;
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