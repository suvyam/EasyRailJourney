package com.easyrailjourney.Validations.SeatValidaters;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SeatValidate.class)
@Documented
public @interface  ValidSeat {

    String message()  default "Provide either id OR coachId and seatNumber";

    Class<?>[] groups() default {};

    Class<? extends  Payload>[] payload () default{};

    
}
