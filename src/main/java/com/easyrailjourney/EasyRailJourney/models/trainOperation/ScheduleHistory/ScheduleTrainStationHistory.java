package com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity
@Table(name = "schedule_train_station_history")
public class ScheduleTrainStationHistory extends BaseModel {

    @Column(name = "schedule_train_id", nullable = false)
    private Long scheduleTrainId;

    @Column(name = "station_id", nullable = false)
    private Long stationId;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stationSequence;

    @Column(nullable = true)
    private Date arrivalTime;

    @Column(nullable = true)
    private Date departureTime;

    @Column(nullable = false)
    private Date archivedAt;
}