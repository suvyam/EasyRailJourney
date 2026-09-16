package com.easyrailjourney.EasyRailJourney.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.models.Ticket;
import com.easyrailjourney.EasyRailJourney.models.TicketPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingPassengerRepo;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.TicketRepo;

import jakarta.transaction.Transactional;

@Service 
public class TicketService {

    private final BookingsRepo bookingsRepo;
    private final BookingPassengerRepo bookingPassengerRepo;
    private final TicketRepo ticketRepo;

    TicketService(BookingsRepo bookingsRepo,BookingPassengerRepo bookingPassengerRepo,TicketRepo ticketRepo){
        this.bookingsRepo = bookingsRepo;
        this.bookingPassengerRepo = bookingPassengerRepo;
        this.ticketRepo = ticketRepo;
    };



    @Transactional
    public Ticket syncTicket(Long bookingId) throws Exception{


       Bookings booking = bookingsRepo.findById(bookingId).orElseThrow(()-> new IllegalArgumentException("Booking Found"));

       List<BookingPassenger> bookingPassengers = booking.getPassengers();

       List<TicketPassenger > AllTicketPassangers  = new ArrayList<>();

       for(BookingPassenger bookingPassenger : bookingPassengers){

        BookingPassenger bp = bookingPassengerRepo.findById(bookingPassenger.getId()).orElseThrow(()->new Exception("Booking Passanger Not Available"));

        TicketPassenger tp = new TicketPassenger();

        tp.setName(bp.getName());
        tp.setAge(bp.getAge().toString());
        tp.setClassName(bp.getSeat().getScheduleTrainClass().getClass().getName());
        tp.setCoachNumber (bp.getSeat().getScheduleTrainCoach().getCoach().getCoachNumber());
        tp.setSeatNumber(bp.getSeat().getSeat().getSeatNumber());
        tp.setTrainName(bp.getBooking().getScheduleTrain().getTrain().getTrainName());
        tp.setArrivalStationName(bp.getBooking().getSourceStation().getStation().getName());
        tp.setDepartureStationName(bp.getBooking().getDestinationStation().getStation().getName());
        tp.setArrivalTime(bp.getBooking().getSourceStation().getArrivalTime());
        tp.setDepartureTime(bp.getBooking().getSourceStation().getDepartureTime());
        tp.setBookingStatus(bp.getPassangerBookingStatus().toString());
        tp.setTrainNumber(bp.getBooking().getScheduleTrain().getTrain().getTrainNumber());

        AllTicketPassangers.add(tp);

       }

       Ticket ticket = null;

        Optional<Ticket> ticketOptional = ticketRepo.findByPnr(booking.getPnr());

        if (ticketOptional.isEmpty()) {
            ticket = new Ticket();
            ticket.setPnr(booking.getPnr());
        }else{
            ticket = ticketOptional.get();
        }


       ticket.setTicketPassenger(AllTicketPassangers);

       if(ticket.getIssuedAt()!=null)ticket.setIssuedAt(new Date());

       ticketRepo.save(ticket);

       return ticket;

    }
    
}
