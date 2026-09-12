package com.easyrailjourney.EasyRailJourney.models;

import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundCalculationType;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RefundRule extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @NotNull(message = "Cancellation hours is required")
    @Column(nullable = false)
    private Integer cancellationHours;

    @NotNull(message = "Refund calculation type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundCalculationType calculationType;

    @NotNull(message = "Refund value is required")
    @Column(nullable = false)
    private Double value;

    @NotNull(message = "Refund priority is required")
    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}