package com.insurance.backend.service.impl;

import com.insurance.backend.dto.request.*;
import com.insurance.backend.dto.response.AuthResponseDto;
import com.insurance.backend.entity.LoginHistory;
import com.insurance.backend.entity.RefreshToken;
import com.insurance.backend.entity.User;
import com.insurance.backend.entity.enums.Role;
import com.insurance.backend.exception.DuplicateResourceException;
import com.insurance.backend.exception.InvalidRequestException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.repository.LoginHistoryRepository;
import com.insurance.backend.repository.UserRepository;
import com.insurance.backend.security.jwt.JwtService;
import com.insurance.backend.security.userdetails.CustomUserDetails;
import com.insurance.backend.service.AuthenticationService;
import com.insurance.backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final LoginHistoryRepository loginHistoryRepository;

    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() == null ? Role.CUSTOMER : request.getRole())
                .enabled(true)
                .accountLocked(false)
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateToken(new CustomUserDetails(user));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (user.isAccountLocked()) {
            throw new InvalidRequestException("Account is locked due to multiple failed login attempts");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            // Reset failed attempts on successful login
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        } catch (Exception e) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= 5) {
                user.setAccountLocked(true);
            }
            userRepository.save(user);
            loginHistoryRepository.save(LoginHistory.builder()
                    .user(user)
                    .loginTime(LocalDateTime.now())
                    .successful(false)
                    .build());
            if (user.isAccountLocked()) {
                throw new InvalidRequestException("Account locked due to 5 consecutive failed login attempts. Contact admin.");
            }
            throw new InvalidRequestException("Invalid email or password. Attempts: " + attempts + "/5");
        }

        loginHistoryRepository.save(LoginHistory.builder()
                .user(user)
                .loginTime(LocalDateTime.now())
                .successful(true)
                .build());

        String accessToken = jwtService.generateToken(new CustomUserDetails(user));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(new CustomUserDetails(user));
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(request.getRefreshToken());

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revokeRefreshToken(refreshToken);
        }
    }

    @Override
    public void changePassword(String email, ChangePasswordRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidRequestException("Current password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, user.getEmail());
        return token;
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto request) {
        String email = resetTokens.get(request.getToken());
        if (email == null) {
            throw new InvalidRequestException("Invalid or expired password reset token");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        resetTokens.remove(request.getToken());
    }
}