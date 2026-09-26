package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.OutboxStatus;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainRescheduleEvent;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainRescheduleOutbox;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRescheduleEventRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRescheduleOutboxRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleTrainRescheduleEventService {


    private final ScheduleTrainRescheduleEventRepo
            rescheduleEventRepo;


    private final ScheduleTrainRescheduleOutboxRepo
            outboxRepo;


    private final BookingsRepo
            bookingsRepo;


    // =====================================================
    // CREATE EVENT + OUTBOX
    // =====================================================

    @Transactional(rollbackFor = Exception.class)
    public ScheduleTrainRescheduleEvent
    createEventAndOutbox(

            Long scheduleTrainId,

            Date oldStartTime,
            Date oldEndTime,

            Date newStartTime,
            Date newEndTime,

            String reason
    ) {


        // =================================================
        // 1. CREATE RESCHEDULE EVENT
        // =================================================

        ScheduleTrainRescheduleEvent event =
                new ScheduleTrainRescheduleEvent();


        event.setScheduleTrainId(
                scheduleTrainId
        );


        event.setOldStartTime(
                oldStartTime
        );


        event.setOldEndTime(
                oldEndTime
        );


        event.setNewStartTime(
                newStartTime
        );


        event.setNewEndTime(
                newEndTime
        );


        event.setReason(
                reason != null
                        && !reason.isBlank()
                        ? reason
                        : "Schedule train time updated"
        );


        event =
                rescheduleEventRepo.saveAndFlush(
                        event
                );


        // =================================================
        // 2. FIND ALL BOOKINGS
        // =================================================

        List<Bookings> bookings =
                bookingsRepo.findByScheduleTrainId(
                        scheduleTrainId
                );


        if (bookings == null
                || bookings.isEmpty()) {

            return event;
        }


        // =================================================
        // 3. CREATE OUTBOX ROWS
        // =================================================

        List<ScheduleTrainRescheduleOutbox>
                outboxList =
                    new ArrayList<>();


        for (Bookings booking :
                bookings) {


            if (booking == null) {
                continue;
            }


            // ---------------------------------------------
            // CANCELLED BOOKING
            // ---------------------------------------------

            if (BookingStatus.CANCELLED.equals(
                    booking.getBookingStatus())) {

                continue;
            }


            // ---------------------------------------------
            // USER VALIDATION
            // ---------------------------------------------

            if (booking.getUser() == null) {
                continue;
            }


            String email =
                    booking.getUser().getEmail();


            if (email == null
                    || email.isBlank()) {

                continue;
            }


            // ---------------------------------------------
            // CREATE OUTBOX
            // ---------------------------------------------

            ScheduleTrainRescheduleOutbox outbox =
                    new ScheduleTrainRescheduleOutbox();


            // ---------------------------------------------
            // EVENT
            // ---------------------------------------------

            outbox.setRescheduleEventId(
                    event.getId()
            );


            // ---------------------------------------------
            // BOOKING
            // ---------------------------------------------

            outbox.setBookingId(
                    booking.getId()
            );


            // ---------------------------------------------
            // IDEMPOTENCY KEY
            // ---------------------------------------------

            String notificationKey =
                    "RESCHEDULE-"
                            + event.getId()
                            + "-BOOKING-"
                            + booking.getId();


            outbox.setNotificationKey(
                    notificationKey
            );


            // ---------------------------------------------
            // USER SNAPSHOT
            // ---------------------------------------------

            outbox.setRecipientEmail(
                    email
            );


            outbox.setUserName(
                    booking.getUser().getFullName()
            );


            outbox.setPnr(
                    booking.getPnr()
            );


            // ---------------------------------------------
            // INITIAL STATE
            // ---------------------------------------------

            outbox.setStatus(
                    OutboxStatus.PENDING
            );


            outbox.setAttemptCount(
                    0
            );


            outboxList.add(
                    outbox
            );
        }


        // =================================================
        // 4. SAVE OUTBOX
        // =================================================

        if (!outboxList.isEmpty()) {

            outboxRepo.saveAllAndFlush(
                    outboxList
            );
        }


        return event;
    }
}