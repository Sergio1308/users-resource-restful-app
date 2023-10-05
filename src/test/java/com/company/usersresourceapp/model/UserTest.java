package com.company.usersresourceapp.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserTest {
    private Date userValidTestDate;

    @BeforeEach
    void setUp() throws ParseException {
        userValidTestDate = new SimpleDateFormat("yyyy-MM-dd").parse("1889-01-01");
    }

    @Test
    public void testValidUser() {
        User user = new User("test@example.com", "John", "Doe", userValidTestDate);
        assertTrue(validate(user).isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        User user = new User("invalidEmail.com", "John", "Doe", userValidTestDate);
        Set<ConstraintViolation<User>> violations = validate(user);
        assertEquals(1, violations.size());
        assertEquals("Email must be in email address format", violations.iterator().next().getMessage());
    }

    @Test
    public void testBlankFirstName() {
        User user = new User("test@example.com", "", "Doe", userValidTestDate);
        Set<ConstraintViolation<User>> violations = validate(user);
        assertEquals(1, violations.size());
        assertEquals("First name can't be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void testBlankLastName() {
        User user = new User("test@example.com", "John", "", userValidTestDate);
        Set<ConstraintViolation<User>> violations = validate(user);
        assertEquals(1, violations.size());
        assertEquals("Last name can't be empty", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullBirthDate() {
        User user = new User("test@example.com", "John", "Doe", null);
        Set<ConstraintViolation<User>> violations = validate(user);

        assertEquals(2, violations.size());
        assertTrue(violations.stream()
                .anyMatch(violation -> "Date can't be empty".equals(violation.getMessage())));
    }

    // Helper method to validate User using Bean Validation
    private Set<ConstraintViolation<User>> validate(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(user);
    }
}
