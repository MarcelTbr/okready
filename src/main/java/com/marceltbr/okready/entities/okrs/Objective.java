package com.marceltbr.okready.entities.okrs;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Objective {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //@Column(name="objective_id")
    long id;


    String title;

    long total_wins;

    @OneToOne(mappedBy="objective", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn( name="semester_objective_id")
    private SemesterObjective semesterObjective;


    @OneToMany(mappedBy = "objective", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<ObjectiveResult> objectiveResults;

    public Objective () {}

    public Objective(String title, long total_wins) {
        this.title = title;
        this.total_wins = total_wins;
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

    public Set<ObjectiveResult> getObjectiveResults() {
        return objectiveResults;
    }

    public long getId() {
        return id;
    }
}
