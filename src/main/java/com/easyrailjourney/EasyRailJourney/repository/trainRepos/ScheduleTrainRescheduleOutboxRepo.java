package com.easyrailjourney.EasyRailJourney.repository.trainRepos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.enums.Trains.OutboxStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainRescheduleOutbox;

public interface ScheduleTrainRescheduleOutboxRepo
        extends JpaRepository<
                ScheduleTrainRescheduleOutbox,
                Long> {


    // =====================================================
    // FIND RETRYABLE RECORDS
    // =====================================================

    @Query("""
        SELECT o
        FROM ScheduleTrainRescheduleOutbox o
        WHERE

            o.status = :pending

            OR

            (
                o.status = :failed
                AND
                (
                    o.nextAttemptAt IS NULL
                    OR o.nextAttemptAt <= :now
                )
            )

            OR

            (
                o.status = :processing
                AND o.lastAttemptAt < :staleBefore
            )

        ORDER BY o.id ASC
        """)
    List<ScheduleTrainRescheduleOutbox>
    findRetryable(

            @Param("pending")
            OutboxStatus pending,

            @Param("failed")
            OutboxStatus failed,

            @Param("processing")
            OutboxStatus processing,

            @Param("now")
            Date now,

            @Param("staleBefore")
            Date staleBefore
    );


    // =====================================================
    // ATOMIC CLAIM
    // =====================================================

    @Transactional
    @Modifying
    @Query("""
        UPDATE ScheduleTrainRescheduleOutbox o
        SET
            o.status = :processing,
            o.lastAttemptAt = :now
        WHERE
            o.id = :id
            AND
            (
                o.status = :pending

                OR

                (
                    o.status = :failed
                    AND
                    (
                        o.nextAttemptAt IS NULL
                        OR o.nextAttemptAt <= :now
                    )
                )

                OR

                (
                    o.status = :processing
                    AND o.lastAttemptAt < :staleBefore
                )
            )
        """)
    int claim(

            @Param("id")
            Long id,

            @Param("pending")
            OutboxStatus pending,

            @Param("failed")
            OutboxStatus failed,

            @Param("processing")
            OutboxStatus processing,

            @Param("now")
            Date now,

            @Param("staleBefore")
            Date staleBefore
    );
}