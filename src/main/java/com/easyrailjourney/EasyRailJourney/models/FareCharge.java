package com.easyrailjourney.EasyRailJourney.models;

import com.easyrailjourney.EasyRailJourney.enums.FareChargeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
public class FareCharge extends BaseModel {

    @NotNull(message = "Fare charge type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FareChargeType type;

    @NotNull(message = "Fare charge amount is required")
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}