package com.easyrailjourney.EasyRailJourney.services.trainServices;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.CoachType;
import com.easyrailjourney.EasyRailJourney.repository.trainRepos.CoachTypeRepo;

@Service
public class CoachTypeService {

    private final CoachTypeRepo coachTypeRepository;

    public CoachTypeService(CoachTypeRepo coachTypeRepository) {
        this.coachTypeRepository = coachTypeRepository;
    }

    public CoachType createCoachType(CoachType coachType) {
        return coachTypeRepository.save(coachType);
    }

    public List<CoachType> getAllCoachTypes() {
        return coachTypeRepository.findAll();
    }

    public CoachType getCoachTypeById(Long id) {

        return coachTypeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "CoachType not found with id: " + id
                        ));
    }

    public CoachType updateCoachType(CoachType coachType) {

        CoachType existing =
                coachTypeRepository.findById(coachType.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "CoachType not found"
                        ));

        existing.setTypeName(coachType.getTypeName());

        return coachTypeRepository.save(existing);
    }

    public void deleteCoachType(Long id) {

        CoachType existing =
                coachTypeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "CoachType not found"
                        ));

        coachTypeRepository.delete(existing);
    }
}