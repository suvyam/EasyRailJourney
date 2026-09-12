package com.easyrailjourney.EasyRailJourney.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyrailjourney.EasyRailJourney.models.SeatWaitlist;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;

import jakarta.persistence.LockModeType;

public interface SeatWaitlistRepo extends JpaRepository<SeatWaitlist, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT w
        FROM SeatWaitlist w
        WHERE w.seat = :seat
          AND w.isDeleted = false
        ORDER BY w.priority ASC,
                 w.position ASC,
                 w.createdAt ASC
        """)
    Optional<SeatWaitlist> findFirstBySeatOrderByPriorityAscPositionAscCreatedAtAsc(
            @Param("seat") ScheduleTrainClassSeat seat
    );

   

    @Query("""
        SELECT COALESCE(MAX(w.position), 0)
        FROM SeatWaitlist w
        WHERE w.seat = :seat
        """)
    Integer findLastPosition(
            @Param("seat") ScheduleTrainClassSeat seat
    );
    void deleteById(Long id);
}
