package com.easyrailjourney.EasyRailJourney.StratergiesImpl.NotificationImpl;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.NotificationStratergy;
import com.easyrailjourney.EasyRailJourney.enums.NotificationType;
import com.easyrailjourney.EasyRailJourney.models.Ticket;
import com.easyrailjourney.EasyRailJourney.models.TicketPassenger;
import com.easyrailjourney.EasyRailJourney.models.bookings.Bookings;
import com.easyrailjourney.EasyRailJourney.repository.BookingsRepo.BookingsRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailNotificationImpl
        implements NotificationStratergy {



    private final JavaMailSender javaMailSender;
    private final BookingsRepo bookingsRepo;


    public EmailNotificationImpl(JavaMailSender javaMailSender, BookingsRepo bookingsRepo){
        this.bookingsRepo = bookingsRepo;
        this.javaMailSender = javaMailSender;
    }



    @Override
    public boolean isMatch(NotificationDto notificationDto) {

         if(NotificationType.EMAIL.equals(
    
                notificationDto.getType()
        )){
            System.err.println("EMAIL MATCH");
            return true;
        }

        return false;
    }

    @Override
    public boolean send(NotificationDto notificationDto) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            

            Ticket ticket = notificationDto.getTicket();
            String pnr = notificationDto.getPnr();
            Bookings bookings = bookingsRepo.findByPnr(pnr);
            

            StringBuilder passengerDetails = new StringBuilder();

            for (TicketPassenger tp : ticket.getTicketPassenger()) {

                passengerDetails.append(
                    "<hr>" +
                    "<p><b>Passenger:</b> " + tp.getName() + "</p>" +
                    "<p><b>Age:</b> " + tp.getAge() + "</p>" +
                    "<p><b>Train:</b> " + tp.getTrainName() +
                    " (" + tp.getTrainNumber() + ")</p>" +
                    "<p><b>Class:</b> " + bookings.getTrainClass().getClass().getName() + "</p>" +
                    "<p><b>Coach:</b> " + tp.getCoachNumber() + "</p>" +
                    "<p><b>Seat:</b> " + tp.getSeatNumber() + "</p>" +
                    "<p><b>From:</b> " + tp.getDepartureStationName() + "</p>" +
                    "<p><b>To:</b> " + tp.getArrivalStationName() + "</p>" +
                    "<p><b>Departure:</b> " + tp.getDepartureTime() + "</p>" +
                    "<p><b>Arrival:</b> " + tp.getArrivalTime() + "</p>" +
                    "<p><b>Status:</b> " + tp.getBookingStatus() + "</p>"
                );
            }
            
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);
            
            helper.setTo(notificationDto.getTo());
            
            helper.setSubject(
                    "EasyRailJourney - Booking Confirmation"
            );
            
            helper.setText(
                "<h2>Booking Created</h2>" +
                "<p>Your booking has been successfully created.</p>" +
                "<p><b>PNR:</b> " + pnr + "</p>" +
                passengerDetails,
                true
            );

    javaMailSender.send(message);

    return true;

} catch (MessagingException | MailException e) {

    System.err.println(
            "Failed to send email: " + e.getMessage()
    );

    return false;
}
    }
}