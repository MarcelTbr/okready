package com.marceltbr.okready.repositories.motivator;


import com.marceltbr.okready.entities.motivator.AppUserMotivator;
import com.marceltbr.okready.entities.motivator.Motivator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MotivatorRepository extends JpaRepository<Motivator, Long>{

    Motivator findById(long id);

}
