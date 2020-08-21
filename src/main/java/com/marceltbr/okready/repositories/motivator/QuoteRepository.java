package com.marceltbr.okready.repositories.motivator;

import com.marceltbr.okready.entities.motivator.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface QuoteRepository extends JpaRepository<Quote, Long>{

    Quote findById(long id);
}
