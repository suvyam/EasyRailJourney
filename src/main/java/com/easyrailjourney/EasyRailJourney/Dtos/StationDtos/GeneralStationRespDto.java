package com.easyrailjourney.EasyRailJourney.Dtos.StationDtos;

import java.util.ArrayList;
import java.util.List;

import com.easyrailjourney.EasyRailJourney.enums.ResponseStatus;
import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;

import lombok.Data;

@Data
public class GeneralStationRespDto {

    private List<Station> stations = new ArrayList<>();
    private Station station;
    private ResponseStatus responseStatus;
    private String message;
}