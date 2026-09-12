package com.easyrailjourney.EasyRailJourney.models;

import java.util.HashSet;
import java.util.Set;

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
public class Country extends BaseModel {

    @NotBlank(message = "Country name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    private Set<States> states = new HashSet<>();

    @Column(nullable = false)
    private Boolean isDeleted = false;
}