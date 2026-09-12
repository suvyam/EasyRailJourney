package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.TrainDtos.ScheduleTrainClassSeatHistoryRespDto;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainClassSeatHistory;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos.ScheduleTrainClassSeatHistoryRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleTrainClassSeatHistoryService {

    private final ScheduleTrainClassSeatHistoryRepo
            scheduleTrainClassSeatHistoryRepo;

    public void archive(
            ScheduleTrainClassSeat scheduleTrainClassSeat) {

        ScheduleTrainClassSeatHistory history =
                new ScheduleTrainClassSeatHistory();

        history.setScheduleTrainClassSeatId(
                scheduleTrainClassSeat.getId());

        history.setScheduleTrainClassId(
                scheduleTrainClassSeat
                        .getScheduleTrainClass()
                        .getId());

        if (scheduleTrainClassSeat
                .getScheduleTrainCoach() != null) {

            history.setScheduleTrainCoachId(
                    scheduleTrainClassSeat
                            .getScheduleTrainCoach()
                            .getId());
        }

        history.setSeatId(
                scheduleTrainClassSeat
                        .getSeat()
                        .getId());

        history.setWaitListCount(
                scheduleTrainClassSeat
                        .getWaitListCount());

        history.setSeatStatus(
                scheduleTrainClassSeat
                        .getSeatStatus());

        history.setSeatbookingStatus(
                scheduleTrainClassSeat
                        .getSeatbookingStatus());

        history.setArchivedAt(new Date());

        scheduleTrainClassSeatHistoryRepo
                .save(history);
    }

    public List<ScheduleTrainClassSeatHistoryRespDto>
    getByScheduleTrainClassId(
            Long scheduleTrainClassId) {

        List<ScheduleTrainClassSeatHistory> histories =
                scheduleTrainClassSeatHistoryRepo
                        .findByScheduleTrainClassIdOrderByArchivedAtDesc(
                                scheduleTrainClassId);

        List<ScheduleTrainClassSeatHistoryRespDto> response =
                new ArrayList<>();

        for (ScheduleTrainClassSeatHistory history : histories) {

            ScheduleTrainClassSeatHistoryRespDto dto =
                    new ScheduleTrainClassSeatHistoryRespDto();

            dto.setId(history.getId());

            dto.setScheduleTrainClassSeatId(
                    history.getScheduleTrainClassSeatId());

            dto.setScheduleTrainClassId(
                    history.getScheduleTrainClassId());

            dto.setScheduleTrainCoachId(
                    history.getScheduleTrainCoachId());

            dto.setSeatId(
                    history.getSeatId());

            dto.setWaitListCount(
                    history.getWaitListCount());

            dto.setSeatStatus(
                    history.getSeatStatus());

            dto.setSeatbookingStatus(
                    history.getSeatbookingStatus());

            dto.setArchivedAt(
                    history.getArchivedAt());

            response.add(dto);
        }

        return response;
    }

    

    public void archiveAll(
        List<ScheduleTrainClassSeat> seats) {

    List<ScheduleTrainClassSeatHistory> historyList =
            new ArrayList<>();

    for (ScheduleTrainClassSeat seat : seats) {

        ScheduleTrainClassSeatHistory history =
                new ScheduleTrainClassSeatHistory();

        history.setScheduleTrainClassSeatId(
                seat.getId());

        history.setScheduleTrainClassId(
                seat.getScheduleTrainClass().getId());

        if (seat.getScheduleTrainCoach() != null) {

            history.setScheduleTrainCoachId(
                    seat.getScheduleTrainCoach().getId());
        }

        history.setSeatId(
                seat.getSeat().getId());

        history.setWaitListCount(
                seat.getWaitListCount());

        history.setSeatStatus(
                seat.getSeatStatus());

        history.setSeatbookingStatus(
                seat.getSeatbookingStatus());

        history.setArchivedAt(new Date());

        historyList.add(history);
    }

    scheduleTrainClassSeatHistoryRepo
            .saveAll(historyList);
}
}