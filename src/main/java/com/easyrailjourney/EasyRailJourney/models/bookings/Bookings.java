package com.easyrailjourney.EasyRailJourney.models.bookings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

@Entity

public class Bookings extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "schedule_train_id", nullable = false)
    @JsonBackReference 
    private ScheduleTrain scheduleTrain;

    @ManyToOne
    @JsonBackReference 
    @JoinColumn(name = "source_station_id", nullable = false)
    private ScheduleTrainStation sourceStation;

    @ManyToOne
    @JsonBackReference 
    @JoinColumn(name = "destination_station_id", nullable = false)
    private ScheduleTrainStation destinationStation;

    @ManyToOne
    @JsonBackReference 
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JsonBackReference 
    @JoinColumn(name = "schedule_train_class_id", nullable = false)
    private ScheduleTrainClass trainClass;

    @Column (nullable=false)
    private int numberOfSeats;

    @Column (nullable=false)
    private Date journeyDate;

    @Enumerated(EnumType.STRING)
    @Column (nullable=false)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column (nullable=false)
    private PaymentStatus paymentStatus;

    @Column (nullable=false)
    private Date bookingDate;

    @Column (nullable=false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "booking")
    @JsonManagedReference
    private List<BookingPassenger> passengers = new ArrayList<>();

    @Column (nullable=false)
    Double totalFare; // update in bookings

    @Column (nullable=false)
    String pnr;
}