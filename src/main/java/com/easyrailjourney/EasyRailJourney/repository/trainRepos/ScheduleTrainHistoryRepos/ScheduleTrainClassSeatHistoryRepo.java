package com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainClassSeatHistory;

public interface ScheduleTrainClassSeatHistoryRepo
        extends JpaRepository<
                ScheduleTrainClassSeatHistory, Long> {

    List<ScheduleTrainClassSeatHistory>
    findByScheduleTrainClassIdOrderByArchivedAtDesc(
            Long scheduleTrainClassId);

    List<ScheduleTrainClassSeatHistory>
    findByScheduleTrainClassSeatIdOrderByArchivedAtDesc(
            Long scheduleTrainClassSeatId);
}