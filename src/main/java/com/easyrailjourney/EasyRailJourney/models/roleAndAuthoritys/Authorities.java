package com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Authorities extends BaseModel {

    @NotBlank 
    @Column (nullable=false)
    String name;
    
}
