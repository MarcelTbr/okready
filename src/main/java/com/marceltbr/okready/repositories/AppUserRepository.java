package com.marceltbr.okready.repositories;

import com.marceltbr.okready.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.LinkedList;

@RepositoryRestResource
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    LinkedList<AppUser> findByUsername(String username);
    AppUser findById(long id);

    boolean existsByUsername(String username);
}
