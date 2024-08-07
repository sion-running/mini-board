package com.wanted.august.service;

import com.wanted.august.exception.AugustApplicationException;
import com.wanted.august.exception.ErrorCode;
import com.wanted.august.model.User;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.model.request.UserLoginRequest;
import com.wanted.august.repository.UserRepository;
import com.wanted.august.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    // 회원가입
    @Override
    public User join(UserJoinRequest request) {
        userRepository.findByUserName(request.getUserName()).ifPresent(it -> {
            throw new AugustApplicationException(ErrorCode.DUPLICATE_USER_NAME);
        });

        String encoded = encoder.encode(request.getPassword());
        UserEntity saved = userRepository.save(UserEntity.toEntity(request, encoded));

        return User.fromEntity(saved);
    }

    @Override
    public String login(UserLoginRequest request) {
        User savedUser = loadUserByUsername(request.getUserName());

        // 비밀번호 비교
        if (!encoder.matches(request.getPassword(), savedUser.getPassword())) {
            throw new AugustApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        return jwtTokenUtil.generateAccessToken(savedUser.getUsername());
    }

    public UserEntity findByUserNameOrElseThrow(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new AugustApplicationException(ErrorCode.USER_NOT_FOUND);
        });
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return User.fromEntity(userRepository.findByUserName(username).orElseThrow(() -> new AugustApplicationException(ErrorCode.USER_NOT_FOUND)));
    }
}
