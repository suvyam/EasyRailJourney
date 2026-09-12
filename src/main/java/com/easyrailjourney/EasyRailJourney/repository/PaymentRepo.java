package com.easyrailjourney.EasyRailJourney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.payments.Payment;

public interface  PaymentRepo extends  JpaRepository<Payment, Long> {

   Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    
}
