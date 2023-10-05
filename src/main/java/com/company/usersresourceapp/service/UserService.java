package com.company.usersresourceapp.service;

import com.company.usersresourceapp.exception.ParametersValidationException;
import com.company.usersresourceapp.exception.UserNotFoundException;
import com.company.usersresourceapp.model.User;
import com.company.usersresourceapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createNewUser(@Valid User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User updatedUser, Long id) {
        User user = findUserById(id);
        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setBirthDate(updatedUser.getBirthDate());
        setValue(user::setAddress, updatedUser.getAddress());
        setValue(user::setPhoneNumber, updatedUser.getPhoneNumber());
        return userRepository.save(user);
    }

    public User updateUserByFields(Map<Object, Object> fields, Long id) {
        User user = findUserById(id);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, (String) key);
            if (field == null) {
                throw new ParametersValidationException("Field '" + key + "' is not valid.");
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        user -> userRepository.deleteById(id),
                        () -> {
                            throw new UserNotFoundException(id);
                        }
                );
    }

    public List<User> searchByDate(Date from, Date to) {
        return userRepository.findByBirthDateBetween(from, to);
    }

    /**
     * Set values via setter method if it is not null, used to modify an object via setter
     * @param setter method that changes the state of an object
     * @param value to set
     * @param <T> generic
     */
    private <T> void setValue(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
