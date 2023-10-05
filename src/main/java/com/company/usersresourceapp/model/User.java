package com.company.usersresourceapp.model;

import com.company.usersresourceapp.util.AgeConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email can't be empty")
    @Email(
            message = "Email must be in email address format",
            regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" // RFC 5322
    )
    private String email;

    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    private String lastName;

    @NotNull(message = "Date can't be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date must be earlier than current date")
    @AgeConstraint()
    private Date birthDate;

    private String address;
    private String phoneNumber;

    public User() {

    }

    public User(String email, String firstName, String lastName, Date birthDate) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
