package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.User;
import com.wanted.august.model.UserRole;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder encoder;

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

    @Test
    void 회원가입시_중복된_유저네임이면_에러발생() {
        // given
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

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(new UserEntity()));
        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class,
                () -> userService.join(request));
        Assertions.assertEquals(ErrorCode.DUPLICATE_USER_NAME, exception.getErrorCode());
    }

    @Test
    void 회원가입시_패스워드는_암호화되어_저장된다() {
        // given
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

        // when
        UserEntity savedEntity = UserEntity.toEntity(request);
        savedEntity.setId(1L);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encryptedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity); // 실제로 save 될 엔티티 생성 및 id도 명시적으로 설정

        Assertions.assertDoesNotThrow(() -> userService.join(request));

        // then
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        Assertions.assertEquals("encryptedPassword", savedUser.getPassword());
    }
}
