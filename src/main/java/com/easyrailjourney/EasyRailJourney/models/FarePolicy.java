package com.easyrailjourney.EasyRailJourney.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
public class FarePolicy extends  BaseModel {
//     1 | STANDARD
// 2 | STATE_BASED
// 3 | SOCIAL
// 4 | SPECIAL
}
