package com.wanted.august.service;

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
    public void join(UserJoinRequest request) {
        log.info("service join called");
    }
}
