package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules;

import java.util.Date;

import lombok.Data;

@Data
public class ScheduleTrainClassHistoryRespDto {

    private Long id;
    private Long scheduleTrainClassId;
    private Long scheduleTrainId;
    private Long trainClassId;
    private Date archivedAt;
}