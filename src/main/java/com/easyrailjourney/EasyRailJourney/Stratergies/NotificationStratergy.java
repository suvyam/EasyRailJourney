package com.easyrailjourney.EasyRailJourney.Stratergies;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;

public interface  NotificationStratergy {

    public boolean isMatch(NotificationDto notificationDto);

   boolean send(NotificationDto name);
    
}
