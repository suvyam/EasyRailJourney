package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;

import lombok.Data;

@Data
public class GeneralScheduleRespDto {

    List<ScheduleRespDto> schedules = new ArrayList<>();
    ScheduleRespDto schedule;
    ResponseStatus responseStatus;
    String message;
}