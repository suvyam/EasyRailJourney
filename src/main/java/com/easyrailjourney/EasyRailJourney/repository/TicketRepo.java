package com.easyrailjourney.EasyRailJourney.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.Ticket;

public interface  TicketRepo extends  JpaRepository<Ticket, Long> {

    Optional<Ticket> findByPnr(String pnr);
    
}
