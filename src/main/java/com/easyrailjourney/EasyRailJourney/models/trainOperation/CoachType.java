
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
public class CoachType extends BaseModel {

    @NotBlank(message = "Type code is required")
    @Column(nullable = false)
    private String typeCode;

    @NotBlank(message = "Type name is required")
    @Column(nullable = false)
    private String typeName;

    @NotBlank(message = "Description is required")
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "coachType")
    @JsonManagedReference
    private Set<Coach> coaches = new HashSet<>();
}