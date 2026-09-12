package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import java.util.List;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
@Entity 
public class TrainType extends  BaseModel {

    String name;

    @OneToMany (mappedBy="trainType")
    @JsonManagedReference 
    List<Train> trains;

    Boolean isDeleted = false;
    
}
