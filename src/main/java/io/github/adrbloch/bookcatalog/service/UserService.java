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

@Service
public class UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUserById(Long id) {
        logger.info("Get user with id: {}", id);
        return checkIfExistsAndReturnUser(id);
    }

    public List<User> getAllUsers() {
        logger.info("Get all users");
        return userRepository.findAll();
    }

    public User createUser(User newUser) throws ResourceAlreadyExistsException, FieldsNotMatchException {
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

        User userToUpdate = checkIfExistsAndReturnUser(id);
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userToUpdate);
    }

    public User deleteUserById(Long id) {
        logger.warn("Delete user with id: {}", id);
        User userToDelete = checkIfExistsAndReturnUser(id);
        userRepository.deleteById(id);
        return userToDelete;
    }

    private User checkIfExistsAndReturnUser(Long id) throws ResourceNotFoundException {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User with id {" + id + "} not found!");
        } else return userRepository.findById(id).get();
    }

}
