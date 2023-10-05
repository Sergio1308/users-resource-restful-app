package com.company.usersresourceapp.controller;

import com.company.usersresourceapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserModelAssemblerTest {
    @Autowired
    private UserModelAssembler userModelAssembler;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("test@example.com", "John", "Doe", new Date());
    }

    @Test
    public void testToModel() {
        EntityModel<User> entityModel = userModelAssembler.toModel(user);
        assertNotNull(entityModel);

        Optional<Link> selfLinkOptional = entityModel.getLink("self");
        assertTrue(selfLinkOptional.isPresent());
        Link selfLink = selfLinkOptional.get();
        assertEquals("self", selfLink.getRel().value());

        Optional<Link> usersLinkOptional = entityModel.getLink("users");
        assertTrue(usersLinkOptional.isPresent());
        Link usersLink = usersLinkOptional.get();
        assertEquals("users", usersLink.getRel().value());

        assertEquals(user, entityModel.getContent());
    }
}
