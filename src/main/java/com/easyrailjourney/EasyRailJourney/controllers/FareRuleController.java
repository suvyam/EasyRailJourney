package com.easyrailjourney.EasyRailJourney.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import  org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleRespDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.FareRuleUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.Fares.GeneralFareRuleRespDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.services.FareService.FareRule.FareRuleService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/fare-rule")
public class FareRuleController {

    private final FareRuleService fareRuleService;

    public FareRuleController(FareRuleService fareRuleService) {
        this.fareRuleService = fareRuleService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_FARE_RULE')")
    public ResponseEntity<GeneralFareRuleRespDto> createFareRule(
           @Valid  @RequestBody FareRuleCreateReqDto reqDto) {

        GeneralFareRuleRespDto respDto =
                new GeneralFareRuleRespDto();

        try {

            FareRuleRespDto fareRule =
                    fareRuleService.createFareRule(reqDto);

            respDto.setFareRule(fareRule);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // GET ALL
    @GetMapping
    @PreAuthorize("hasAuthority('READ_FARE_RULE')")
    public ResponseEntity<GeneralFareRuleRespDto> getAllFareRules() {

        GeneralFareRuleRespDto respDto =
                new GeneralFareRuleRespDto();

        try {

            List<FareRuleRespDto> fareRules =
                    fareRuleService.getAllFareRules();

            respDto.setFareRules(fareRules);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // SEARCH
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_FARE_RULE')")
    public ResponseEntity<GeneralFareRuleRespDto> searchFareRule(
            @RequestParam(required = false) Long trainId,
            @RequestParam(required = false) Long stateId,
            Long classType) {

        GeneralFareRuleRespDto respDto =
                new GeneralFareRuleRespDto();

        if (trainId == null && stateId == null && classType==null) {
            return ResponseEntity.notFound().build();
        }

        try {

            List<FareRuleRespDto> fareRules =
                    fareRuleService.searchFareRule(
                         trainId,
                         stateId,
                         classType
                    );

            respDto.setFareRules(fareRules);
            respDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {

            respDto.setResponseStatus(ResponseStatus.FAILURE);
            respDto.setMessage(e.getMessage());
        }

        return ResponseEntity.ok().body(respDto);
    }

    // UPDATE
    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_FARE_RULE')")
    public ResponseEntity<GeneralFareRuleRespDto> updateFareRule(
        @Valid  @RequestBody FareRuleUpdateReqDto reqDto) {

        GeneralFareRuleRespDto respDto =
                new GeneralFareRuleRespDto();

        try {

            boolean ans =
                    fareRuleService.updateFareRule(reqDto);

            if (ans) {

                respDto.setMessage("Successfully Updated");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);

            } else {

                respDto.setMessage("Not Updated");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage("Failed Updated");
            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.badRequest().body(respDto);
    }

    // SOFT DELETE
    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_FARE_RULE')")
    public ResponseEntity<GeneralFareRuleRespDto> deleteFareRule(
        @Valid  @RequestBody FareRuleDeleteReqDto reqDto) {

        GeneralFareRuleRespDto respDto =
                new GeneralFareRuleRespDto();

        try {

            boolean ans =
                    fareRuleService.deleteFareRule(reqDto);

            if (ans) {

                respDto.setMessage("Deleted Successfully");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);

            } else {

                respDto.setMessage("Not Deleted");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }

    // PERMANENT DELETE
    @DeleteMapping("/delete-permanently")
    @PreAuthorize("hasAuthority('DELETE_FARE_RULE')")
    public ResponseEntity<GeneralFareRuleRespDto>
            deleteFareRulePermanently(
                @Valid  @RequestBody FareRuleDeleteReqDto reqDto) {

        GeneralFareRuleRespDto respDto =
                new GeneralFareRuleRespDto();

        try {

            boolean ans =
                    fareRuleService.deleteFareRulePermanently(
                            reqDto
                    );

            if (ans) {

                respDto.setMessage("Deleted Successfully");
                respDto.setResponseStatus(ResponseStatus.SUCCESS);

            } else {

                respDto.setMessage("Not Deleted");
                respDto.setResponseStatus(ResponseStatus.FAILURE);
            }

        } catch (Exception e) {

            respDto.setMessage(
                    "Delete Interruption " + e.getMessage()
            );

            respDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return ResponseEntity.ok().body(respDto);
    }
}
