package com.company.usersresourceapp.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * A custom validator for the {@link AgeConstraint} annotation, ensuring that
 * a date represents an age greater than specified value.
 */
@Component
public class AgeValidator implements ConstraintValidator<AgeConstraint, Date> {
    @Setter(AccessLevel.PROTECTED)
    @Value("${minimum.user.age}")
    private int minimumAge;

    /**
     * Validates that the provided date corresponds to an age greater than specified value.
     * @param dateOfBirth   The date of birth to be validated.
     * @param context       The context in which the validation is executed.
     * @return              true if the age is greater than specified value; false otherwise.
     */
    @Override
    public boolean isValid(Date dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return false;
        }
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);
        Calendar now = Calendar.getInstance();

        int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            // Adjust age if birthdate hasn't occurred yet this year
            age--;
        }
        if (age <= minimumAge) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("User age must be greater than " + minimumAge)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
