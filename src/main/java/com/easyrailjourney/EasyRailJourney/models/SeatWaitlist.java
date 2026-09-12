package com.easyrailjourney.EasyRailJourney.models;
import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_seat_waitlist_position",
            columnNames = {"schedule_train_class_seat_id", "position"}
        )
    }
)
public class SeatWaitlist extends BaseModel {

    @NotNull(message = "Schedule train class seat is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_train_class_seat_id", nullable = false)
    @JsonBackReference
    private ScheduleTrainClassSeat seat;

    @NotNull(message = "Booking is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference
    private Bookings booking;

    @NotNull(message = "Booking passenger is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_passenger_id", nullable = false)
    @JsonBackReference
    private BookingPassenger passenger;

    @NotNull(message = "Priority is required")
    @Column(nullable = false)
    private Integer priority = 1;

    @NotNull(message = "Waitlist position is required")
    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}