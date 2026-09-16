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

import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.CityCreateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.CityDeleteReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.CityUpdateReqDto;
import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.GeneralCityRespDto;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.services.CityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
         @PreAuthorize ("hasAuthority('ADD_CITY')")
    public ResponseEntity<GeneralCityRespDto> createCity(
            @Valid @RequestBody CityCreateReqDto dto) {

        GeneralCityRespDto response = new GeneralCityRespDto();

        try {
            response.setCity(cityService.createCity(dto));
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize ("hasAuthority('READ_CITY')")
    public ResponseEntity<GeneralCityRespDto> getAllCities() {

        GeneralCityRespDto response = new GeneralCityRespDto();

        try {
            response.setCities(cityService.getAllCities());
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize ("hasAuthority('READ_CITY')")
    public ResponseEntity<GeneralCityRespDto> getCity(
            @PathVariable Long id) {

        GeneralCityRespDto response = new GeneralCityRespDto();

        try {
            response.setCity(cityService.getCityById(id));
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize ("hasAuthority('UPDATE_CITY')")
    public ResponseEntity<GeneralCityRespDto> updateCity(
            @Valid @RequestBody CityUpdateReqDto dto) {

        GeneralCityRespDto response = new GeneralCityRespDto();

        try {
            response.setCity(cityService.updateCity(dto));
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize ("hasAuthority('DELETE_CITY')")
    public ResponseEntity<GeneralCityRespDto> deleteCity(
            @Valid @RequestBody CityDeleteReqDto dto) {

        GeneralCityRespDto response = new GeneralCityRespDto();

        try {
            cityService.deleteCity(dto);
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("City deleted successfully");
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-permanently/{id}")
    @PreAuthorize ("hasAuthority('DELETE_CITY')")
    public ResponseEntity<GeneralCityRespDto> deletePermanently(
            @PathVariable Long id) {

        GeneralCityRespDto response = new GeneralCityRespDto();

        try {
            cityService.deleteCityPermanently(id);
            response.setResponseStatus(ResponseStatus.SUCCESS);
            response.setMessage("City permanently deleted");
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
            response.setMessage(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}