package com.easyrailjourney.EasyRailJourney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.AuthRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.RefreshReqstDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.UserLoginReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.UserDtos.UserRegisterReqDto;
import com.easyrailjourney.EasyRailJourney.models.users.RefreshToken;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.RefreshTokenRepo;
import com.easyrailjourney.EasyRailJourney.services.AuthService;
import com.easyrailjourney.EasyRailJourney.services.JwtService;
import com.easyrailjourney.EasyRailJourney.services.RefreshTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserDetailsService userDetailsService;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            RefreshTokenRepo refreshTokenRepo,
            UserDetailsService userDetailsService) {

        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(
            @Valid @RequestBody UserRegisterReqDto entity)
            throws Exception {
          

        return ResponseEntity.ok(
                authService.registerUser(entity)
        );
    }


    @PostMapping("/login")

    public ResponseEntity<AuthRespDto> login(
            @RequestBody UserLoginReqDto entity) {

        // 1. Create unauthenticated request
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        entity.getProfileName(),
                        entity.getPassword()
                );

        // 2. Authenticate username + password
        Authentication authentication =
                authenticationManager.authenticate(authenticationRequest);

        // 3. Generate access token
        String accessToken =
                jwtService.generateAccessToken(authentication);

        // 4. Generate refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(
                        authentication.getName()
                );

                AuthRespDto resp = new AuthRespDto();
                resp.setAccessToken(accessToken);
                resp.setRefreshToken(refreshToken);

        return ResponseEntity.ok(
                resp
        );
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthRespDto> refresh(
            @RequestBody RefreshReqstDto request) {

        // 1. Find refresh token
        RefreshToken refreshToken =
                refreshTokenRepo.findByToken(
                        request.getRefreshToken()
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Invalid refresh token"
                        )
                );

        // 2. Check expiration
        refreshTokenService.verifyExpiration(refreshToken);

        // 3. Get username from refresh token
        String username = refreshToken.getProfileName();

        // 4. Load current user from DB
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        // 5. Create authenticated Authentication
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        // 6. Generate new access token
        String newAccessToken =
                jwtService.generateAccessToken(authentication);

        // 7. Generate new refresh token
        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(
                        authentication.getName()
                );

        // 8. Delete old refresh token
        refreshTokenService.deleteToken(
                refreshToken.getToken()
        );

        // 9. Return both
        AuthRespDto resp = new AuthRespDto();
        resp.setAccessToken(newAccessToken);
        resp.setRefreshToken(newRefreshToken);

        return ResponseEntity.ok(
                resp
        );
    }
}