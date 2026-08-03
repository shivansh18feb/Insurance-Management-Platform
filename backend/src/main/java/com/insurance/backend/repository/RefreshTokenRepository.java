package com.insurance.backend.repository;

import com.insurance.backend.entity.RefreshToken;
import com.insurance.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    void deleteByUser(User user);

    void deleteByToken(String token);

    boolean existsByToken(String token);

    List<RefreshToken> findByUserAndRevokedFalse(User user);

    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
}