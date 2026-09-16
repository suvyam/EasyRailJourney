package com.easyrailjourney.EasyRailJourney.adaptors.PaymentAdaptors;

import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;

import jakarta.validation.Valid;

@Component 
public interface PaymentGatewayAdapter {

    PaymentStratergyResp pay(@Valid PaymentCreateReqDto request);
}
