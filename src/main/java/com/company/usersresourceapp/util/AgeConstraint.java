package com.company.usersresourceapp.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * A custom validation annotation for ensuring that a date is associated with
 * an age greater than a specified value.
 */
@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeConstraint {
    String message() default "Age must be greater than {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
