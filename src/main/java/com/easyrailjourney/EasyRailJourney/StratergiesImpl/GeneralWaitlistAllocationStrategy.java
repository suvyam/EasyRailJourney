package com.easyrailjourney.EasyRailJourney.StratergiesImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Stratergies.WaitlistAllocationStrategy;
import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatBookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatStatus;
import com.easyrailjourney.EasyRailJourney.models.SeatWaitlist;
import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingPassengerRepo;
import com.easyrailjourney.EasyRailJourney.repository.SeatWaitlistRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassSeatRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class GeneralWaitlistAllocationStrategy
        implements WaitlistAllocationStrategy {

    private final SeatWaitlistRepo seatWaitlistRepo;
    private final BookingPassengerRepo bookingPassengerRepo;
    private final ScheduleTrainClassSeatRepo seatRepo;

    @Override
    @Transactional
    public boolean allocateNextPassenger(
            ScheduleTrainClassSeat seat) {

        Optional<SeatWaitlist> optional =
                seatWaitlistRepo
                        .findFirstBySeatOrderByPriorityAscPositionAscCreatedAtAsc(
                                seat
                        );

        if (optional.isEmpty()) {
            return false;
        }

        SeatWaitlist waitlist =
                optional.get();

        BookingPassenger passenger =
                waitlist.getPassenger();

        // --------------------------------
        // ASSIGN SEAT
        // --------------------------------

        passenger.setSeat(seat);

        passenger.setPassangerBookingStatus(
                BookingStatus.BOOKED
        );

        // --------------------------------
        // MARK SEAT BOOKED
        // --------------------------------

        seat.setSeatbookingStatus(
                SeatBookingStatus.BOOKED
        );

        seat.setSeatStatus(
                SeatStatus.UNLOCKED
        );

        bookingPassengerRepo.save(passenger);
        seatRepo.save(seat);

        // --------------------------------
        // REMOVE FROM WAITLIST
        // --------------------------------

        seatWaitlistRepo.delete(waitlist);

        return true;
    }

   
}