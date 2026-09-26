package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Trains.OutboxStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "schedule_train_reschedule_outbox",

    uniqueConstraints = {

        @UniqueConstraint(
            name = "uk_reschedule_event_booking",
            columnNames = {
                "reschedule_event_id",
                "booking_id"
            }
        ),

        @UniqueConstraint(
            name = "uk_notification_key",
            columnNames = {
                "notification_key"
            }
        )
    },

    indexes = {

        @Index(
            name = "idx_reschedule_outbox_status",
            columnList = "status"
        ),

        @Index(
            name = "idx_reschedule_outbox_event",
            columnList = "reschedule_event_id"
        ),

        @Index(
            name = "idx_reschedule_outbox_booking",
            columnList = "booking_id"
        )
    }
)
public class ScheduleTrainRescheduleOutbox
        extends BaseModel {

    @Column(
        name = "reschedule_event_id",
        nullable = false
    )
    private Long rescheduleEventId;


    @Column(
        name = "booking_id",
        nullable = false
    )
    private Long bookingId;


    /*
     * Unique identity of this notification.
     *
     * One reschedule event + one booking
     * = one notification key.
     */
    @Column(
        name = "notification_key",
        nullable = false,
        unique = true
    )
    private String notificationKey;


    /*
     * Snapshot recipient information.
     */
    @Column(
        name = "recipient_email",
        nullable = false
    )
    private String recipientEmail;


    @Column(
        name = "user_name"
    )
    private String userName;


    @Column(
        name = "pnr"
    )
    private String pnr;


    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false
    )
    private OutboxStatus status;


    @Column(
        name = "attempt_count",
        nullable = false
    )
    private Integer attemptCount = 0;


    @Column(
        name = "last_attempt_at"
    )
    private Date lastAttemptAt;


    @Column(
        name = "next_attempt_at"
    )
    private Date nextAttemptAt;


    @Column(
        name = "sent_at"
    )
    private Date sentAt;


    @Column(
        name = "last_error",
        length = 2000
    )
    private String lastError;
}