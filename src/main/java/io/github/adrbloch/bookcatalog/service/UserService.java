package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.User;
import io.github.adrbloch.bookcatalog.exception.FieldsNotMatchException;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUserById(Long id) {
        logger.info("Get user with id: {}", id);
        return returnUserIfExistsById(id);
    }

    public User getUserByName(String username) {
        logger.info("Get user with username: {}", username);
        return returnUserIfExistsByUsername(username);
    }

    public List<User> getAllUsers() {
        logger.info("Get all users");
        return userRepository.findAll();
    }

    public User createUser(User newUser) {
        logger.info("Create user...");
        if (userRepository.findByUsername(newUser.getUsername()).isPresent())
            throw new ResourceAlreadyExistsException("User already exists!");

        if (!newUser.getPassword().equals(newUser.getMatchingPassword()))
            throw new FieldsNotMatchException("Passwords do not match!");

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    public User updateUser(Long id, User user) {
        logger.info("Update user with id: {}", id);

        User userToUpdate = returnUserIfExistsById(id);
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userToUpdate);
    }

    public User deleteUserById(Long id) {
        logger.warn("Delete user with id: {}", id);
        User userToDelete = returnUserIfExistsById(id);
        userRepository.deleteById(id);
        return userToDelete;
    }

    User returnUserIfExistsById(Long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new ResourceNotFoundException("User with id: {" + id + "} not found!");
        } else
            return userById.get();
    }

    User returnUserIfExistsByUsername(String username) {
        Optional<User> userByName = userRepository.findByUsername(username);
        if (userByName.isEmpty()) {
            throw new ResourceNotFoundException("User with username: {" + username + "} not found!");
        } else
            return userByName.get();
    }

}
