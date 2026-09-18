package com.easyrailjourney.EasyRailJourney.StratergiesImpl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.RefundCalculationStrategy;
import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundCalculationType;
import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStrategyType;
import com.easyrailjourney.EasyRailJourney.models.RefundRule;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.ScheduleTrainStation;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.repository.RefundRuleRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.ScheduleTrainStationRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeneralRefundCalculationStrategy
        implements RefundCalculationStrategy {

    private final RefundRuleRepo refundRuleRepo;

    private final ScheduleTrainStationRepo scheduleTrainStationRepo;

    @Override
    public RefundStrategyType getStrategyType() {
        return RefundStrategyType.GENERAL;
    }

    @Override
    public RefundCalculationRespDto calculate(Bookings booking) {

        Double paidAmount = booking.getTotalFare();

        Date cancellationTime = new Date();

        /*
         * Get the ScheduleTrainStation for the booked
         * source station.
         */

        System.out.println(
                "Schedule ID = " +
                booking.getScheduleTrain().getId()
            );
            
            System.out.println(
                "Source Station ID = " +
                booking.getSourceStation().getId()
            );
       

            ScheduleTrainStation sourceScheduleStation = booking.getSourceStation();
                        

        /*
         * Departure time of the station from which
         * passenger actually booked the journey.
         */
        Date sourceDepartureTime =
                sourceScheduleStation.getDepartureTime();

        /*
         * If cancellation happens at or after the
         * source station departure time:
         *
         * Refund = 0
         * Deduction = full paid amount
         */
        if (!cancellationTime.before(sourceDepartureTime)) {

            RefundCalculationRespDto response =
                    new RefundCalculationRespDto();

            response.setBookingId(booking.getId());
            response.setPaidAmount(paidAmount);
            response.setRefundAmount(0.0);
            response.setDeduction(paidAmount);
            response.setStrategy(
                    RefundStrategyType.GENERAL.name()
            );
            response.setMessage(
                    "Cancellation happened after departure time "
                    + "of the booked source station. No refund applicable."
            );

            return response;
        }

        /*
         * Cancellation happened before source station departure.
         * Now calculate how many hours are remaining.
         */
        long hoursBeforeSourceDeparture =
                (sourceDepartureTime.getTime()
                        - cancellationTime.getTime())
                        / (1000 * 60 * 60);

        /*
         * Find the refund rule applicable for this train
         * and remaining cancellation time.
         */
        RefundRule rule =
                findApplicableRule(
                        booking,
                        hoursBeforeSourceDeparture
                );

        /*
         * Calculate refund according to the rule.
         */
        Double refundAmount =
                calculateRefund(
                        paidAmount,
                        rule
                );

        Double deduction =
                paidAmount - refundAmount;

        RefundCalculationRespDto response =
                new RefundCalculationRespDto();

        response.setBookingId(booking.getId());
        response.setPaidAmount(paidAmount);
        response.setRefundAmount(refundAmount);
        response.setDeduction(deduction);
        response.setStrategy(
                RefundStrategyType.GENERAL.name()
        );
        response.setMessage(
                "Refund calculated successfully."
        );

        return response;
    }

    private RefundRule findApplicableRule(
            Bookings booking,
            long hoursBeforeSourceDeparture) {

        Train train =
                booking.getScheduleTrain().getTrain();

        List<RefundRule> rules =
                refundRuleRepo.findApplicableRules(
                        train.getId(),
                        hoursBeforeSourceDeparture
                );

        if (rules.isEmpty()) {
            throw new RuntimeException(
                    "No applicable refund rule found"
            );
        }

        return rules.get(0);
    }

    private Double calculateRefund(
            Double paidAmount,
            RefundRule rule) {

        if (rule.getCalculationType()
                == RefundCalculationType.FIXED) {

            return rule.getValue();
        }

        if (rule.getCalculationType()
                == RefundCalculationType.PERCENTAGE) {

            return (paidAmount * rule.getValue()) / 100;
        }

        throw new RuntimeException(
                "Unsupported refund calculation type"
        );
    }
}