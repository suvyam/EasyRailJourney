package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import java.util.HashSet;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;




@Getter
@Setter
@Entity
public class SeatType extends BaseModel {

    @NotBlank(message = "Seat type code is required")
    @Column(nullable = false)
    private String typeCode;

    @NotBlank(message = "Seat type name is required")
    @Column(nullable = false)
    private String typeName;

    @NotBlank(message = "Seat type description is required")
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "seatType")
    @JsonManagedReference
    private Set<Seat> seats = new HashSet<>();
}