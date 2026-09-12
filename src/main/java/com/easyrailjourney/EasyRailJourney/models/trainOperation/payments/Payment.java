package com.easyrailjourney.EasyRailJourney.models.trainOperation.payments;

import java.math.BigDecimal;
import java.util.Date;

import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentType;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Payment extends BaseModel {


    @NotBlank (message="idempotencyKey required in payment")
    @Column (nullable=false)
    private String idempotencyKey;

    @NotNull (message="bookingId required in payment")
    @Column (nullable=false)
    private Long bookingId;

    @NotNull (message="userId required in payment")
    @Column (nullable=false)
    private Long userId;

    @NotNull (message="payemnt amount is required in payment")
    @Column (nullable=false)
    private Double amount;

    @NotNull (message="payemntMethod is required in payment")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @NotNull (message="payemntStatus is required in payment")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;


    @NotNull (message="payemntType is required in payment")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false , columnDefinition= "varchar(20) default 'BOOKING_PAYMENT'")
    private PaymentType paymentType;

    private String gatewayTransactionId;


    private String gatewayOrderId;

    private String failureReason;

    @NotNull (message="payemnt initialised time is required")
    @Column (nullable=false)
    private Date paymentInitiatedAt;

    private Date paymentCompletedAt;

}