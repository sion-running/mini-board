package com.wanted.august.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP NOT NULL COMMENT '생성일자'")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정일자'")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "bit default false NOT NULL COMMENT '삭제여부'")
    private Boolean isDeleted;

}
