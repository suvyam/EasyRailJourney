package com.easyrailjourney.EasyRailJourney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStatus;
import com.easyrailjourney.EasyRailJourney.models.Refund;


public interface RefundRepo extends JpaRepository<Refund, Long> {

    Optional<Refund> findByBookingId(Long bookingId);

    Optional<Refund> findByPaymentId(Long paymentId);

    Boolean existsByBooking_IdAndRefundStatus(Long bookingId,RefundStatus rs);
}