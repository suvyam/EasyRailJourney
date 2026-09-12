package com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "schedule_train_history")
public class ScheduleTrainHistory extends BaseModel {

    @Column(name = "schedule_train_id", nullable = false)
    private Long scheduleTrainId;

    @Column(name = "train_id", nullable = false)
    private Long trainId;

    @Column(nullable = false)
    private Date journeyStartTime;

    @Column(nullable = false)
    private Date journeyEstimatedEndTime;

    @Column(name = "departure_station_id", nullable = false)
    private Long departureStationId;

    @Column(name = "destination_station_id", nullable = false)
    private Long destinationStationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainStatus status;

    @Column(nullable = false)
    private Date archivedAt;
}