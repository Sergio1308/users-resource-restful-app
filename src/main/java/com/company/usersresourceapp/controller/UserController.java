package com.company.usersresourceapp.controller;

import com.company.usersresourceapp.exception.ParametersValidationException;
import com.company.usersresourceapp.model.User;
import com.company.usersresourceapp.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    private final UserModelAssembler userModelAssembler;

    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<User>> createUser(@Valid @RequestBody User user) {
        EntityModel<User> entityModel = userModelAssembler.toModel(userService.createNewUser(user));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@Valid @RequestBody User updatedUser, @PathVariable Long id) {
        return ResponseEntity.ok(userModelAssembler.toModel(userService.updateUser(updatedUser, id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUserPartially(@RequestBody Map<Object, Object> fields, @PathVariable Long id) {
        return ResponseEntity.ok(userModelAssembler.toModel(userService.updateUserByFields(fields, id)));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers() {
        List<EntityModel<User>> users = userService.findAllUsers().stream()
                .map(userModelAssembler::toModel)
                .toList();
        return new ResponseEntity<>(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userModelAssembler.toModel(userService.findUserById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User " + id + " deleted successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<EntityModel<User>>> searchUsersByBirthDateRange(
            @RequestParam(value = "from") @DateTimeFormat(pattern = "yyyy-MM-dd") @Past(
                    message = "Parameter 'from' must be less than 'to'") Date from,
            @RequestParam(value = "to") @DateTimeFormat(pattern = "yyyy-MM-dd") @Past(
                    message = "Parameter 'from' must be less than 'to'") Date to
    ) {
        if (!from.before(to)) {
            throw new ParametersValidationException("Parameter 'from' must be less than 'to'");
        }
        List<EntityModel<User>> users = userService.searchByDate(from, to).stream()
                .map(userModelAssembler::toModel)
                .toList();
        return new ResponseEntity<>(CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()), HttpStatus.OK);
    }
}
