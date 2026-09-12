package com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;
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
@Table(name = "schedule_train_coach_history")
public class ScheduleTrainCoachHistory extends BaseModel {

    @Column(name = "schedule_train_coach_id", nullable = false)
    private Long scheduleTrainCoachId;

    @Column(name = "schedule_train_class_id", nullable = false)
    private Long scheduleTrainClassId;

    @Column(name = "coach_id", nullable = false)
    private Long coachId;

    @Column(nullable = false)
    private String coachPosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GeneralStatus status;

    @Column(nullable = false)
    private Date archivedAt;
}