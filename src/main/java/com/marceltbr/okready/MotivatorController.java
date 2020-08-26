package com.marceltbr.okready;


import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.motivator.Motivator;
import com.marceltbr.okready.repositories.AppUserRepository;
import com.marceltbr.okready.repositories.motivator.CategoryRepository;
import com.marceltbr.okready.repositories.motivator.MotivatorRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/api")
@RestController
public class MotivatorController extends HelperFunctions {

    @Autowired
    private AppUserRepository appUserRepo;


    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private MotivatorRepository motivatorRepo;


    /**
     * ======== ENDPOINTS ========
     */


    @RequestMapping(value = "save_category",  method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> saveCategoryObject(@RequestBody String categoryJson, Authentication auth) {

        if (auth != null) {

            JSONObject json = new JSONObject(categoryJson);


            String categoryName = json.getString("name");
            AppUser appUser = appUserRepo.findByUsername(auth.getName()).get(0);

            Motivator userMotivator = appUser.getAppUserMotivatorSet().iterator().next().getMotivator();
            makeCategory(categoryName, userMotivator, categoryRepo);

            System.out.println(categoryName + " " + appUser.getUsername());





            return new ResponseEntity<>(HelperFunctions.makeMap("success", "Category Saved"), HttpStatus.ACCEPTED);

        } else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);

        }
    }

    @RequestMapping(value = "save_motivators_list",  method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> saveMotivatorsList(@RequestBody String motivatorsJson, Authentication auth) {

        if (auth != null) {

            JSONObject json = new JSONObject(motivatorsJson);

                System.out.println(motivatorsJson.toString());

            AppUser appUser = appUserRepo.findByUsername(auth.getName()).get(0);

            Motivator usersMotivator = appUser.getAppUserMotivatorSet().iterator().next().getMotivator();

            JSONArray motivators_list = json.getJSONArray("data");

            Iterator motivatorsIterator = motivators_list.iterator();

            while(motivatorsIterator.hasNext()){

                saveMotivatorListFromObject(motivatorsIterator.next(), usersMotivator);
            }



            return new ResponseEntity<>(HelperFunctions.makeMap("success", "List Saved"), HttpStatus.ACCEPTED);

        } else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);

        }
    }

    private Motivator saveMotivatorListFromObject(Object nextObject, Motivator motivator) {


        String motivatorStr = nextObject.toString();

        System.out.println(motivatorStr);

        JSONObject motivatorObj = new JSONObject(  motivatorStr );

        String categoryName = motivatorObj.getString("category");

        boolean categoryExists = motivator.getCategorySet().stream().filter(category -> Objects.equals(category.getName(), categoryName ) ).count() > 0;

        if(!categoryExists){

            makeCategory(categoryName, motivator, categoryRepo);

            System.out.println(categoryName + " created!\n");
        } else {
            System.out.println(categoryName + " already exists!\n");
        }




        return motivator;
    }
}