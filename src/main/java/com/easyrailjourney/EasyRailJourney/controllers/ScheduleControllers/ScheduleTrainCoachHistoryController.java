package com.easyrailjourney.EasyRailJourney.controllers.ScheduleControllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.ScheduleTrainCoachHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.services.trainServices.ScheduleTrainCoachHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/schedule-train-coach-history")
@RequiredArgsConstructor
public class ScheduleTrainCoachHistoryController {

    private final ScheduleTrainCoachHistoryService
            scheduleTrainCoachHistoryService;

    @GetMapping("/schedule-train-class/{scheduleTrainClassId}")
    public List<ScheduleTrainCoachHistoryRespDto>
    getByScheduleTrainClassId(
            @PathVariable Long scheduleTrainClassId) {

        return scheduleTrainCoachHistoryService
                .getByScheduleTrainClassId(
                        scheduleTrainClassId);
    }
}