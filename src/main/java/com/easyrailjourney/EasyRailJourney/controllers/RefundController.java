package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundRespDto;
import com.easyrailjourney.EasyRailJourney.services.RefundServices.RefundService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @GetMapping("/calculate/{bookingId}")
    @PreAuthorize("hasAuthority('CALCULATE_REFUND')")
    
    public ResponseEntity<RefundCalculationRespDto> calculateRefund(
            @Valid  @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                refundService.calculateRefund(bookingId)
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_REFUND')")
    public ResponseEntity<List<RefundRespDto>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_REFUND')")
    public ResponseEntity<RefundRespDto> getRefundById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                refundService.getRefundById(id)
        );
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAuthority('READ_REFUND')")
    public ResponseEntity<RefundRespDto> getRefundByBookingId(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                refundService.getRefundByBookingId(bookingId)
        );
    }

    @PostMapping("/process/{bookingId}")
    public ResponseEntity<RefundRespDto> processRefund(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                refundService.processRefund(bookingId)
        );
    }
}