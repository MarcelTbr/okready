package com.marceltbr.okready.repositories.motivator;

import com.marceltbr.okready.entities.motivator.CategoryQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CategoryQuoteRepository extends JpaRepository<CategoryQuote, Long> {


    CategoryQuote findById(long id);


}
