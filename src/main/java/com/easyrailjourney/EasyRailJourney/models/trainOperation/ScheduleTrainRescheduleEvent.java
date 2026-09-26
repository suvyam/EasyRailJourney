package com.easyrailjourney.EasyRailJourney.models.trainOperation;

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
@Table(name = "schedule_train_reschedule_event")
public class ScheduleTrainRescheduleEvent
        extends BaseModel {

    @Column(
            name = "schedule_train_id",
            nullable = false
    )
    private Long scheduleTrainId;

    @Column(
            name = "old_start_time",
            nullable = false
    )
    private Date oldStartTime;

    @Column(
            name = "old_end_time",
            nullable = false
    )
    private Date oldEndTime;

    @Column(
            name = "new_start_time",
            nullable = false
    )
    private Date newStartTime;

    @Column(
            name = "new_end_time",
            nullable = false
    )
    private Date newEndTime;

    @Column(
            nullable = false
    )
    private String reason;
}