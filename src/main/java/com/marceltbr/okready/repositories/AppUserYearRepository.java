package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.AppUserYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AppUserYearRepository extends JpaRepository<AppUserYear, Long> {

    AppUserYear findById(long id);

    List<AppUserYear> findAllByAppUser(AppUser appUser);
}
