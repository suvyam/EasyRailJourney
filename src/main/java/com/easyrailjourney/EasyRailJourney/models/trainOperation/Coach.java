package com.easyrailjourney.EasyRailJourney.models.trainOperation;
import java.util.HashSet;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity


public class Coach extends BaseModel {

    @NotBlank(message = "Coach number is required")
    @Column(nullable = false)
    private String coachNumber;

    @ManyToOne
    @JoinColumn(name = "coach_type_id", nullable = false)
    @JsonBackReference
    private CoachType coachType;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "coach")
    @JsonManagedReference
    private Set<Seat> seats = new HashSet<>();
}