package com.insurance.backend.service.impl;

import com.insurance.backend.entity.RefreshToken;
import com.insurance.backend.entity.User;
import com.insurance.backend.exception.InvalidRequestException;
import com.insurance.backend.exception.ResourceNotFoundException;
import com.insurance.backend.repository.RefreshTokenRepository;
import com.insurance.backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.expiration:604800000}")
    private long refreshTokenExpirationMs;

    @Override
    public RefreshToken createRefreshToken(User user) {
        revokeAllUserTokens(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRequestException("Refresh token was expired or revoked. Please make a new signin request");
        }
        return refreshToken;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenAndRevokedFalse(token);
    }

    @Override
    public RefreshToken rotateRefreshToken(String oldToken) {
        RefreshToken refreshToken = findByToken(oldToken)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
        
        verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return createRefreshToken(user);
    }

    @Override
    public void revokeRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        });
    }

    @Override
    public void revokeAllUserTokens(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserAndRevokedFalse(user);
        for (RefreshToken token : tokens) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }
}
