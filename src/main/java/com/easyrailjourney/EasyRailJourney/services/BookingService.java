package com.easyrailjourney.EasyRailJourney.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.BookingPassengerReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.BookingResponseDto;
import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.CreateBookingReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.BookingDtos.GeneralBookingReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.enums.Bookings.BookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.NotificationType;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatBookingStatus;
import com.easyrailjourney.EasyRailJourney.enums.Trains.SeatStatus;
import com.easyrailjourney.EasyRailJourney.models.SeatWaitlist;
import com.easyrailjourney.EasyRailJourney.models.Ticket;
import com.easyrailjourney.EasyRailJourney.models.bookings.BookingPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrain;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClass;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainClassSeat;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.payments.Payment;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingPassengerRepo;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.UserRepo.UserRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainClassSeatRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainStationRepo;
import com.easyrailjourney.EasyRailJourney.services.FareService.FareCalculationService;
import com.easyrailjourney.EasyRailJourney.services.Payments.PaymentService;
import com.easyrailjourney.EasyRailJourney.services.RefundServices.RefundService;
import com.easyrailjourney.EasyRailJourney.services.trainServices.SeatAllocationService;

@Service
public class BookingService {

    private final BookingPassengerRepo bookingPassengerRepo;

    private final ScheduleTrainRepo trainRepo;

    private final ScheduleTrainClassRepo scheduleTrainClassRepo;

    private final ScheduleTrainClassSeatRepo scheduleTrainClassSeatRepo;

    private final ScheduleTrainStationRepo scheduleTrainStationRepo;

    private final UserRepo userRepo;

    private final BookingsRepo bookingsRepo;

    private final PaymentService paymentService;

    private final SeatAllocationService seatAllocationService;

    private final WaitlistService waitlistService;

    private final FareCalculationService fareCalculationService;

    private final RefundService refundService;

    private final TicketService ticketService;

    private final NotificationService notificationService;

