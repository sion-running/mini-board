package com.wanted.august.repository;

import com.wanted.august.model.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUserName(String userName);
    RefreshTokenEntity findByToken(String token);
    @Transactional
    void deleteByUserName(@Param("userName") String userName);
}
