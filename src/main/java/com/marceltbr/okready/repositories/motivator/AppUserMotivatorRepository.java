package com.marceltbr.okready.repositories.motivator;

import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.motivator.AppUserMotivator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AppUserMotivatorRepository extends JpaRepository<AppUserMotivator, Long> {

    AppUserMotivator findById(long id);

    List<AppUserMotivator> findAllByAppUser(AppUser appUser);


}
