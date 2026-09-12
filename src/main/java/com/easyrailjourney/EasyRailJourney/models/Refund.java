package com.easyrailjourney.EasyRailJourney.models;
import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStatus;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "refund")
@Getter
@Setter
public class Refund extends BaseModel {

    @NotNull(message = "Booking is required")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Bookings booking;

    @NotNull(message = "Paid amount is required")
    @Column(nullable = false)
    private Double paidAmount;

    @NotNull(message = "Refund amount is required")
    @Column(nullable = false)
    private Double refundAmount;

    @NotNull(message = "Deduction amount is required")
    @Column(nullable = false)
    private Double deductionAmount;

    @NotNull(message = "Refund status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus refundStatus = RefundStatus.PENDING;

    private Date refundDate;

    private Long paymentId;

    private String reason;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}