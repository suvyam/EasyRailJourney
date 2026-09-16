package com.easyrailjourney.EasyRailJourney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.services.WaitlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/waitlist")
@RequiredArgsConstructor
public class WaitlistController {

    private final WaitlistService waitlistService;

    @GetMapping("/seat/{seatId}/next")
    @PreAuthorize ("hasAuthority('READ_NEXT_PASSENGER')")
    public ResponseEntity<?> getNextPassenger(
            @PathVariable Long seatId) {

        // seat lookup will be required here
        
        return ResponseEntity.ok("Next passenger");
    }

    @PostMapping("/seat/{seatId}/allocate")
    @PreAuthorize("hasAuthority('ALLOCATE_WAITLIST_SEAT')")
    public ResponseEntity<String> allocateSeat(
            @PathVariable Long seatId) {

        // seat lookup will be required here

        return ResponseEntity.ok(
                "Waitlisted passenger allocated successfully"
        );
    }

    @DeleteMapping ("/{waitlistId}")
    @PreAuthorize("hasAuthority('REMOVE_FROM_WAITLIST')")
    public ResponseEntity<String> removeFromWaitlist(
            @PathVariable Long waitlistId) {

                try {
                    waitlistService.removeFromWaitlist(waitlistId);
                } catch (Exception e) {
                    return ResponseEntity.notFound().build();
                }


        return ResponseEntity.ok(
                "Passenger removed from waitlist"
        );
    }
}