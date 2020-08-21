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

    public Category() {
    }


    public long getId() {
        return id;
    }
}
