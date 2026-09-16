package com.easyrailjourney.EasyRailJourney.controllers;

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

import com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos.CountryCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos.CountryDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos.CountryUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos.GeneralCountryRespDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.services.CountryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
        @PreAuthorize ("hasAuthority('ADD_COUNTRY')")
    public ResponseEntity<GeneralCountryRespDto> createCountry(
            @Valid @RequestBody CountryCreateReqDto dto) {

        GeneralCountryRespDto response = new GeneralCountryRespDto();

        try {
            response.setCountry(countryService.createCountry(dto));
            response.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize ("hasAuthority('READ_COUNTRY')")
    public ResponseEntity<GeneralCountryRespDto> getAllCountries() {

        GeneralCountryRespDto response = new GeneralCountryRespDto();

        try {
            response.setCountries(countryService.getAllCountries());
            response.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize ("hasAuthority('READ_COUNTRY')")
    public ResponseEntity<GeneralCountryRespDto> getCountry(
            @PathVariable Long id) {

        GeneralCountryRespDto response = new GeneralCountryRespDto();

        try {
            response.setCountry(countryService.getCountryById(id));
            response.setResponseStatus(com.easyrailjourney.EasyRailJourney.enums.ResponseStatus.SUCCESS);

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize ("hasAuthority('UPDATE_COUNTRY')")
    public ResponseEntity<GeneralCountryRespDto> updateCountry(
            @Valid @RequestBody CountryUpdateReqDto dto) {

        GeneralCountryRespDto response = new GeneralCountryRespDto();

        try {
            response.setCountry(countryService.updateCountry(dto));
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Country updated successfully");

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize ("hasAuthority('DELETE_COUNTRY')")
    public ResponseEntity<GeneralCountryRespDto> deleteCountry(
            @Valid @RequestBody CountryDeleteReqDto dto) {

        GeneralCountryRespDto response = new GeneralCountryRespDto();

        try {
            countryService.deleteCountry(dto);
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Country deleted successfully");

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-permanently/{id}")
    @PreAuthorize ("hasAuthority('DELETE_COUNTRY')")
    public ResponseEntity<GeneralCountryRespDto> deletePermanently(
            @PathVariable Long id) {

        GeneralCountryRespDto response = new GeneralCountryRespDto();

        try {
            countryService.deleteCountryPermanently(id);
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("Country permanently deleted");

        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}