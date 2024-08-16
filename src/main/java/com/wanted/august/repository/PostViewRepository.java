package com.wanted.august.repository;

import com.wanted.august.model.entity.PostViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostViewRepository extends JpaRepository<PostViewEntity, Long> {
    Optional<PostViewEntity> findByPostIdAndUserName(Long postId, String userName);
}
