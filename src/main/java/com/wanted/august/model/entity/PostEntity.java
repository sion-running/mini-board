package com.wanted.august.model.entity;

import com.wanted.august.model.User;
import com.wanted.august.model.request.PostCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Builder
@Table(name = "post")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 200, nullable = false)
    @Size(max = 200)
    private String title;

    @Column(length = 1000, nullable = false)
    @Size(max = 1000)
    private String content;

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
