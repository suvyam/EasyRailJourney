package com.easyrailjourney.EasyRailJourney.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.exceptions.ResourceNotFoundException;
import com.easyrailjourney.EasyRailJourney.models.Ticket;
import com.easyrailjourney.EasyRailJourney.models.TicketPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingPassengerRepo;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.TicketRepo;

@Service
public class TicketService {

    private final BookingsRepo bookingsRepo;

    private final BookingPassengerRepo
            bookingPassengerRepo;

    private final TicketRepo ticketRepo;


    public TicketService(
            BookingsRepo bookingsRepo,
            BookingPassengerRepo bookingPassengerRepo,
            TicketRepo ticketRepo) {

        this.bookingsRepo =
                bookingsRepo;

        this.bookingPassengerRepo =
                bookingPassengerRepo;

        this.ticketRepo =
                ticketRepo;
    }


    // =====================================================
    // SYNC TICKET
    // =====================================================

    @Transactional
    public Ticket syncTicket(
            Long bookingId)
            throws Exception {

        Bookings booking =
                bookingsRepo.findById(
                        bookingId
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking Not Found"
                        )
                );
       

        return createTicket(booking);
    }


    @Transactional
    public Ticket getTicketByPnr(String pnr) throws ResourceNotFoundException , Exception{
       Optional<Bookings> bookingOptional = Optional.ofNullable(bookingsRepo.findByPnr(pnr));

       if(bookingOptional.isEmpty()) new ResourceNotFoundException("Booking not found with Pnr");

        return createTicket(bookingOptional.get());


    }

    public Ticket createTicket(Bookings booking) throws Exception{
         List<BookingPassenger> bookingPassengers =
                booking.getPassengers();

        List<TicketPassenger>
                allTicketPassengers =
                    new ArrayList<>();

        // =================================================
        // FIND OR CREATE TICKET
        // =================================================

        Ticket ticket;

        Optional<Ticket> ticketOptional =
                ticketRepo.findByPnr(
                        booking.getPnr()
                );

        if (ticketOptional.isEmpty()) {

            ticket =
                    new Ticket();

            ticket.setPnr(
                    booking.getPnr()
            );

        } else {

            ticket =
                    ticketOptional.get();
        }

        // =================================================
        // CREATE TICKET PASSENGERS
        // =================================================

        if (bookingPassengers != null) {

            for (BookingPassenger bookingPassenger :
                    bookingPassengers) {

                BookingPassenger bp =
                        bookingPassengerRepo
                                .findById(
                                        bookingPassenger.getId()
                                )
                                .orElseThrow(() ->
                                        new Exception(
                                                "Booking Passanger Not Available"
                                        )
                                );

                if (bp.getSeat() == null) {
                    continue;
                }

                TicketPassenger tp =
                        new TicketPassenger();

                // -----------------------------------------
                // IMPORTANT
                // owning side of relationship
                // -----------------------------------------

                tp.setTicket(
                        ticket
                );

                // -----------------------------------------
                // PASSENGER
                // -----------------------------------------

                tp.setName(
                        bp.getName()
                );

                tp.setAge(
                        bp.getAge().toString()
                );

                // -----------------------------------------
                // CLASS
                // -----------------------------------------

                tp.setClassName(
                        bp.getSeat()
                                .getScheduleTrainClass()
                                .getTrainClass()
                                .getClassName()
                );

                // -----------------------------------------
                // COACH
                // -----------------------------------------

                tp.setCoachNumber(
                        bp.getSeat()
                                .getScheduleTrainCoach()
                                .getCoach()
                                .getCoachNumber()
                );

                // -----------------------------------------
                // SEAT
                // -----------------------------------------

                tp.setSeatNumber(
                        bp.getSeat()
                                .getSeat()
                                .getSeatNumber()
                );

                // -----------------------------------------
                // TRAIN
                // -----------------------------------------

                tp.setTrainName(
                        bp.getBooking()
                                .getScheduleTrain()
                                .getTrain()
                                .getTrainName()
                );

                tp.setTrainNumber(
                        bp.getBooking()
                                .getScheduleTrain()
                                .getTrain()
                                .getTrainNumber()
                );

                // -----------------------------------------
                // STATION NAMES
                //
                // Keep your existing business mapping.
                // -----------------------------------------

                tp.setDepartureStationName(
                        booking.getSourceStation().getStation().getName()
                    );
                    
                    tp.setArrivalStationName(
                        booking.getDestinationStation().getStation().getName()
                    );

                // -----------------------------------------
                // TIMES
                //
                // Keep your existing mapping unchanged.
                // -----------------------------------------

                tp.setDepartureTime(
                        booking.getSourceStation().getDepartureTime()
                    );
                    
                    tp.setArrivalTime(
                        booking.getDestinationStation().getArrivalTime()
                    );

                // -----------------------------------------
                // BOOKING STATUS
                // -----------------------------------------

                tp.setBookingStatus(
                        bp.getPassangerBookingStatus()
                                .toString()
                );

                allTicketPassengers.add(
                        tp
                );
            }
        }

        // =================================================
        // SET TICKET PASSENGERS
        // =================================================

        if (ticket.getTicketPassenger() == null) {
                ticket.setTicketPassenger(new ArrayList<>());
            }
            
            ticket.getTicketPassenger().clear();
            ticket.getTicketPassenger().addAll(allTicketPassengers);

        // =================================================
        // ISSUED AT
        // =================================================

        if (ticket.getIssuedAt() == null) {

            ticket.setIssuedAt(
                    new Date()
            );
        }

        // =================================================
        // SEAT COUNT
        // =================================================

        ticket.setSeatCount(
                booking.getNumberOfSeats()
        );

        // =================================================
        // SAVE
        // =================================================

        ticketRepo.saveAndFlush(
                ticket
        );

        System.out.println(
                "called ticket"
        );

        return ticket;
    }


    // =====================================================
    // UPDATE EXISTING TICKET TIME SNAPSHOTS
    // =====================================================

    @Transactional
    public void updateTicketTimes(
            String pnr,
            long deltaMillis) {

        if (pnr == null
                || pnr.isBlank()) {

            return;
        }

        Optional<Ticket> ticketOptional =
                ticketRepo.findByPnr(
                        pnr
                );

        if (ticketOptional.isEmpty()) {

            /*
             * Ticket may not exist because ticket generation
             * previously failed. We do not create a new ticket
             * during schedule rescheduling.
             */

            return;
        }

        Ticket ticket =
                ticketOptional.get();

        if (ticket.getTicketPassenger() == null) {

            return;
        }

        for (TicketPassenger passenger :
                ticket.getTicketPassenger()) {

            // ---------------------------------------------
            // ARRIVAL
            // ---------------------------------------------

            if (passenger.getArrivalTime() != null) {

                passenger.setArrivalTime(
                        new Date(
                                passenger.getArrivalTime()
                                        .getTime()
                                        + deltaMillis
                        )
                );
            }

            // ---------------------------------------------
            // DEPARTURE
            // ---------------------------------------------

            if (passenger.getDepartureTime() != null) {

                passenger.setDepartureTime(
                        new Date(
                                passenger.getDepartureTime()
                                        .getTime()
                                        + deltaMillis
                        )
                );
            }
        }

        ticketRepo.saveAndFlush(
                ticket
        );
    }
}