package com.easyrailjourney.EasyRailJourney.adaptors.PaymentAdaptors;

import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;

import jakarta.validation.Valid;


@Component
public class PhonePeAdapter implements PaymentGatewayAdapter {

    @Override
    public PaymentStratergyResp pay(@Valid  PaymentCreateReqDto request) {

        // Convert your request into PhonePe's request format

        // Call PhonePe API

        // Receive PhonePe response

        PaymentStratergyResp response = new PaymentStratergyResp();

        // response.setStatus(...);
        // response.setTransactionId(...);
        // response.setFailReason(...);

        return response;
    }
}