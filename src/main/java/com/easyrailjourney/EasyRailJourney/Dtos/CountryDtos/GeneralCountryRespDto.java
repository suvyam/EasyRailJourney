package com.easyrailjourney.EasyRailJourney.Dtos.CountryDtos;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.Country;

import lombok.Data;




@Data
public class GeneralCountryRespDto {

    private Country country;
    private List<Country> countries = new ArrayList<>();

    private ResponseStatus responseStatus;
    private String message;
}