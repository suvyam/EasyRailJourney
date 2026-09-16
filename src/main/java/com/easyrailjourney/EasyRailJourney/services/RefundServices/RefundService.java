package com.easyrailjourney.EasyRailJourney.services.RefundServices;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundRespDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.RefundCalculationStrategy;
import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStatus;
import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStrategyType;
import com.easyrailjourney.EasyRailJourney.models.Refund;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.RefundRepo;

import jakarta.transaction.Transactional;



@Service
public class RefundService {

        private final BookingsRepo bookingRepo;
        private final RefundRepo refundRepo;
        private final Map<RefundStrategyType, RefundCalculationStrategy> strategyMap;
    
        public RefundService(
                BookingsRepo bookingRepo,
                RefundRepo refundRepo,
                List<RefundCalculationStrategy> strategies) {
    
            this.bookingRepo = bookingRepo;
            this.refundRepo = refundRepo;
    
            this.strategyMap = strategies.stream()
                    .collect(Collectors.toMap(
                            RefundCalculationStrategy::getStrategyType,
                            Function.identity()
                    ));
        }

        
    public RefundCalculationRespDto calculateRefund(
            Long bookingId) {

        Bookings booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        Train train = booking.getScheduleTrain().getTrain();

        RefundStrategyType strategyType =
                train.getRefundStrategyType();

        RefundCalculationStrategy strategy =
                strategyMap.get(strategyType);

        if (strategy == null) {
            throw new RuntimeException(
                    "Refund strategy not configured"
            );
        }

        return strategy.calculate(booking);
    }

    public List<RefundRespDto> getAllRefunds() {

        return refundRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }


    public RefundRespDto getRefundById(Long id) {

        Refund refund = refundRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Refund not found")
                );

        return convertToDto(refund);
    }


    public RefundRespDto getRefundByBookingId(Long bookingId) {

        Refund refund = refundRepo.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Refund not found for booking"
                        )
                );

        return convertToDto(refund);
    }

    @Transactional
    public RefundRespDto processRefund(Long bookingId) {

        Bookings booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        if (refundRepo.existsByBooking_IdAndRefundStatus(bookingId,RefundStatus.SUCCESS)) {
            throw new RuntimeException(
                    "Refund already exists for this booking"
            );
        }

        RefundCalculationRespDto calculation =
                calculateRefund(bookingId);

        Refund refund = new Refund();

        refund.setBooking(booking);

        refund.setPaidAmount(
                calculation.getPaidAmount()
        );

        refund.setRefundAmount(
                calculation.getRefundAmount()
        );

        refund.setDeductionAmount(
                calculation.getDeduction()
        );

        refund.setRefundStatus(com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStatus.PROCESSED);

        refund.setRefundDate(
               new Date()
        );

        refund.setReason(
                "Booking cancellation"
        );

        

        Refund savedRefund = refundRepo.save(refund);



        // Update according to your existing BookingStatus field
        // booking.setStatus(...);  
        //call payemt and then set refund status sucess and payment id

        refund.setRefundStatus(com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStatus.SUCCESS);

        bookingRepo.save(booking);

        return convertToDto(savedRefund);
    }

    private RefundRespDto convertToDto(Refund refund) {

        RefundRespDto response = new RefundRespDto();


        response.setId(refund.getId());

        if (refund.getBooking() != null) {
            response.setBookingId(
                    refund.getBooking().getId()
            );
        }

        response.setPaidAmount(
                refund.getPaidAmount()
        );

        response.setRefundAmount(
                refund.getRefundAmount()
        );

        response.setDeductionAmount(
                refund.getDeductionAmount()
        );

        // response.setRefundStatus(
        //         refund.getRefundStatus()
        // );

        response.setRefundDate(
                refund.getRefundDate()
        );

        response.setReason(
                refund.getReason()
        );

        return response;
    }
}