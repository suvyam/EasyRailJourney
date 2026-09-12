package com.easyrailjourney.EasyRailJourney.models.trainOperation;


import java.util.HashSet;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class ScheduleTrainClass extends BaseModel {

    @NotNull(message = "Schedule train is required")
    @ManyToOne
    @JoinColumn(name = "schedule_train_id", nullable = false)
    @JsonBackReference
    private ScheduleTrain scheduleTrain;

    @NotNull(message = "Train class is required")
    @ManyToOne
    @JoinColumn(name = "train_class_id", nullable = false)
    @JsonBackReference
    private TrainClass trainClass;

    @OneToMany(mappedBy = "scheduleTrainClass")
    @JsonManagedReference
    private Set<ScheduleTrainCoach> coaches = new HashSet<>();

    @OneToMany(mappedBy = "scheduleTrainClass")
    @JsonManagedReference
    private Set<ScheduleTrainClassSeat> scheduleTrainClassSeats = new HashSet<>();

    @Column(nullable = false)
    private Boolean isDeleted = false;
}