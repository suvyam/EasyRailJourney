package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundRespDto;
import com.easyrailjourney.EasyRailJourney.services.RefundServices.RefundService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @GetMapping("/calculate/{bookingId}")
    public ResponseEntity<RefundCalculationRespDto> calculateRefund(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                refundService.calculateRefund(bookingId)
        );
    }

    @GetMapping
    public ResponseEntity<List<RefundRespDto>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundRespDto> getRefundById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                refundService.getRefundById(id)
        );
    }

    @GetMapping("/booking/{bookingId}")
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