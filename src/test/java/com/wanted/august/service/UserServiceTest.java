package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.User;
import com.wanted.august.model.UserRole;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.model.request.UserLoginRequest;
import com.wanted.august.repository.UserRepository;
import com.wanted.august.utils.JwtTokenUtil;
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

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void 회원가입_성공후_User_반환() {
        String userName = "sion1234";
        String nickName = "sion";
        String password = "paSS123!@#";
        String email = "sion@naver.com";
        String phone = "010-1111-2222";

        UserJoinRequest request = new UserJoinRequest(
                userName,
                nickName,
                password,
                email,
                phone,
                UserRole.USER
        );

        UserEntity entity = UserEntity.toEntity(request, "encryptedPassword");
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
        String nickName = "sion";
        String password = "paSS123!@#";
        String email = "sion@naver.com";
        String phone = "010-1111-2222";

        UserJoinRequest request = new UserJoinRequest(
                userName,
                nickName,
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
        String nickName = "sion";
        String password = "paSS123!@#";
        String email = "sion@naver.com";
        String phone = "010-1111-2222";

        UserJoinRequest request = new UserJoinRequest(
                userName,
                nickName,
                password,
                email,
                phone,
                UserRole.USER
        );

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(encoder.encode(request.getPassword())).thenReturn("encryptedPassword");
        UserEntity savedEntity = UserEntity.toEntity(request, "encryptedPassword");
        savedEntity.setId(1L);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity); // 실제로 save 될 엔티티 생성 및 id도 명시적으로 설정

        // when
        Assertions.assertDoesNotThrow(() -> userService.join(request));

        // then
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        Assertions.assertEquals("encryptedPassword", savedUser.getPassword());
    }

    @Test
    void 로그인시_패스워드_불일치하면_에러발생() {
        // given
        String userName = "sion1234";
        String password = "paSS123!@#";
        String loginPassword = "password1"; // 로그인 시 입력한 패스워드
        String encryptedPassword = "encryptedPassword"; // 암호화된 패스워드

        UserLoginRequest request = new UserLoginRequest(
            userName, loginPassword
        );

        UserEntity entity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password(encryptedPassword)
                .build();

        // when
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(entity));
        when(encoder.matches(loginPassword, entity.getPassword())).thenReturn(false);

        AugustApplicationException exception = Assertions.assertThrows(AugustApplicationException.class
                , () -> userService.login(request));

        // then
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    // TODO static 메소드 모킹
    @Test
    void 로그인에_성공하면_토큰을_반환한다() {
        // given
        String userName = "sion1234";
        String loginPassword = "password1"; // 로그인 시 입력한 패스워드
        String encryptedPassword = "encryptedPassword"; // 암호화된 패스워드
        String expectedToken = "dummyToken";

        UserLoginRequest request = new UserLoginRequest(
                userName, loginPassword
        );

        UserEntity entity = UserEntity.builder()
                .id(1L)
                .userName(userName)
                .password(encryptedPassword)
                .build();


        // Mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(entity));
        when(encoder.matches(loginPassword, encryptedPassword)).thenReturn(true); // 패스워드 일치

        // when
        String token = userService.login(request);

        // then
        assertThat(token).isNotBlank();
    }
}
