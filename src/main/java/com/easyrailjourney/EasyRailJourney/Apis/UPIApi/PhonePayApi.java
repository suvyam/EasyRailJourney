package com.easyrailjourney.EasyRailJourney.Apis.UPIApi;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.adaptors.PaymentAdaptors.PayTmAdaptor;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;

@Component
public class PhonePayApi extends  PayTmAdaptor {

      public boolean isSupported(PaymentMethod method){
        return method == PaymentMethod.PHONEPE;
      }
    
}
