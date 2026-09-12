package com.easyrailjourney.EasyRailJourney.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.States;


public interface StateRepo extends JpaRepository<States, Long> {

    Optional<States> findByIdAndIsDeleted(Long id, boolean isDeleted);

    List<States> findByNameContainingIgnoreCaseAndIsDeleted(
            String name,
            boolean isDeleted
    );
}
