package com.easyrailjourney.EasyRailJourney.Apis.UPIApi;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.adaptors.PaymentAdaptors.PayTmAdaptor;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;


@Component
public class PayTmApi extends  PayTmAdaptor {

    public boolean isSupported(PaymentMethod method){
        return method == PaymentMethod.PAYTM;
      }

//       TASK 1 → Paytm account + test credentials
// TASK 2 → PaytmConfig
// TASK 3 → PayTmApi
// TASK 4 → Initiate Transaction API
// TASK 5 → Return txnToken to frontend
// TASK 6 → Paytm callback
// TASK 7 → Verify transaction status
// TASK 8 → Update Booking + Seat
// TASK 9 → Payment failure → release seats
// TASK 10 → Refund on cancellation
    
}
