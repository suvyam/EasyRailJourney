package com.easyrailjourney.EasyRailJourney.models.users;

import java.time.Instant;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;


@Data
@Entity
public class RefreshToken  extends  BaseModel{


    @Column (unique = true, nullable = false)
    private String token;

    private String profileName;

    private Instant expiresAt;


}