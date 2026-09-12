package com.easyrailjourney.EasyRailJourney.StratergiesImpl.NotificationImpl;
import org.springframework.stereotype.Component;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.Stratergies.NotificationStratergy;
import com.easyrailjourney.EasyRailJourney.enums.NotificationType;

@Component
public class WhatsAppNotificationImpl implements  NotificationStratergy {

    @Override
    public boolean isMatch(NotificationDto notificationDto) {

         if(NotificationType.WHATSAPP.equals(
                notificationDto.getType()
        )){
            System.err.println("WHATS APP MATCH");
            return true;
        }

        return false;
    }

    @Override
    public boolean send(NotificationDto name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'send'");
    }

    
    
}
