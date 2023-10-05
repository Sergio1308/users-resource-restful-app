package com.company.usersresourceapp.controller;

import com.company.usersresourceapp.model.User;
import com.company.usersresourceapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserModelAssembler userModelAssembler;

    private User testUser;

    @BeforeEach
    public void setUp() throws ParseException {
        testUser = new User("test@example.com", "John", "Doe",
                new SimpleDateFormat("yyyy-MM-dd").parse("1889-01-01"));
        testUser.setId(1L);
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createNewUser(any(User.class))).thenReturn(testUser);
        when(userModelAssembler.toModel(testUser)).thenReturn(EntityModel.of(testUser));
        when(userModelAssembler.toModel(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    EntityModel<User> entityModel = EntityModel.of(user);
                    entityModel.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
                    return entityModel;
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthDate\":\"1990-01-01\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andReturn();

        verify(userService, times(1)).createNewUser(any(User.class));
        verify(userModelAssembler, times(1)).toModel(testUser);
    }

    @Test
    public void testGetUser() throws Exception {
        when(userService.findUserById(1L)).thenReturn(testUser);
        when(userModelAssembler.toModel(testUser)).thenReturn(EntityModel.of(testUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).findUserById(1L);
        verify(userModelAssembler, times(1)).toModel(testUser);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(testUser, testUser);
        when(userService.findAllUsers()).thenReturn(users);
        when(userModelAssembler.toModel(testUser)).thenReturn(EntityModel.of(testUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userList").isArray())
                .andExpect(jsonPath("$._embedded.userList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.userList[1].id").value(1));

        verify(userService, times(1)).findAllUsers();
        verify(userModelAssembler, times(2)).toModel(any(User.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));

        when(userService.updateUser(any(User.class), eq(1L))).thenReturn(updatedUser);
        when(userModelAssembler.toModel(updatedUser)).thenReturn(EntityModel.of(updatedUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"updated@example.com\",\"firstName\":\"Updated\",\"lastName\":\"User\",\"birthDate\":\"1990-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));

        verify(userService, times(1)).updateUser(any(User.class), eq(1L));
        verify(userModelAssembler, times(1)).toModel(updatedUser);
    }

    @Test
    public void testUpdateUserPartially() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setPhoneNumber("1234567890");

        when(userService.updateUserByFields(any(Map.class), eq(1L))).thenReturn(updatedUser);
        when(userModelAssembler.toModel(updatedUser)).thenReturn(EntityModel.of(updatedUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));

        verify(userService, times(1)).updateUserByFields(any(Map.class), eq(1L));
        verify(userModelAssembler, times(1)).toModel(updatedUser);
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        verify(userService, times(1)).deleteUserById(eq(1L));
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("1889-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("1990-12-31");

        List<User> users = Arrays.asList(testUser, testUser);
        when(userService.searchByDate(fromDate, toDate)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("from", "1889-01-01")
                        .param("to", "1990-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).searchByDate(fromDate, toDate);
    }
}
