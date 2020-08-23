package com.marceltbr.okready;


import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.motivator.Category;
import com.marceltbr.okready.entities.motivator.Motivator;
import com.marceltbr.okready.repositories.AppUserRepository;
import com.marceltbr.okready.repositories.motivator.CategoryRepository;
import com.marceltbr.okready.repositories.motivator.MotivatorRepository;
import org.hibernate.id.uuid.Helper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

}