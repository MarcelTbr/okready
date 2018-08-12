package com.marceltbr.okready.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




    @Entity
    public class AppUser {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;

        private String username;

        private String password;

        public AppUser(){}

        public AppUser(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }