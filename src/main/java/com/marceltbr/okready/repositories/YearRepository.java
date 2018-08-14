package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.LinkedList;

@RepositoryRestResource
public interface YearRepository extends JpaRepository<Year, Long> {
    LinkedList<Year> findByYear(Long year);
    Year findById(long id);
}
