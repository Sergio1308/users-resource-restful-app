package com.company.usersresourceapp.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AgeValidatorTest {

    @Autowired
    private AgeValidator ageValidator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ageValidator.setMinimumAge(18);
        when(context.buildConstraintViolationWithTemplate("User age must be greater than 18"))
                .thenReturn(violationBuilder);
    }

    @Test
    public void testValidAge() {
        Date dateOfBirth = createDateWithAge(20);
        assertTrue(ageValidator.isValid(dateOfBirth, context));
    }

    @Test
    public void testInvalidAge() {
        Date dateOfBirth = createDateWithAge(16);
        assertFalse(ageValidator.isValid(dateOfBirth, context));
    }

    @Test
    public void testNullDateOfBirth() {
        assertFalse(ageValidator.isValid(null, context));
    }

    @Test
    public void testAgeEqualsMinimumAge() {
        Date dateOfBirth = createDateWithAge(18);
        assertFalse(ageValidator.isValid(dateOfBirth, context));
    }

    private Date createDateWithAge(int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);
        return calendar.getTime();
    }
}
