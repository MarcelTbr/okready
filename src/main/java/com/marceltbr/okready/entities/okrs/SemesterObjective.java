package com.marceltbr.okready.entities.okrs;

import javax.persistence.*;

@Entity
public class SemesterObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="semester_id")
    private Semester semester;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name="objective_id")
    private Objective objective;

    public SemesterObjective () {}

    public SemesterObjective(Semester semester, Objective objective) {
        this.semester = semester;
        this.objective = objective;
    }

    public long getId() {
        return id;
    }

    public Semester getSemester() {
        return semester;
    }

    public Objective getObjective() {
        return objective;
    }
}
