package com.easyrailjourney.EasyRailJourney.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundRuleReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundRuleRespDto;
import com.easyrailjourney.EasyRailJourney.services.RefundServices.RefundRuleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/refund-rule")
@RequiredArgsConstructor
public class RefundRuleController {

    private final RefundRuleService refundRuleService;

    @PostMapping
      @PreAuthorize("hasAuthority('REFUND_RULE_CREATE')")
    public ResponseEntity<RefundRuleRespDto> createRule(
            @Valid @RequestBody RefundRuleReqDto request) {

                RefundRuleRespDto resp = new RefundRuleRespDto();

                try {
                   ResponseEntity.ok().body(refundRuleService.createRule(request));

                   
                } catch (Exception e) {
                        
                        resp.setMessage(e.getMessage());
                }

               return ResponseEntity.status(409).body(resp);
       
    }

    @GetMapping
    @PreAuthorize("hasAuthority('REFUND_RULE_READ')")
    public ResponseEntity<List<RefundRuleRespDto>> getAllRules() {

        return ResponseEntity.ok(
                refundRuleService.getAllRules()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('REFUND_RULE_READ')")
    public ResponseEntity<RefundRuleRespDto> getRuleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                refundRuleService.getRuleById(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('REFUND_RULE_UPDATE')")
    public ResponseEntity<RefundRuleRespDto> updateRule(
            @PathVariable Long id,
            @Valid @RequestBody RefundRuleReqDto request) {

        return ResponseEntity.ok(
                refundRuleService.updateRule(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('REFUND_RULE_DELETE')")
    public ResponseEntity<String> deleteRule(
            @PathVariable Long id) {

        refundRuleService.deleteRule(id);

        return ResponseEntity.ok(
                "Refund rule deactivated successfully"
        );
    }
}