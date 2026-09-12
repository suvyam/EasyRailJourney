package com.easyrailjourney.EasyRailJourney.Stratergies;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMode;

public interface PaymentStrategy {

   boolean isSupported(PaymentMode mode, PaymentMethod method);

   PaymentStratergyResp pay(PaymentCreateReqDto request);
}