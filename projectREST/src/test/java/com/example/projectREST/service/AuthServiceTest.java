package com.example.projectREST.service;

import com.example.projectREST.model.User;
import com.example.projectREST.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest
class AuthServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Test
    void register_ShouldCreateNewUser_WhenEmailNotTaken() {
        String email = "new@mail.com";
        String password = "secret";
        String fullname = "John Doe";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("hashed");

        User user = authService.register(email, password, fullname);

        assertEquals(email, user.getEmail());
        assertEquals("hashed", user.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrow_WhenEmailExists() {
        String email = "existing@mail.com";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> authService.register(email, "secret", "Jane Doe"));
    }

    @Test
    void login_ShouldReturnToken_WhenValidCredentials() {
        String email = "user@mail.com";
        String rawPassword = "secret";
        User user = new User(1L, email, "hashed", "User", Set.of());

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);

        String token = authService.login(email, rawPassword);

        assertNotNull(token);
    }

    @Test
    void login_ShouldThrow_WhenUserNotFound() {
        String email = "unknown@mail.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.login(email, "wrong"));
    }

    @Test
    void login_ShouldThrow_WhenPasswordInvalid() {
        String email = "user@mail.com";
        User user = new User(1L, email, "hashed", "User", Set.of());

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("wrong", user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.login(email, "wrong"));
    }

}