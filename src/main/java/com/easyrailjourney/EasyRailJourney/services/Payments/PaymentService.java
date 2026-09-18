package com.easyrailjourney.EasyRailJourney.services.Payments;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;
import com.easyrailjourney.EasyRailJourney.Stratergies.PaymentStrategy;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMode;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentType;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.payments.Payment;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.PaymentRepo;
import com.easyrailjourney.EasyRailJourney.repository.UserRepo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    private final BookingsRepo bookingsRepo;
    private final UserRepo userRepo;
    private final PaymentRepo paymentRepo;
    private final List<PaymentStrategy> paymentStrategies;

    public PaymentService(
            BookingsRepo bookingsRepo,
            UserRepo userRepo,
            PaymentRepo paymentRepo,
            List<PaymentStrategy> paymentStrategies) {

        this.bookingsRepo = bookingsRepo;
        this.userRepo = userRepo;
        this.paymentRepo = paymentRepo;
        this.paymentStrategies = paymentStrategies;
    }

    // =====================================================
    // MAKE PAYMENT
    // =====================================================

    @PreAuthorize("isAuthenticated()")
    public PaymentStratergyResp makePayment(
            PaymentCreateReqDto request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Payment request cannot be null");
        }

        if (request.getPaymentMode() == null) {
            throw new IllegalArgumentException(
                    "Payment mode is required");
        }

        if (request.getPaymentMethod() == null) {
            throw new IllegalArgumentException(
                    "Payment method is required");
        }

        PaymentMode paymentMode =
                parsePaymentMode(request.getPaymentMode());

        PaymentMethod paymentMethod =
                parsePaymentMethod(request.getPaymentMethod());

        PaymentStrategy strategy =
                getPaymentStrategy(
                        paymentMode,
                        paymentMethod);

        return strategy.pay(request);
    }

    // =====================================================
    // CREATE PAYMENT
    // =====================================================

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public Payment createPayment(
            PaymentCreateReqDto reqDto) throws Exception {

        if (reqDto == null) {
            throw new Exception(
                    "Payment request cannot be null");
        }

        // =====================================================
        // 1. GET AUTHENTICATED USER
        // =====================================================

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new SecurityException(
                    "User is not authenticated");
        }

        String authenticatedUsername =
                authentication.getName();

        // =====================================================
        // 2. FIND AUTHENTICATED USER
        // =====================================================

        Optional<Users> userOptional =
                userRepo.findByProfileNameAndIsDeleted(authenticatedUsername, false);

        if (userOptional.isEmpty()) {
            throw new Exception(
                    "Authenticated user not found");
        }

        Users user = userOptional.get();

        // =====================================================
        // 3. VALIDATE BOOKING ID
        // =====================================================

        if (reqDto.getBookingId() == null) {
            throw new Exception(
                    "Booking id missing");
        }

        Optional<Bookings> bookingOptional =
                bookingsRepo.findById(
                        reqDto.getBookingId());

        if (bookingOptional.isEmpty()) {
            throw new Exception(
                    "Booking not found");
        }

        Bookings booking = bookingOptional.get();

        // =====================================================
        // 4. VERIFY BOOKING BELONGS TO AUTHENTICATED USER
        // =====================================================

        if (booking.getUser().getId() == null
                || !booking.getUser().getId()
                        .equals(user.getId())) {

            throw new SecurityException(
                    "You are not allowed to pay for this booking");
        }

        // =====================================================
        // 5. VALIDATE AMOUNT
        // =====================================================

        if (reqDto.getAmount() == null) {
            throw new Exception(
                    "Amount missing");
        }

        if (booking.getTotalFare() == null) {
            throw new Exception(
                    "Booking total fare is missing");
        }

        if (reqDto.getAmount()
                .compareTo(booking.getTotalFare()) != 0) {

            throw new Exception(
                    "Amount does not match actual booking fare");
        }

        // =====================================================
        // 6. VALIDATE IDEMPOTENCY KEY
        // =====================================================

        if (reqDto.getIdempotencyKey() == null
                || reqDto.getIdempotencyKey().isBlank()) {

            throw new Exception(
                    "Idempotency key missing");
        }

        Optional<Payment> paymentOptional =
                paymentRepo.findByIdempotencyKey(
                        reqDto.getIdempotencyKey());

        if (paymentOptional.isPresent()) {

            Payment existingPayment =
                    paymentOptional.get();

            // Extra security check
            if (!existingPayment.getUserId()
                    .equals(user.getId())) {

                throw new SecurityException(
                        "Invalid idempotency key");
            }

            return existingPayment;
        }

        // =====================================================
        // 7. VALIDATE PAYMENT METHOD / MODE
        // =====================================================

        if (reqDto.getPaymentMethod() == null) {
            throw new Exception(
                    "Payment method missing");
        }

        if (reqDto.getPaymentMode() == null) {
            throw new Exception(
                    "Payment mode missing");
        }

        PaymentMethod paymentMethod =
                parsePaymentMethod(
                        reqDto.getPaymentMethod());

        PaymentMode paymentMode =
                parsePaymentMode(
                        reqDto.getPaymentMode());

        // =====================================================
        // 8. CREATE PAYMENT ENTITY
        // =====================================================

        Payment payment = new Payment();

        payment.setBookingId(
                booking.getId());

        payment.setUserId(
                user.getId());

        payment.setAmount(
                reqDto.getAmount());

        payment.setIdempotencyKey(
                reqDto.getIdempotencyKey());

        payment.setPaymentMethod(
                paymentMethod);

        if (reqDto.getPaymentType() != null) {

            payment.setPaymentType(
                    reqDto.getPaymentType());

        } else {

            payment.setPaymentType(
                    PaymentType.BOOKING_PAYMENT);
        }

        payment.setStatus(
                PaymentStatus.CREATED);

                payment.setPaymentInitiatedAt(new Date());

        paymentRepo.save(payment);

        // =====================================================
        // 9. FIND PAYMENT STRATEGY
        // =====================================================

        PaymentStrategy paymentStrategy =
                getPaymentStrategy(
                        paymentMode,
                        paymentMethod);

        // =====================================================
        // 10. EXECUTE PAYMENT
        // =====================================================

        PaymentStratergyResp response =
                paymentStrategy.pay(reqDto);

        // =====================================================
        // 11. UPDATE PAYMENT
        // =====================================================

        payment.setFailureReason(
                response.getFailReason());

        payment.setStatus(
                response.getStatus());

        payment.setGatewayTransactionId(
                response.getTransactionId());

        return payment;
    }

    // =====================================================
    // FIND PAYMENT STRATEGY
    // =====================================================

    private PaymentStrategy getPaymentStrategy(
            PaymentMode paymentMode,
            PaymentMethod paymentMethod) {

        return paymentStrategies.stream()
                .filter(strategy ->
                        strategy.isSupported(
                                paymentMode,
                                paymentMethod))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment method/mode not supported"));
    }

    // =====================================================
    // PARSE PAYMENT METHOD
    // =====================================================

    private PaymentMethod parsePaymentMethod(
            String paymentMethod) {

        try {

            return PaymentMethod.valueOf(
                    paymentMethod.toUpperCase());

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid payment method: "
                            + paymentMethod);
        }
    }

    // =====================================================
    // PARSE PAYMENT MODE
    // =====================================================

    private PaymentMode parsePaymentMode(
            String paymentMode) {

        try {

            return PaymentMode.valueOf(
                    paymentMode.toUpperCase());

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid payment mode: "
                            + paymentMode);
        }
    }
}