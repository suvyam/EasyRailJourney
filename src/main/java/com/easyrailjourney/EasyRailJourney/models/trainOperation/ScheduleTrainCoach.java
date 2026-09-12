package com.easyrailjourney.EasyRailJourney.models.trainOperation;


import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class ScheduleTrainCoach extends BaseModel {

    @NotNull(message = "Schedule train class is required")
    @ManyToOne
    @JoinColumn(name = "schedule_train_class_id", nullable = false)
    @JsonBackReference
    private ScheduleTrainClass scheduleTrainClass;

    @NotNull(message = "Coach is required")
    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @JsonBackReference
    private Coach coach;

    @NotBlank(message = "Coach position is required")
    @Column(nullable = false)
    private String coachPosition;

    @NotNull(message = "Coach status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GeneralStatus status;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}