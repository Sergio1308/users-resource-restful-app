package com.company.usersresourceapp.repository;

import com.company.usersresourceapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByBirthDateBetween(Date from, Date to);
}