    public BookingService(
            ScheduleTrainRepo trainRepo,
            ScheduleTrainClassRepo scheduleTrainClassRepo,
            ScheduleTrainClassSeatRepo scheduleTrainClassSeatRepo,
            ScheduleTrainStationRepo scheduleTrainStationRepo,
            UserRepo userRepo,
            BookingsRepo bookingsRepo,
            PaymentService paymentService,
            SeatAllocationService seatAllocationService,
            WaitlistService waitlistService,
            FareCalculationService fareCalculationService,
            BookingPassengerRepo bookingPassengerRepo,
            RefundService refundService,
            TicketService ticketService,
            NotificationService notificationService) {

        this.trainRepo = trainRepo;
        this.scheduleTrainClassRepo = scheduleTrainClassRepo;
        this.scheduleTrainClassSeatRepo = scheduleTrainClassSeatRepo;
        this.scheduleTrainStationRepo = scheduleTrainStationRepo;
        this.userRepo = userRepo;
        this.bookingsRepo = bookingsRepo;
        this.paymentService = paymentService;
        this.seatAllocationService = seatAllocationService;
        this.waitlistService = waitlistService;
        this.fareCalculationService = fareCalculationService;
        this.bookingPassengerRepo = bookingPassengerRepo;
        this.refundService = refundService;
        this.ticketService = ticketService;
        this.notificationService = notificationService;
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    public Boolean validation(GeneralBookingReqDto reqDto)
            throws Exception {

        if (reqDto == null) {
            throw new Exception(
                    "Booking validation request is required.");
        }

        if (reqDto.getSchedule_train_id() == null) {
            throw new Exception(
                    "Please enter schedule train id.");
        }

        ScheduleTrain scheduleTrain =
                trainRepo.findById(reqDto.getSchedule_train_id())
                        .orElseThrow(
                                () -> new Exception(
                                        "Schedule train not found.")
                        );

        if (Boolean.TRUE.equals(scheduleTrain.getIsDeleted())) {
            throw new Exception(
                    "Schedule train is deleted.");
        }

        if (reqDto.getSrcStation() == null
                || reqDto.getDstStation() == null) {

            throw new Exception(
                    "Source station and destination station are required.");
        }

        if (reqDto.getSrcStation()
                .equals(reqDto.getDstStation())) {

            throw new Exception(
                    "Source and destination station cannot be same.");
        }

        // ---------------------------------------------
        // SOURCE STATION
        // ---------------------------------------------

        ScheduleTrainStation sourceScheduleStation =
                scheduleTrainStationRepo
                        .findByScheduleTrainIdAndStationId(
                                scheduleTrain.getId(),
                                reqDto.getSrcStation()
                        )
                        .orElseThrow(
                                () -> new Exception(
                                        "Source station does not exist "
                                                + "in this schedule train.")
                        );

        // ---------------------------------------------
        // DESTINATION STATION
        // ---------------------------------------------

        ScheduleTrainStation destinationScheduleStation =
                scheduleTrainStationRepo
                        .findByScheduleTrainIdAndStationId(
                                scheduleTrain.getId(),
                                reqDto.getDstStation()
                        )
                        .orElseThrow(
                                () -> new Exception(
                                        "Destination station does not exist "
                                                + "in this schedule train.")
                        );

        // ---------------------------------------------
        // ROUTE ORDER
        // ---------------------------------------------

        if (sourceScheduleStation.getStationSequence()
                >= destinationScheduleStation.getStationSequence()) {

            throw new Exception(
                    "Source station must come before destination station.");
        }

        // ---------------------------------------------
        // TRAIN CLASS
        // ---------------------------------------------

        if (reqDto.getTrainClass() == null
                || reqDto.getTrainClass().isBlank()) {

            throw new Exception(
                    "Train class is required.");
        }

        if (reqDto.getScheduleTrainClassId() == null) {
            throw new Exception(
                    "Schedule train class id is required.");
        }

        ScheduleTrainClass trainClass =
                scheduleTrainClassRepo
                        .findById(reqDto.getScheduleTrainClassId())
                        .orElseThrow(
                                () -> new Exception(
                                        "Schedule train class not found.")
                        );

        if (trainClass.getScheduleTrain() == null
                || !trainClass.getScheduleTrain()
                        .getId()
                        .equals(scheduleTrain.getId())) {

            throw new Exception(
                    "Schedule train class does not belong to this schedule train."
            );
        }

        // ---------------------------------------------
        // SEAT AVAILABILITY
        // ---------------------------------------------

        if (reqDto.getNumberOfSeats() == null
                || reqDto.getNumberOfSeats() <= 0) {

            throw new Exception(
                    "Number of seats must be greater than zero.");
        }

        List<ScheduleTrainClassSeat> seats =
                scheduleTrainClassSeatRepo.findSeatAvailablity(
                        trainClass.getId(),
                        reqDto.getNumberOfSeats()
                );

        if (seats == null
                || seats.size() < reqDto.getNumberOfSeats()) {

            throw new Exception(
                    "Required number of seats are not available.");
        }

        if (sourceScheduleStation.getDepartureTime() == null) {
            throw new Exception(
                    "Source station departure time is not available.");
        }

        if (sourceScheduleStation.getDepartureTime()
                .compareTo(new Date()) <= 0) {

            throw new Exception(
                    "Cannot book: train has already departed.");
        }

        return true;
    }

    // =========================================================
    // CREATE BOOKING
    // =========================================================

    @Transactional(rollbackFor = Exception.class)
    public Bookings createBooking(CreateBookingReqDto reqDto)
            throws Exception {

        // ---------------------------------------------
        // BASIC REQUEST VALIDATION
        // ---------------------------------------------

        if (reqDto == null) {
            throw new Exception(
                    "Booking request is required.");
        }

        if (reqDto.getScheduleTrainId() == null) {
            throw new Exception(
                    "Schedule train id is required.");
        }

        if (reqDto.getSourceStationId() == null
                || reqDto.getDestinationStationId() == null) {

            throw new Exception(
                    "Source station and destination station are required.");
        }

        if (reqDto.getSourceStationId()
                .equals(reqDto.getDestinationStationId())) {

            throw new Exception(
                    "Source and destination station cannot be same.");
        }

        if (reqDto.getNumberOfSeats() == null
                || reqDto.getNumberOfSeats() <= 0) {

            throw new Exception(
                    "Number of seats must be greater than zero.");
        }

        // ---------------------------------------------
        // SCHEDULE TRAIN
        // ---------------------------------------------

        ScheduleTrain train =
                trainRepo.findById(reqDto.getScheduleTrainId())
                        .orElseThrow(
                                () -> new Exception(
                                        "Schedule train not found.")
                        );

        if (Boolean.TRUE.equals(train.getIsDeleted())) {
            throw new Exception(
                    "Schedule train is deleted.");
        }

        // ---------------------------------------------
        // SOURCE AND DESTINATIN STATION
        // ---------------------------------------------

        ScheduleTrainStation src =
        scheduleTrainStationRepo
                .findByScheduleTrainIdAndStationId(
                        train.getId(),
                        reqDto.getSourceStationId()
                )
                .orElseThrow(
                        () -> new Exception(
                                "Source station does not exist in this schedule train."
                        )
                );

ScheduleTrainStation dst =
        scheduleTrainStationRepo
                .findByScheduleTrainIdAndStationId(
                        train.getId(),
                        reqDto.getDestinationStationId()
                )
                .orElseThrow(
                        () -> new Exception(
                                "Destination station does not exist in this schedule train."
                        )
                );

        // ---------------------------------------------
        // ROUTE ORDER
        // ---------------------------------------------

        if (src.getStationSequence()
                >= dst.getStationSequence()) {

            throw new Exception(
                    "Source station must come before destination station.");
        }

        // ---------------------------------------------
        // BOOKING TIMING
        // ---------------------------------------------

        if (src.getDepartureTime() == null) {
            throw new Exception(
                    "Source station departure time is not available.");
        }

        if (src.getDepartureTime()
                .compareTo(new Date()) <= 0) {

            throw new Exception(
                    "Cannot book: train has already departed.");
        }

        // ---------------------------------------------
        // TRAIN CLASS
        // ---------------------------------------------

        if (reqDto.getScheduleTrainClassId() == null) {
            throw new Exception(
                    "Schedule train class id is required.");
        }

        ScheduleTrainClass trainClass =
                scheduleTrainClassRepo
                        .findById(reqDto.getScheduleTrainClassId())
                        .orElseThrow(
                                () -> new Exception(
                                        "Schedule train class not found.")
                        );

        if (trainClass.getScheduleTrain() == null
                || !trainClass.getScheduleTrain()
                        .getId()
                        .equals(train.getId())) {

            throw new Exception(
                    "Schedule train class does not belong to this schedule train."
            );
        }

        // ---------------------------------------------
        // USER
        // ---------------------------------------------

        if (reqDto.getUserEmail() == null
                || reqDto.getUserEmail().isBlank()) {

            throw new Exception(
                    "User email is required.");
        }

        Users user =
                userRepo.findByEmail(
                        reqDto.getUserEmail()
                     
                )
                .orElseThrow(
                        () -> new Exception(
                                "User not registered or account is deleted.")
                );

        // ---------------------------------------------
        // PASSENGER VALIDATION
        // ---------------------------------------------

        List<BookingPassengerReqDto> passengerDtos =
                reqDto.getPassengers();

        if (passengerDtos == null
                || passengerDtos.isEmpty()) {

            throw new Exception(
                    "Passenger details are required.");
        }

        if (passengerDtos.size()
                != reqDto.getNumberOfSeats()) {

            throw new Exception(
                    "Number of passengers must match number of seats.");
        }

        for (BookingPassengerReqDto bp : passengerDtos) {

            if (bp == null) {
                throw new Exception(
                        "Passenger details cannot be null.");
            }

            if (bp.getName() == null
                    || bp.getName().isBlank()
                    || bp.getAge() == null
                    || bp.getGender() == null) {

                throw new Exception(
                        "Passenger name, age and gender are required.");
            }

            if (bp.getAge() <= 0) {
                throw new Exception(
                        "Passenger age must be greater than zero.");
            }
        }

        // ---------------------------------------------
        // CALCULATE FARE
        // ---------------------------------------------

        FareCalculationReqDto fareCalculationReqDto =
                new FareCalculationReqDto();

        fareCalculationReqDto.setTrainId(
                train.getId()
        );

        fareCalculationReqDto.setDestinationStationId(
                dst.getId()
        );

        fareCalculationReqDto.setSourceStationId(
                src.getId()
        );

        fareCalculationReqDto.setClassType(
                trainClass.getTrainClass()
        );

        fareCalculationReqDto.setPassengerCount(
                reqDto.getNumberOfSeats()
        );

        FareCalculationRespDto fare =
                fareCalculationService.calculate(
                        fareCalculationReqDto
                );

        System.err.println(
                fare.getTotalFare()
        );

        // ---------------------------------------------
        // CREATE BOOKING
        // ---------------------------------------------

        Bookings booking =
                new Bookings();

        booking.setBookingDate(
                reqDto.getBookingDate()
        );

        booking.setBookingStatus(
                BookingStatus.PENDING
        );

        booking.setDeleted(
                reqDto.isDeleted()
        );

        booking.setDestinationStation(
                dst
        );

        booking.setSourceStation(
                src
        );

        booking.setNumberOfSeats(
                reqDto.getNumberOfSeats()
        );

        booking.setPaymentStatus(
                PaymentStatus.PENDING
        );

        booking.setScheduleTrain(
                train
        );

        booking.setUser(
                user
        );

        booking.setTrainClass(
                trainClass
        );

        booking.setJourneyDate(
                reqDto.getJourneyDate()
        );

        booking.setTotalFare(
                fare.getTotalFare()
        );

        // ---------------------------------------------
        // SAVE BOOKING FIRST
        // ---------------------------------------------

        booking =
                bookingsRepo.save(
                        booking
                );

        // ---------------------------------------------
        // FIND AVAILABLE SEATS
        //
        // Booking is already saved, so booking.getId()
        // is available for your existing findSeat(...)
        // method.
        // ---------------------------------------------

        List<ScheduleTrainClassSeat> seats =
                scheduleTrainClassSeatRepo.findSeat(
                        trainClass.getId(),
                        booking.getId(),
                        reqDto.getNumberOfSeats()
                );

        if (seats == null
                || seats.size() < reqDto.getNumberOfSeats()) {

            throw new Exception(
                    "Required number of seats are not available.");
        }

        // ---------------------------------------------
        // TEMPORARILY LOCK SEATS
        // ---------------------------------------------

        for (ScheduleTrainClassSeat seat : seats) {

            if (seat == null) {
                throw new Exception(
                        "Invalid seat found.");
            }

            seat.setSeatStatus(
                    SeatStatus.LOCKED
            );
        }

        // ---------------------------------------------
        // CREATE PASSENGERS
        // AND ASSIGN ONE SEAT TO EACH PASSENGER
        // ---------------------------------------------

        List<BookingPassenger> bookingPassengers =
                new ArrayList<>();

        for (int i = 0; i < passengerDtos.size(); i++) {

            BookingPassengerReqDto bp =
                    passengerDtos.get(i);

            ScheduleTrainClassSeat seat =
                    seats.get(i);

            BookingPassenger bookingPassenger =
                    new BookingPassenger();

            bookingPassenger.setName(
                    bp.getName()
            );

            bookingPassenger.setAge(
                    bp.getAge()
            );

            bookingPassenger.setGender(
                    bp.getGender()
            );

            bookingPassenger.setBooking(
                    booking
            );


            bookingPassenger.setSeat(
                    seat
            );


            bookingPassengers.add(
                    bookingPassenger
            );
        }

        booking.setPassengers(
                bookingPassengers
        );

        // ---------------------------------------------
        // SAVE PASSENGERS
        // ---------------------------------------------

        if (!bookingPassengers.isEmpty()) {

            bookingPassengerRepo.saveAll(
                    bookingPassengers
            );
        }

        // ---------------------------------------------
        // CREATE PAYMENT
        // ---------------------------------------------

        PaymentCreateReqDto paymentCreateReqDto =  new PaymentCreateReqDto();

        paymentCreateReqDto.setBookingId(
                booking.getId()
        );

        paymentCreateReqDto.setUserId(
                user.getId()
        );

        paymentCreateReqDto.setAmount(
                fare.getTotalFare()
        );

        paymentCreateReqDto.setIdempotencyKey(
                UUID.randomUUID().toString()
        );

        paymentCreateReqDto.setPaymentMode(
                reqDto.getPaymentMode()
        );

        paymentCreateReqDto.setPaymentMethod(
                reqDto.getPaymentMethod()
        );

        paymentCreateReqDto.setSenderAccountNumber(
                reqDto.getSenderAccountNumber()
        );

        Payment payment = paymentService.createPayment( paymentCreateReqDto );

        // ---------------------------------------------
        // PAYMENT SUCCESS
        // ---------------------------------------------

        if (payment.getStatus()
                .equals(PaymentStatus.SUCCESS)) {

            boolean allocated =
                    seatAllocationService.allocateSeat(
                            seats,
                            booking
                    );

            // Release temporary locks
            for (ScheduleTrainClassSeat seat :
                    seats) {

                seat.setSeatStatus(
                        SeatStatus.UNLOCKED
                );
            }

            if (!allocated) {

                /*
                 * Production system:
                 * payment was successful but allocation failed.
                 * Refund should be triggered here.
                 */

                throw new Exception(
                        "Payment completed, but seats "
                                + "could not be allocated.");
            }

        } else {

            booking.setPaymentStatus(
                    PaymentStatus.FAILED
            );

            bookingsRepo.save(
                    booking
            );

            // Because @Transactional uses rollbackFor = Exception.class,
            // booking/passenger/seat DB changes will roll back.
            throw new Exception(
                    "Payment failed.");
        }

        // ---------------------------------------------
        // FINAL BOOKING STATE
        // ---------------------------------------------

        booking.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        booking.setBookingStatus(
                BookingStatus.CREATED
        );

        booking.setPnr(
                UUID.randomUUID().toString()
        );

        return bookingsRepo.save(
                booking
        );
    }

    // =========================================================
    // BOOKING -> RESPONSE DTO
    // =========================================================

    public BookingResponseDto convertToResponse(
            Bookings booking) {

        BookingResponseDto response =
                new BookingResponseDto();

        response.setScheduleTrainName(
                booking.getScheduleTrain()
                        .getTrain()
                        .getTrainName()
        );

        response.setSourceStation(
                booking.getSourceStation()
                        .getStation()
                        .getName()
        );

        response.setDestinationStation(
                booking.getDestinationStation()
                        .getStation()
                        .getName()
        );

        response.setUserName(
                booking.getUser()
                        .getFullName()
        );

        response.setTrainClassName(
                booking.getTrainClass()
                        .getTrainClass()
                        .getClassName()
        );

        response.setNumberOfSeats(
                booking.getNumberOfSeats()
        );

        response.setJourneyDate(
                booking.getJourneyDate()
        );

        response.setBookingStatus(
                booking.getBookingStatus()
        );

        response.setPaymentStatus(
                booking.getPaymentStatus()
        );

        response.setBookingDate(
                booking.getBookingDate()
        );

        response.setTotalFare(
                booking.getTotalFare()
        );

        response.setPnr(
                booking.getPnr()
        );

        List<BookingPassenger> bookingPassengers =
                new ArrayList<>();

        if (booking.getPassengers() != null) {

            for (BookingPassenger passenger :
                    booking.getPassengers()) {

                if (passenger.getName() != null) {

                    response.getPassangerDetails().put(
                            passenger.getName(),
                            passenger.getAge()
                    );

                    System.out.println(
                            "Called"
                    );
                }

                bookingPassengers.add(
                        passenger
                );
            }
        }

        bookingsRepo.save(booking);

        // =========================================================
        // TICKET GENERATE
        // =========================================================

        Ticket ticket = null;

        try {

                System.out.print(booking.getPnr());
            // add aspect
            ticket =
                    ticketService.syncTicket(
                            booking.getId()
                    );

            System.out.println(
                    ticket.getPnr()
                            + " Ticket PNR"
            );

        } catch (Exception e) {

            System.out.println(
                    "ticket did not generated"
            );

            // retry logic
            // TODO: handle exception
        }

        // =========================================================
        // NOTIFICATION CREATED AND SEND
        // =========================================================

        NotificationDto notification =
                new NotificationDto();

        notification.setType(
                NotificationType.EMAIL
        );

        notification.setTo(
                booking.getUser().getEmail()
        );

        notification.setUserName(
                booking.getUser().getFullName()
        );

        notification.setText(
                "Your booking has been created. "
                        + "PNR: "
                        + booking.getPnr()
        );

        notification.setPnr(
                booking.getPnr()
        );

        notification.setTicket(
                ticket
        );

        Boolean ans =
                notificationService.sendNotification(
                        notification
                );

        if (!ans) {
            // retry
        }

        return response;
    }

    // =========================================================
    // LIST BOOKING -> LIST RESPONSE
    // =========================================================

    public List<BookingResponseDto> convertToResponseList(
            List<Bookings> bookings) {

        List<BookingResponseDto> responses =
                new ArrayList<>();

        if (bookings == null
                || bookings.isEmpty()) {

            return responses;
        }

        for (Bookings booking :
                bookings) {

            responses.add(
                    convertToResponse(
                            booking
                    )
            );
        }

        return responses;
    }

    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    @Transactional(rollbackFor = Exception.class)
    public boolean cancelBooking(Long bookingId) {

        Bookings booking =
                bookingsRepo.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found."
                                ));

