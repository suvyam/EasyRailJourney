package com.easyrailjourney.EasyRailJourney.repository.BookingsRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;


public interface  BookingsRepo  extends  JpaRepository<Bookings, Long>{
    
    Bookings findByPnr(String pnr);
}
