package com.easyrailjourney.Validations.CoachValidaters;


import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator
        implements ConstraintValidator<AtLeastOneNotNull, Object> {

    private String[] fields;

    @Override
    public void initialize(AtLeastOneNotNull annotation) {
        this.fields = annotation.fields();
    }

    @Override
    public boolean isValid(
            Object object,
            ConstraintValidatorContext context) {

        if (object == null) {
            return true;
        }

        for (String fieldName : fields) {

            try {

                Field field =
                        object.getClass()
                              .getDeclaredField(fieldName);

                field.setAccessible(true);

                Object value = field.get(object);

                if (value != null) {
                    return true;
                }

            } catch (NoSuchFieldException |
                     IllegalAccessException e) {

                throw new RuntimeException(
                        "Unable to validate field: " + fieldName,
                        e
                );
            }
        }

        return false;
    }
}