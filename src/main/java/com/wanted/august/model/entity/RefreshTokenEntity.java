package com.wanted.august.model.entity;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "refresh_token")
@Entity
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, unique = true)
    private String userName;

    public void validateSameToken(String token) {
        if (!this.token.equals(token)) {
            throw new AugustApplicationException(ErrorCode.INVALID_TOKEN);
        }
    }
}
