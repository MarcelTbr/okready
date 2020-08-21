package com.marceltbr.okready.repositories.okrs;

import com.marceltbr.okready.entities.okrs.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    Objective findById(long id);

}
