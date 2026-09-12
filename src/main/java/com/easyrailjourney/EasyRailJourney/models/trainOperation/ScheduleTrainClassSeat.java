package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import java.util.HashSet;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatBookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ScheduleTrainClassSeat extends BaseModel {

    @NotNull(message = "Schedule train class is required")
    @ManyToOne
    @JoinColumn(name = "schedule_train_class_id", nullable = false)
    @JsonBackReference
    private ScheduleTrainClass scheduleTrainClass;

    @ManyToOne
    @JoinColumn(name = "schedule_train_coach_id", nullable = true)
    @JsonBackReference
    private ScheduleTrainCoach scheduleTrainCoach;

    @NotNull(message = "Seat is required")
    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private int waitListCount = 0;

    @NotNull(message = "Seat status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus = SeatStatus.UNLOCKED;

    @NotNull(message = "Seat booking status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "seatbooking_status", nullable = false)
    private SeatBookingStatus seatbookingStatus = SeatBookingStatus.EMPTY;

    @OneToMany(mappedBy = "seat")
    @JsonManagedReference
    private Set<BookingPassenger> bookingPassengers = new HashSet<>();

    @Column(nullable = false)
    private boolean isDeleted = false;
}