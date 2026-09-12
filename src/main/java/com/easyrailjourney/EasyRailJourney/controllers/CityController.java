package com.easyrailjourney.EasyRailJourney.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyrailjourney.EasyRailJourney.Dtos.CityDtos.*;
import com.easyrailjourney.EasyRailJourney.services.CityService;
import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
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