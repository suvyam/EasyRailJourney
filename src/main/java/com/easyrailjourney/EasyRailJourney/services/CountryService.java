package com.easyrailjourney.EasyRailJourney.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos.CountryCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos.CountryDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos.CountryUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.models.Country;
import com.easyrailjourney.EasyRailJourney.repository.CountryRepo;

@Service
public class CountryService {

    private final CountryRepo countryRepo;

    public CountryService(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }



    @Transactional
    public Country createCountry(CountryCreateReqDto dto) {

        Country country = new Country();
        country.setName(dto.getName());

        return countryRepo.save(country);
    }

  
    public List<Country> getAllCountries() {
        return  countryRepo.findAll();
    }


    public Country getCountryById(Long id) {

        return countryRepo.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

   
    @Transactional
    public Country updateCountry(CountryUpdateReqDto dto) {

        Country country = countryRepo.findByIdAndIsDeleted(dto.getId(), false)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        country.setName(dto.getName());

        return countryRepo.save(country);
    }

   
    @Transactional
    public boolean deleteCountry(CountryDeleteReqDto dto) {

        Country country = countryRepo.findByIdAndIsDeleted(dto.getId(), false)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        country.setIsDeleted(true);

        countryRepo.save(country);

        return true;
    }

    @Transactional
    public boolean deleteCountryPermanently(Long id) {

        Country country = countryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        countryRepo.delete(country);

        return true;
    }
}