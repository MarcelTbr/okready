package com.marceltbr.okready.repositories.motivator;

import com.marceltbr.okready.entities.motivator.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById(long id);

    Category findByName(String categoryName);
}
