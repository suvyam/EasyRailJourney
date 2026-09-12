package com.easyrailjourney.EasyRailJourney.models;

import java.util.HashSet;
import java.util.Set;

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
public class States extends BaseModel {

    @NotBlank(message = "State name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Country is required")
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    @JsonBackReference
    private Country country;

    @OneToMany(mappedBy = "state")
    @JsonManagedReference
    private Set<City> cities = new HashSet<>();

    @Column(nullable = false)
    private Boolean isDeleted = false;
}