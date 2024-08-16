package com.wanted.august.repository;

import com.wanted.august.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Optional<CommentEntity> findByPostIdAndUserName(Long postId, String userName);
}
