package com.wanted.august.service;

import com.wanted.august.model.User;
import com.wanted.august.model.entity.UserEntity;
import com.wanted.august.model.request.UserJoinRequest;
import com.wanted.august.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    // 회원가입
    @Override
    public User join(UserJoinRequest request) {
        UserEntity saved = userRepository.save(UserEntity.toEntity(request));
        return User.fromEntity(saved);
    }
}
