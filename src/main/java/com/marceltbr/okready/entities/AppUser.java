package com.marceltbr.okready.entities;

import com.marceltbr.okready.entities.motivator.AppUserMotivator;
import com.marceltbr.okready.entities.okrs.AppUserYear;

import javax.persistence.*;
import java.util.Set;


@Entity
public class AppUser {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<AppUserYear> appUserYears;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "appUserMotivator_id")
    Set<AppUserMotivator> appUserMotivatorSet;

    public AppUser(){}

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<AppUserYear> getAppUserYears() {
        return appUserYears;
    }

    public Set<AppUserMotivator> getAppUserMotivatorSet() {
        return appUserMotivatorSet;
    }
}