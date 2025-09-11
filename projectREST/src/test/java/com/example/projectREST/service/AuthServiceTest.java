package com.example.projectREST.service;

import com.example.projectREST.model.UserEntity;
import com.example.projectREST.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

//ExtendsWith(//Junit5)
@SpringBootTest
class AuthServiceTest {

    //@Mock
    @MockitoBean
    private UserRepository userRepository;

    //@Mock
    @MockitoBean
    private PasswordEncoder passwordEncoder;

    //Mock
    @Autowired
    private AuthService authService;

    @Test
    void register_ShouldCreateNewUser_WhenEmailNotTaken() {
        String email = "new@mail.com";
        String password = "secret";
        String fullname = "John Doe";

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(password)).thenReturn("hashed");

        UserEntity userEntity = authService.register(email, password, fullname);

        assertEquals(email, userEntity.getEmail());
        assertEquals("hashed", userEntity.getPassword());
        verify(userRepository).save(userEntity);
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
        UserEntity userEntity = new UserEntity();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        Mockito.when(passwordEncoder.matches(rawPassword, userEntity.getPassword())).thenReturn(true);

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
        UserEntity userEntity = new UserEntity();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        Mockito.when(passwordEncoder.matches("wrong", userEntity.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.login(email, "wrong"));
    }

}