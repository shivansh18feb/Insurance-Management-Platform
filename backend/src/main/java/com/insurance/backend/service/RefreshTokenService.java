package com.insurance.backend.service;

import com.insurance.backend.entity.RefreshToken;
import com.insurance.backend.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken rotateRefreshToken(String oldToken);

    void revokeRefreshToken(String token);

    void revokeAllUserTokens(User user);
}