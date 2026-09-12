package com.easyrailjourney.EasyRailJourney.StratergiesImpl.NotificationImpl;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.NotificationStratergy;
import com.easyrailjourney.EasyRailJourney.enums.NotificationType;
import com.easyrailjourney.EasyRailJourney.models.Ticket;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailNotificationImpl
        implements NotificationStratergy {



    private final JavaMailSender javaMailSender;

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
                    "<p><b>Ticket:</b> " + ticket + "</p>",
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