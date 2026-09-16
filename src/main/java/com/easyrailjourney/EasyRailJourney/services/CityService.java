package com.easyrailjourney.EasyRailJourney.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.CityCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.CityDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.CityUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.City;
import com.easyrailjourney.EasyRailJourney.models.States;
import com.easyrailjourney.EasyRailJourney.repository.CityRepo;
import com.easyrailjourney.EasyRailJourney.repository.StateRepo;



@Service
public class CityService {

    private final CityRepo cityRepo;
    private final StateRepo stateRepo;

    public CityService(CityRepo cityRepo, StateRepo stateRepo) {
        this.cityRepo = cityRepo;
        this.stateRepo = stateRepo;
    }

    @Transactional
    public City createCity(CityCreateReqDto dto) {

        States state = stateRepo.findById(dto.getStateId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        City city = new City();
        city.setName(dto.getName());
        city.setState(state);

        return cityRepo.save(city);
    }


   
    public List<City> getAllCities() {
        return  cityRepo.findAll();
    }


    public City getCityById(Long id) {
        return cityRepo.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new RuntimeException("City not found"));
    }


    @Transactional
    public City updateCity(CityUpdateReqDto dto) {

        City city = cityRepo.findByIdAndIsDeleted(dto.getId(), false)
                .orElseThrow(() -> new RuntimeException("City not found"));

        States state = stateRepo.findById(dto.getStateId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        city.setName(dto.getName());
        city.setState(state);

        return cityRepo.save(city);
    }


    @Transactional
    public boolean deleteCity(CityDeleteReqDto dto) {

        City city = cityRepo.findByIdAndIsDeleted(dto.getId(), false)
                .orElseThrow(() -> new RuntimeException("City not found"));

        city.setIsDeleted(true);
    

        cityRepo.save(city);

        return true;
    }


    @Transactional
    public boolean deleteCityPermanently(Long id) {

        City city = cityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));

        cityRepo.delete(city);

        return true;
    }
}