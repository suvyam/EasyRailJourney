package com.easyrailjourney.EasyRailJourney.repository.BookingsRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;


public interface  BookingsRepo  extends  JpaRepository<Bookings, Long>{

    List<Bookings> findByScheduleTrainId(Long scheduleTrainId);
    
    Bookings findByPnr(String pnr);
}
