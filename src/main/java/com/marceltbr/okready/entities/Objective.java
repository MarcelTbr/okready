package com.marceltbr.okready.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Objective {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;


    ArrayList<Object> results;

    String title;

    long total_wins;

    public Objective () {}

    public Objective(ArrayList<Object> results, String title, long total_wins) {
        this.results = results;
        this.title = title;
        this.total_wins = total_wins;
    }

    public ArrayList<Object> getResults() {
        return results;
    }

    public void setResults(ArrayList<Object> results) {
        this.results = results;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTotal_wins() {
        return total_wins;
    }

    public void setTotal_wins(long total_wins) {
        this.total_wins = total_wins;
    }
}
