package com.marceltbr.okready.repositories.okrs;

import com.marceltbr.okready.entities.okrs.YearSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface YearSemesterRepository extends JpaRepository<YearSemester, Long> {
    YearSemester findById(long id);
}
