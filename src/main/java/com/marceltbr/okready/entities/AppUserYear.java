package com.marceltbr.okready.entities;

import javax.persistence.*;

@Entity
public class AppUserYear {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app_user_id")
    private AppUser appUser;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="year_id")
    private Year year;

    public AppUserYear () {}

    public AppUserYear(Year year, AppUser appUser) {
        this.year = year;
        this.appUser = appUser;
    }

    public long getId() {
        return id;
    }

    public Year getYear() {
        return year;
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
