package com.wanted.august.repository;

import com.wanted.august.model.PostDetail;
import com.wanted.august.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
//    List<PostEntity> findAllByOrderByCreatedAtDesc();
//    List<PostEntity> findAllByOrderByCreatedAtAsc();
//    List<PostEntity> findByTitleContaining(String title);

    @Query("SELECT new com.wanted.august.model.PostDetail(p.id,  u.userName, p.title, p.content, p.createdAt, p.updatedAt, COUNT(pv.id)) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN PostViewEntity pv ON p.id = pv.postId " +
            "GROUP BY p.id, u.userName")
    Page<PostDetail> findPostDetailsWithViewCount(Pageable pageable);

    @Query("SELECT new com.wanted.august.model.PostDetail(p.id,  u.userName, p.title, p.content, p.createdAt, p.updatedAt, COUNT(pv.id)) " +
            "FROM PostEntity p " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN PostViewEntity pv ON p.id = pv.postId " +
            "WHERE p.title LIKE %:title% " +
            "GROUP BY p.id, u.userName")
    Page<PostDetail> findPostDetailsWithViewCountByTitle(Pageable pageable, @Param("title") String title);

}
