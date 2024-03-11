package com.marco.loginjwt.domain.auth.refresh_token;

import com.marco.loginjwt.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUser(User user);

    Optional<RefreshToken> findByToken(String token);
}
