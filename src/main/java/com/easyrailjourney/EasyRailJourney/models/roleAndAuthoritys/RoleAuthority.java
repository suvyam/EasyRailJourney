package com.easyrailjourney.EasyRailJourney.models.roleAndAuthoritys;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RoleAuthority  extends BaseModel{

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;

    @ManyToOne
    @JsonBackReference 
    @JoinColumn(name = "authority_id")
    private Authorities authority;

    
}
