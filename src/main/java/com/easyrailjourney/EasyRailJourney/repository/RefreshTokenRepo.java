package com.easyrailjourney.EasyRailJourney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.users.RefreshToken;

public interface RefreshTokenRepo
        extends JpaRepository <RefreshToken, Long> {

    Optional <RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}