package com.easyrailjourney.EasyRailJourney.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.City;


public interface CityRepo extends JpaRepository<City, Long> {

    Optional<City> findByIdAndIsDeleted(Long id, boolean isDeleted);

    List<City> findByNameContainingIgnoreCaseAndIsDeleted(
            String name,
            boolean isDeleted
    );
}