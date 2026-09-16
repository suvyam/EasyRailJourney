package com.easyrailjourney.EasyRailJourney.services;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.NotificationStratergy;

@Service
public class NotificationService {

    // learn email dependency and congurure with what and why 
    // For example:
    // MAIL_USERNAME=your-email@gmail.com
    // MAIL_PASSWORD=your-app-password
    // For Gmail, use an App Password, not your normal Google account password.
    // use notification type for is match
    private final List<NotificationStratergy> notificationStrategies;

    NotificationService(List<NotificationStratergy> notificationStrategies){
        this.notificationStrategies = notificationStrategies;
    }

    

    @PreAuthorize ("hasAuthority('SEND_NOTIFICATION')")
    public boolean sendNotification( NotificationDto request) {


        System.out.println("TYPE = " + request.getType());

        notificationStrategies.forEach(
                strategy -> System.out.println(
                        "STRATEGY = " + strategy.getClass().getName()
                )
        );

            NotificationStratergy strategy =
                notificationStrategies.stream()
                        .filter(s -> s.isMatch(request))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No notification strategy found for "
                                                + request.getType()
                                ));

        return strategy.send(request);
    }

    
}
