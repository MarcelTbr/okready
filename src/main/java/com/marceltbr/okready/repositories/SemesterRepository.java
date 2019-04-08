package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.Semester;
import com.marceltbr.okready.entities.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findById(long id);

    List<Semester> findAllByValue(long value);
}
