package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainCoach;


public interface ScheduleTrainClassSeatRepo extends JpaRepository<ScheduleTrainClassSeat, Long> {

    @Query(value = """
        SELECT s.*
        FROM schedule_train_class_seat s
        WHERE s.schedule_train_class_id = :scheduleTrainClassId
          AND s.seat_status != 'LOCKED'
          AND NOT EXISTS (
              SELECT 1
              FROM booking_passenger bp
              WHERE bp.schedule_train_class_seat_id = s.id
                AND bp.booking_id = :bookingId
          )
        ORDER BY
            CASE
                WHEN s.seatbooking_status = 'EMPTY' THEN 0
                ELSE 1
            END,
            s.wait_list_count ASC,
            s.id ASC
        LIMIT :numberOfSeat
        """, nativeQuery = true)
    List<ScheduleTrainClassSeat> findSeat(
        @Param("scheduleTrainClassId") Long scheduleTrainClassId,
        @Param("bookingId") Long bookingId,
        @Param("numberOfSeat") int numberOfSeat
    );




    @Query(value = """
        SELECT *
        FROM schedule_train_class_seat s
        WHERE s.schedule_train_class_id = :scheduleTraninClassId
        AND s.seat_status != 'LOCKED'
        ORDER BY s.wait_list_count LIMIT :numberOfSeat
        """, nativeQuery = true)

    List<ScheduleTrainClassSeat> findSeatAvailablity(
        @Param("scheduleTraninClassId") Long scheduleTraninClassId, 
        @Param("numberOfSeat") int numberOfSeat
    );

    List<ScheduleTrainClassSeat> findByScheduleTrainClass_IdAndIsDeletedFalse(
        Long scheduleTrainClassId
    );

    List<ScheduleTrainClassSeat> findByScheduleTrainCoach(
        ScheduleTrainCoach scheduleTrainCoach
);
    
}
