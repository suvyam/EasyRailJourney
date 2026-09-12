package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;

public interface ScheduleTrainStationRepo
        extends JpaRepository<ScheduleTrainStation, Long> {

    Optional<ScheduleTrainStation>
    findByScheduleTrainAndStation(
            ScheduleTrain scheduleTrain,
            Station station
    );

    @Query("""
        SELECT s
        FROM ScheduleTrainStation s
        WHERE s.scheduleTrain.id = :scheduleTrainId
          AND s.station.id = :stationId
    """)
    Optional<ScheduleTrainStation> findByScheduleTrainIdAndStationId(
            @Param("scheduleTrainId") Long scheduleTrainId,
            @Param("stationId") Long stationId
    );

    @Query("""
        SELECT s
        FROM ScheduleTrainStation s
        WHERE
            (:id IS NOT NULL AND s.id = :id)
            OR (:scheduleTrainId IS NOT NULL
                AND s.scheduleTrain.id = :scheduleTrainId)
            OR (:stationId IS NOT NULL
                AND s.station.id = :stationId)
            OR (:stopSequence IS NOT NULL
                AND s.stationSequence = :stopSequence)
        """)
    List<ScheduleTrainStation> searchScheduleTrainStation(
            @Param("id") Long id,
            @Param("scheduleTrainId") Long scheduleTrainId,
            @Param("stationId") Long stationId,
            @Param("stopSequence") Integer stopSequence
    );

    List<ScheduleTrainStation> findByScheduleTrain( ScheduleTrain scheduleTrain);

    void deleteByScheduleTrain(ScheduleTrain scheduleTrain);
}