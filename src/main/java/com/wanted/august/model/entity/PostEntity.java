package com.wanted.august.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wanted.august.model.User;
import com.wanted.august.model.request.PostCreateRequest;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Data
@Builder
@Table(name = "post")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@EntityListeners(AuditingEntityListener.class)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 200, nullable = false)
    @Size(max = 200)
    private String title;

    @Column(length = 1000, nullable = false)
    @Size(max = 1000)
    private String content;

    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP NOT NULL COMMENT '생성일자'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일자'")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "datetime default NULL COMMENT '삭제일자'")
    private LocalDateTime deletedAt;

    public static PostEntity toEntity(PostCreateRequest request, UserEntity user) {
        return PostEntity.builder()
                .user(user)
                .title(request.getTitle())
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
