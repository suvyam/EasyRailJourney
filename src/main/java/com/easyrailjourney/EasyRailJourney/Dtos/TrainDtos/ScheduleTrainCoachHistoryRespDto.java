package com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.GeneralStatus;

import lombok.Data;

@Data
public class ScheduleTrainCoachHistoryRespDto {

    private Long id;
    private Long scheduleTrainCoachId;
    private Long scheduleTrainClassId;
    private Long coachId;
    private String coachPosition;
    private GeneralStatus status;
    private Date archivedAt;
}