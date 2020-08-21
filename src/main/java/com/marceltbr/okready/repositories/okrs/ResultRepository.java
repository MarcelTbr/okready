package com.marceltbr.okready.repositories.okrs;

import com.marceltbr.okready.entities.okrs.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ResultRepository extends JpaRepository<Result, Long> {

    Result findById(long id);
}
