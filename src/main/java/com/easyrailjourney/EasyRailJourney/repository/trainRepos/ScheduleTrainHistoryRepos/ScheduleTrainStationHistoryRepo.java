package com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainHistoryRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleHistory.ScheduleTrainStationHistory;



@Repository
public interface  ScheduleTrainStationHistoryRepo extends  JpaRepository<ScheduleTrainStationHistory , Long>{
     @Query("""
        SELECT s
        FROM ScheduleTrainStationHistory s
        WHERE
            (:id IS NOT NULL AND s.id = :id)
            OR (:scheduleTrainId IS NOT NULL
                AND s.scheduleTrainId = :scheduleTrainId)
            OR (:stationId IS NOT NULL
                AND s.stationId = :stationId)
            OR (:stopSequence IS NOT NULL
                AND s.stationSequence = :stopSequence)
        """)
    List<ScheduleTrainStationHistory> searchScheduleTrainStationHistorys(
            @Param("id") Long id,
            @Param("scheduleTrainId") Long scheduleTrainId,
            @Param("stationId") Long stationId,
            @Param("stopSequence") Integer stopSequence
    );
}
