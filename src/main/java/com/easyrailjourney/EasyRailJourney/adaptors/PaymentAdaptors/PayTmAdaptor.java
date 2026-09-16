package com.easyrailjourney.EasyRailJourney.adaptors.PaymentAdaptors;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;

import jakarta.validation.Valid;

@Component
public class PayTmAdaptor implements  PaymentGatewayAdapter {


  @Override
  public PaymentStratergyResp pay(@Valid  PaymentCreateReqDto request) {

      // Convert your request into Paytm's request format

      // Call Paytm API

      // Receive Paytm response

      // Convert Paytm response into your common response

      PaymentStratergyResp response = new PaymentStratergyResp();
      response.setStatus(PaymentStatus.SUCCESS);
      response.setTransactionId("1");

      // response.setStatus(...);
      // response.setTransactionId(...);
      // response.setFailReason(...);

      return response;
  }

    
}
