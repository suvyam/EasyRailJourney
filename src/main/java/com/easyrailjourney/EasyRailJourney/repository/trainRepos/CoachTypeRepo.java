package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.CoachType;


public interface CoachTypeRepo extends JpaRepository<CoachType, Long> {
    
}