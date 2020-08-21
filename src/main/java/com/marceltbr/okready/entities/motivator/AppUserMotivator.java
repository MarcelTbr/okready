package com.marceltbr.okready.entities.motivator;

import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.okrs.Year;

import javax.persistence.*;

@Entity
public class AppUserMotivator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="app_user_id")
    private AppUser appUser;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="motivator_id")
    private Motivator motivator;

    public AppUserMotivator() {}

    public AppUserMotivator(Motivator motivator, AppUser appUser) {
        this.motivator = motivator;
        this.appUser = appUser;
    }

    public long getId() {
        return id;
    }

    public Motivator getMotivator() {
        return motivator;
    }

    public AppUser getAppUser() {
        return appUser;
    }

}
