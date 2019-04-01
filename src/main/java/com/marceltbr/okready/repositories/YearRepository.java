package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.Year;
import com.marceltbr.okready.entities.YearSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.LinkedList;
import java.util.Optional;

@RepositoryRestResource
public interface YearRepository extends JpaRepository<Year, Long> {
    LinkedList<Year> findByYear(Long year);
    Year findById(long id);

    Optional<Year> findByYearSemestersLike(YearSemester yearSemester);
}
