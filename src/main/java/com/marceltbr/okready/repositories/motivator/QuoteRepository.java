package com.marceltbr.okready.repositories.motivator;

import com.marceltbr.okready.entities.motivator.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface QuoteRepository extends JpaRepository<Quote, Long>{

    Optional<Quote> findById(long id);

    List<Quote> findByContent(String quoteString);
}
