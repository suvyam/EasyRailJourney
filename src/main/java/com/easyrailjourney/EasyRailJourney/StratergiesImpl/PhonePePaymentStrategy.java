package com.easyrailjourney.EasyRailJourney.StratergiesImpl;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;
import com.easyrailjourney.EasyRailJourney.Stratergies.PaymentStrategy;
import com.easyrailjourney.EasyRailJourney.adaptors.PaymentAdaptors.*;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMode;


@Component
public class PhonePePaymentStrategy implements PaymentStrategy {

    private final PhonePeAdapter phonePeAdapter;

    public PhonePePaymentStrategy(PhonePeAdapter phonePeAdapter) {
        this.phonePeAdapter = phonePeAdapter;
    }

    @Override
    public boolean isSupported(
            PaymentMode mode,
            PaymentMethod method) {

        return method == PaymentMethod.PHONEPE;
    }

    @Override
    public PaymentStratergyResp pay(PaymentCreateReqDto request) {

        return phonePeAdapter.pay(request);
    }
}