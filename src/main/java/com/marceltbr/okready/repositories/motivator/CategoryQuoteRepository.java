package com.marceltbr.okready.repositories.motivator;

import com.marceltbr.okready.entities.motivator.CategoryQuote;
import com.marceltbr.okready.entities.motivator.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Set;

@RepositoryRestResource
public interface CategoryQuoteRepository extends JpaRepository<CategoryQuote, Long> {


    CategoryQuote findById(long id);

    CategoryQuote findByQuote(Quote savedQuote);

    Set<CategoryQuote> findByCategoryId(long id);
}
