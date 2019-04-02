package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.SemesterObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SemesterObjectiveRepository extends JpaRepository<SemesterObjective, Long> {
    SemesterObjective findById(long id);
}
