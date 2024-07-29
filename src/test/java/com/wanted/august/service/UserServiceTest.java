package com.wanted.august.service;

import com.wanted.august.model.User;
import com.wanted.august.model.UserRole;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Test
    void 회원가입_성공후_User_반환() {
        String userName = "sion1234";
        String writer = "sion";
        String password = "paSS123!@#";
        String email = "sion@naver.com";
        String phone = "010-1111-2222";

        UserJoinRequest request = new UserJoinRequest(
                userName,
                writer,
                password,
                email,
                phone,
                UserRole.USER
        );

        UserEntity entity = UserEntity.toEntity(request);
        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(entity);

        User actual = userService.join(request);
        assertThat(actual.getId()).isEqualTo(entity.getId());
        assertThat(actual.getEmail()).isEqualTo(request.getEmail());
        assertThat(actual.getPhone()).isEqualTo(request.getPhone());
    }

}
