package com.easyrailjourney.EasyRailJourney.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.Country;


public interface CountryRepo extends JpaRepository<Country, Long> {

    Optional <Country> findByIdAndIsDeleted(Long id, boolean isDeleted);

    Set <Country> findByNameContainingIgnoreCaseAndIsDeleted(
            String name,
            boolean isDeleted
    );
}