package com.easyrailjourney.EasyRailJourney.Dtos.DataMeet;

import lombok.Data;

@Data
public class DataMeetStationDto {

    private String type;

    private Geometry geometry;

    private Properties properties;
}