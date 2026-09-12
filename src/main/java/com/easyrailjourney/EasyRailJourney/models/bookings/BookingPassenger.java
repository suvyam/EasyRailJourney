package com.easyrailjourney.EasyRailJourney.models.bookings;

import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter 
@Entity
public class BookingPassenger extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference 
    private Bookings booking;

    @Column (nullable=false)
    String name;

    @Column (nullable=false)
    Integer age;

    @ManyToOne 
    @JoinColumn (name="schedule_train_class_seat_id",nullable=false)
    @JsonBackReference 
    private ScheduleTrainClassSeat seat;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    BookingStatus passangerBookingStatus = BookingStatus.CREATED;

 

}