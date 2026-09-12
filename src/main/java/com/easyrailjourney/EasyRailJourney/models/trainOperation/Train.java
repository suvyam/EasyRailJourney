package com.easyrailjourney.EasyRailJourney.models.trainOperation;

import java.util.HashSet;
import java.util.Set;

import com.easyrailjourney.EasyRailJourney.Stratergies.FareStrategyType;
import com.easyrailjourney.EasyRailJourney.enums.Refunds.RefundStrategyType;
import com.easyrailjourney.EasyRailJourney.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity

public class Train extends BaseModel {

    @Column (nullable=false)
    private String trainName;

    @Column (nullable=false)
    private String trainNumber;


    
    @ManyToOne 
    @JsonBackReference 
    @JoinColumn(name = "train_type_id", nullable = false)
    private TrainType trainType;

    @Column (nullable=false)
    private Boolean status = true;

    @Column (nullable=false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "train")
    @JsonManagedReference 
    @Column (nullable=false)
    private Set<ScheduleTrain> schedules = new HashSet<>();


    @Enumerated(EnumType.STRING)
    @Column (nullable=false)
    private FareStrategyType fareStrategyType;


    @Enumerated(EnumType.STRING)
    @Column (nullable=false)
    private RefundStrategyType refundStrategyType;
}