package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleClasses;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;

import lombok.Data;


@Data
public class GeneralScheduleTrainClassSeatRespDto {

    private List<ScheduleTrainClassSeat> scheduleTrainClassSeats = new ArrayList<>();

    private ScheduleTrainClassSeat scheduleTrainClassSeat;

    private ResponseStatus responseStatus;

    private String message;
}