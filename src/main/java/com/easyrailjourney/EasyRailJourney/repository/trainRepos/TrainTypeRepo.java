package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainType;


public interface TrainTypeRepo extends JpaRepository<TrainType, Long> {

    Optional<TrainType> findByNameAndIsDeletedFalse(String name);

    Boolean existsByName(String name);

    Optional<TrainType> findByName(String name);

}