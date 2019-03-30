package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.Year;
import com.marceltbr.okready.entities.YearSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.LinkedList;

@RepositoryRestResource
public interface YearSemesterRepository extends JpaRepository<YearSemester, Long> {
    YearSemester findById(long id);
}
