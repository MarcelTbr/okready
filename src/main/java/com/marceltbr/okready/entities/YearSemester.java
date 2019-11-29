package com.marceltbr.okready.entities;

import javax.persistence.*;

@Entity
public class YearSemester {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name="year_id")
    private Year year;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name="semester_id")
    private Semester semester;

    public YearSemester(){}

    public YearSemester(Year year, Semester semester) {
        this.year = year;
        this.semester = semester;
    }

    public long getId() {
        return id;
    }

    public Year getYear() {
        return year;
    }

    public Semester getSemester() {
        return semester;
    }
}
