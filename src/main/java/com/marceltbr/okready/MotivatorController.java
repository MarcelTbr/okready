package com.marceltbr.okready;


import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.motivator.Motivator;
import com.marceltbr.okready.repositories.AppUserRepository;
import com.marceltbr.okready.repositories.motivator.CategoryQuoteRepository;
import com.marceltbr.okready.repositories.motivator.CategoryRepository;
import com.marceltbr.okready.repositories.motivator.QuoteRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;

@RequestMapping("/api")
@RestController
public class MotivatorController extends HelperFunctions {

    @Autowired
    private AppUserRepository appUserRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private CategoryQuoteRepository categoryQuoteRepo;

    @Autowired
    private QuoteRepository quoteRepo;


    private static final Logger LOGGER = Logger.getLogger(MotivatorController.class.getName());

    /**
     * ======== ENDPOINTS ========
     */

    @RequestMapping("get_motivators_list")
    public ResponseEntity<Map<String, Object>> getMotivatorsListDTO(Authentication auth){

        if (auth != null) {


            AppUser appUser = appUserRepo.findByUsername(auth.getName()).get(0);

            Motivator userMotivator = appUser.getAppUserMotivatorSet().iterator().next().getMotivator();

            LOGGER.info("Current user: " + appUser.getUsername());


            Object motivatorsListDTO = makeMotivatorsListDTO(userMotivator, categoryQuoteRepo);

            LOGGER.warning(motivatorsListDTO.toString());

            return new ResponseEntity<>(HelperFunctions.makeMap("dto", motivatorsListDTO), HttpStatus.ACCEPTED);

        } else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);

        }

    }


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

            Iterator categoriesIterator = motivators_list.iterator();

            while(categoriesIterator.hasNext()){

                saveCategoryFromMotivatorListObject(categoriesIterator.next(), usersMotivator, categoryRepo);
            }

            //loop again and this time check if there are quotes to save
            Iterator secondCategoriesIterator = motivators_list.iterator();
            while(secondCategoriesIterator.hasNext()){

                saveCategoryQuotesFromMotivatorListObject(secondCategoriesIterator.next(), categoryRepo, quoteRepo, categoryQuoteRepo);
            }


            LOGGER.info("MotivatorsList successfully Saved!");

            return new ResponseEntity<>(HelperFunctions.makeMap("success", "Motivators List Successfully Saved"), HttpStatus.ACCEPTED);

        } else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);

        }
    }


}