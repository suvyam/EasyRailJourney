package com.easyrailjourney.EasyRailJourney.models;

import java.util.HashSet;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.models.trainOperation.Station;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class City extends BaseModel {

    @NotBlank(message = "City name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "State is required")
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    @JsonBackReference
    private States state;

    @OneToMany(mappedBy = "city")
    @JsonManagedReference
    private Set<Station> stations = new HashSet<>();

    @Column(nullable = false)
    private Boolean isDeleted = false;
}