package com.easyrailjourney.EasyRailJourney.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class FareRuleCharge extends BaseModel {

    @NotNull(message = "Fare rule is required")
    @ManyToOne
    @JoinColumn(name = "fare_rule_id", nullable = false)
    @JsonBackReference
    private FareRule fareRule;

    @NotNull(message = "Fare charge is required")
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fare_charge_id", nullable = false)
    private FareCharge fareCharge;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}