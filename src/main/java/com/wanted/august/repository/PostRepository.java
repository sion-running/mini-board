package com.wanted.august.repository;

import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByOrderByCreatedAtDesc();
    List<PostEntity> findAllByOrderByCreatedAtAsc();
}
