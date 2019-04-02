package com.marceltbr.okready.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class Objective {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="objective_id")
    long id;

    @Column(length = 5000)
    ArrayList<Object> results;

    String title;

    long total_wins;

    @OneToOne(mappedBy="objective", fetch = FetchType.EAGER)
    @JoinColumn( name="semester_objective_id")
    private SemesterObjective semesterObjective;


    public Objective () {}

    public Objective(ArrayList<Object> results, String title, long total_wins) {
        this.results = results;
        this.title = title;
        this.total_wins = total_wins;
    }

    public Objective(ArrayList<Object> results, String title, long total_wins, SemesterObjective semesterObjective) {
        this.results = results;
        this.title = title;
        this.total_wins = total_wins;
        this.semesterObjective = semesterObjective;
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
