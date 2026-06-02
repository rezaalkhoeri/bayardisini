package com.pos.bayardisini.auth.service;

import com.pos.bayardisini.auth.dto.*;
import com.pos.bayardisini.auth.entity.User;
import com.pos.bayardisini.auth.repository.UserRepository;
import com.pos.bayardisini.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        public void register(RegisterRequest request) {

                if (userRepository.existsByEmail(request.email())) {
                        throw new RuntimeException("Email already used");
                }

                UUID tenantId;
                try {
                        tenantId = UUID.fromString(request.tenantId());
                } catch (Exception e) {
                        throw new RuntimeException("Invalid tenantId format");
                }

                User user = User.builder()
                                .id(UUID.randomUUID())
                                .tenantId(tenantId)
                                .fullName(request.fullName())
                                .email(request.email())
                                .phone(request.phone())
                                .passwordHash(passwordEncoder.encode(request.password()))
                                .isActive(true)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .deletedAt(null)
                                .build();

                userRepository.save(user);
        }

        public AuthResponse login(LoginRequest request) {

                User user = userRepository.findByEmail(
                                request.email())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                boolean valid = passwordEncoder.matches(
                                request.password(),
                                user.getPasswordHash());

                if (!valid) {
                        throw new RuntimeException("Invalid email or password");
                }

                String token = jwtService.generateToken(user);
                return new AuthResponse(token);
        }

        public MeResponse me(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow();

                return new MeResponse(
                                user.getId(),
                                user.getTenantId(),
                                user.getFullName(),
                                user.getEmail());
        }
}