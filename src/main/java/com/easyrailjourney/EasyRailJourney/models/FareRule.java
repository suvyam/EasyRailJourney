package com.easyrailjourney.EasyRailJourney.models;

import com.easyrailjourney.EasyRailJourney.Stratergies.FareCalculationType;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.TrainClass;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@Table(name = "fare_rule")
public class FareRule extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private States state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_class_id")
    private TrainClass classType;

    @NotNull(message = "Fare calculation type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FareCalculationType calculationType;

    @NotNull(message = "Fare value is required")
    @Column(nullable = false)
    private Double value;

    @NotNull(message = "Fare priority is required")
    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}

// fare rule policy  standard  add for shooiwng which rule to follow  for piroty 