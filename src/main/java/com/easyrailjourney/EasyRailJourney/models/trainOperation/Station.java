package com.easyrailjourney.EasyRailJourney.models.trainOperation;
import com.easyrailjourney.EasyRailJourney.enums.Trains.StationStatus;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.easyrailjourney.EasyRailJourney.models.City;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Station extends BaseModel {

    @NotBlank(message = "Station code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Station name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "City is required")
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @JsonBackReference
    private City city;

    private String zone;

    private Float latitude;

    private Float longitude;

    @NotNull(message = "Station status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StationStatus status;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}