        booking.setBookingStatus(
                BookingStatus.CANCELLED
        );

        for (BookingPassenger bp :
                booking.getPassengers()) {

            if (BookingStatus.BOOKED.equals(
                    bp.getPassangerBookingStatus())) {

                ScheduleTrainClassSeat seat =
                        bp.getSeat();

                // Cancel current confirmed passenger
                seat.setSeatbookingStatus(
                        SeatBookingStatus.EMPTY
                );

                bp.setPassangerBookingStatus(
                        BookingStatus.CANCELLED
                );

                // Find next waitlisted passenger
                Optional<SeatWaitlist> waitlist =
                        waitlistService.findNextPassenger(
                                seat
                        );

                if (waitlist.isPresent()) {

                    SeatWaitlist next =
                            waitlist.get();

                    BookingPassenger bp2 =
                            next.getPassenger();

                    // Promote waitlisted passenger
                    seat.setSeatbookingStatus(
                            SeatBookingStatus.BOOKED
                    );

                    seat.setSeatStatus(
                            SeatStatus.UNLOCKED
                    );

                    bp2.setSeat(
                            seat
                    );

                    bp2.setPassangerBookingStatus(
                            BookingStatus.BOOKED
                    );

                    try {

                        waitlistService.removeFromWaitlist(
                                next.getId()
                        );

                    } catch (Exception e) {
                        // keep existing behavior
                    }

                } else {

                    // Nobody waiting
                    seat.setSeatbookingStatus(
                            SeatBookingStatus.EMPTY
                    );

                    seat.setSeatStatus(
                            SeatStatus.UNLOCKED
                    );
                }

            } else if (BookingStatus.WAITLISTED.equals(
                    bp.getPassangerBookingStatus())) {

                // Just cancel the waitlisted passenger
                bp.setPassangerBookingStatus(
                        BookingStatus.CANCELLED
                );

                /*
                 * Do NOT change seat status here.
                 *
                 * Waitlisted passenger does not own
                 * a physical seat yet.
                 */
            }
        }

        bookingsRepo.save(
                booking
        );

        // check if cancellation time > schedule train time
        // then deny refund
        refundService.processRefund(
                bookingId
        );

        return true;
    }
}