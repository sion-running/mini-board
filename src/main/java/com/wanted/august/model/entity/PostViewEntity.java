package com.wanted.august.model.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Getter
@Setter
@Table(name = "post_view",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_name"}))
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class PostViewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;
    private String userName;
}
