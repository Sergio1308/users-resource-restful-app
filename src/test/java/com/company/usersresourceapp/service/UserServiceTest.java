package com.company.usersresourceapp.service;

import com.company.usersresourceapp.exception.UserNotFoundException;
import com.company.usersresourceapp.model.User;
import com.company.usersresourceapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewUser() {
        User testUser = new User();
        testUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User createdUser = userService.createNewUser(testUser);

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
    }

    @Test
    public void testFindUserById() {
        User testUser = new User();
        testUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User foundUser = userService.findUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    public void testFindUserByIdNotFound() {
        // Configure the repository to return an empty optional, simulating a user not found
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method being tested and expect a UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    public void testFindAllUsers() {
        // Create a list of test users
        List<User> testUsers = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        testUsers.add(user1);
        testUsers.add(user2);

        when(userRepository.findAll()).thenReturn(testUsers);

        // Call the service method being tested
        List<User> foundUsers = userService.findAllUsers();

        // Assert that the found users match the test users
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        assertEquals(1L, foundUsers.get(0).getId());
        assertEquals(2L, foundUsers.get(1).getId());
    }
}
