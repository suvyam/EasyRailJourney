package com.easyrailjourney.EasyRailJourney.repository.BookingsRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;

public interface BookingPassengerRepo extends  JpaRepository<BookingPassenger, Long> {
    
}
