package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatBookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatStatus;

import lombok.Data;

@Data
public class ScheduleTrainClassSeatHistoryRespDto {

    private Long id;
    private Long scheduleTrainClassSeatId;
    private Long scheduleTrainClassId;
    private Long scheduleTrainCoachId;
    private Long seatId;
    private int waitListCount;
    private SeatStatus seatStatus;
    private SeatBookingStatus seatbookingStatus;
    private Date archivedAt;
}