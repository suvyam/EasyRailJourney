package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.enums.Trains.TrainStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;


public interface  ScheduleTrainRepo extends  JpaRepository<ScheduleTrain, Long> {


    @Query("""
        SELECT s
        FROM ScheduleTrain s
        WHERE s.train.id = :trainId
          AND s.journeyStartTime < :journeyEndTime
          AND s.journeyEstimatedEndTime > :journeyStartTime
          AND s.isDeleted = false
    """)
    List<ScheduleTrain> findOverlappingSchedules(
            @Param("trainId") Long trainId,
            @Param("journeyStartTime") Date journeyStartTime,
            @Param("journeyEndTime") Date journeyEndTime
    );


    @Query("""
        SELECT DISTINCT s
        FROM ScheduleTrain s
        JOIN s.trainClasses stc
        JOIN stc.coaches stcoach
        WHERE stcoach.coach.id = :coachId
          AND s.journeyStartTime < :journeyEndTime
          AND s.journeyEstimatedEndTime > :journeyStartTime
          AND s.isDeleted = false
          AND stc.isDeleted = false
          AND stcoach.isDeleted = false
    """)
    List<ScheduleTrain> findOverlappingSchedulesByCoach(
            @Param("coachId") Long coachId,
            @Param("journeyStartTime") Date journeyStartTime,
            @Param("journeyEndTime") Date journeyEndTime
    );

    @Query("""
    SELECT DISTINCT s
    FROM ScheduleTrain s
    LEFT JOIN s.trainStations departureStop
    LEFT JOIN s.trainStations destinationStop
    WHERE
        (
            (:id IS NOT NULL AND s.id = :id)

            OR

            (:trainId IS NOT NULL AND s.train.id = :trainId)

            OR

            (
                :departureStationId IS NOT NULL
                AND :destinationStationId IS NOT NULL
                AND departureStop.station.id = :departureStationId
                AND destinationStop.station.id = :destinationStationId
                AND departureStop.stationSequence < destinationStop.stationSequence
                AND :status IS NOT NULL AND s.status = :status
            )
        )
        AND s.isDeleted = :isDeleted
    """)
   List<ScheduleTrain> searchSchedule(
        Long id,
        Long trainId,
        Long departureStationId,
        Long destinationStationId,
        TrainStatus status,
        boolean isDeleted
   );
    
}
