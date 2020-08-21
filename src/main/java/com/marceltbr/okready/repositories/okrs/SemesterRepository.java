package com.marceltbr.okready.repositories.okrs;

import com.marceltbr.okready.entities.okrs.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findById(long id);

    List<Semester> findAllByValue(long value);
}
