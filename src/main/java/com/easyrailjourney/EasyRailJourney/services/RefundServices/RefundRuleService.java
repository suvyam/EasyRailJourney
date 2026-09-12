package com.easyrailjourney.EasyRailJourney.services.RefundServices;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundRuleReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.RefundDtos.RefundRuleRespDto;
import com.easyrailjourney.EasyRailJourney.models.RefundRule;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Train;
import com.easyrailjourney.EasyRailJourney.repository.RefundRuleRepo;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.TrainRepo;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class RefundRuleService {

    private final RefundRuleRepo refundRuleRepo;
    private final TrainRepo trainRepo;

    public RefundRuleRespDto createRule(
            RefundRuleReqDto request) {

        Train train = trainRepo.findById(request.getTrainId())
                .orElseThrow(() ->
                        new RuntimeException("Train not found"));

        RefundRule rule = new RefundRule();

        rule.setTrain(train);
        rule.setCancellationHours(
                request.getCancellationHours()
        );
        rule.setCalculationType(
                request.getCalculationType()
        );
        rule.setValue(request.getValue());
        rule.setPriority(request.getPriority());
        rule.setActive(request.isActive());

        return convertToDto(
                refundRuleRepo.save(rule)
        );
    }

    public List<RefundRuleRespDto> getAllRules() {

        return refundRuleRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public RefundRuleRespDto getRuleById(Long id) {

        RefundRule rule = refundRuleRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Refund rule not found"));

        return convertToDto(rule);
    }

    public RefundRuleRespDto updateRule(
            Long id,
            RefundRuleReqDto request) {

        RefundRule rule = refundRuleRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Refund rule not found"));

        Train train = trainRepo.findById(request.getTrainId())
                .orElseThrow(() ->
                        new RuntimeException("Train not found"));

        rule.setTrain(train);
        rule.setCancellationHours(
                request.getCancellationHours()
        );
        rule.setCalculationType(
                request.getCalculationType()
        );
        rule.setValue(request.getValue());
        rule.setPriority(request.getPriority());
        rule.setActive(request.isActive());

        return convertToDto(
                refundRuleRepo.save(rule)
        );
    }

    public void deleteRule(Long id) {

        RefundRule rule = refundRuleRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Refund rule not found"));

        // Soft delete
        rule.setActive(false);

        refundRuleRepo.save(rule);
    }

    private RefundRuleRespDto convertToDto(
            RefundRule rule) {

        RefundRuleRespDto response =
                new RefundRuleRespDto();

        response.setId(rule.getId());

        if (rule.getTrain() != null) {
            response.setTrainId(
                    rule.getTrain().getId()
            );
        }

        response.setCancellationHours(
                rule.getCancellationHours()
        );

        response.setCalculationType(
                rule.getCalculationType()
        );

        response.setValue(
                rule.getValue()
        );

        response.setPriority(
                rule.getPriority()
        );

        response.setActive(
                rule.isActive()
        );

        return response;
    }
}