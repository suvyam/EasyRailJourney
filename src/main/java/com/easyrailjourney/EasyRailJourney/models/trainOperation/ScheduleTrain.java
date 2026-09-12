package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
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
public class ScheduleTrain extends BaseModel {

    @NotNull(message = "Train is required")
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    @JsonBackReference
    private Train train;

    @NotNull(message = "Journey start time is required")
    @Column(nullable = false)
    private Date journeyStartTime;

    @NotNull(message = "Journey estimated end time is required")
    @Column(nullable = false)
    private Date journeyEstimatedEndTime;

    @NotNull(message = "Departure station is required")
    @ManyToOne
    @JoinColumn(name = "departure_station_id", nullable = false)
    @JsonBackReference
    private Station departureStation;

    @NotNull(message = "Destination station is required")
    @ManyToOne
    @JoinColumn(name = "destination_station_id", nullable = false)
    @JsonBackReference
    private Station destinationStation;

    @NotNull(message = "Train status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainStatus status;

    @OneToMany(mappedBy = "scheduleTrain")
    @JsonManagedReference
    private Set<ScheduleTrainClass> trainClasses = new HashSet<>();

    @OneToMany(mappedBy = "scheduleTrain")
    @JsonManagedReference
    private Set<ScheduleTrainStation> trainStations = new HashSet<>();

    @OneToMany(mappedBy = "scheduleTrain")
    @JsonManagedReference
    private Set<Bookings> booking = new HashSet<>();

    @Column(nullable = false)
    private Boolean isDeleted = false;
}