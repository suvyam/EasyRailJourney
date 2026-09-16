package com.easyrailjourney.EasyRailJourney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.NotificationDto;
import com.easyrailjourney.EasyRailJourney.services.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/email")
    @PreAuthorize ("hasRole('ADMIN')")
    public ResponseEntity<String> sendEmail(
           @Valid  @RequestBody NotificationDto request) {

        boolean sent =
                notificationService.sendNotification(request);

        if (sent) {
            return ResponseEntity.ok(
                    "Email sent successfully"
            );
        }

        return ResponseEntity.internalServerError()
                .body("Email sending failed");
    }
}