package com.wanted.august.repository;

import com.wanted.august.model.Post;
import com.wanted.august.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
//    List<PostEntity> findAllByOrderByCreatedAtDesc();
//    List<PostEntity> findAllByOrderByCreatedAtAsc();
//    List<PostEntity> findByTitleContaining(String title);

    @Query("SELECT new com.wanted.august.model.Post(p.id, u.userName, p.title, p.content, COUNT(pv.id), p.createdAt, p.updatedAt, p.deletedAt) " +
            "FROM PostEntity p " +
            "JOIN p.user u " +
            "LEFT JOIN PostViewEntity pv ON p.id = pv.postId " +
            "GROUP BY p.id, u.userName")
    Page<Post> findPostWithViewCount(Pageable pageable);

    @Query("SELECT new com.wanted.august.model.Post(p.id, u.userName, p.title, p.content, COUNT(pv.id), p.createdAt, p.updatedAt, p.deletedAt) " +
            "FROM PostEntity p " +
            "JOIN p.user u " +
            "LEFT JOIN PostViewEntity pv ON p.id = pv.postId " +
            "WHERE p.title LIKE %:title% " +
            "GROUP BY p.id, u.userName")
    Page<Post> findPostWithViewCountByTitle(Pageable pageable, @Param("title") String title);

}
