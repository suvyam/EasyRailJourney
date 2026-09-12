package com.easyrailjourney.EasyRailJourney.controllers.ScheduleControllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleTrainHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule-train-history")
@RequiredArgsConstructor
public class ScheduleTrainHistoryController {

    private final ScheduleTrainHistoryService
            scheduleTrainHistoryService;

    @GetMapping("/schedule-train/{scheduleTrainId}")
    public List<ScheduleTrainHistoryRespDto>
    getByScheduleTrainId(
            @PathVariable Long scheduleTrainId) {

        return scheduleTrainHistoryService
                .getByScheduleTrainId(scheduleTrainId);
    }
}