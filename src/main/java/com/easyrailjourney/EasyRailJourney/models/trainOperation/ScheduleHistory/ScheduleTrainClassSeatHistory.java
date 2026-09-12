package com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatBookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatStatus;
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
@Table(name = "schedule_train_class_seat_history")
public class ScheduleTrainClassSeatHistory
        extends BaseModel {

    @Column(
        name = "schedule_train_class_seat_id",
        nullable = false
    )
    private Long scheduleTrainClassSeatId;

    @Column(
        name = "schedule_train_class_id",
        nullable = false
    )
    private Long scheduleTrainClassId;

    @Column(name = "schedule_train_coach_id")
    private Long scheduleTrainCoachId;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private int waitListCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus seatStatus;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "seatbooking_status",
        nullable = false
    )
    private SeatBookingStatus seatbookingStatus;

    @Column(nullable = false)
    private Date archivedAt;
}