package com.marceltbr.okready.entities.okrs;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Year {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long year;

    @OneToMany(mappedBy = "year", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<YearSemester> yearSemesters;

    @OneToOne(mappedBy ="year", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private AppUserYear appUserYear;

    public Year(){}

    public Year(long year) {
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public long getYear() {
        return year;
    }

    public Set<YearSemester> getYearSemesters() {
        return yearSemesters;
    }

    public AppUserYear getAppUserYear() {
        return appUserYear;
    }
}
