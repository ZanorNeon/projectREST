//package com.example.projectREST.service;
//
//import com.example.projectREST.model.Role;
//import com.example.projectREST.model.User;
//import com.example.projectREST.repository.RoleRepository;
//import com.example.projectREST.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Set;
//
//
//public class AuthService {
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtProvider jwtProvider;
//
//    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtProvider = jwtProvider;
//    }
//
//    public User register(String email, String password, String fullName) {
//        if (userRepository.existsByEmail(email)) {
//            throw new IllegalArgumentException("Email already in use");
//        }
//
//        Role userRole = roleRepository.findByName("USER")
//                .orElseThrow(() -> new RuntimeException("Role USER not found"));
//
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setFullName(fullName);
//        user.setEnabled(true);
//        user.setRoles(Set.of());
//
//        userRepository.save(user);
//        return user;
//    }
//
//    public String login(String email, String password) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new IllegalArgumentException("Invalid email or password");
//        }
//
//        return jwtProvider.generateToken(user);
//    }
//
//}
