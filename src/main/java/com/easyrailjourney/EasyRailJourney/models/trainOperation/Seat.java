package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "seat",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_seat_coach_seat_number",
            columnNames = {"coach_id", "seat_number"}
        )
    }
)

public class Seat extends BaseModel {

    @NotBlank(message = "Seat number is required")
    @Column(nullable = false)
    private String seatNumber;

    @NotNull(message = "Coach is required")
    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonBackReference
    private Coach coach;

    @NotNull(message = "Seat type is required")
    @ManyToOne
    @JoinColumn(name = "seat_type_id", nullable = false)
    @JsonBackReference
    private SeatType seatType;

    @Column(nullable = false)
    private boolean isDeleted = false;
}