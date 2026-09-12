package com.easyrailjourney.EasyRailJourney.Dtos;

import com.easyrailjourney.EasyRailJourney.enums.NotificationType;
import com.easyrailjourney.EasyRailJourney.models.Ticket;

import lombok.Data;

@Data
public class NotificationDto {

    private NotificationType type;

    private String to;

    private String text;

    private String userName;

    private Ticket ticket;

    private String pnr;
}