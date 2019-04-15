package com.marceltbr.okready.entities;

import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Semester {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long value;
    private String name;

    @OneToOne(mappedBy="semester", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private YearSemester yearSemester;

    @OneToMany(mappedBy="semester", fetch= FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<SemesterObjective> semesterObjectives;

    public Semester(){}

    public Semester(long value, String name) {
        this.value = value;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public long getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public YearSemester getYearSemester() {
        return yearSemester;
    }

    public void setYearSemester(YearSemester yearSemester) {
        this.yearSemester = yearSemester;
    }

    public Set<SemesterObjective> getSemesterObjectives() {
        return semesterObjectives;
    }

    public void setSemesterObjectives(Set<SemesterObjective> semesterObjectives) {
        this.semesterObjectives = semesterObjectives;
    }
}
