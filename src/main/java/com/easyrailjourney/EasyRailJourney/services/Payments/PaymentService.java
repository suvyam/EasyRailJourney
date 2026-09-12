package com.easyrailjourney.EasyRailJourney.services.Payments;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.PaymentDtos.PaymentStratergyResp;
import com.easyrailjourney.EasyRailJourney.Stratergies.PaymentStrategy;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMethod;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentMode;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentStatus;
import com.easyrailjourney.EasyRailJourney.enums.Payments.PaymentType;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.payments.Payment;
import com.easyrailjourney.EasyRailJourney.models.users.Users;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;
import com.easyrailjourney.EasyRailJourney.repository.PaymentRepo;
import com.easyrailjourney.EasyRailJourney.repository.UserRepo.UserRepo;



@Service
public class PaymentService {

    private final BookingsRepo bookingsRepo;
    private final UserRepo userRepo;
    private final PaymentRepo paymentRepo;
    private final List<PaymentStrategy > paymentStrategies;


    public PaymentService(BookingsRepo bookingsRepo,UserRepo userRepo,PaymentRepo paymentRepo,List<PaymentStrategy > paymentStrategies) {
    this.bookingsRepo = bookingsRepo;
    this.userRepo = userRepo;
    this.paymentRepo = paymentRepo;
    this.paymentStrategies = paymentStrategies;
    }

    public PaymentStratergyResp makePayment(
            PaymentCreateReqDto request) {

        PaymentStrategy strategy =
                paymentStrategies.stream()
                        .filter(s -> s.isSupported(
                                PaymentMode.valueOf(request.getPaymentMode().toUpperCase()),
                                PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase())
                        ))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment method not supported"
                                ));

        return strategy.pay(request);
    }

    public Payment createPayment(PaymentCreateReqDto reqDto) throws Exception{


    //Validate Bookimg

    if(reqDto.getBookingId()==null)throw new Exception("Booking id missing");

    Optional<Bookings> bookingOptional = bookingsRepo.findById(reqDto.getBookingId());

    if(bookingOptional.isEmpty()) throw new Exception("Booking not found");

    //Validate User

    if(reqDto.getUserId()==null) throw new Exception("User id missing");
    Optional<Users> userOptional = userRepo.findById(reqDto.getUserId());
    if(userOptional.isEmpty()) throw new Exception("User not found");

    // Validate amount

    if(reqDto.getAmount()==null || reqDto.getAmount()!=bookingOptional.get().getTotalFare())throw new Exception("Amount missing or amount not match actual fare");

    // Validate idempotencyKey;

    if(reqDto.getIdempotencyKey()==null)throw new Exception("idempotencyKey missing");

    Optional<Payment> paymentOptional= paymentRepo.findByIdempotencyKey(reqDto.getIdempotencyKey());

    if(paymentOptional.isPresent())return paymentOptional.get(); // if failed thne  recall payment

    // Validate PaymentMethod AND Mode

    if(reqDto.getPaymentMethod()==null)throw new Exception("PaymentMethod missing");

    Payment payment = new Payment();

    PaymentMethod paymentMethod = PaymentMethod.valueOf(reqDto.getPaymentMethod().toUpperCase());

    if(!PaymentMethod.PAYTM.equals(paymentMethod) && !PaymentMethod.PHONEPE.equals(paymentMethod))throw new Exception("Payment Method not supported");


    PaymentMode paymentMode = PaymentMode.valueOf(reqDto.getPaymentMode().toUpperCase());
    if(!PaymentMode.CREDITCARD.equals(paymentMode) && !PaymentMode.UPI.equals(paymentMode))throw new Exception("Payment Mode not supported");


  if (reqDto.getPaymentType() != null) {
    payment.setPaymentType(reqDto.getPaymentType());
} else {
    payment.setPaymentType(PaymentType.BOOKING_PAYMENT);
}
    payment.setBookingId(bookingOptional.get().getId());
    payment.setUserId(userOptional.get().getId());
    payment.setAmount(reqDto.getAmount());
    payment.setIdempotencyKey(reqDto.getIdempotencyKey());
    payment.setPaymentMethod(paymentMethod);
    payment.setStatus(PaymentStatus.CREATED);

    paymentRepo.save(payment);


    // check method exits using strng
    PaymentStrategy paymentStatergy = paymentStrategies.stream()
    .filter(strategy ->
            strategy.isSupported(
                      paymentMode,
                    paymentMethod
            )
    )
    .findFirst()
    .orElseThrow(() -> new Exception("Payment method/mode not supported"));
   

    //-------update ----------------

    PaymentStratergyResp resp = paymentStatergy.pay(reqDto);

    payment.setFailureReason(resp.getFailReason());
    payment.setStatus(resp.getStatus());
    payment.setGatewayTransactionId(resp.getTransactionId());


    return payment;
    


    }

 
}
