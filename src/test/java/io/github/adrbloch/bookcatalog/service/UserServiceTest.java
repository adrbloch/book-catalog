package io.github.adrbloch.bookcatalog.service;

import io.github.adrbloch.bookcatalog.domain.User;
import io.github.adrbloch.bookcatalog.exception.FieldsNotMatchException;
import io.github.adrbloch.bookcatalog.exception.ResourceAlreadyExistsException;
import io.github.adrbloch.bookcatalog.exception.ResourceNotFoundException;
import io.github.adrbloch.bookcatalog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private User initializeUser() {

        User user = new User("user", "password", "password");
        user.setId(1L);
        return user;
    }

    @Test
    void createAlreadyExistingUserThrowsException() {

        //given
        User user = new User();
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        //when
        //then
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(user));
    }

    @Test
    void createUserWithUnmatchedPasswordsThrowsException() {

        //given
        User user = initializeUser();
        user.setMatchingPassword("password2");

        //when
        //then
        assertThrows(FieldsNotMatchException.class, () -> userService.createUser(user));
    }

    @Test
    void checkIfPasswordIsEncodedAfterCreateNewUser() {

        //given
        User user = initializeUser();
        String password = user.getPassword();
        given(userRepository.save(user)).willReturn(user);

        //when
        User encodedUser = userService.createUser(user);

        //then
        assertTrue(passwordEncoder.matches(password, encodedUser.getPassword()));
    }

    @Test
    void returnUserAfterUpdate() {

        //given
        User user = initializeUser();
        String password = user.getPassword();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        //when
        User updatedUser = userService.updateUser(user.getId(), user);

        //then
        assertEquals(1L, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getMatchingPassword());
        assertTrue(passwordEncoder.matches(password, updatedUser.getPassword()));
    }

    @Test
    void returnUserAfterDeleteById() {

        //given
        User user = initializeUser();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //when
        userService.deleteUserById(user.getId());

        //then
        assertEquals(1L, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("password", user.getMatchingPassword());
    }

    @Test
    void ifUserNotExistsByIdThrowException() {

        //given
        User user = new User();
        Long id = user.getId();
        given(userRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> userService.returnUserIfExistsById(id));
    }

    @Test
    void returnUserIfExistsById() {

        //given
        User user = initializeUser();
        Long id = user.getId();
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        //when
        userService.returnUserIfExistsById(id);

        //then
        assertEquals(1L, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("password", user.getMatchingPassword());
    }

    @Test
    void ifUserNotExistsByUsernameThrowException() {

        //given
        User user = new User();
        String username = user.getUsername();
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> userService
                .returnUserIfExistsByUsername(username));
    }

    @Test
    void returnUserIfExistsByUsername() {

        //given
        User user = initializeUser();
        String username = user.getUsername();
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        //when
        userService.returnUserIfExistsByUsername(username);

        //then
        assertEquals(1L, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("password", user.getMatchingPassword());
    }

}