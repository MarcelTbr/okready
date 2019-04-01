package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ObjectiveRepository extends JpaRepository<Objective, Long> {
    Objective findById(long id);


}
