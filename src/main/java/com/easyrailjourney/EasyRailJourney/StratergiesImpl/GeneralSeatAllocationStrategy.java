package com.easyrailjourney.EasyRailJourney.StratergiesImpl;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Stratergies.SeatAllocationStrategy;
import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatBookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatStatus;
import com.easyrailjourney.EasyRailJourney.models.SeatWaitlist;
import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.repository.SeatWaitlistRepo;
import com.easyrailjourney.EasyRailJourney.services.WaitlistService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneralSeatAllocationStrategy
        implements SeatAllocationStrategy {

    private final SeatWaitlistRepo seatWaitlistRepo;
    private final WaitlistService waitlistService;

    @Override
    @Transactional
    public boolean allocateSeat(
            List<ScheduleTrainClassSeat> seats,
            Bookings booking) throws Exception {

        Iterator<ScheduleTrainClassSeat> iterator =
                seats.iterator();

        for (BookingPassenger passenger :
                booking.getPassengers()) {

            if (!iterator.hasNext()) {
                throw new Exception(
                        "Not enough seats for passengers."
                );
            }

            ScheduleTrainClassSeat seat =
                    iterator.next();

            passenger.setBooking(booking);

            if (SeatBookingStatus.EMPTY.equals(
                    seat.getSeatbookingStatus())) {

                // ----------------------------
                // CONFIRMED
                // ----------------------------

                seat.setSeatbookingStatus(
                        SeatBookingStatus.BOOKED
                );

                passenger.setSeat(seat);

                passenger.setPassangerBookingStatus(
                        BookingStatus.BOOKED
                );

            } else {

                // ----------------------------
                // WAITLIST
                // ----------------------------

                passenger.setSeat(seat);

                passenger.setPassangerBookingStatus(
                        BookingStatus.WAITLISTED
                );

                SeatWaitlist waitlist =
                        new SeatWaitlist();

                waitlist.setBooking(booking);
                waitlist.setPassenger(passenger);
                waitlist.setSeat(seat);

                waitlist.setPriority(1);

                waitlist.setPosition(
                        waitlistService.getNextPosition(seat)
                );

                seatWaitlistRepo.save(waitlist);
            }

            seat.setSeatStatus(
                    SeatStatus.UNLOCKED
            );
        }

        return true;
    }
}