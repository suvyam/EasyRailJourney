package com.easyrailjourney.EasyRailJourney.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.models.users.RefreshToken;
import com.easyrailjourney.EasyRailJourney.repository.RefreshTokenRepo;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Value("${jwt.refresh-expiry}")
    private Long refreshExpiry;

    @Transactional
    public void deleteToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }


    @Transactional
    public RefreshToken createRefreshToken(String profileName) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setProfileName(profileName);
        refreshToken.setExpiresAt(
                Instant.now().plusSeconds(refreshExpiry)
        );

        return refreshTokenRepo.save(refreshToken);
    }


    @Transactional
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {

            refreshTokenRepo.delete(refreshToken);

            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }
}
