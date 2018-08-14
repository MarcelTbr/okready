package com.marceltbr.okready.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Year {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long year;

    public Year(){}

    public Year(long year) {
        this.year = year;
    }

    public long getYear() {
        return year;
    }
}
