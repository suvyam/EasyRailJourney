package com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainHistory;

public interface ScheduleTrainHistoryRepo
        extends JpaRepository<ScheduleTrainHistory, Long> {

    List<ScheduleTrainHistory>
    findByScheduleTrainIdOrderByArchivedAtDesc(Long scheduleTrainId);
}