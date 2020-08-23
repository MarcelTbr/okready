package com.marceltbr.okready.entities.motivator;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Motivator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @OneToOne(mappedBy="motivator", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private AppUserMotivator appUserMotivator;


    @OneToMany( fetch = FetchType.EAGER)
    @JoinColumn(name="motivator_id")
    private Set<Category> categorySet;

    public Motivator () {}


    public long getId() {
        return id;
    }

    public AppUserMotivator getAppUserMotivator() {
        return appUserMotivator;
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }
}
