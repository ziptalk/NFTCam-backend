package com.example.nftcam.api.entity.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByUserId(Long userId);
    Optional<UserToken> findUserTokenByRefreshToken(String token);
}
