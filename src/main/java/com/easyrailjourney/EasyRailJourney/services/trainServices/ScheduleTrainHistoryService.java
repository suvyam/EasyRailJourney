package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.Schedules.ScheduleTrainHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainHistory;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos.ScheduleTrainHistoryRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleTrainHistoryService {

    private final ScheduleTrainHistoryRepo scheduleTrainHistoryRepo;


    @Transactional 
    public void archive(ScheduleTrain scheduleTrain) {

        ScheduleTrainHistory history =
                new ScheduleTrainHistory();

        history.setScheduleTrainId(
                scheduleTrain.getId());

        history.setTrainId(
                scheduleTrain.getTrain().getId());

        history.setJourneyStartTime(
                scheduleTrain.getJourneyStartTime());

        history.setJourneyEstimatedEndTime(
                scheduleTrain.getJourneyEstimatedEndTime());

        history.setDepartureStationId(
                scheduleTrain.getDepartureStation().getId());

        history.setDestinationStationId(
                scheduleTrain.getDestinationStation().getId());

        history.setStatus(
                scheduleTrain.getStatus());

        history.setArchivedAt(new Date());

        scheduleTrainHistoryRepo.save(history);
    }

    public List<ScheduleTrainHistoryRespDto> getByScheduleTrainId(Long scheduleTrainId) {

        List<ScheduleTrainHistory> histories =
                scheduleTrainHistoryRepo
                        .findByScheduleTrainIdOrderByArchivedAtDesc(
                                scheduleTrainId);

        List<ScheduleTrainHistoryRespDto> response =
                new ArrayList<>();

        for (ScheduleTrainHistory history : histories) {

            ScheduleTrainHistoryRespDto dto =
                    new ScheduleTrainHistoryRespDto();

            dto.setId(history.getId());
            dto.setScheduleTrainId(
                    history.getScheduleTrainId());
            dto.setTrainId(
                    history.getTrainId());
            dto.setJourneyStartTime(
                    history.getJourneyStartTime());
            dto.setJourneyEstimatedEndTime(
                    history.getJourneyEstimatedEndTime());
            dto.setDepartureStationId(
                    history.getDepartureStationId());
            dto.setDestinationStationId(
                    history.getDestinationStationId());
            dto.setStatus(
                    history.getStatus());
            dto.setArchivedAt(
                    history.getArchivedAt());

            response.add(dto);
        }

        return response;
    }

    
}