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
@Table(name = "schedule_train_class_history")
public class ScheduleTrainClassHistory extends BaseModel {

    @Column(name = "schedule_train_class_id", nullable = false)
    private Long scheduleTrainClassId;

    @Column(name = "schedule_train_id", nullable = false)
    private Long scheduleTrainId;

    @Column(name = "train_class_id", nullable = false)
    private Long trainClassId;

    @Column(nullable = false)
    private Date archivedAt;
}