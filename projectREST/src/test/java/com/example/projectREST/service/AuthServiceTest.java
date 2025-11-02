package com.example.projectREST.service;

import com.example.projectREST.model.RoleEntity;
import com.example.projectREST.model.UserEntity;
import com.example.projectREST.repository.RoleRepository;
import com.example.projectREST.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserEntity userEntity;

    private RoleEntity userRole;

    @BeforeEach
    void setUp() {
        userRole = new RoleEntity();
        userRole.setId(1L);
        userRole.setName("USER");
    }


    @Test
    void register_ShouldCreateNewUser_WhenEmailNotTaken() {
        String email = "new@mail.com";
        String password = "secret";
        String fullname = "John Doe";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("hashed");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        UserEntity userEntity = authService.register(email, password, fullname);

        assertNotNull(userEntity);
        assertEquals(email, userEntity.getEmail());
        assertEquals("hashed", userEntity.getPassword());
        assertTrue(userEntity.getRoles().stream().anyMatch(r -> r.getName().equals("USER") || r.getName().equals("ROLE_USER"))
        );
        verify(userRepository).save(any(UserEntity.class));
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