package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.enums.NotificationType;
import com.easyrailjourney.EasyRailJourney.enums.Trains.OutboxStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainRescheduleEvent;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainRescheduleOutbox;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRescheduleEventRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRescheduleOutboxRepo;
import com.easyrailjourney.EasyRailJourney.services.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleTrainRescheduleOutboxService {


    private final ScheduleTrainRescheduleOutboxRepo
            outboxRepo;


    private final ScheduleTrainRescheduleEventRepo
            eventRepo;


    private final NotificationService
            notificationService;


    /*
     * PROCESSING record older than 5 minutes
     * is treated as stale.
     */
    private static final long
            STALE_PROCESSING_MILLIS =
                5L * 60L * 1000L;


    // =====================================================
    // APPLICATION STARTUP
    // =====================================================

    @EventListener(ApplicationReadyEvent.class)
    public void processOnStartup() {

        processOutbox();
    }


    // =====================================================
    // PROCESS EVERY 30 SECONDS
    // =====================================================

    @Scheduled(
        fixedDelayString =
            "${schedule.reschedule.outbox.fixed-delay-ms:30000}"
    )
    public void processOutbox() {


        Date now =
                new Date();


        Date staleBefore =
                new Date(
                        now.getTime()
                                - STALE_PROCESSING_MILLIS
                );


        List<ScheduleTrainRescheduleOutbox>
                records =
                    outboxRepo.findRetryable(

                            OutboxStatus.PENDING,

                            OutboxStatus.FAILED,

                            OutboxStatus.PROCESSING,

                            now,

                            staleBefore
                    );


        if (records == null
                || records.isEmpty()) {

            return;
        }


        for (ScheduleTrainRescheduleOutbox outbox :
                records) {

            processOne(
                    outbox.getId()
            );
        }
    }


    // =====================================================
    // PROCESS ONE OUTBOX
    // =====================================================

    private void processOne(
            Long outboxId) {


        Date now =
                new Date();


        Date staleBefore =
                new Date(
                        now.getTime()
                                - STALE_PROCESSING_MILLIS
                );


        // =================================================
        // 1. CLAIM
        // =================================================

        int claimed =
                outboxRepo.claim(

                        outboxId,

                        OutboxStatus.PENDING,

                        OutboxStatus.FAILED,

                        OutboxStatus.PROCESSING,

                        now,

                        staleBefore
                );


        if (claimed == 0) {

            /*
             * Another processor claimed it.
             */
            return;
        }


        // =================================================
        // 2. READ FRESH OUTBOX
        // =================================================

        Optional<ScheduleTrainRescheduleOutbox>
                outboxOptional =
                    outboxRepo.findById(
                            outboxId
                    ); // why  to read fresher when above we find claim 


        if (outboxOptional.isEmpty()) {
            return;
        }


        ScheduleTrainRescheduleOutbox outbox =
                outboxOptional.get();


        // =================================================
        // 3. IF ALREADY SENT, STOP
        // =================================================

        if (OutboxStatus.SENT.equals(
                outbox.getStatus())) {

            return;
        }


        // =================================================
        // 4. FIND RESCHEDULE EVENT
        // =================================================

        Optional<ScheduleTrainRescheduleEvent>
                eventOptional =
                    eventRepo.findById(
                            outbox.getRescheduleEventId()
                    );


        if (eventOptional.isEmpty()) {

            markFailed(
                    outbox,
                    "Reschedule event not found"
            );

            return;
        }


        ScheduleTrainRescheduleEvent event =
                eventOptional.get();


        // =================================================
        // 5. CREATE NOTIFICATION
        // =================================================

        NotificationDto notification =
                new NotificationDto();


        notification.setType(
                NotificationType.EMAIL
        );


        notification.setTo(
                outbox.getRecipientEmail()
        );


        notification.setUserName(
                outbox.getUserName()
        );


        notification.setPnr(
                outbox.getPnr()
        );


        notification.setText(
                buildMessage(
                        event,
                        outbox
                )
        );

        notification.setNotificationKey(
            outbox.getNotificationKey()
        );


        // =================================================
        // 6. SEND
        // =================================================

        try {


            Boolean sent =
                    notificationService
                            .sendNotification(
                                    notification
                            );


            if (Boolean.TRUE.equals(sent)) {

                markSent(
                        outbox.getId()
                );

            } else {

                markFailed(
                        outbox,
                        "Notification service returned false"
                );
            }


        } catch (Exception e) {


            String error =
                    e.getMessage();


            if (error == null
                    || error.isBlank()) {

                error =
                        e.getClass()
                                .getSimpleName();
            }


            markFailed(
                    outbox,
                    error
            );
        }
    }


    // =====================================================
    // BUILD EMAIL MESSAGE
    // =====================================================

    private String buildMessage(

            ScheduleTrainRescheduleEvent event,

            ScheduleTrainRescheduleOutbox outbox
    ) {


        SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a"
                );


        StringBuilder sb =
                new StringBuilder();


        sb.append(
                "Dear "
        );


        sb.append(
                outbox.getUserName() != null
                        ? outbox.getUserName()
                        : "Passenger"
        );


        sb.append(
                ","
        );


        sb.append(
                "\n\nYour train schedule has been changed."
        );


        sb.append(
                "\n\nPrevious journey time: "
        );


        sb.append(
                formatter.format(
                        event.getOldStartTime()
                )
        );


        sb.append(
                " to "
        );


        sb.append(
                formatter.format(
                        event.getOldEndTime()
                )
        );


        sb.append(
                "\nNew journey time: "
        );


        sb.append(
                formatter.format(
                        event.getNewStartTime()
                )
        );


        sb.append(
                " to "
        );


        sb.append(
                formatter.format(
                        event.getNewEndTime()
                )
        );


        if (outbox.getPnr() != null) {

            sb.append(
                    "\n\nPNR: "
            );


            sb.append(
                    outbox.getPnr()
            );
        }


        if (event.getReason() != null
                && !event.getReason().isBlank()) {

            sb.append(
                    "\nReason: "
            );


            sb.append(
                    event.getReason()
            );
        }


        sb.append(
                "\n\nPlease check your updated journey details."
        );


        return sb.toString();
    }


    // =====================================================
    // MARK SENT
    // =====================================================

    private void markSent(
            Long outboxId) {


        Optional<ScheduleTrainRescheduleOutbox>
                optional =
                    outboxRepo.findById(
                            outboxId
                    );


        if (optional.isEmpty()) {
            return;
        }


        ScheduleTrainRescheduleOutbox outbox =
                optional.get();


        outbox.setStatus(
                OutboxStatus.SENT
        );


        outbox.setSentAt(
                new Date()
        );


        outbox.setLastError(
                null
        );


        outbox.setNextAttemptAt(
                null
        );


        outboxRepo.save(
                outbox
        );
    }


    // =====================================================
    // MARK FAILED
    // =====================================================

    private void markFailed(

            ScheduleTrainRescheduleOutbox outbox,

            String error
    ) {


        Integer attempts =
                outbox.getAttemptCount();


        if (attempts == null) {
            attempts = 0;
        }


        attempts++;


        outbox.setAttemptCount(
                attempts
        );


        outbox.setStatus(
                OutboxStatus.FAILED
        );


        outbox.setLastError(
                error
        );


        outbox.setLastAttemptAt(
                new Date()
        );


        outbox.setNextAttemptAt(
                calculateNextAttempt(
                        attempts
                )
        );


        outboxRepo.save(
                outbox
        );
    }


    // =====================================================
    // RETRY BACKOFF
    // =====================================================

    private Date calculateNextAttempt(
            int attempts) {


        int delayMinutes;


        if (attempts <= 1) {

            delayMinutes = 1;

        } else if (attempts == 2) {

            delayMinutes = 5;

        } else if (attempts == 3) {

            delayMinutes = 15;

        } else {

            delayMinutes = 30;
        }


        Calendar calendar =
                Calendar.getInstance();


        calendar.add(
                Calendar.MINUTE,
                delayMinutes
        );


        return calendar.getTime();
    }
}