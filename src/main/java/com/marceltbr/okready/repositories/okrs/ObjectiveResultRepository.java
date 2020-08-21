package com.marceltbr.okready.repositories.okrs;

import com.marceltbr.okready.entities.okrs.ObjectiveResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ObjectiveResultRepository extends JpaRepository<ObjectiveResult, Long> {

    ObjectiveResult findById(long id);
}
