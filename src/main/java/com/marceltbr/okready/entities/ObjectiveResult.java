package com.marceltbr.okready.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ObjectiveResult {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="objective_id")
    private Objective objective;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="result_id")
    private Result result;

    public ObjectiveResult(){}


    public ObjectiveResult(Objective objective, Result result) {
        this.objective = objective;
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public Objective getObjective() {
        return objective;
    }

    public Result getResult() {
        return result;
    }
}
