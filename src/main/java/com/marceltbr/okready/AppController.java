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
    private AppUserRepository appUserRepo;

    @Autowired
    private AppUserYearRepository appUserYearRepo;

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

    @RequestMapping( value = "save_semester_edit", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>>saveSemesterEdit(@RequestBody String semesterJson, Authentication auth) {

        if(auth != null) {

            JSONObject json = new JSONObject(semesterJson);

            Long yearNum = json.getLong("year");
            Long value = json.getLong("value");

            // turn a javascript object into an ArrayList
            ArrayList<Map<String,Object>> okrArrayList = makeOKRArrayList(json, false);


            List<Semester> semesterList = semesterRepo.findAllByValue(value);

            // get the semester of the given year from the list
            List<Semester> foundSemesterList = semesterList.stream().filter(s -> Objects.equals(s.getYearSemester().getYear().getYear(), yearNum))
                    .collect(Collectors.toList());


            if(foundSemesterList.size() > 0) {

                updateSemester(okrArrayList);

                return new ResponseEntity<>(makeMap("success", "Semester saved"), HttpStatus.ACCEPTED);

            } else {

                return new ResponseEntity<>(makeMap("error", "Semester not found"), HttpStatus.CONFLICT);

            }

        }else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);

        }

    }

    private void updateSemester(ArrayList<Map<String, Object>> okrArrayList) {


        okrArrayList.stream().map(obj-> {

            Map<String,Object> objective = (Map<String, Object>) obj;

            ArrayList resultsList = (ArrayList) obj.get("results");

            return resultsList;
        }).flatMap( results -> results.stream() ).forEachOrdered(res ->{
                        System.out.println(res.toString());

                Map<String,Object> resultMap = (Map<String,Object>) res;
                long id = new Double ((Double) resultMap.get("id")).longValue();

                Result result = resultRepo.findById(id);

                long wins = new Double((Double) resultMap.get("wins")).longValue();

                result.setWins(wins);

                resultRepo.save(result);
        });



    }

    @RequestMapping("get_semester/{year}/{semesterValue}")
    public ResponseEntity<Map<String,Object>> getSemester(Authentication auth, @PathVariable long year, @PathVariable long semesterValue){

        if(auth != null) {

            //$scope.semester = {
            //            name: "1st",
            //            okr_array: [],
            //            value: 1

            // $scope.okr_array = [
            //     {   id: 1,
            //         title: "something",
            //         total_wins: 50,
            //         results: [
            //             {result_id: 1, title: "something else", wins_ratio: 1, wins: 8},
            //             {result_id: 2, title: "something else", wins_ratio: 1, wins: 8}
            //         ]
            //     },
            //     {   id: 2,
            //         title: "something",
            //         total_wins: 50,
            //         results: [
            //             {id: 3, title: "something else", wins_ratio: 1, wins: 8},
            //             {id: 4, title: "something else", wins_ratio: 1, wins: 8}
            //         ]
            //     }];

            List<Semester> semesterList = semesterRepo.findAll();

            List<Semester> semesterFoundList = semesterList.stream().filter(semester -> Objects.equals(semester.getYearSemester().getYear().getYear(), year))
                    .collect(Collectors.toList()).stream()
                    .filter(sem -> Objects.equals(sem.getValue(), semesterValue))
                    .collect(Collectors.toList());


            if(semesterFoundList.size() > 0 ){


                /** filter out semesters by user */
                List<Semester> userSemesters = semesterFoundList.stream().filter(sem -> {

                    String username = sem.getYearSemester().getYear().getAppUserYear().getAppUser().getUsername();

                    return Objects.equals(username, auth.getName());

                }).collect(Collectors.toList());



                ArrayList<Map<String,Object>> objectiveArrayList = new ArrayList<>();
                userSemesters.stream().forEach( semester -> {

                    semester.getSemesterObjectives().stream().map(semObj -> semObj.getObjective()).forEach( objective -> {


                        List<Result> results = objective.getObjectiveResults().stream().map(objRes -> {

                            return objRes.getResult();

                        }).collect(Collectors.toList());

                                    //System.out.println(results.get(0).toString());

                        /** sort results by id **/
                        List<Result> sortedResults = results.stream().sorted(Comparator.comparing(Result::getId))
                                        .collect(Collectors.toList());


                        HashMap<String, Object> objectiveMap = new HashMap<String, Object>(){{
                            put("id", objective.getId());
                            put("title", objective.getTitle());
                            put("total_wins", objective.getTotal_wins());
                            put("results", sortedResults);
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
            Long value = json.getLong("value"); //semester Value
            String name = json.getString("name"); //semester name

            AppUser appUser = appUserRepo.findByUsername(auth.getName()).get(0);

            List<Semester> foundSemesterList =
                    appUserYearRepo.findAllByAppUser(appUser).stream().map(usrYear -> usrYear.getYear())
                    .filter(year -> Objects.equals(year.getYear(), yearNum))
                    .map(year -> year.getYearSemesters())
                    .flatMap(yearSemesters -> yearSemesters.stream())
                    .map(yearSem -> yearSem.getSemester())
                    .filter(semester -> {
                        long semesterValue = semester.getValue();

                        return Objects.equals(value, semesterValue);
                    })
                    .collect(Collectors.toList());


          if(foundSemesterList.size() > 0) {

              return new ResponseEntity<>(makeMap("error", "Semester already there"), HttpStatus.CONFLICT);

          } else {

              saveYearAndSemester(yearNum, value, name, json, auth);

              return new ResponseEntity<>(makeMap("success", "Semester Saved"), HttpStatus.ACCEPTED);
          }


        }else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);
        }

    }

    private ArrayList<Map<String, Object>> makeOKRArrayList(JSONObject json, boolean firstSave) {
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

                //only add wins if it's the first save: when creating the Semester
                if(firstSave) {
                    resultMap.put("wins", 0L);
                }

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

    private void saveYearAndSemester(Long yearNum, Long value, String name, JSONObject json, Authentication auth ) {

        // turn a javascript object into an ArrayList
        ArrayList<Map<String,Object>> okrArrayList = makeOKRArrayList(json, true);

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
            year = yearList.stream().filter( yr -> {
                String username = yr.getAppUserYear().getAppUser().getUsername();

                return Objects.equals(username, auth.getName());
            }).collect(Collectors.toList()).get(0);
        } else{
            //otherwise create and save it
            year = new Year(yearNum); yearRepo.save(year);
        }

        YearSemester yearSemester = new YearSemester(year, semester);
        yearSemesterRepo.save(yearSemester);

        AppUser appUser =  appUserRepo.findByUsername(auth.getName()).get(0);

        AppUserYear appUserYear = new AppUserYear(year, appUser);
        appUserYearRepo.save(appUserYear);

        appUserRepo.save(appUser);
    }

    @RequestMapping("get_semesters")
    public ResponseEntity<Map<String,Object>> getSemesters(Authentication auth){
        if(auth != null) {

            List<Year> years = yearRepo.findAll();

            List<Year> userYears = years.stream().filter(year -> {

                String username = year.getAppUserYear().getAppUser().getUsername();

                return Objects.equals(username, auth.getName());
            }).collect(Collectors.toList());

            Set<Object> yearsDTO = userYears.stream().map(year->{

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