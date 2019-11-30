package com.marceltbr.okready;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/errors")
@RestController
public class MyErrorController implements ErrorController {


    public void myErrorHandler(HttpServletRequest request, HttpServletResponse res) throws IOException{

        Object status = res.getStatus();
        System.out.println("status: " + status);

        String error_message = "Something went wrong...";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (Objects.equals(statusCode, HttpStatus.NOT_FOUND.value())) {
                error_message = "Page not found";
                res.sendRedirect("/show_error/"+ statusCode);

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                error_message = "Internal error...";
                res.sendRedirect("/show_error/" + statusCode);


            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()){
                error_message= "Unauthorized";
                res.sendRedirect("/show_error/" + statusCode );

            }

        }else {
            res.sendRedirect("/show_error/"+ HttpStatus.INTERNAL_SERVER_ERROR);

        }

        System.out.println("myErrorHandler: " + error_message);
        System.out.println("statusCode: " + status);
        return;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * ========= ENDPOINTS  =======
     */


    @RequestMapping("check_session")
    public ResponseEntity<Map<String,Object>> sessionCheck(Authentication auth){


        System.out.println(">>>SESSION CHECK");

        if (auth != null) {

            return new ResponseEntity<>(makeMap("session", true), HttpStatus.ACCEPTED);

        } else {

            return new ResponseEntity<>(makeMap("session", false), HttpStatus.UNAUTHORIZED);

        }
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
