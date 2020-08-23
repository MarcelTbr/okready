package com.marceltbr.okready.entities.motivator;

import javax.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne( fetch = FetchType.EAGER)
    @JoinColumn(name="motivator_id")
    private Motivator motivator;

    private String name;

    public Category() {
    }


//    public Category( String name) {
//        this.name = name;
//    }

    public Category(Motivator motivator, String name) {
        this.motivator = motivator;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public Motivator getMotivator() {
        return motivator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
