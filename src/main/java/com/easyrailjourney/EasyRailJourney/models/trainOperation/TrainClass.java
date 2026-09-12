package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import com.easyrailjourney.EasyRailJourney.models.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity

public class TrainClass extends BaseModel {

    @Column(nullable=false)
    private String classCode;

    @Column(nullable=false)
    private String className;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false )
    private boolean isDeleted = false;

    private String reason;

}