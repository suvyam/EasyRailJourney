package com.easyrailjourney.EasyRailJourney.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos.StateCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos.StateDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.StatesDtos.StateUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.Country;
import com.easyrailjourney.EasyRailJourney.models.States;
import com.easyrailjourney.EasyRailJourney.repository.CountryRepo;
import com.easyrailjourney.EasyRailJourney.repository.StateRepo;

@Service
public class StateService {

    private final StateRepo stateRepo;
    private final CountryRepo countryRepo;

    public StateService(StateRepo stateRepo, CountryRepo countryRepo) {
        this.stateRepo = stateRepo;
        this.countryRepo = countryRepo;
    }

    public States createState(StateCreateReqDto dto) {

        Country country = countryRepo.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        States state = new States();

        state.setName(dto.getName());
        state.setCountry(country);

        return stateRepo.save(state);
    }

    public List<States> getAllStates() {
        return stateRepo.findAll();
    }

    public States getStateById(Long id) {

        return stateRepo.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new RuntimeException("State not found"));
    }

    public States updateState(StateUpdateReqDto dto) {

        States state = stateRepo.findByIdAndIsDeleted(dto.getId(), false)
                .orElseThrow(() -> new RuntimeException("State not found"));

        Country country = countryRepo.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        state.setName(dto.getName());
        state.setCountry(country);

        return stateRepo.save(state);
    }

    public boolean deleteState(StateDeleteReqDto dto) {

        States state = stateRepo.findByIdAndIsDeleted(dto.getId(), false)
                .orElseThrow(() -> new RuntimeException("State not found"));

        state.setIsDeleted(true);

        stateRepo.save(state);

        return true;
    }

    public boolean deleteStatePermanently(Long id) {

        States state = stateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found"));

        stateRepo.delete(state);

        return true;
    }
}