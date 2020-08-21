package com.marceltbr.okready;


import com.marceltbr.okready.repositories.AppUserRepository;
import org.hibernate.id.uuid.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api")
@RestController
public class MotivatorController extends HelperFunctions {

    @Autowired
    private AppUserRepository appUserRepo;


    /**
     * ======== ENDPOINTS ========
     */


    /*TODO: change endpoint */
    @RequestMapping("do_something/{id}")
    public ResponseEntity<Map<String,Object>> doSomething(Authentication auth, @PathVariable long id) {

        if (auth != null) {


            return new ResponseEntity<>(HelperFunctions.makeMap("success", "Semester Deleted"), HttpStatus.ACCEPTED);

        } else {

            return new ResponseEntity<>(makeMap("error", "User not authenticated"), HttpStatus.FORBIDDEN);

        }
    }

}