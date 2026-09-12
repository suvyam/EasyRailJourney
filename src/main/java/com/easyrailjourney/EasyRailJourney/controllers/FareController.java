package com.easyrailjourney.EasyRailJourney.controllers;




import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareCalculationRespDto;
import com.easyrailjourney.EasyRailJourney.services.FareService.FareCalculationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/fare")
@RequiredArgsConstructor
public class FareController {

    private final FareCalculationService fareCalculationService;

    @PostMapping("/calculate")
    public ResponseEntity<FareCalculationRespDto> calculateFare(
            @Valid @RequestBody FareCalculationReqDto request
    ) {

        return ResponseEntity.ok(
                fareCalculationService.calculate(request)
        );
    }
}