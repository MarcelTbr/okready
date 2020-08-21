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
    @JoinColumn(name="category_id")
    private Set<Category> categorieSet;

    public Motivator () {}

    public Motivator(AppUserMotivator appUserMotivator, Set<Category> categorieSet) {
        this.appUserMotivator = appUserMotivator;
        this.categorieSet = categorieSet;
    }

    public long getId() {
        return id;
    }
}
