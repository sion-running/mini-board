package com.wanted.august.model.entity;

import com.wanted.august.model.request.CommentCreateRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Table(name = "comment")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Where(clause = "deleted_at is NULL")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Long postId;

    @Column(length = 400, nullable = false)
    @Size(max = 400)
    private String content;

    public static CommentEntity toEntity(CommentCreateRequest request, String userName) {
        return CommentEntity.builder()
                .userName(userName)
                .postId(request.getPostId())
                .content(request.getContent())
                .build();
    }
}
