package com.easyrailjourney.EasyRailJourney.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtService {


    @Value("${jwt.issuer}")
    String issuer;

    @Value("${jwt.access-expiry}")
    Long accessExpiry;

    @Value("${jwt.refresh-expiry}")
    Long refreshExpiry;

    @Autowired
    private  JwtEncoder jwtEncoder;

        @Transactional
    public String generateAccessToken(Authentication authentication) {

    Instant now = Instant.now();

    List<String> authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(a -> !a.startsWith("ROLE_"))
            .toList();

    List<String> roles = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(a -> a.startsWith("ROLE_"))
            .toList();

    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
            .issuer(issuer)
            .expiresAt(now.plus(accessExpiry, ChronoUnit.HOURS))
            .subject(authentication.getName())
            .claim("authorities", authorities)
            .claim("roles", roles)
            .claim("type", "access")
            .build();

    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256)
            .build();

    JwtEncoderParameters parameters =
            JwtEncoderParameters.from(jwsHeader, claimsSet);

    Jwt jwt = jwtEncoder.encode(parameters);

    return jwt.getTokenValue();
}
}




