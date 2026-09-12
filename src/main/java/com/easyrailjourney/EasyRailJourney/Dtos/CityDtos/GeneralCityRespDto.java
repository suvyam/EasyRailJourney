package com.easyrailjourney.EasyRailJourney.Dtos.CityDtos;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.City;

import lombok.Data;

@Data
public class GeneralCityRespDto {

    private City city;
    private List<City> cities = new ArrayList<>();

    private ResponseStatus responseStatus;
    private String message;
}