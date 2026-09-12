package com.easyrailjourney.EasyRailJourney.models.trainOperation;



import java.util.Date;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
            name = "uk_schedule_train_station_sequence",
            columnNames = {"schedule_train_id", "stop_sequence"}
        )
    }
)
public class ScheduleTrainStation extends BaseModel {

    @NotNull(message = "Schedule train is required")
    @ManyToOne
    @JoinColumn(name = "schedule_train_id", nullable = false)
    @JsonBackReference
    private ScheduleTrain scheduleTrain;

    @NotNull(message = "Station is required")
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    @JsonBackReference
    private Station station;

    @NotNull(message = "Station sequence is required")
    @Column(name = "stop_sequence", nullable = false)
    private Integer stationSequence;

    @NotNull(message = "Arrival time is required")
    @Column(nullable = false)
    private Date arrivalTime;

    @NotNull(message = "Departure time is required")
    @Column(nullable = false)
    private Date departureTime;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}