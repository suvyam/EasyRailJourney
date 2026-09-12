package com.easyrailjourney.EasyRailJourney.StratergiesImpl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;
import com.easyrailjourney.EasyRailJourney.Stratergies.PaymentStrategy;
import com.easyrailjourney.EasyRailJourney.adaptors.PaymentAdaptors.PayTmAdaptor;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMode;

@Component
public class PaytmPaymentStrategy implements PaymentStrategy {

    private final PayTmAdaptor paytmAdapter;

    public PaytmPaymentStrategy(@Qualifier("payTmAdaptor")  PayTmAdaptor paytmAdapter) {
    this.paytmAdapter = paytmAdapter;
    }

    @Override
    public boolean isSupported(
            PaymentMode mode,
            PaymentMethod method) {

        return method == PaymentMethod.PAYTM;
    }



    @Override
    public PaymentStratergyResp pay(PaymentCreateReqDto request) {

        return paytmAdapter.pay(request);
    }
}